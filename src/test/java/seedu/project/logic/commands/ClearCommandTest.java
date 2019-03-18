package seedu.project.logic.commands;

import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.testutil.TypicalTasks.getTypicalProject;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import org.junit.Test;

import seedu.project.logic.CommandHistory;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.UserPrefs;
import seedu.project.model.project.Project;

public class ClearCommandTest {

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_emptyProject_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        expectedModel.commitProject();

        assertCommandSuccess(new ClearCommand(), model, commandHistory, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyProject_success() {
        Model model = new ModelManager(getTypicalProjectList(), getTypicalProject(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalProjectList(), getTypicalProject(), new UserPrefs());
        expectedModel.setProject(new Project());
        expectedModel.commitProject();

        assertCommandSuccess(new ClearCommand(), model, commandHistory, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}