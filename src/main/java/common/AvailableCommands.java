package common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * All available instructions of this program are enumerated in {@code AvailableCommands enum}.
 * Their titles with patterns of simple arguments and descriptions are also here.
 * It is necessary to include here a new constant and its relevant data when adding
 * a new instruction to execute to this program.
 */
public enum AvailableCommands {
    HELP("help", "", "вывести справку по доступным командам"),
    INFO("info", "", "вывести в стандартный поток вывода информацию о коллекции" +
            " (тип, дата инициализации, количество элементов и т.д.)"),
    SHOW("show", "", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении"),
    ADD("add", "MusicBand", "{element}: добавить новый элемент в коллекцию"),
    UPDATE("update [0-9]+$", "MusicBand", "id {element}: обновить значение элемента коллекции, id которого равен заданному"),
    REMOVE_BY_ID("remove_by_id [0-9]+$", "", "id :удалить элемент из коллекции по его id"),
    CLEAR("clear", "", "очистить коллекцию"),
    SAVE("save", "", "сохранить коллекцию в файл"),
    EXECUTE_SCRIPT("execute_script .*\\.txt$", "Script", "file_name :считать и исполнить скрипт из указанного файла." +
            " В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме."),
    EXIT("exit", "", "завершить программу (без сохранения в файл)"),
    ADD_IF_MAX("add_if_max", "MusicBand", "{element} : добавить новый элемент в коллекцию, если его значение" +
            " превышает значение наибольшего элемента этой коллекции"),
    ADD_IF_MIN("add_if_min", "MusicBand", "{element} : добавить новый элемент в коллекцию, если его значение меньше, " +
            "чем у наименьшего элемента этой коллекции"),
    REMOVE_GREATER("remove_greater", "MusicBand", "{element} : удалить из коллекции все элементы, превышающие заданный"),
    REMOVE_ALL_BY_FRONT_MAN("remove_all_by_front_man", "FrontMan", "frontMan :удалить из коллекции все элементы," +
            " значение поля frontMan которого эквивалентно заданному"),
    GROUP_COUNTING_BY_FRONT_MAN("group_counting_by_front_man", "", "сгруппировать элементы коллекции " +
            "по значению поля frontMan, вывести количество элементов в каждой группе"),
    PRINT_UNIQUE_NUMBER_OF_PARTICIPANTS("print_unique_number_of_participants", "", "вывести уникальные " +
            "значения поля numberOfParticipants всех элементов в коллекции");

    private final String title;
    private final String description;
    private final String argumentTitle;

    /**
     * Creates  {@code AvailableCommands Enum} constant.
     */
    AvailableCommands(String title, String argumentTitle, String description) {
        this.title = title;
        this.description = description;
        this.argumentTitle = argumentTitle;
    }

    /**
     * Method makes regular expression from title of command.
     *
     * @param input string to compare with pattern.
     * @return regular expression (matcher).
     */
    public Matcher getRegex(String input) {
        Pattern pattern = Pattern.compile(title);
        return pattern.matcher(input);
    }

    public String getDescription() {
        return this.description;
    }

    public String getArgumentTitle() {
        return this.argumentTitle;
    }

    public String getTitle() {
        return this.title;
    }
}
