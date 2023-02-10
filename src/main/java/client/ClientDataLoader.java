package client;

import common.ScanValidation;
import common.basic.*;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TimeZone;

public abstract class ClientDataLoader {
    /**
     * {@code Scanner} for getting users' input.
     */
    protected final Scanner scanner1 = new Scanner(System.in);

    /**
     * Loads from input a name of a new {@link MusicBand} object.
     *
     * @return not empty {@code String} name.
     */
    protected String loadBandName() {
        System.out.println("Введите название группы");
        return ScanValidation.nextNonEmptyLine();
    }

    /**
     * Creates a new {@link Coordinates} object from input.
     *
     * @return valid {@link Coordinates} object with (X,Y) <= 381.
     */
    protected Coordinates loadBandCoordinates() {
        System.out.println("Введите целочисленную координату Х (не более 381)");
        int coordinateX = ScanValidation.nextInt();
        while (coordinateX > 381) {
            System.out.println("Число должно быть не больше 381, введите корректное число");
            coordinateX = ScanValidation.nextInt();
        }
        System.out.println("Введите координату Y");
        double coordinateY = ScanValidation.nextDouble();
        while (coordinateY > 381) {
            System.out.println("Число должно быть не больше 381, введите корректное число");
            coordinateY = ScanValidation.nextDouble();
        }
        return new Coordinates(coordinateX, coordinateY);
    }

    /**
     * Loads from input a number of participants of a new {@link MusicBand} object.
     *
     * @return {@code long} number of participants > 0.
     */
    protected long loadNumberOfParticipants() {
        System.out.println("Введите количество участников группы.");
        long numberOfParticipants = ScanValidation.nextLong();
        while (numberOfParticipants <= 0) {
            System.out.println("В группе должно быть 1 и боле участников. Введите корректное количество.");
            numberOfParticipants = ScanValidation.nextLong();
        }
        return numberOfParticipants;
    }

    /**
     * Loads from input {@link MusicGenre} of a new {@link MusicBand} object.
     *
     * @return {@link MusicGenre} object.
     */
    protected MusicGenre loadBandMusicGenre() {
        System.out.println("Введите один из возможных жанров:");
        Arrays.stream(MusicGenre.values()).forEachOrdered(genre -> System.out.println(genre.toString()));
        return ScanValidation.nextGenre();
    }

    /**
     * Loads name for new {@link Person} object.
     *
     * @return not empty {@code String} name.
     */
    protected String loadFrontManName() {
        System.out.println("Введите имя фронтмена группы");
        return ScanValidation.nextNonEmptyLine();
    }

    /**
     * Loads Height for new {@link Person} object.
     *
     * @return {@code long} height > 0.
     */
    protected long loadFrontManHeight() {
        System.out.println("Введите рост фронтмена группы");
        long frontManHeight = ScanValidation.nextLong();
        while (frontManHeight <= 0) {
            System.out.println("Рост должен быть больше нуля, введите корректный рост.");
            frontManHeight = ScanValidation.nextLong();
        }
        return frontManHeight;
    }

    /**
     * Loads Weight for new {@link Person} object.
     *
     * @return {@code int} weight > 0.
     */
    protected int loadFrontManWeight() {
        System.out.println("Введите вес фронтмена группы");
        int frontManWeight = ScanValidation.nextInt();
        while (frontManWeight <= 0) {
            System.out.println("Вес должен быть больше нуля, введите корректный вес.");
            frontManWeight = ScanValidation.nextInt();
        }
        return frontManWeight;
    }

    /**
     * Loads PassportID for new {@link Person} object.
     *
     * @return {@code String} with length <= 29 or {@code null}
     * if user do not know PassportId. Method checks inputted id
     * for uniqueness.
     */
    protected String loadFrontManPassportID() {
        System.out.println("Введите уникальный номер паспорта фронтмена группы.");
        System.out.println("Если он неизвестен - пропустите.");
        String frontManPassportId = scanner1.nextLine();
        if (frontManPassportId.equals("")) return null;
        while ((frontManPassportId.length() > 29) || (ClientStatusRegister.passports.contains(frontManPassportId))) {
            if (frontManPassportId.length() > 29)
                System.out.println("Длинна строки не должна превышать 29 символов, введите ее правильно.");
            if (ClientStatusRegister.passports.contains(frontManPassportId))
                System.out.println("Человек с таким ID уже существует, попробуйте ввести другой");
            frontManPassportId = scanner1.nextLine();
        }
        return frontManPassportId;
    }

    /**
     * Loads birthday for new {@link Person} object if it is
     * known by user, or birthday is loaded as {@code null}.
     *
     * @return {@link ZonedDateTime} object.
     */
    protected ZonedDateTime giveBirthFrontMan() {
        String lineWithTime = scanner1.nextLine();
        if (lineWithTime.equals(""))
            return null;
        try {
            Scanner s = new Scanner(lineWithTime);
            return ZonedDateTime.of(s.nextInt(), s.nextInt(), s.nextInt(),
                    s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), ZoneId.of(TimeZone.getDefault().getID()));
        } catch (NoSuchElementException | DateTimeException e) {
            System.out.println("Дата была введена неверно, введите её корректно.");
            return giveBirthFrontMan();
        }
    }

    /**
     * Loads a new valid {@link Person} object form user's input
     * using data loading methods. Override these methods
     * if you need to upload otherwise.
     * {@link Person} object can be {@code null} if the user decides so.
     *
     * @return {@link Person} object from input data.
     */
    protected Person loadFrontMan() {
        System.out.println("В группе есть фронтмен ?");
        System.out.println("Если есть, то напишите \"да\", если нет - пропустите (Enter).");
        String answer = scanner1.nextLine();
        while (!answer.equals("да")) {
            if (answer.equals("")) return null;
            System.out.println("Неверный ввод, введите \"да\" или же пропустите (Enter).");
            answer = scanner1.nextLine();
        }
        String frontManName = loadFrontManName();
        long frontManHeight = loadFrontManHeight();
        int frontManWeight = loadFrontManWeight();
        String frontManPassportId = loadFrontManPassportID();
        System.out.println("Введите время рождения фронтмена (через пробел): год месяц день час мин. сек. м.сек.");
        System.out.println("Если оно не известно - пропустите этот пункт.");
        ZonedDateTime frontManBirthday = giveBirthFrontMan();
        return new Person(frontManName, frontManHeight, frontManWeight, frontManBirthday, frontManPassportId);
    }

    private Long generateId() {
        long i = (long) (Math.random() * 100000 + 1);
        while (ClientStatusRegister.uniqueIdList.contains(i)) {
            i = (long) (Math.random() * 100000 + 1);
        }
        return i;
    }

    /**
     * Loads a new valid {@link MusicBand} object form user's input
     * using data loading methods. Override these methods
     * if you need to upload otherwise.
     *
     * @return {@link MusicBand} object from input data.
     */
    public MusicBand loadObjectFromData() {
        String nameOfBand = loadBandName();
        Coordinates bandCoordinates = loadBandCoordinates();
        long numberOfParticipants = loadNumberOfParticipants();
        MusicGenre musicGenre = loadBandMusicGenre();
        Person frontMan = loadFrontMan();
        MusicBand band = new MusicBand(nameOfBand, bandCoordinates, numberOfParticipants,
                musicGenre, frontMan);
        band.setId(generateId());
        return band;
    }
}
