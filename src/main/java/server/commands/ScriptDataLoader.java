package server.commands;

import server.JaxbManager;
import server.ScriptScanValidation;
import server.ServerStatusRegister;
import common.basic.*;
import common.exceptions.InvalidDataFromFileException;

import java.time.DateTimeException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.TimeZone;

/**
 * Class {@code ScriptDateLoader} is designed to create {@link MusicBand},
 * {@link Person} and {@link Coordinates} objects by data from scripts.
 */
public class ScriptDataLoader {

    /**
     * Loads from script a name of a new {@link MusicBand} object.
     *
     * @return not empty {@code String} name.
     * @throws InvalidDataFromFileException if name is empty.
     */
    protected String loadBandName() throws InvalidDataFromFileException {
        return ScriptScanValidation.ReadNextNonEmptyLine();
    }

    /**
     * Creates a new {@link Coordinates} object from script.
     *
     * @return valid {@link Coordinates} object with (X,Y) <= 381.
     * @throws InvalidDataFromFileException if coordinates in the script
     *                                      are not valid.
     */
    protected Coordinates loadBandCoordinates() throws InvalidDataFromFileException {
        int coordinateX = ScriptScanValidation.ReadNextInt();
        double coordinateY = ScriptScanValidation.ReadNextDouble();
        if ((coordinateX > 381) || (coordinateY > 381)) {
            throw new InvalidDataFromFileException("Значения координат превышают 381.");
        }
        return new Coordinates(coordinateX, coordinateY);
    }

    /**
     * Loads from script a number of participants of a new {@link MusicBand} object.
     *
     * @return {@code long} number of participants > 0.
     * @throws InvalidDataFromFileException if number of participants
     *                                      in the script <= 0 or is invalid.
     */
    protected long loadNumberOfParticipants() throws InvalidDataFromFileException {
        long numberOfParticipants = ScriptScanValidation.ReadNextLong();
        if (numberOfParticipants == 0) {
            throw new InvalidDataFromFileException("Количество участников должно быть больше нуля.");
        }
        return numberOfParticipants;
    }

    /**
     * Loads from script {@link MusicGenre} of a new {@link MusicBand} object.
     *
     * @return {@link MusicGenre} object.
     * @throws InvalidDataFromFileException if object in the script does not
     *                                      exist in {@link MusicGenre}.
     */
    protected MusicGenre loadBandMusicGenre() throws InvalidDataFromFileException {
        return ScriptScanValidation.ReadNextGenre();
    }

    /**
     * Loads name from script for new {@link Person} object.
     *
     * @return not empty {@code String} name.
     * @throws InvalidDataFromFileException if name in the script is empty.
     */
    protected String loadFrontManName() throws InvalidDataFromFileException {
        return ScriptScanValidation.ReadNextNonEmptyLine();
    }

    /**
     * Loads Height from script for new {@link Person} object.
     *
     * @return {@code long} height > 0.
     * @throws InvalidDataFromFileException if height in the script <= 0 or invalid.
     */
    protected long loadFrontManHeight() throws InvalidDataFromFileException {
        long frontManHeight = ScriptScanValidation.ReadNextLong();
        if (frontManHeight <= 0) {
            throw new InvalidDataFromFileException("Рост фронтмена должен быть больше нуля.");
        }
        return frontManHeight;
    }

    /**
     * Loads Weight from script for new {@link Person} object.
     *
     * @return {@code int} weight > 0.
     * @throws InvalidDataFromFileException if weight in the script <= 0 or invalid.
     */
    protected int loadFrontManWeight() throws InvalidDataFromFileException {
        int frontManWeight = ScriptScanValidation.ReadNextInt();
        if (frontManWeight <= 0) {
            throw new InvalidDataFromFileException("Вес фронтмена должен быть больше нуля.");
        }
        return frontManWeight;
    }

