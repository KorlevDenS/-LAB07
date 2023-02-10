package common.basic;

import common.exceptions.InvalidDataFromFileException;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Class {@code MusicBand} makes an object that represents a music
 * band and keeps its data.
 */
@XmlRootElement(name = "MusicBand")
@XmlType(propOrder = {"id", "name", "numberOfParticipants", "frontMan", "genre",
        "coordinates", "creationDate"})
public class MusicBand implements Comparable<MusicBand>, Serializable {


    /**
     * Unique id of {@code MusicBand} object. The field shouldn't be {@code null}
     * and should be more than 0. Its value is generated automatically
     * by method {@link MusicBand#generateId().}
     */
    private Long id;
    /**
     * Name of {@code MusicBand} object. Cannot be {@code null} or empty.
     */
    private String name;
    /**
     * Position on of {@code MusicBand} object. Cannot be {@code null}.
     */
    private Coordinates coordinates;
    /**
     * Creation date of music band. The field shouldn't be {@code null}.
     * Its value is generated automatically by method {@link MusicBand#generateDate().}
     */
    private java.time.LocalDate creationDate;
    /**
     * Number of music band participants. Should be greater than 0.
     */
    private long numberOfParticipants;
    /**
     * Music genre of music band from {@link MusicGenre}. Cannot be {@code null}.
     */
    private MusicGenre genre;
    /**
     * FrontMan of the music band. Can be {@code null}.
     */
    private Person frontMan;

    /**
     * Constructs {@code MusicBand} object for Xml.
     */
    public MusicBand() {
    }

    /**
     * Constructs a new music band.
     *
     * @param name                 - for {@link MusicBand#name}
     * @param coordinates          - for {@link MusicBand#coordinates}
     * @param numberOfParticipants - for {@link MusicBand#numberOfParticipants}
     * @param genre                - for {@link MusicBand#genre}
     * @param frontMan             - for {@link MusicBand#frontMan}
     */
    public MusicBand(String name, Coordinates coordinates, long numberOfParticipants, MusicGenre genre, Person frontMan) {
        this.name = name;
        this.coordinates = coordinates;
        this.numberOfParticipants = numberOfParticipants;
        this.genre = genre;
        this.frontMan = frontMan;
        this.creationDate = generateDate();
        this.id = generateId();
    }

    /**
     * Special constructor to use only for updating {@code MusicBand} object in Collections.
     *
     * @param id can be taken only from {@code MusicBand} objects with an already generated id.
     */
    public MusicBand(String name, Coordinates coordinates, long numberOfParticipants, MusicGenre genre,
                     Person frontMan, long id) {
        this.name = name;
        this.coordinates = coordinates;
        this.numberOfParticipants = numberOfParticipants;
        this.genre = genre;
        this.creationDate = generateDate();
        this.frontMan = frontMan;
        this.id = id;
    }

    /**
     * Generates unique {@link MusicBand#id}
     * not to repeat the id of already created music band.
     *
     * @return unique id of MusicBand.
     */
    private Long generateId() {
        return (Long) (long) (Math.random() * 100000 + 1);
    }

    /**
     * Generates a date of music band creation since 1970
     * up to last year at the time of execution.
     *
     * @return creation date of the music band.
     */
    private java.time.LocalDate generateDate() {
        LocalDate freshDate = LocalDate.now();
        int maxYear = freshDate.getYear();
        int creationYear = (int) ((Math.random() * (maxYear - 1970)) + 1970);
        int creationMonth = (int) ((Math.random() * (13 - 1)) + 1);
        int creationDay = (int) ((Math.random() * (29 - 1)) + 1);
        return LocalDate.of(creationYear, creationMonth, creationDay);
    }

    /**
     * @return {@link MusicBand#numberOfParticipants} of the object.
     */
    public long getNumberOfParticipants() {
        return this.numberOfParticipants;
    }

