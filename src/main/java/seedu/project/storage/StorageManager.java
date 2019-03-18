package seedu.project.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.project.commons.core.LogsCenter;
import seedu.project.commons.exceptions.DataConversionException;
import seedu.project.model.ReadOnlyProjectList;
import seedu.project.model.ReadOnlyUserPrefs;
import seedu.project.model.UserPrefs;
import seedu.project.model.project.ReadOnlyProject;

/**
 * Manages storage of Project data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private ProjectListStorage projectListStorage;
    private ProjectStorage projectStorage;
    private UserPrefsStorage userPrefsStorage;

    public StorageManager(ProjectListStorage projectListStorage, ProjectStorage projectStorage,
                          UserPrefsStorage userPrefsStorage) {
        super();
        this.projectListStorage = projectListStorage;
        this.projectStorage = projectStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }

    // ================ ProjectList methods ==========================

    @Override
    public Path getProjectListFilePath() {
        return projectListStorage.getProjectListFilePath();
    }

    @Override
    public Optional<ReadOnlyProjectList> readProjectList() throws DataConversionException, IOException {
        return readProjectList(projectListStorage.getProjectListFilePath());
    }

    @Override
    public Optional<ReadOnlyProjectList> readProjectList(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return projectListStorage.readProjectList(filePath);
    }

    @Override
    public void saveProjectList(ReadOnlyProjectList projectList) throws IOException {
        saveProjectList(projectList, projectListStorage.getProjectListFilePath());
    }

    @Override
    public void saveProjectList(ReadOnlyProjectList projectList, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        projectListStorage.saveProjectList(projectList, filePath);
    }

    @Override
    public void backupProjectList(ReadOnlyProjectList projectList) throws IOException {
        logger.fine("Backing up to temporary location");
        projectListStorage.backupProjectList(projectList);
    }

    // ================ Project methods ==============================

    @Override
    public Path getProjectFilePath() {
        return projectStorage.getProjectFilePath();
    }

    @Override
    public Optional<ReadOnlyProject> readProject() throws DataConversionException, IOException {
        return readProject(projectStorage.getProjectFilePath());
    }

    @Override
    public Optional<ReadOnlyProject> readProject(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return projectStorage.readProject(filePath);
    }

    @Override
    public void saveProject(ReadOnlyProject project) throws IOException {
        saveProject(project, projectStorage.getProjectFilePath());
    }

    @Override
    public void saveProject(ReadOnlyProject project, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        projectStorage.saveProject(project, filePath);
    }

    @Override
    public void backupProject(ReadOnlyProject project) throws IOException {
        logger.fine("Backing up to temporary location");
        projectStorage.backupProject(project);
    }

}