package seedu.project.logic.commands;

import static seedu.project.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;
import static seedu.project.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import org.junit.Test;

import seedu.project.commons.core.Messages;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.ProjectList;
import seedu.project.model.UserPrefs;
import seedu.project.model.project.Project;
import seedu.project.model.task.Task;
import seedu.project.testutil.TaskBuilder;

public class AnalyseCommandTest {
    private Model model = new ModelManager(getTypicalProjectList(), new Project(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_notAtProjectLevel_failure() {
        LogicManager.setState(true);
        model.setSelectedProject(null);
        AnalyseCommand analyseCommand = new AnalyseCommand();
        String expectedMessage = String.format(Messages.MESSAGE_RETURN_TO_PROJECT_LEVEL, AnalyseCommand.COMMAND_WORD);
        assertCommandFailure(analyseCommand, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_analyse_success() {
        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        LogicManager.setState(false);

        Task firstTask = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        Task taskAlreadyCompleted = new TaskBuilder(firstTask).withTags("completed").build();
        Model expectedModel = new ModelManager(
                new ProjectList(model.getProjectList()), new Project(model.getProject()), new UserPrefs());
        model.setTask(firstTask, taskAlreadyCompleted);

        AnalyseCommand analyseCommand = new AnalyseCommand();
        String expectedMessage = "Sample Project 1: 1 tasks completed. (Percentage of project completed: 16.7%)\n"
                + "Sample Project 2: 0 tasks completed. (Percentage of project completed: 0.0%)\n"
                + "Sample Project 3: 0 tasks completed. (Percentage of project completed: 0.0%)\n";

        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        model.commitProject();
        model.setProject(model.getSelectedProject(), (Project) model.getProject());
        model.commitProjectList();

        expectedModel.setProject(expectedModel.getFilteredProjectList().get(0));
        expectedModel.setSelectedProject(expectedModel.getFilteredProjectList().get(0));

        expectedModel.setTask(firstTask, taskAlreadyCompleted);
        expectedModel.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        expectedModel.commitProject();
        expectedModel.setProject(expectedModel.getSelectedProject(), (Project) expectedModel.getProject());
        expectedModel.commitProjectList();

        assertCommandSuccess(analyseCommand, model, commandHistory, expectedMessage, expectedModel);
    }
}
