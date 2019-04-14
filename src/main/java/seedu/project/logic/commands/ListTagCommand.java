package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.project.commons.core.Messages;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.model.Model;

/**
 * Lists all unique tags and their tasks to the user.
 */
public class ListTagCommand extends Command {
    public static final String COMMAND_ALIAS = "lt";
    public static final String COMMAND_WORD = "listtag";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows a list of all available tags prefix and its related tasks. " + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        if (!LogicManager.getState()) {
            throw new CommandException(String.format(Messages.MESSAGE_GO_TO_TASK_LEVEL, COMMAND_WORD));
        }

        String filteredTags = model.getTagWithTaskList();
        return new CommandResult(filteredTags);
    }

}
