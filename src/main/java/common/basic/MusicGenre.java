package common.basic;

import java.io.Serializable;

/**
 * A set of {@code Enum} constants with names of available music genres.
 * It is used as a parameter to create MusicBand object.
 */
public enum MusicGenre implements Serializable {

    PSYCHEDELIC_ROCK("Psychedelic Rock"),
    PSYCHEDELIC_CLOUD_RAP("Psychedelic Cloud Rap"),
    POST_ROCK("Post Rock"),
    POST_PUNK("Post Punk"),
    BRIT_POP("Post Pop");

    private final String description;

    /**
     * Creates MusicGenre {@code Enum} constant.
     */
    MusicGenre(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
