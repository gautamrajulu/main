package seedu.project.logic.commands;

import static seedu.project.logic.commands.CommandTestUtil.VALID_NAME_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.logic.commands.CommandTestUtil.showTaskAtIndex;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;
import static seedu.project.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import java.util.List;

import org.junit.Test;

import seedu.project.commons.core.Messages;
import seedu.project.commons.core.index.Index;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.ProjectList;
import seedu.project.model.UserPrefs;
import seedu.project.model.project.Project;
import seedu.project.model.project.VersionedProject;
import seedu.project.model.task.Task;
import seedu.project.testutil.TaskBuilder;

public class CompareCommandTest {
    private Model model = new ModelManager(getTypicalProjectList(), new Project(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexWithEdit_success() {

        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        showTaskAtIndex(model, INDEX_FIRST_TASK);

        Task taskInFilteredList = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        Task editedTask = new TaskBuilder(taskInFilteredList).withName(VALID_NAME_CP2106).build();
        editedTask.updateTaskId(taskInFilteredList.getTaskId());
        CompareCommand compareCommand = new CompareCommand(INDEX_FIRST_TASK);

        String tempCurrent = "Name: Orbital Project";
        String tempCompared = "Name: Group meeting";
        String expectedMessage = String.format(compareCommand.MESSAGE_COMPARE_TASK_SUCCESS, tempCurrent, tempCompared);

        Model expectedModel = new ModelManager(
                new ProjectList(model.getProjectList()), new Project(model.getProject()), new UserPrefs());

        expectedModel.setProject(model.getFilteredProjectList().get(0));
        expectedModel.setSelectedProject(model.getFilteredProjectList().get(0));
        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        LogicManager.setState(true);

        expectedModel.setTask(expectedModel.getFilteredTaskList().get(0), editedTask);
        expectedModel.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        expectedModel.commitProject();

        expectedModel.setProject(expectedModel.getSelectedProject(),
                (Project) expectedModel.getProject()); //sync project list
        expectedModel.commitProjectList();

        model.setTask(model.getFilteredTaskList().get(0), editedTask);
        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        model.commitProject();

        if (model.getProject().getClass().equals(VersionedProject.class)) {
            model.setProject(model.getSelectedProject(), (VersionedProject) model.getProject());
        } else {
            model.setProject(model.getSelectedProject(), (Project) model.getProject());
        }
        model.commitProjectList();

        List<String> tempString = expectedModel.compareTask(expectedModel.getFilteredTaskList().get(0));

        //String expectedMessage = String.format(compareCommand.MESSAGE_COMPARE_TASK_SUCCESS,
        // tempString.get(0), tempString.get(1));

        assertCommandSuccess(compareCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_failure() {

        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        showTaskAtIndex(model, INDEX_FIRST_TASK);

        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);

        CompareCommand compareCommand = new CompareCommand(outOfBoundsIndex);

        assertCommandFailure(compareCommand, model, commandHistory, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexWithNoEdit_success() {

        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        showTaskAtIndex(model, INDEX_FIRST_TASK);

        CompareCommand compareCommand = new CompareCommand(INDEX_FIRST_TASK);

        Model expectedModel = new ModelManager(
                new ProjectList(model.getProjectList()), new Project(model.getProject()), new UserPrefs());

        expectedModel.setProject(model.getFilteredProjectList().get(0));
        expectedModel.setSelectedProject(model.getFilteredProjectList().get(0));

        LogicManager.setState(true);

        expectedModel.commitProject();

        expectedModel.setProject(expectedModel.getSelectedProject(),
                (Project) expectedModel.getProject()); //sync project list
        expectedModel.commitProjectList();

        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        model.commitProject();

        if (model.getProject().getClass().equals(VersionedProject.class)) {
            model.setProject(model.getSelectedProject(), (VersionedProject) model.getProject());
        } else {
            model.setProject(model.getSelectedProject(), (Project) model.getProject());
        }
        model.commitProjectList();

        List<String> tempString = expectedModel.compareTask(expectedModel.getFilteredTaskList().get(0));

        assertCommandSuccess(compareCommand, model, commandHistory,
                compareCommand.MESSAGE_COMPARE_TASK_FAILURE, expectedModel);
    }
}
