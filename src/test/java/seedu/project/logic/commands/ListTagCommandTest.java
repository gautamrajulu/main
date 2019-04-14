package seedu.project.logic.commands;

import static seedu.project.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import org.junit.Test;

import seedu.project.commons.core.Messages;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.UserPrefs;
import seedu.project.model.project.Project;

public class ListTagCommandTest {
    private Model model = new ModelManager(getTypicalProjectList(), new Project(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_listAtProjectLevel_failure() {
        model.setSelectedTask(null);
        LogicManager.setState(false);
        ListTagCommand listTagCommand = new ListTagCommand();
        String expectedMessage = String.format(Messages.MESSAGE_GO_TO_TASK_LEVEL, listTagCommand.COMMAND_WORD);
        assertCommandFailure(listTagCommand, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_listAllTags_success() {
        Model expectedModel = new ModelManager(getTypicalProjectList(), new Project(), new UserPrefs());

        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));
        expectedModel.setProject(model.getFilteredProjectList().get(0));
        expectedModel.setSelectedProject(model.getFilteredProjectList().get(0));

        LogicManager.setState(true);
        assertCommandSuccess(new ListTagCommand(), model, commandHistory, model.getTagWithTaskList(), expectedModel);
    }
}

