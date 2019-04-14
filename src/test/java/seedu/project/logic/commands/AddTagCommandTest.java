package seedu.project.logic.commands;

import static seedu.project.logic.commands.CommandTestUtil.VALID_NAME_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_TAG_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_TAG_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;
import static seedu.project.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import seedu.project.commons.core.Messages;
import seedu.project.commons.core.index.Index;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.Name;
import seedu.project.model.ProjectList;
import seedu.project.model.UserPrefs;
import seedu.project.model.project.Project;
import seedu.project.model.tag.GroupTag;
import seedu.project.model.tag.Tag;
import seedu.project.model.task.Task;
import seedu.project.testutil.TaskBuilder;

public class AddTagCommandTest {
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
    public void execute_invalidIndex_failure() {
        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));
        LogicManager.setState(true);

        Set<Tag> sampleTargetSet = new HashSet<Tag>(
                Arrays.asList(new Tag(VALID_TAG_CS2101), new Tag(VALID_TAG_CP2106)));
        Name sampleTargetName = new Name(VALID_NAME_CP2106);

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);
        GroupTag sampleGroupTag = new GroupTag(sampleTargetName, sampleTargetSet);
        AddTagCommand addTagCommand = new AddTagCommand(outOfBoundIndex, sampleGroupTag.getName().toString());
        assertCommandFailure(addTagCommand, model, commandHistory, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexWithoutDefineTag_failure() {
        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));

        Index indexLastTask = Index.fromOneBased(model.getFilteredTaskList().size());
        Task lastTask = model.getFilteredTaskList().get(indexLastTask.getZeroBased());
        TaskBuilder listOfTask = new TaskBuilder(lastTask);
        Task taskWithGroup = listOfTask.withTags(VALID_TAG_CS2101, VALID_TAG_CP2106).build();

        Set<Tag> sampleTargetSet = new HashSet<Tag>(
                Arrays.asList(new Tag(VALID_TAG_CS2101), new Tag(VALID_TAG_CP2106)));
        GroupTag sampleGroupTag = new GroupTag(lastTask.getName(), sampleTargetSet);
        AddTagCommand addTagCommand =
                new AddTagCommand(INDEX_FIRST_TASK, sampleGroupTag.getName().toString());

        String expectedMessage = String.format(
                addTagCommand.MESSAGE_GROUPTAG_NOT_FOUND, sampleGroupTag.getName().toString());
        Model expectedModel = new ModelManager(
                new ProjectList(model.getProjectList()), new Project(model.getProject()), new UserPrefs());

        expectedModel.setProject(model.getFilteredProjectList().get(0));
        expectedModel.setSelectedProject(model.getFilteredProjectList().get(0));

        LogicManager.setState(true);

        expectedModel.setTask(lastTask, taskWithGroup);
        expectedModel.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        expectedModel.commitProject();

        expectedModel.setProject(
                expectedModel.getSelectedProject(), (Project) expectedModel.getProject());
        expectedModel.commitProjectList();

        assertCommandFailure(addTagCommand, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_validIndexWithDefineTag_success() {
        model.setProject(model.getFilteredProjectList().get(0));
        model.setSelectedProject(model.getFilteredProjectList().get(0));
        LogicManager.setState(true);

        Model expectedModel = new ModelManager(
                new ProjectList(model.getProjectList()), new Project(model.getProject()), new UserPrefs());

        Index indexLastTask = Index.fromOneBased(model.getFilteredTaskList().size());
        Task lastTask = model.getFilteredTaskList().get(indexLastTask.getZeroBased());
        TaskBuilder listOfTask = new TaskBuilder(lastTask);
        Task taskWithGroup = listOfTask.withTags(VALID_TAG_CS2101, VALID_TAG_CP2106, "TUTORIAL").build();

        int taskId = lastTask.getTaskId();
        taskWithGroup.updateTaskId(taskId);
        commandHistory.addHistoryTaskId(Integer.toString(taskId));

        Set<Tag> sampleTargetSet = new HashSet<Tag>(
                Arrays.asList(new Tag(VALID_TAG_CS2101), new Tag(VALID_TAG_CP2106)));
        GroupTag sampleGroupTag = new GroupTag(lastTask.getName(), sampleTargetSet);
        AddTagCommand addTagCommand = new AddTagCommand(indexLastTask, sampleGroupTag.getName().toString());
        model.addGroupTag(sampleGroupTag);
        String expectedMessage = String.format(addTagCommand.MESSAGE_COMPLETED_SUCCESS,
                sampleGroupTag.getName().toString());

        expectedModel.setProject(model.getFilteredProjectList().get(0));
        expectedModel.setSelectedProject(model.getFilteredProjectList().get(0));
        expectedModel.addGroupTag(sampleGroupTag);

        expectedModel.setTask(lastTask, taskWithGroup);
        expectedModel.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        expectedModel.commitProject();

        expectedModel.setProject(expectedModel.getSelectedProject(), (Project) expectedModel.getProject());
        expectedModel.commitProjectList();
        assertCommandSuccess(addTagCommand, model, commandHistory, expectedMessage, expectedModel);
    }
}

