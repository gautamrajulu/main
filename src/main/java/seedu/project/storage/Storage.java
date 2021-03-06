package seedu.project.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.project.commons.exceptions.DataConversionException;
import seedu.project.model.ReadOnlyProjectList;
import seedu.project.model.ReadOnlyUserPrefs;
import seedu.project.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends ProjectListStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getProjectListFilePath();

    @Override
    Optional<ReadOnlyProjectList> readProjectList() throws DataConversionException, IOException;

    @Override
    void saveProjectList(ReadOnlyProjectList projectList) throws IOException;

}
