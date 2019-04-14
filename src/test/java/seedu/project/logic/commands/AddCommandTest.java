package seedu.project.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.project.commons.core.GuiSettings;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.commands.exceptions.CommandException;
import seedu.project.model.Model;
import seedu.project.model.ReadOnlyProjectList;
import seedu.project.model.ReadOnlyUserPrefs;
import seedu.project.model.project.Project;
import seedu.project.model.project.ReadOnlyProject;
import seedu.project.model.tag.GroupTag;
import seedu.project.model.tag.Tag;
import seedu.project.model.task.Task;
import seedu.project.testutil.TaskBuilder;

public class AddCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullTask_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_taskAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingTaskAdded modelStub = new ModelStubAcceptingTaskAdded();
        Task validTask = new TaskBuilder().build();

        CommandResult commandResult = new AddCommand(validTask).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS_TASK, validTask), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validTask), modelStub.tasksAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicateTask_throwsCommandException() throws Exception {
        Task validTask = new TaskBuilder().build();
        AddCommand addCommand = new AddCommand(validTask);
        ModelStub modelStub = new ModelStubWithTask(validTask);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_TASK);
        addCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void equals() {
        Task alice = new TaskBuilder().withName("Alice").build();
        Task bob = new TaskBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different task -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void commitProjectList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSelectedProject(Project project) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyProperty<Project> selectedProjectProperty() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Project getSelectedProject() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredProjectList(Predicate<Project> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getProjectListFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setProjectListFilePath(Path projectListFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setProjectList(ReadOnlyProjectList projectList) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyProjectList getProjectList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasProject(Project project) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<GroupTag> getGroupTagList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasGroupTag(GroupTag groupTag) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteProject(Project project) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addProject(Project project) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addGroupTag(GroupTag groupTag) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setProject(ReadOnlyProject newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setProject(Project target, Project editedProject) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyProject getProject() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasTask(Task task) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteTask(Task target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public List<String> compareTask(Task target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void clearTasks() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addTask(Task task) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setTask(Task target, Task editedTask) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Task> getFilteredTaskList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public String getTagWithTaskList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Project> getFilteredProjectList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredTaskList(Predicate<Task> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoProjectList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoProject() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoProject() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoProjectList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoProject() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoProject() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitProject() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyProperty<Task> selectedTaskProperty() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Task getSelectedTask() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSelectedTask(Task task) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteTag(Tag tag) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single task.
     */
    private class ModelStubWithTask extends ModelStub {
        private final Task task;

        ModelStubWithTask(Task task) {
            requireNonNull(task);
            this.task = task;
        }

        @Override
        public boolean hasTask(Task task) {
            requireNonNull(task);
            return this.task.isSameTask(task);
        }
    }

    /**
     * A Model stub that always accept the task being added.
     */
    private class ModelStubAcceptingTaskAdded extends ModelStub {
        final ArrayList<Task> tasksAdded = new ArrayList<>();

        @Override
        public boolean hasTask(Task task) {
            requireNonNull(task);
            return tasksAdded.stream().anyMatch(task::isSameTask);
        }

        @Override
        public void addTask(Task task) {
            requireNonNull(task);
            tasksAdded.add(task);
        }

        @Override
        public void commitProject() {
            // called by {@code AddCommand#execute()}
        }

        @Override
        public void commitProjectList() {
            // called by {@code AddCommand#execute()}
        }

        @Override
        public Project getSelectedProject() {
            return new Project();
        }

        @Override
        public void setProject(Project target, Project editedProject) {
            // called by {@code AddCommand#execute()}
        }

        @Override
        public ReadOnlyProject getProject() {
            return new Project();
        }
    }

}
