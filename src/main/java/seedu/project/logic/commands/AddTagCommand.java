package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.project.logic.parser.CliSyntax.PREFIX_GROUPTAG;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;

import java.util.List;

import seedu.project.commons.core.Messages;
import seedu.project.commons.core.index.Index;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.model.Model;

import seedu.project.model.project.Project;

import seedu.project.model.project.VersionedProject;
import seedu.project.model.task.Task;

/**
 * Marks an existing task as completed and removes it from UI display
 */
public class AddTagCommand extends Command {
    public static final String COMMAND_ALIAS = "at";
    public static final String COMMAND_WORD = "addtag";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a group tag to the task.\n"
            + "Parameters: INDEX (must be a positive integer) " + PREFIX_GROUPTAG + "GROUPTAG\t" + "Example: "
            + COMMAND_WORD + " 1 " + PREFIX_GROUPTAG + "Consultation";
    public static final String MESSAGE_COMPLETED_SUCCESS = "Group tag %1$s applied to task.";
    public static final String MESSAGE_GROUPTAG_NOT_FOUND = "Group tag %1$s not found, please use "
            + DefineTagCommand.COMMAND_WORD + " to add them first.";

    private final Index index;
    private final String groupTag;

    /**
     * @param index of the task in the filtered task list to be completed
     */
    public AddTagCommand(Index index, String groupTag) {
        requireNonNull(index);
        this.index = index;
        this.groupTag = groupTag;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        if (!LogicManager.getState()) {
            throw new CommandException(String.format(Messages.MESSAGE_GO_TO_TASK_LEVEL, COMMAND_WORD));
        } else if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        int taskId;
        Task targetTask = lastShownList.get(index.getZeroBased());
        Task taskToAdd = new Task(targetTask.getName(), targetTask.getDescription(), targetTask.getDeadline(),
                targetTask.getTags());
        taskId = targetTask.getTaskId();
        targetTask.updateTaskId(taskId);

        history.addHistoryTaskId(Integer.toString(taskId));

        Boolean[] groupExists = { false };
        model.getGroupTagList().forEach(groupTag -> {
            if (groupTag.getName().toString().equals(this.groupTag)) {
                groupTag.getTags().forEach(tag -> {
                    taskToAdd.addTag(tag);
                    groupExists[0] = true;
                });
            }
        });

        if (!groupExists[0]) {
            throw new CommandException(String.format(MESSAGE_GROUPTAG_NOT_FOUND, this.groupTag));
        }
        model.setTask(targetTask, taskToAdd);
        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        model.commitProject();

        if (model.getProject().getClass().equals(VersionedProject.class)) {
            model.setProject(model.getSelectedProject(), (VersionedProject) model.getProject());
        } else {
            model.setProject(model.getSelectedProject(), (Project) model.getProject());
        }
        model.commitProjectList();

        return new CommandResult(String.format(MESSAGE_COMPLETED_SUCCESS, this.groupTag));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddTagCommand)) {
            return false;
        }

        // state check
        AddTagCommand e = (AddTagCommand) other;
        return index.equals(e.index);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("index: ").append(index);
        return builder.toString();
    }
}
