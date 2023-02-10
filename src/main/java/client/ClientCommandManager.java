package client;

import common.AvailableCommands;
import common.CommandManagement;
import common.InstructionPattern;
import common.exceptions.InvalidDataFromFileException;

import java.util.Arrays;
import java.util.Scanner;

public class ClientCommandManager extends ClientDataLoader implements CommandManagement<AvailableCommands, InstructionPattern, AvailableCommands> {

    private String instructionTitle;

    public ClientCommandManager(String instructionTitle) {
        this.instructionTitle = instructionTitle;
    }

    public AvailableCommands instructionFetch() {
        for (AvailableCommands command : AvailableCommands.values()) {
            if (command.getRegex(instructionTitle).matches()) {
                return command;
            }
        }
        System.out.println("Команда не существует или введена некорректно");
        System.out.println("Введите одну из доступных команд из списка:");
        Arrays.stream(AvailableCommands.values()).forEachOrdered(s -> System.out.println(s.getTitle()));
        Scanner scanner = new Scanner(System.in);
        instructionTitle = scanner.nextLine();
        return instructionFetch();
    }

    public String operandFetch() {
        Scanner scanner = new Scanner(instructionTitle);
        scanner.next();
        if (scanner.hasNext()) return scanner.next();
        else return "";
    }

    public InstructionPattern execution(AvailableCommands command) throws InvalidDataFromFileException {
        InstructionPattern instructionPattern = new InstructionPattern(command, operandFetch());
        instructionPattern.setInstructionType(command.toString());
        chooseAndLoadArguments(instructionPattern);
        return instructionPattern;
    }

    public void chooseAndLoadArguments(InstructionPattern pattern) throws InvalidDataFromFileException {
        switch (pattern.getArgumentTitle()) {
            case ("MusicBand"):
                pattern.setMusicBand(super.loadObjectFromData());
                break;
            case ("FrontMan"):
                pattern.setFrontMan(loadFrontMan());
                break;
            case ("Script"):
                RecursiveScriptReader scriptReader = new RecursiveScriptReader(pattern.getOperand());
                scriptReader.installScriptData();
                pattern.setInfoData(scriptReader.getInfoData());
                pattern.setMistakesInfo(scriptReader.getMistakesInfo());
                pattern.setCommandsAndData(scriptReader.getCommandsAndData());
            default:
                break;
        }
    }

    @Override
    protected String loadFrontManPassportID() {
        System.out.println("Введите уникальный номер паспорта фронтмена группы.");
        System.out.println("Если он неизвестен - пропустите.");
        String frontManPassportId = scanner1.nextLine();
        if (frontManPassportId.equals("")) return null;
        while ((frontManPassportId.length() > 29)) {
            System.out.println("Длинна строки не должна превышать 29 символов, введите ее правильно.");
            frontManPassportId = scanner1.nextLine();
        }
        return frontManPassportId;
    }
}
