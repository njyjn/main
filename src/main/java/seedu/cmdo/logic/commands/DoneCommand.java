package seedu.cmdo.logic.commands;

import seedu.cmdo.commons.core.Messages;
import seedu.cmdo.commons.core.UnmodifiableObservableList;
import seedu.cmdo.commons.exceptions.CantDoneBlockedSlotException;
import seedu.cmdo.model.task.Done;
import seedu.cmdo.model.task.ReadOnlyTask;
import seedu.cmdo.model.task.Task;
import seedu.cmdo.model.task.UniqueTaskList.TaskAlreadyDoneException;
import seedu.cmdo.model.task.UniqueTaskList.TaskNotFoundException;

//@@author A0141128R
/*
 * Marks a task as done, identified using it's last displayed index from CMDo.
 * The task is then only displayed in the done list and not shown in the general list
*/

public class DoneCommand extends Command {

    public static final String COMMAND_WORD = "done";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the task identified by the index number used in the last task listing as complete and deletes it from the list.\n"
            + "Parameters: <INDEX> (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DONE_TASK_SUCCESS = "Done task: %1$s";
    public static final String MESSAGE_ALREADY_DONE = "Already done!";
    public static final String MESSAGE_CANNOT_DONE = "You can't do a blocked timeslot... Right?";

    public final int targetIndex;

    public DoneCommand(int targetIndex) {
        this.targetIndex = targetIndex;
        this.isUndoable = true;
    }
    

    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        ReadOnlyTask taskToComplete = lastShownList.get(targetIndex - 1);

        try {
        	if(taskToComplete.getBlock() == true)
        		throw new CantDoneBlockedSlotException("");
        	if (taskToComplete.checkDone().value.equals(true))
        		throw new TaskAlreadyDoneException();
        	Task newTask = new Task(taskToComplete, new Done(true));
        	newTask.checkDone().setDone();
            model.doneTask(taskToComplete, newTask);
        } catch (TaskNotFoundException tnfe) {
            assert false : "The target task cannot be missing";
        } catch (TaskAlreadyDoneException tade) {
        	return new CommandResult(String.format(MESSAGE_ALREADY_DONE));
        } catch (CantDoneBlockedSlotException cdbs){
        	return new CommandResult(String.format(MESSAGE_CANNOT_DONE));
        }
        
        return new CommandResult(String.format(MESSAGE_DONE_TASK_SUCCESS, taskToComplete.getDetail().details));
    }

}