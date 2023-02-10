import client.ClientCommandManager;
import client.ClientDataInstaller;
import client.ClientStatusRegister;
import client.Demonstrator;
import common.CompleteMessage;
import common.InstructionPattern;
import common.TransportedData;
import common.exceptions.InvalidDataFromFileException;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.Scanner;


public class ClientControlUnit {

    static DatagramSocket ds;
    static DatagramPacket dp;
    static InetAddress host;
    static int port;
    static CompleteMessage receivedMessage;
    static CompleteMessage sendingMessage;

    public static void prepareData() throws IOException {
        /*ClientStatusRegister.current = new Date();
        try {
            ClientStatusRegister.currentXml = new File(System.getenv("COLLECTION_FILE"));
        } catch (NullPointerException e) {
            System.out.println("Необходимая переменная окружения не задана. \n" +
                    "Задайте переменную COLLECTION_FILE при помощи команды export c необходимым файлом xml.");
            System.exit(0);
        }*/
        ClientStatusRegister.currentXml = new File("src/main/resources/MusicBandCollections.xml");
        try {
            ClientStatusRegister.xmlData = Files.readAllBytes(ClientStatusRegister.currentXml.toPath());
        } catch (NoSuchFileException e) {
            System.out.println("Файл, указанный в переменной среды не существует. \n" +
                    "Задайте переменную COLLECTION_FILE при помощи команды export c существующим файлом xml.");
            System.exit(0);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InvalidDataFromFileException {
        prepareData();

        while (true) {
            sendData();
            getData();
            if (receivedMessage.getResultPattern().isTimeToExit()) {
                FileOutputStream outputStream = new FileOutputStream(ClientStatusRegister.currentXml);
                outputStream.write(receivedMessage.getTransportedData().getXmlData());
                System.exit(0);
            }
        }

    }

    public static void sendData() throws IOException {
        Scanner commandScanner = new Scanner(System.in);
        String currentCommand = commandScanner.nextLine();
        ClientCommandManager commandManager = new ClientCommandManager(currentCommand);
        try {
            InstructionPattern instructionPattern = commandManager.execution(commandManager.instructionFetch());
            TransportedData transportedData = ClientDataInstaller.installIntoTransported();
            sendingMessage = new CompleteMessage(transportedData, instructionPattern);
            ds = new DatagramSocket();
            host = InetAddress.getByName("localhost");
            port = 6789;
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(o);
            out.writeObject(sendingMessage);
            byte[] buff = o.toByteArray();
            dp = new DatagramPacket(buff, buff.length, host, port);
            ds.send(dp);
        } catch (InvalidDataFromFileException e) {
            System.out.println(e.getMessage());
            sendData();
        } finally {
            ClientStatusRegister.xmlData = null;
        }

    }

    public static void getData() throws IOException, ClassNotFoundException {
        byte[] newArr = new byte[1048576];
        if (!receiveToByteArray(newArr)) {
            System.out.println("Нажмите Enter, чтобы повторить ввод команды или введите exit для завершения сеанса");
            Scanner s = new Scanner(System.in);
            if (s.nextLine().trim().equals("exit")) {
                System.out.println("Завершение работы...");
                System.exit(0);
            } else return;
        }
        ByteArrayInputStream i = new ByteArrayInputStream(newArr);
        ObjectInputStream in = new ObjectInputStream(i);
        receivedMessage = (CompleteMessage) in.readObject();
        ClientDataInstaller clientDataInstaller = new ClientDataInstaller(receivedMessage.getTransportedData());
        clientDataInstaller.installFromTransported();
        Demonstrator demonstrator = new Demonstrator(receivedMessage.getResultPattern());
        demonstrator.demonstrateCommandResult();
    }

    public static boolean receiveToByteArray(byte[] receivedData) throws IOException {
        ds.setSoTimeout(10000);
        dp = new DatagramPacket(receivedData, receivedData.length);
        try {
            ds.receive(dp);
            return true;
        } catch (SocketTimeoutException e) {
            System.out.println("Сервер в данный момент недоступен.");
            return false;
        }
    }
}
