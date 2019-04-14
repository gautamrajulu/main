package seedu.project.model;

import static java.util.Objects.requireNonNull;
import static seedu.project.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.project.commons.core.GuiSettings;
import seedu.project.commons.core.LogsCenter;
import seedu.project.model.project.Project;
import seedu.project.model.project.ReadOnlyProject;
import seedu.project.model.project.VersionedProject;
import seedu.project.model.project.exceptions.ProjectNotFoundException;
import seedu.project.model.tag.GroupTag;
import seedu.project.model.tag.Tag;
import seedu.project.model.task.Task;
import seedu.project.model.task.exceptions.TaskNotFoundException;


/**
 * Represents the in-memory model of the project data.
 */
public class ModelManager implements Model {

    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedProjectList versionedProjectList;
    private final UserPrefs userPrefs;
    private final FilteredList<Project> filteredProjects;
    private final SimpleObjectProperty<Project> selectedProject = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Task> selectedTask = new SimpleObjectProperty<>();

    private VersionedProject versionedProject;
    private FilteredList<Task> filteredTasks;

    /**
     * Initializes a ModelManager with the given project and userPrefs.
     */
    public ModelManager(ReadOnlyProjectList projectList, ReadOnlyProject project, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(projectList, project, userPrefs);

        logger.fine("Initializing with project list: " + projectList + " and user prefs " + userPrefs);

        versionedProjectList = new VersionedProjectList(projectList);
        versionedProject = new VersionedProject(project);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredProjects = new FilteredList<>(versionedProjectList.getProjectList());
        filteredProjects.addListener(this::ensureSelectedProjectIsValid);
        filteredTasks = new FilteredList<>(versionedProject.getTaskList());
        filteredTasks.addListener(this::ensureSelectedTaskIsValid);
    }

    public ModelManager() {
        this(new ProjectList(), new Project(), new UserPrefs());
    }

