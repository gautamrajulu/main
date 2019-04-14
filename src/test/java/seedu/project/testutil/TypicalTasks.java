package seedu.project.testutil;

import static seedu.project.logic.commands.CommandTestUtil.VALID_DEADLINE_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_DEADLINE_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.VALID_DESCRIPTION_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_DESCRIPTION_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.VALID_NAME_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_NAME_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.VALID_PROJECT_NAME_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_PROJECT_NAME_CS2101;
import static seedu.project.logic.commands.CommandTestUtil.VALID_TAG_CP2106;
import static seedu.project.logic.commands.CommandTestUtil.VALID_TAG_CS2101;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.project.model.ProjectList;
import seedu.project.model.project.Project;
import seedu.project.model.task.Task;

/**
 * A utility class containing a list of {@code Task} objects to be used in tests.
 */
public class TypicalTasks {

    public static final Task GROUP_MEETING = new TaskBuilder().withName("Group meeting")
            .withDescription("find teammates for group discussion on presentation")
            .withDeadline("01-01-2019")
            .withTags("TYPICAL").build();
    public static final Task REPORT_SUBMISSION = new TaskBuilder().withName("Report submission")
            .withDescription("submit report to John Doe")
            .withDeadline("01-01-2019")
            .withTags("TYPICAL").build();
    public static final Task FEEDBACK = new TaskBuilder().withName("Feedback")
            .withDescription("submit feedback")
            .withDeadline("01-01-2019").build();
    public static final Task TEACHING_FEEDBACK = new TaskBuilder().withName("Teaching Feedback")
            .withDescription("submit teaching feedback via portal")
            .withDeadline("01-01-2019").withTags("TYPICAL").build();
    public static final Task PRINT = new TaskBuilder().withName("Print slides")
            .withDescription("print chapter 4 slides")
            .withDeadline("01-01-2019").build();
    public static final Task QUIZ = new TaskBuilder().withName("Complete quiz")
            .withDescription("attempt quiz at portal, multiple attempts possible")
            .withDeadline("01-01-2019").build();
    public static final Task CONSULTATION = new TaskBuilder().withName("Go for consultation")
            .withDescription("find mr john doe for consultation at office 04-15")
            .withDeadline("01-01-2019").build();

    // Manually added
    public static final Task LECTURE = new TaskBuilder().withName("Attend lecture")
            .withDescription("attend lecture at utown lecture theatre")
            .withDeadline("01-01-2019").build();
    public static final Task TUTORIAL = new TaskBuilder().withName("Attend tutorial")
            .withDescription("attend tutorial at utown classroom")
            .withDeadline("01-01-2019").withTags("TUTORIAL").build();

    // Manually added for SortByDeadlineCommandTest
    public static final Task REPORT1 = new TaskBuilder().withName("Report 1 submission")
            .withDescription("Submit report via LumiNUS")
            .withDeadline("05-04-2019").withTags("REPORT").build();

    public static final Task REPORTFINAL = new TaskBuilder().withName("Final Report")
            .withDescription("Final report presentation via hardcopy")
            .withDeadline("02-01-2020").withTags("REPORT").build();

    public static final Task REPORT2 = new TaskBuilder().withName("Report 2 submission")
            .withDescription("Submit report via LumiNUS")
            .withDeadline("02-07-2019").withTags("REPORT").build();

    public static final Task PRESENTATION = new TaskBuilder().withName("Oral Presentation")
            .withDescription("Oral presentation based on report 1")
            .withDeadline("05-05-2019").withTags("PRESENTATION").build();


    public static final Project SAMPLE1 = new ProjectBuilder().withTask(GROUP_MEETING, PRINT,
            TEACHING_FEEDBACK, CONSULTATION, LECTURE, TUTORIAL).withName("Sample Project 1").build();
    public static final Project SAMPLE2 = new ProjectBuilder().withTask(REPORT_SUBMISSION)
            .withName("Sample Project 2").build();
    public static final Project SAMPLE3 = new ProjectBuilder().withTask(REPORT1, REPORTFINAL,
            REPORT2, PRESENTATION).withName("Sample Project 3").build();

    // Manually added - Task's details found in {@code CommandTestUtil}
    public static final Task CS2101_MILESTONE = new TaskBuilder().withName(VALID_NAME_CS2101)
            .withDescription(VALID_DESCRIPTION_CS2101).withDeadline(VALID_DEADLINE_CS2101)
            .withTags(VALID_TAG_CS2101).build();
    public static final Task CP2106_MILESTONE = new TaskBuilder().withName(VALID_NAME_CP2106)
            .withDescription(VALID_DESCRIPTION_CP2106).withDeadline(VALID_DEADLINE_CP2106)
            .withTags(VALID_TAG_CP2106).build();

    public static final Project CS2101 = new ProjectBuilder().withTask(GROUP_MEETING)
            .withName(VALID_PROJECT_NAME_CS2101).build();
    public static final Project CP2106 = new ProjectBuilder().withTask(CP2106_MILESTONE)
            .withName(VALID_PROJECT_NAME_CP2106).build();

    public static final String KEYWORD_MATCHING_TEST = "Feedback"; // A keyword that matches Feedback

    private TypicalTasks() {} // prevents instantiation

    /**
     * Returns an {@code ProjectList} with the typical projects.
     */
    public static ProjectList getTypicalProjectList() {
        ProjectList pl = new ProjectList();
        for (Project project : getTypicalProjects()) {
            pl.addProject(project);
        }
        return pl;
    }

    /**
     * Returns an {@code Project} with all the typical tasks.
     */
    public static Project getTypicalProject() {
        Project p = new Project();
        p.setName("TYPICAL");
        for (Task task : getTypicalTasks()) {
            p.addTask(task);
        }
        return p;
    }

    public static List<Project> getTypicalProjects() {
        return new ArrayList<>(Arrays.asList(SAMPLE1, SAMPLE2, SAMPLE3));
    }

    public static List<Task> getTypicalTasks() {
        return new ArrayList<>(Arrays.asList(GROUP_MEETING, REPORT_SUBMISSION, FEEDBACK, TEACHING_FEEDBACK, PRINT,
                QUIZ, CONSULTATION));
    }
}
