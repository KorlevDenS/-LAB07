import common.CompleteMessage;
import common.InstructionPattern;
import common.ResultPattern;
import common.TransportedData;
import common.exceptions.InvalidDataFromFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.JaxbManager;
import server.ServerCommandManager;
import server.ServerDataInstaller;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;

public class ServerControlUnit {

    private static final Logger logger = LoggerFactory.getLogger(ServerControlUnit.class);

    static DatagramChannel dc;
    static ByteBuffer buf;
    static int port = 6789;
    static SocketAddress adr;
    static CompleteMessage receivedMessage;

    public ServerControlUnit() throws IOException, InvalidDataFromFileException, JAXBException, ClassNotFoundException {
        dc = DatagramChannel.open();
        logger.info("DatagramChannel opened");
        call();
    }


    public void call() throws IOException, ClassNotFoundException, InvalidDataFromFileException, JAXBException {
        adr = new InetSocketAddress(port);

        dc.bind(adr);
        logger.info("Cвязка канала с адресом {}, порт {}", adr, port);

        buf = ByteBuffer.allocate(1048576);
        logger.info("Ожидание данных от клиента.");
        adr = dc.receive(buf);
        logger.info("Данные клиента получены {}", buf);

        ByteArrayInputStream i = new ByteArrayInputStream(buf.array());
        ObjectInputStream in = new ObjectInputStream(i);
        try {
            receivedMessage = (CompleteMessage) in.readObject();
            logger.info("Данные клиента успешно получены и десериализованы {}", receivedMessage);
        } catch (ClassNotFoundException e) {
            logger.debug("Ошибка десериализация объекта receivedMessage", e);
            logger.info("Перезапуск DatagramChannel");
            dc.close();
            new ServerControlUnit();
        }
        ServerDataInstaller installer = new ServerDataInstaller(receivedMessage.getTransportedData());
        installer.installFromTransported();
        logger.info("Установлены данные:\n {}\n {}\n {}\n {} ", receivedMessage.getTransportedData().getAppleMusic()
        , receivedMessage.getTransportedData().getCurrent(), receivedMessage.getTransportedData().getPassports(),
                receivedMessage.getTransportedData().getUniqueIdList());
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
            logger.info("Начало выполнения команды {} клиента.", instructionPattern.getTitleRegex());
            try {
                resultPattern = commandManager.execution(commandManager.instructionFetch());
                resultPattern.getReports().addAll(0,loadXmlInfo);
            } catch (InvalidDataFromFileException e) {
                logger.error("Команда {} организована не верно и работает с ошибкой", instructionPattern.getTitleRegex());
                logger.info("Перезапуск DatagramChannel");
                dc.close();
                new ServerControlUnit();
                return;
            }
            logger.info("Команда {} выполнена и сформирован результат.", instructionPattern.getTitleRegex());
        } catch (JAXBException e) {
            logger.debug("Файл xml содержит синтаксические ошибки или невалидные данные.");
            resultPattern = new ResultPattern();
            resultPattern.getReports().add("Файл xml содержит ошибки и не может быть загружен в коллекцию. \n" +
                    "При выполнении команды exit файл будет перезаписан на основе выполнения следующих команд. \n" +
                    "Текущее содержимое файла во избежании потери информации: \n" +
                    new String(receivedMessage.getTransportedData().getXmlData()));
        }

        TransportedData newData = ServerDataInstaller.installIntoTransported();
        CompleteMessage sendingMessage = new CompleteMessage(newData, resultPattern);
        logger.info("Данне после выполнения {} успешно загружены", instructionPattern.getTitleRegex());

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(o);
        out.writeObject(sendingMessage);
        byte[] buff = o.toByteArray();

        buf = ByteBuffer.wrap(buff);
        dc.send(buf, adr);
        logger.info("Команда {} выполнена, данные отправлены", instructionPattern.getTitleRegex());

        dc.close();
        logger.info("DatagramChannel закрыт.");
        new ServerControlUnit();

    }

    public static ArrayList<String> installXmlData(byte[] xmlData) throws JAXBException {
        JaxbManager manager = new JaxbManager();
        manager.readXml(xmlData);
        return manager.validateXmlCollection();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InvalidDataFromFileException, JAXBException {
        new ServerControlUnit();
    }
}
