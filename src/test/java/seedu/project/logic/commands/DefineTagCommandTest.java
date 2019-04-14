package seedu.project.logic.commands;

import static seedu.project.logic.commands.CommandTestUtil.VALID_NAME_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_TAG_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_TAG_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.Name;
import seedu.project.model.UserPrefs;
import seedu.project.model.project.Project;
import seedu.project.model.tag.GroupTag;
import seedu.project.model.tag.Tag;

public class DefineTagCommandTest {
    private Model model = new ModelManager(getTypicalProjectList(), new Project(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalProjectList(), new Project(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_duplicateDefineTag_failure() {
        model.setProject(model.getFilteredProjectList().get(0));

        Set<Tag> sampleTargetSet = new HashSet<Tag>(
                Arrays.asList(new Tag(VALID_TAG_CS2101), new Tag(VALID_TAG_CP2106)));
        Name sampleTargetName = new Name(VALID_NAME_CP2106);
        GroupTag sampleGroupTag = new GroupTag(sampleTargetName, sampleTargetSet);

        model.addGroupTag(sampleGroupTag);
        LogicManager.setState(true);

        DefineTagCommand defineTagCommand = new DefineTagCommand(sampleGroupTag);
        String expectedMessage = String.format(
                defineTagCommand.MESSAGE_DUPLICATE_GROUPTAG, sampleGroupTag.getName().toString());
        assertCommandFailure(defineTagCommand, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_validDefineTag_success() {
        model.setProject(model.getFilteredProjectList().get(0));
        expectedModel.setProject(model.getFilteredProjectList().get(0));

        Set<Tag> sampleTargetSet = new HashSet<Tag>(
                Arrays.asList(new Tag(VALID_TAG_CS2101), new Tag(VALID_TAG_CP2106)));
        Name sampleTargetName = new Name(VALID_NAME_CP2106);
        GroupTag sampleGroupTag = new GroupTag(sampleTargetName, sampleTargetSet);

        DefineTagCommand defineTagCommand = new DefineTagCommand(sampleGroupTag);
        String expectedMessage = String.format(defineTagCommand.SUCCESS_MESSAGE, sampleGroupTag.getName().toString());
        expectedModel.addGroupTag(sampleGroupTag);
        expectedModel.commitProjectList();

        assertCommandSuccess(defineTagCommand, model, commandHistory, expectedMessage, expectedModel);
    }
}