    public void setNumberOfParticipants(long numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    /**
     * @return {@link  MusicBand#frontMan} of the object.
     */
    public Person getFrontMan() {
        if (frontMan == null) return null;
        return this.frontMan;
    }

    /**
     * @param frontMan to set field {@link MusicBand#frontMan}.
     */
    public void setFrontMan(Person frontMan) {
        this.frontMan = frontMan;
    }

    /**
     * @return {@link MusicBand#id} of the object.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * @param id to set field {@link MusicBand#id}.
     */
    public void setId(Long id) {
        if (id == null) {
            this.id = null;
            return;
        }
        this.id = id;
    }

    /**
     * @param coordinates to set field {@link MusicBand#coordinates}.
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * @return {@link  MusicBand#coordinates} of the object.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return {@link  MusicBand#name} of the object.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name to set field {@link MusicBand#name}.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Parses string representation of creation date for
     * interaction with xml file. Class {@link LocalDate}
     * is not adapted to work with JAXB.
     *
     * @param date to set field {@link MusicBand#creationDate}.
     */
    public void setCreationDate(String date) throws InvalidDataFromFileException {
        try {
            String[] dates = date.split("-");
            int year = Integer.parseInt(dates[0]);
            int month = Integer.parseInt(dates[1]);
            int day = Integer.parseInt(dates[2]);
            this.creationDate = LocalDate.of(year, month, day);
        } catch (NumberFormatException e) {
            throw new InvalidDataFromFileException("Дата создания группы указана с ошибкой.");
        }
    }

    /**
     * Parses {@link LocalDate} representation of creation date for
     * interaction with xml file. Class {@link LocalDate}
     * is not adapted to work with JAXB.
     *
     * @return string representation of {@link LocalDate} object.
     */
    public String getCreationDate() {
        return creationDate.toString();
    }

    /**
     * @return {@link  MusicBand#genre} of the object.
     */
    public MusicGenre getGenre() {
        return genre;
    }

    /**
     * @param genre to set field {@link MusicBand#genre}.
     */
    public void setGenre(MusicGenre genre) {
        this.genre = genre;
    }

    /**
     * Compares two MusicBand objects using numbers of their participants.
     *
     * @param anotherBand {@link MusicBand#numberOfParticipants} of another
     *                    {@code MusicBand} object to compare with this one.
     * @return numerical difference.
     */
    @Override
    public int compareTo(MusicBand anotherBand) {
        return (int) (this.numberOfParticipants - anotherBand.numberOfParticipants);
    }

    /**
     * Compares this object to the specified object.
     *
     * @param obj the object to compare with.
     * @return {@code true} if the unique {@link MusicBand#id}s
     * of the objets are the same, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass())
            return false;
        MusicBand otherObj = (MusicBand) obj;
        return id.equals(otherObj.id);
    }

    /**
     * Returns a hash code using values of not {@code null} MusicBand fields.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        try {
            return this.name.hashCode()
                    + this.id.hashCode()
                    + this.coordinates.hashCode()
                    + this.genre.hashCode();
        } catch (NullPointerException e) {
            return (int) (Math.random() * 100000);
        }
    }

    /**
     * Returns a {@code String} object representing this {@code MusicBand} value.
     *
     * @return a string representation of the value of this object.
     */
    @Override
    public String toString() {
        return getClass().getName()
                + "[id=" + id
                + ";name=" + name
                + ";creationDate=" + creationDate
                + ";coordinates=" + coordinates
                + ";numberOfParticipants=" + numberOfParticipants
                + ";genre=" + genre
                + ";frontMan=" + frontMan
                + "]";
    }

    public String toScriptString() {
        if (frontMan == null)
            return name + "\n"
                    + coordinates.getX() + "\n"
                    + coordinates.getY() + "\n"
                    + numberOfParticipants + "\n"
                    + genre + "\n"
                    + "" + "\n"
                    + creationDate + "\n"
                    + id + "\n";
        Optional<String> nullablePassword = Optional.ofNullable(frontMan.getPassportID());
        Optional<String> nullableBirthday = Optional.ofNullable(frontMan.getBirthdayForScript());
        return name + "\n"
                + coordinates.getX() + "\n"
                + coordinates.getY() + "\n"
                + numberOfParticipants + "\n"
                + genre + "\n"
                + "да" + "\n"
                + frontMan.getName() + "\n"
                + frontMan.getHeight() + "\n"
                + frontMan.getWeight() + "\n"
                + nullablePassword.orElse("") + "\n"
                + nullableBirthday.orElse("") + "\n"
                + creationDate + "\n"
                + id + "\n";
    }
}
