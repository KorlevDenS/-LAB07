import server.ScriptCommandManager;
import server.ServerStatusRegister;
import common.basic.MusicBand;
import server.JaxbManager;
import common.exceptions.InvalidDataFromFileException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;

import static server.JaxbManager.*;

@Deprecated
public class Main {

    static Scanner commandScanner = new Scanner(System.in);

    public static void main(String[] args) throws InvalidDataFromFileException {
        ServerStatusRegister.appleMusic = new HashSet<>();
        ServerStatusRegister.current = new Date();
        //try {
        //    ServerStatusRegister.currentXml = new File(System.getenv("COLLECTION_FILE"));
        //} catch (NullPointerException e) {
        //    System.out.println("Необходимая переменная окружения не задана. \n" +
        //            "Задайте переменную COLLECTION_FILE при помощи команды export c необходимым файлом xml.");
        //    System.exit(0);
        //}
        ServerStatusRegister.currentXml = new File("src/main/resources/MusicBandCollections.xml");
        try {
            JaxbManager manager = new JaxbManager();
           //manager.readXml();
           //manager.validateXmlData();
        } catch (JAXBException e) {
            System.out.println("Не удалось загрузить коллекцию из файла, нарушен формат XML.");
        }
        for (MusicBand band : ServerStatusRegister.appleMusic) {
            ServerStatusRegister.uniqueIdList.add(band.getId());
            if ((band.getFrontMan() != null)&&(band.getFrontMan().getPassportID() != null))
                ServerStatusRegister.passports.add(band.getFrontMan().getPassportID());
        }

        //idValidation();
        //passwordValidation();
        scanCommand();
    }

    public static void scanCommand() throws InvalidDataFromFileException {
        String line = commandScanner.nextLine();
        ScriptCommandManager manager = new ScriptCommandManager(line);
        manager.execution(manager.instructionFetch());
        if (line.equals("exit")) return;
        scanCommand();
    }
}
