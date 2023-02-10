package common;

import common.basic.MusicBand;
import common.basic.Person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class InstructionPattern implements Serializable {

    private final String argumentTitle;
    private final String titleRegex;
    private final String description;
    private String instructionType;
    private MusicBand musicBand;
    private Person frontMan;
    private String operand;
    protected ArrayList<String> infoData;
    protected String commandsAndData;
    protected LinkedHashMap<String, String> mistakesInfo;

    public InstructionPattern(AvailableCommands command, String operand) {
        this.operand = operand;
        this.instructionType = command.toString();
        this.titleRegex = command.getTitle();
        this.description = command.getDescription();
        this.argumentTitle = command.getArgumentTitle();
    }

    public void setCommandsAndData(String commandsAndData) {
        this.commandsAndData = commandsAndData;
    }

    public void setMistakesInfo(LinkedHashMap<String,String> mistakesInfo) {
        this.mistakesInfo = mistakesInfo;
    }

    public void setInfoData(ArrayList<String> infoData) {
        this.infoData = infoData;
    }

    public void setFrontMan(Person frontMan) {
        this.frontMan = frontMan;
    }

    public void setMusicBand(MusicBand band) {
        this.musicBand = band;
    }


    public void setInstructionType(String type) {
        this.instructionType = type;
    }

    public String getInstructionType() {
        return instructionType;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public String getArgumentTitle() {
        return argumentTitle;
    }

    public String getTitleRegex() {
        return titleRegex;
    }

    public String getDescription() {
        return description;
    }

    public MusicBand getMusicBand() {
        return musicBand;
    }

    public Person getFrontMan() {
        return frontMan;
    }

    public ArrayList<String> getInfoData() {
        return this.infoData;
    }

    public String getCommandsAndData() {
        return this.commandsAndData;
    }

    public LinkedHashMap<String, String> getMistakesInfo() {
        return this.mistakesInfo;
    }

}
