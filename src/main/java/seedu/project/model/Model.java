package seedu.project.model;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.project.commons.core.GuiSettings;
import seedu.project.model.project.Project;
import seedu.project.model.project.ReadOnlyProject;
import seedu.project.model.tag.GroupTag;
import seedu.project.model.tag.Tag;
import seedu.project.model.task.Task;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Task> PREDICATE_SHOW_ALL_TASKS = unused -> true;

    /** {@code Predicate} that always evaluate to false */
    Predicate<Task> PREDICATE_SHOW_NO_TASKS = unused -> false;

    /** {@code Predicate} that always evaluate to true */
    Predicate<Project> PREDICATE_SHOW_ALL_PROJECTS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' project file path.
     */
    Path getProjectListFilePath();

    /**
     * Sets the user prefs' project file path.
     */
    void setProjectListFilePath(Path projectListFilePath);

    /**
     * Replaces project list data with the data in {@code projectList}.
     */
    void setProjectList(ReadOnlyProjectList projectList);

    /** Returns the ProjectList */
    ReadOnlyProjectList getProjectList();

    /**
     * Returnsli true if a project with the same identity as {@code project} exists in the
     * project.
     */
    boolean hasProject(Project project);

    /** Returns the ProjectList */
    ObservableList<GroupTag> getGroupTagList();

    /**
     * Returns true if a GroupTag with the same identity as {@code GroupTag} exists in the
     * project.
     */
    boolean hasGroupTag(GroupTag groupTag);

    /**
     * Deletes the given task. The task../../
     * must exist in the project.
     */
    void deleteProject(Project project);

    /**
     * Adds the given project. {@code project} must not already exist in the project list.
     */
    void addProject(Project project);

    /**
     * Adds the given project. {@code project} must not already exist in the project list.
     */
    void addGroupTag(GroupTag groupTag);

    /**
     * Replaces project data with the data in {@code project}.
     */
    void setProject(ReadOnlyProject object);

    /**
     * Replaces the given project {@code target} with {@code editedProject}.
     * {@code target} must exist in the project list. The project identity of
     * {@code editedProject} must not be the same as another existing project in the
     * project list.
     */
    void setProject(Project target, Project editedProject);


    /** Returns the Project */
    ReadOnlyProject getProject();

    /**
     * Returns true if a task with the same identity as {@code task} exists in the
     * project.
     */
    boolean hasTask(Task task);

    /**
     * Deletes the given task. The task must exist in the project.
     */
    void deleteTask(Task target);

    /**
     * Compares the given task with the last edited version. The task must exist in the project.
     * Returns a list of string containing 2 string, which contains the differences in Name / Description / Deadline
     * Returns null if there is no difference.
     */
    List<String> compareTask(Task target);

    /**
     * Adds the given task. {@code task} must not already exist in the project.
     */
    void addTask(Task task);

    /**
     * Replaces the given task {@code target} with {@code editedTask}.
     * {@code target} must exist in the project. The task identity of
     * {@code editedTask} must not be the same as another existing task in the
     * project.
     */
    void setTask(Task target, Task editedTask);

    void clearTasks();

    /** Returns an unmodifiable view of the filtered project list */
    ObservableList<Project> getFilteredProjectList();

    /**
     * Updates the filter of the filtered project list to filter by the given
     * {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredProjectList(Predicate<Project> predicate);

    /** Returns an unmodifiable view of the filtered task list */
    ObservableList<Task> getFilteredTaskList();

    /** Returns an String tags and their associated task list */
    String getTagWithTaskList();

    /**
     * Updates the filter of the filtered task list to filter by the given
     * {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredTaskList(Predicate<Task> predicate);

    /**
     * Returns true if the model has previous project list states to restore.
     */
    boolean canUndoProjectList();

    /**
     * Returns true if the model has previous project states to restore.
     */
    boolean canUndoProject();

    /**
     * Returns true if the model has undone project states to restore.
     */
    boolean canRedoProject();

    /**
     * Restores the model's project list to its previous state.
     */
    void undoProjectList();

    /**
     * Restores the model's project to its previous state.
     */
    void undoProject();

    /**
     * Restores the model's project to its previously undone state.
     */
    void redoProject();

    /**
     * Saves the current project list state for undo/redo.
     */
    void commitProjectList();

    /**
     * Saves the current project state for undo/redo.
     */
    void commitProject();

    /**
     * Selected project in the filtered project list. null if no project is selected.
     */
    ReadOnlyProperty<Project> selectedProjectProperty();

    /**
     * Returns the selected project in the filtered project list. null if no project is
     * selected.
     */
    Project getSelectedProject();

    /**
     * Sets the selected project in the filtered project list.
     */
    void setSelectedProject(Project project);

    /**
     * Selected task in the filtered task list. null if no task is selected.
     */
    ReadOnlyProperty<Task> selectedTaskProperty();

    /**
     * Returns the selected task in the filtered task list. null if no task is
     * selected.
     */
    Task getSelectedTask();

    /**
     * Sets the selected task in the filtered task list.
     */
    void setSelectedTask(Task task);

    /**
     * Removes the given tag from all persons.
     */
    void deleteTag(Tag tag);
}