    // =========== UserPrefs
    // ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getProjectListFilePath() {
        return userPrefs.getProjectListFilePath();
    }

    @Override
    public void setProjectListFilePath(Path projectListFilePath) {
        requireNonNull(projectListFilePath);
        userPrefs.setProjectListFilePath(projectListFilePath);
    }

    // =========== ProjectList
    // ================================================================================

    @Override
    public void setProjectList(ReadOnlyProjectList projectList) {
        versionedProjectList.clear();
        versionedProjectList.resetData(projectList);
        versionedProjectList.populate(projectList);
    }

    @Override
    public ReadOnlyProjectList getProjectList() {
        return versionedProjectList;
    }

    @Override
    public boolean hasProject(Project project) {
        requireNonNull(project);
        return versionedProjectList.hasProject(project);
    }

    @Override
    public ObservableList<GroupTag> getGroupTagList() {
        return versionedProjectList.getGroupTagList();
    }

    @Override
    public boolean hasGroupTag(GroupTag groupTag) {
        requireNonNull(groupTag);
        return versionedProjectList.hasGroupTag(groupTag);
    }

    @Override
    public void deleteProject(Project target) {
        versionedProjectList.removeProject(target);
    }

    @Override
    public void addProject(Project project) {
        versionedProjectList.addProject(project);
        updateFilteredProjectList(PREDICATE_SHOW_ALL_PROJECTS);
    }

    @Override
    public void addGroupTag(GroupTag groupTag) {
        versionedProjectList.addGroupTag(groupTag);
    }

    @Override
    public void setProject(Project target, Project editedProject) {
        requireAllNonNull(target, editedProject);

        versionedProjectList.setProject(target, editedProject);
        setSelectedProject(editedProject);
        //this.setSelectedProject( (Project) this.getProject());
    }

    // =========== Project
    // ================================================================================

    @Override
    public void setProject(ReadOnlyProject project) {
        if (versionedProjectList.getProjectList().get(getFilteredProjectList().indexOf(project)) instanceof Project) {
            versionedProject = new VersionedProject(project);
            versionedProject.resetName(project);
            versionedProject.resetData(project);
        } else {
            versionedProject = (VersionedProject) versionedProjectList.getProjectList().get(getFilteredProjectList()
                    .indexOf(project));
        }
        filteredTasks = new FilteredList<>(versionedProject.getTaskList());
        filteredTasks.addListener(this::ensureSelectedTaskIsValid);
        /*versionedProject.clear();
        versionedProject.resetName(project);
        versionedProject.resetData(project);
        versionedProject.populate(project);*/
    }

    @Override
    public ReadOnlyProject getProject() {
        return versionedProject;
    }

    @Override
    public boolean hasTask(Task task) {
        requireNonNull(task);
        return versionedProject.hasTask(task);
    }

    @Override
    public void deleteTask(Task target) {
        versionedProject.removeTask(target);
    }

    @Override
    public void addTask(Task task) {
        versionedProject.addTask(task);
        updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
    }

    @Override
    public void setTask(Task target, Task editedTask) {
        requireAllNonNull(target, editedTask);

        versionedProject.setTask(target, editedTask);
    }

    @Override
    public void clearTasks() {
        versionedProject.clearTasks();
    }

    // =========== Filtered Project List Accessors
    // =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Project} backed by the
     * internal list of {@code versionedProjectList}
     */
    @Override
    public ObservableList<Project> getFilteredProjectList() {
        return filteredProjects;
    }

    @Override
    public void updateFilteredProjectList(Predicate<Project> predicate) {
        requireNonNull(predicate);
        filteredProjects.setPredicate(predicate);
    }

    // =========== Filtered Task List Accessors
    // =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Task} backed by the
     * internal list of {@code versionedProject}
     */
    @Override
    public ObservableList<Task> getFilteredTaskList() {
        return filteredTasks;
    }

    /**
     * Returns string of tags and their associated task name {@code String}
     */
    public String getTagWithTaskList() {
        List<Tag> uniqueTagList = getUniqueTagList();

        // arrange print string
        String stringToPrint = "";
        for (Tag tag : uniqueTagList) {
            stringToPrint += tag.toStringWithoutBrackets() + ": ";
            for (Task task : filteredTasks) {
                if (task.getTags().contains(tag)) {
                    stringToPrint += "[" + task.getName().toString() + "]";
                }
            }
            stringToPrint += "\n";
        }
        return stringToPrint;
    }

    /**
     * Returns list of tags {@code List<Tag>}
     */
    public List<Tag> getUniqueTagList() {
        // get set of unique tags
        List<Tag> allTags = new ArrayList<>();
        filteredTasks.forEach(entry -> {
            entry.getTags().forEach(tag -> allTags.add(tag));
        });
        Set<Tag> uniqueTagSet = new HashSet<>(allTags);

        // convert uniqueTag set to list
        List<Tag> uniqueTagList = new ArrayList<>();
        uniqueTagList.addAll(uniqueTagSet);

        return uniqueTagList;
    }

    @Override
    public void updateFilteredTaskList(Predicate<Task> predicate) {
        requireNonNull(predicate);
        filteredTasks.setPredicate(predicate);
    }

    // =========== Undo/Redo/Compare
    // =================================================================================

    @Override
    public boolean canUndoProjectList() {
        return versionedProjectList.canUndo();
    }

    @Override
    public boolean canUndoProject() {
        return versionedProject.canUndo();
    }

    @Override
    public boolean canRedoProject() {
        return versionedProject.canRedo();
    }

    @Override
    public void undoProjectList() {
        versionedProjectList.undo();
    }

    @Override
    public void undoProject() {
        versionedProject.undo();
    }

    @Override
    public void redoProject() {
        versionedProject.redo();
    }

    @Override
    public void commitProjectList() {
        versionedProjectList.commit();
    }

    @Override
    public void commitProject() {
        versionedProject.commit();
    }

    @Override
    public List<String> compareTask(Task target) {
        return versionedProject.compareTask(target);
    }

    // =========== Selected project
    // ===========================================================================

    @Override
    public ReadOnlyProperty<Project> selectedProjectProperty() {
        return selectedProject;
    }

    @Override
    public Project getSelectedProject() {
        return selectedProject.getValue();
    }

    @Override
    public void setSelectedProject(Project project) {
        if (project != null && !filteredProjects.contains(project)) {
            throw new ProjectNotFoundException();
        }
        selectedProject.setValue(project);
    }

    /**
     * Ensures {@code selectedProject} is a valid project in {@code filteredProjects}.
     */
    private void ensureSelectedProjectIsValid(ListChangeListener.Change<? extends Project> change) {
        while (change.next()) {
            if (selectedProject.getValue() == null) {
                // null is always a valid selected task, so we do not need to check that it is
                // valid anymore.
                return;
            }

            boolean wasSelectedProjectReplaced = change.wasReplaced()
                    && change.getAddedSize() == change.getRemovedSize()
                    && change.getRemoved().contains(selectedProject.getValue());
            if (wasSelectedProjectReplaced) {
                // Update selectedTask to its new value.
                int index = change.getRemoved().indexOf(selectedProject.getValue());
                selectedProject.setValue(change.getAddedSubList().get(index));
                continue;
            }

            boolean wasSelectedProjectRemoved = change.getRemoved().stream()
                    .anyMatch(removedProject -> selectedProject.getValue().isSameProject(removedProject));
            if (wasSelectedProjectRemoved) {
                // Select the task that came before it in the list,
                // or clear the selection if there is no such task.
                selectedProject.setValue(change.getFrom() > 0 ? change.getList().get(change.getFrom() - 1) : null);
            }
        }
    }

    // =========== Selected task
    // ===========================================================================

    @Override
    public ReadOnlyProperty<Task> selectedTaskProperty() {
        return selectedTask;
    }

    @Override
    public Task getSelectedTask() {
        return selectedTask.getValue();
    }

    @Override
    public void setSelectedTask(Task task) {
        if (task != null && !filteredTasks.contains(task)) {
            throw new TaskNotFoundException();
        }
        selectedTask.setValue(task);
    }

    /**
     * Ensures {@code selectedTask} is a valid task in {@code filteredTasks}.
     */
    private void ensureSelectedTaskIsValid(ListChangeListener.Change<? extends Task> change) {
        while (change.next()) {
            if (selectedTask.getValue() == null) {
                // null is always a valid selected task, so we do not need to check that it is
                // valid anymore.
                return;
            }

            boolean wasSelectedTaskReplaced = change.wasReplaced() && change.getAddedSize() == change.getRemovedSize()
                    && change.getRemoved().contains(selectedTask.getValue());
            if (wasSelectedTaskReplaced) {
                // Update selectedTask to its new value.
                int index = change.getRemoved().indexOf(selectedTask.getValue());
                selectedTask.setValue(change.getAddedSubList().get(index));
                continue;
            }

            boolean wasSelectedTaskRemoved = change.getRemoved().stream()
                    .anyMatch(removedTask -> selectedTask.getValue().isSameTask(removedTask));
            if (wasSelectedTaskRemoved) {
                // Select the task that came before it in the list,
                // or clear the selection if there is no such task.
                selectedTask.setValue(change.getFrom() > 0 ? change.getList().get(change.getFrom() - 1) : null);
            }
        }
    }

    @Override
    public void deleteTag(Tag tag) {
        versionedProject.removeTag(tag);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedProjectList.equals(other.versionedProjectList)
                && versionedProject.equals(other.versionedProject)
                && userPrefs.equals(other.userPrefs)
                && filteredTasks.equals(other.filteredTasks)
                && Objects.equals(selectedTask.get(), other.selectedTask.get());
    }


}
