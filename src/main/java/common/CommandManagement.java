package common;

import common.exceptions.InvalidDataFromFileException;

/**
 * Interface {@code CommandManagement} requires implementing classes
 * to realise methods to manage commands.
 */
public interface CommandManagement<T, U, S> {
    /**
     * Method produces selection and validation of user instruction.
     * If there are no such instruction in {@link AvailableCommands}
     * {@code InstructionFetch} will ask user to enter valid one
     * from the list of available commands.
     *
     * @return {@link AvailableCommands} object.
     */
    T instructionFetch();


    /**
     * Method is used to execute current instruction.
     * {@code execution} creates valid {@code Command} objects
     * Method also passes parameters to {@code Command}  objects.
     *
     * @param command {@link AvailableCommands} object.
     */
    U execution(S command) throws InvalidDataFromFileException;
}
