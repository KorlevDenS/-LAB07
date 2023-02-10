package client;

import common.ResultPattern;

import java.util.Objects;

public final class Demonstrator {
    private final ResultPattern resultOfCommand;

    public Demonstrator(ResultPattern resultOfCommand) {
        this.resultOfCommand = resultOfCommand;
    }

    public void demonstrateCommandResult() {
        resultOfCommand.getReports().forEach(System.out::println);
    }

    public ResultPattern resultOfCommand() {
        return resultOfCommand;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Demonstrator that = (Demonstrator) obj;
        return Objects.equals(this.resultOfCommand, that.resultOfCommand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultOfCommand);
    }

    @Override
    public String toString() {
        return "Demonstrator[" +
                "resultOfCommand=" + resultOfCommand + ']';
    }

}
