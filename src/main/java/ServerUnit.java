import common.CompleteMessage;
import common.InstructionPattern;
import common.ResultPattern;
import common.TransportedData;
import common.exceptions.InvalidDataFromFileException;
import server.JaxbManager;
import server.ServerCommandManager;
import server.ServerDataInstaller;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;

@Deprecated
public class ServerUnit {

    static DatagramChannel dc;
    static ByteBuffer buf;
    static int port = 6789;
    static SocketAddress adr;
    static CompleteMessage receivedMessage;

    public ServerUnit() throws IOException, InvalidDataFromFileException, JAXBException, ClassNotFoundException {
        dc = DatagramChannel.open();
        call();
    }


    public void call() throws IOException, ClassNotFoundException, InvalidDataFromFileException, JAXBException {
        adr = new InetSocketAddress(port);

        dc.bind(adr);

        buf = ByteBuffer.allocate(1048576);
        adr = dc.receive(buf);

        ByteArrayInputStream i = new ByteArrayInputStream(buf.array());
        ObjectInputStream in = new ObjectInputStream(i);
        try {
            receivedMessage = (CompleteMessage) in.readObject();
        } catch (ClassNotFoundException e) {
            dc.close();
            new ServerUnit();
        }
        ServerDataInstaller installer = new ServerDataInstaller(receivedMessage.getTransportedData());
        installer.installFromTransported();
        formAndSendResult();
    }


    public void formAndSendResult() throws InvalidDataFromFileException, IOException, JAXBException, ClassNotFoundException {
        InstructionPattern instructionPattern = receivedMessage.getInstructionPattern();
        ResultPattern resultPattern;
        ArrayList<String> loadXmlInfo = new ArrayList<>(1);
        try {
            if (receivedMessage.getTransportedData().getXmlData() != null)
                loadXmlInfo = installXmlData(receivedMessage.getTransportedData().getXmlData());
            ServerCommandManager commandManager = new ServerCommandManager(instructionPattern);
            try {
                resultPattern = commandManager.execution(commandManager.instructionFetch());
                resultPattern.getReports().addAll(0,loadXmlInfo);
            } catch (InvalidDataFromFileException e) {
                dc.close();
                new ServerUnit();
                return;
            }
        } catch (JAXBException e) {
            resultPattern = new ResultPattern();
            resultPattern.getReports().add("Файл xml содержит ошибки и не может быть загружен в коллекцию. \n" +
                    "При выполнении команды exit файл будет перезаписан на основе выполнения следующих команд. \n" +
                    "Текущее содержимое файла во избежании потери информации: \n" +
                    new String(receivedMessage.getTransportedData().getXmlData()));
        }

        TransportedData newData = ServerDataInstaller.installIntoTransported();
        CompleteMessage sendingMessage = new CompleteMessage(newData, resultPattern);

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(o);
        out.writeObject(sendingMessage);
        byte[] buff = o.toByteArray();

        buf = ByteBuffer.wrap(buff);
        dc.send(buf, adr);

        dc.close();
        new ServerUnit();

    }

    public static ArrayList<String> installXmlData(byte[] xmlData) throws JAXBException {
        JaxbManager manager = new JaxbManager();
        manager.readXml(xmlData);
        return manager.validateXmlCollection();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InvalidDataFromFileException, JAXBException {
        new ServerUnit();
    }
}
