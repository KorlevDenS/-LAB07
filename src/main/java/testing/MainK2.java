package testing;

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
import java.util.Date;
import java.util.Scanner;


public class MainK2 {

    static DatagramSocket ds;
    static DatagramPacket dp;
    static InetAddress host;
    static int port;

    public static void prepareData() {
        ClientStatusRegister.current = new Date();
        //try {
        //    ClientStatusRegister.currentXml = new File(System.getenv("COLLECTION_FILE"));
        //} catch (NullPointerException e) {
        //    System.out.println("Необходимая переменная окружения не задана. \n" +
        //            "Задайте переменную COLLECTION_FILE при помощи команды export c необходимым файлом xml.");
        //    System.exit(0);
        //}
        ClientStatusRegister.currentXml = new File("src/main/resources/MusicBandCollections.xml");
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InvalidDataFromFileException {
        prepareData();

        while (true) {
            Scanner commandScanner = new Scanner(System.in);
            String currentCommand = commandScanner.nextLine();
            ClientCommandManager commandManager = new ClientCommandManager(currentCommand);
            InstructionPattern instructionPattern = commandManager.execution(commandManager.instructionFetch());

            //загрузка текущего состояния для дальнейшей отправки
            TransportedData transportedData = ClientDataInstaller.installIntoTransported();

            CompleteMessage completeMessage = new CompleteMessage(transportedData, instructionPattern);
            //готовность к отправке

            ds = new DatagramSocket();
            host = InetAddress.getByName("localhost");
            port = 6789;

            ByteArrayOutputStream o = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(o);
            out.writeObject(completeMessage);
            byte[] buff = o.toByteArray();


            dp = new DatagramPacket(buff, buff.length, host, port);
            ds.send(dp);


            byte[] newArr = new byte[1048576];
            dp = new DatagramPacket(newArr, newArr.length);
            ds.receive(dp);

            ByteArrayInputStream i = new ByteArrayInputStream(newArr);
            ObjectInputStream in = new ObjectInputStream(i);
            CompleteMessage receivedMessage = (CompleteMessage) in.readObject();
            //обратный пакет получен

            //установка и вывод результата
            ClientDataInstaller clientDataInstaller = new ClientDataInstaller(receivedMessage.getTransportedData());
            clientDataInstaller.installFromTransported();
            Demonstrator demonstrator = new Demonstrator(receivedMessage.getResultPattern());
            demonstrator.demonstrateCommandResult();
            if (receivedMessage.getResultPattern().isTimeToExit()) System.exit(0);
        }

    }
}