package seedu.project.logic.commands;

import static seedu.project.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.project.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.project.model.Model.PREDICATE_SHOW_ALL_TASKS;
import static seedu.project.testutil.TypicalTasks.getTypicalProjectList;

import java.util.ArrayList;
import java.util.Comparator;

import org.junit.Test;

import seedu.project.commons.core.Messages;
import seedu.project.logic.CommandHistory;
import seedu.project.logic.LogicManager;
import seedu.project.model.Model;
import seedu.project.model.ModelManager;
import seedu.project.model.UserPrefs;
import seedu.project.model.project.Project;
import seedu.project.model.project.VersionedProject;
import seedu.project.model.task.Task;

import javafx.collections.transformation.SortedList;
import javafx.collections.ObservableList;

public class SortByDeadlineCommandTest {
    private Model model = new ModelManager(getTypicalProjectList(), new Project(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalProjectList(), new Project(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_notProjectLevel_failure() {
        LogicManager.setState(true);
        model.setSelectedProject(null);
        SortByDeadlineCommand sortByDeadlineCommand = new SortByDeadlineCommand();
        String expectedMessage = String.format(Messages.MESSAGE_RETURN_TO_PROJECT_LEVEL,
                SortByDeadlineCommand.COMMAND_WORD);
        assertCommandFailure(sortByDeadlineCommand, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_sort_success() {
        expectedModel.setProject(expectedModel.getFilteredProjectList().get(2));
        expectedModel.setSelectedProject(expectedModel.getFilteredProjectList().get(2));

        LogicManager.setState(false);

        ObservableList<Task> filteredTasks = expectedModel.getFilteredTaskList();

        Comparator<Task> taskComparator = new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                String[] arrString = o1.getDeadline().toString().split("-", 3);
                String o1Temp = arrString[2] + arrString[1] + arrString[0];
                int o1TempInt = Integer.parseInt(o1Temp);

                String[] arrString2 = o2.getDeadline().toString().split("-", 3);
                String o2Temp = arrString2[2] + arrString2[1] + arrString2[0];
                int o2TemptInt = Integer.parseInt(o2Temp);

                if (o2TemptInt - o1TempInt > 0) {
                    return -1;
                } else if (o2TemptInt - o1TempInt == 0) {
                    return 0;
                } else if (o2TemptInt - o1TempInt < 0) {
                    return 1;
                }

                return 0;
            }

        };

        int size = filteredTasks.size();

        SortedList<Task> sortedList = filteredTasks.sorted(taskComparator);

        ArrayList<Task> properList = new ArrayList<Task>();

        for (int i = 0; i < size; i++) {
            properList.add(sortedList.get(i));
        }


        expectedModel.clearTasks();

        for (int i = 0; i < size; i++) {
            System.out.println(((properList.get(i)).getName()).toString());
            expectedModel.addTask(properList.get(i));
        }

        expectedModel.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);

        expectedModel.commitProject();

        //this will not work if user clicks on a different project while on task level??? lock UI at prev panel
        if (expectedModel.getProject().getClass().equals(VersionedProject.class)) {
            expectedModel.setProject(expectedModel.getSelectedProject(), (VersionedProject) expectedModel.getProject());
        } else {
            expectedModel.setProject(expectedModel.getSelectedProject(), (Project) expectedModel.getProject());
        }

        expectedModel.commitProjectList();

        model.setSelectedProject(model.getFilteredProjectList().get(2));
        model.setProject(model.getFilteredProjectList().get(2));

        SortByDeadlineCommand sortCommand = new SortByDeadlineCommand();

        assertCommandSuccess(sortCommand, expectedModel, commandHistory,
                SortByDeadlineCommand.MESSAGE_SUCCESS_TASK, model);
    }
}
