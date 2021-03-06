package seedu.project;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import seedu.project.commons.core.Config;
import seedu.project.commons.core.LogsCenter;
import seedu.project.commons.core.Version;
import seedu.project.commons.exceptions.DataConversionException;
import seedu.project.commons.util.ConfigUtil;
import seedu.project.commons.util.StringUtil;
import seedu.project.logic.Logic;
import seedu.project.logic.LogicManager;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.ProjectList;
import seedu.project.model.ReadOnlyProjectList;
import seedu.project.model.ReadOnlyUserPrefs;
import seedu.project.model.UserPrefs;
import seedu.project.model.project.Project;
import seedu.project.model.project.ReadOnlyProject;
import seedu.project.model.util.SampleDataUtil;
import seedu.project.storage.JsonProjectListStorage;
import seedu.project.storage.JsonUserPrefsStorage;
import seedu.project.storage.ProjectListStorage;
import seedu.project.storage.Storage;
import seedu.project.storage.StorageManager;
import seedu.project.storage.UserPrefsStorage;
import seedu.project.ui.Ui;
import seedu.project.ui.UiManager;

/**
 * The main entry point to the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(0, 6, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing Project ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        ProjectListStorage projectListStorage = new JsonProjectListStorage(userPrefs.getProjectListFilePath());
        storage = new StorageManager(projectListStorage, userPrefsStorage);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s address
     * book and {@code userPrefs}. <br>
     * The data from the sample project will be used instead if
     * {@code storage}'s project is not found, or an empty project will be
     * used instead if errors occur when reading {@code storage}'s project.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        Optional<ReadOnlyProjectList> projectListOptional;
        ReadOnlyProjectList initialProjectList;
        ReadOnlyProject initialProject;

        try {
            projectListOptional = storage.readProjectList();
            if (!projectListOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample ProjectList");
            }
            initialProjectList = projectListOptional.orElseGet(SampleDataUtil::getSampleProjectList);
            initialProject = new Project();
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty ProjectList");
            initialProjectList = new ProjectList();
            initialProject = new Project();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty ProjectList");
            initialProjectList = new ProjectList();
            initialProject = new Project();
        }
        return new ModelManager(initialProjectList, initialProject, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                    + "Using default config properties");
            initializedConfig = new Config();
        }

        // Update config file in case it was missing to begin with or there are
        // new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs
     * file path, or a new {@code UserPrefs} with default configuration if errors
     * occur when reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                    + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty Project");
            initializedPrefs = new UserPrefs();
        }

        // Update prefs file in case it was missing to begin with or there are
        // new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting Project " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Address Book ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
