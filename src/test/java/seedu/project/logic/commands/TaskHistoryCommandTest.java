package seedu.project.logic.commands;

import static seedu.project.logic.commands.CommandTestUtil.VALID_NAME_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_NAME_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.logic.commands.CommandTestUtil.showTaskAtIndex;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;
import static seedu.project.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import java.util.ArrayList;

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

public class TaskHistoryCommandTest {
    private Model model = new ModelManager(getTypicalProjectList(), new Project(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexWithEdit_success() {

        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        showTaskAtIndex(model, INDEX_FIRST_TASK);

        // String taskId = Integer.toString(model.getFilteredTaskList().get(0).getTaskId());

        Task taskInFilteredList = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        Task editedTask1 = new TaskBuilder(taskInFilteredList).withName(VALID_NAME_CP2106).build();
        Task editedTask2 = new TaskBuilder(taskInFilteredList).withName(VALID_NAME_CS2101).build();
        TaskHistoryCommand taskHistoryCommand = new TaskHistoryCommand(INDEX_FIRST_TASK);
        String taskId = Integer.toString(editedTask2.getTaskId());


        String tempCurrent1 = "edit 1 n/Orbital Project";
        String tempCurrent2 = "edit 1 n/Milestone";
        ArrayList<String> commandList = new ArrayList<>();
        commandList.add(tempCurrent2);
        commandList.add(tempCurrent1);
        commandHistory.add("select 1");
        commandHistory.addHistoryTaskId(taskId);
        commandHistory.add(tempCurrent1);
        commandHistory.addHistoryTaskId(taskId);
        commandHistory.add(tempCurrent2);


        String expectedMessage = String.format(TaskHistoryCommand.MESSAGE_SUCCESS,
                INDEX_FIRST_TASK.getOneBased(), String.join("\n", commandList));

        Model expectedModel = new ModelManager(
                new ProjectList(model.getProjectList()), new Project(model.getProject()), new UserPrefs());

        expectedModel.setProject(model.getFilteredProjectList().get(0));
        expectedModel.setSelectedProject(model.getFilteredProjectList().get(0));
        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        LogicManager.setState(true);

        //Add first task
        expectedModel.setTask(model.getFilteredTaskList().get(0), editedTask1);
        expectedModel.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        expectedModel.commitProject();
        expectedModel.setProject(expectedModel.getSelectedProject(),
                (Project) expectedModel.getProject()); //sync project list
        expectedModel.commitProjectList();

        //Add second task
        expectedModel.setTask(expectedModel.getFilteredTaskList().get(0), editedTask2);
        expectedModel.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        expectedModel.commitProject();
        expectedModel.setProject(expectedModel.getSelectedProject(),
                (Project) expectedModel.getProject()); //sync project list
        expectedModel.commitProjectList();

        //Add first task
        model.setTask(model.getFilteredTaskList().get(0), editedTask1);
        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        model.commitProject();
        if (model.getProject().getClass().equals(VersionedProject.class)) {
            model.setProject(model.getSelectedProject(), (VersionedProject) model.getProject());
        } else {
            model.setProject(model.getSelectedProject(), (Project) model.getProject());
        }
        model.commitProjectList();

        //Add second task
        model.setTask(model.getFilteredTaskList().get(0), editedTask2);
        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        model.commitProject();
        if (model.getProject().getClass().equals(VersionedProject.class)) {
            model.setProject(model.getSelectedProject(), (VersionedProject) model.getProject());
        } else {
            model.setProject(model.getSelectedProject(), (Project) model.getProject());
        }
        model.commitProjectList();

        //String expectedMessage = String.format(compareCommand.MESSAGE_COMPARE_TASK_SUCCESS,
        //tempString.get(0), tempString.get(1));

        assertCommandSuccess(taskHistoryCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexWithNoEdit_success() {

        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        TaskHistoryCommand taskHistoryCommand = new TaskHistoryCommand(INDEX_FIRST_TASK);

        showTaskAtIndex(model, INDEX_FIRST_TASK);

        // String taskId = Integer.toString(model.getFilteredTaskList().get(0).getTaskId());

        //String expectedMessage = String.format(TaskHistoryCommand.MESSAGE_SUCCESS,
        // INDEX_FIRST_TASK.getOneBased(), String.join("\n", commandList));

        Model expectedModel = new ModelManager(
                new ProjectList(model.getProjectList()), new Project(model.getProject()), new UserPrefs());

        expectedModel.setProject(model.getFilteredProjectList().get(0));
        expectedModel.setSelectedProject(model.getFilteredProjectList().get(0));
        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        LogicManager.setState(true);

        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        model.commitProject();
        if (model.getProject().getClass().equals(VersionedProject.class)) {
            model.setProject(model.getSelectedProject(), (VersionedProject) model.getProject());
        } else {
            model.setProject(model.getSelectedProject(), (Project) model.getProject());
        }
        model.commitProjectList();

        expectedModel.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        expectedModel.commitProject();
        expectedModel.setProject(expectedModel.getSelectedProject(),
                (Project) expectedModel.getProject()); //sync project list
        expectedModel.commitProjectList();

        assertCommandSuccess(taskHistoryCommand, model, commandHistory,
                TaskHistoryCommand.MESSAGE_NO_HISTORY, expectedModel);
    }

    @Test
    public void execute_invalidIndex_failure() {

        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        showTaskAtIndex(model, INDEX_FIRST_TASK);

        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);

        TaskHistoryCommand taskHistoryCommand = new TaskHistoryCommand(outOfBoundsIndex);

        assertCommandFailure(taskHistoryCommand, model, commandHistory, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }
}
