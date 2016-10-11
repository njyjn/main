package seedu.address.logic.commands;


/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed uncompleted task";

    public ListCommand() {}

    @Override
    public CommandResult execute() {
        model.updateFilteredListShowUndoneTask();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