    /**
     * Loads PassportID from script for new {@link Person} object.
     *
     * @param addToCollection {@code true} if t is going to be added to the
     *                        collection with its music band.
     * @return @return {@code String} with length <= 29 or {@code null}
     * if user do not know PassportId. Method checks inputted id
     * for uniqueness if it is going to be added to the collection with
     * its music band.
     * @throws InvalidDataFromFileException if length > 29 or password
     *                                      is not unique.
     */
    protected String loadFrontManPassportID(boolean addToCollection) throws InvalidDataFromFileException {
        String frontManPassportId = ServerStatusRegister.scriptScanner.nextLine();
        if (frontManPassportId.equals("")) return null;
        if (frontManPassportId.length() > 29) {
            throw new InvalidDataFromFileException("Длинна строки превысила 29 символов.");
        }
        if ((ServerStatusRegister.passports.contains(frontManPassportId)) && (addToCollection)) {
            throw new InvalidDataFromFileException("Человек с введенным ID уже существует.");
        }
        return frontManPassportId;
    }

    /**
     * Loads birthday for new {@link Person} object if it is
     * known by user, else birthday is loaded as {@code null}.
     *
     * @return {@link ZonedDateTime} object.
     * @throws InvalidDataFromFileException birthday in script is invalid.
     */
    protected ZonedDateTime giveBirthFrontMan() throws InvalidDataFromFileException {
        String LineWithTime = ServerStatusRegister.scriptScanner.nextLine();
        if (LineWithTime.equals(""))
            return null;
        try {
            Scanner s = new Scanner(LineWithTime);
            return ZonedDateTime.of(s.nextInt(), s.nextInt(), s.nextInt(),
                    s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), ZoneId.of(TimeZone.getDefault().getID()));
        } catch (NoSuchElementException | DateTimeException ex) {
            throw new InvalidDataFromFileException("Дата рождения фронтмена записана некорректно.");
        }
    }

    /**
     * Loads a new valid {@link Person} object form script
     * using data loading methods.
     * {@link Person} object can be {@code null} if the user decides so.
     *
     * @return {@link Person} object from script.
     * @throws InvalidDataFromFileException if some fields in script are invalid.
     */
    protected Person loadFrontManFromData(boolean addToCollection) throws InvalidDataFromFileException {
        if (!Objects.equals(ServerStatusRegister.scriptScanner.nextLine(), "да")) return null;
        String frontManName = loadFrontManName();
        if (frontManName.equals("")) return null;
        long frontManHeight = loadFrontManHeight();
        int frontManWeight = loadFrontManWeight();
        String frontManPassportId = loadFrontManPassportID(addToCollection);
        ZonedDateTime frontManBirthday = giveBirthFrontMan();
        return new Person(frontManName, frontManHeight, frontManWeight, frontManBirthday, frontManPassportId);
    }

    private Long generateId() {
        long i = (long) (Math.random() * 100000 + 1);
        while (ServerStatusRegister.uniqueIdList.contains(i)) {
            i = (long) (Math.random() * 100000 + 1);
        }
        return i;
    }

    /**
     * Loads a new valid {@link MusicBand} object form script
     * using data loading methods.
     *
     * @return {@link MusicBand} object from script.
     * @throws InvalidDataFromFileException if some fields in script are invalid.
     */
    public MusicBand loadObjectFromData() throws InvalidDataFromFileException {
        String nameOfBand;
        Coordinates bandCoordinates;
        long numberOfParticipants;
        MusicGenre musicGenre;
        Person frontMan;
        nameOfBand = loadBandName();
        bandCoordinates = loadBandCoordinates();
        numberOfParticipants = loadNumberOfParticipants();
        musicGenre = loadBandMusicGenre();
        frontMan = loadFrontManFromData(true);
        if (!JaxbManager.readingXml) {
            MusicBand band = new MusicBand(nameOfBand, bandCoordinates, numberOfParticipants,
                    musicGenre, frontMan);
            band.setId(generateId());
            return band;
        } else {
            MusicBand xmlBand = new MusicBand(nameOfBand, bandCoordinates, numberOfParticipants,
                    musicGenre, frontMan);
            xmlBand.setCreationDate(ScriptScanValidation.ReadNextNonEmptyLine());
            xmlBand.setId(ScriptScanValidation.ReadNextLong());
            return xmlBand;
        }
    }
}
