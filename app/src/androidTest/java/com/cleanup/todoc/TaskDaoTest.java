package com.cleanup.todoc;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    private TodocDatabase database;

    private static long PROJECT_ID = 1;
    private static Project PROJECT_DEMO = new Project(PROJECT_ID, "Philippe", 0);

    private static Task NEW_TASK_1 = new Task(1, 1, "Mario", 0);
    private static Task NEW_TASK_2 = new Task(2, 1, "Luigi",1);
    private static Task NEW_TASK_3 = new Task(3, 1, "Peach", 2);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                 TodocDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    @Test
    public void insertAndGetProject() throws InterruptedException {

        this.database.projectDao().createProject(PROJECT_DEMO);

        Project project = LiveDataTestUtil.getValue(this.database.projectDao().getProject());
        assertTrue(project.getName().equals(PROJECT_DEMO.getName()) && project.getId() == PROJECT_ID);
    }



    @Test
    public void getTasksWhenNoTaskInserted() throws InterruptedException {
        // TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void insertAndGetTasks() throws InterruptedException {

        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(NEW_TASK_1);
        this.database.taskDao().insertTask(NEW_TASK_2);
        this.database.taskDao().insertTask(NEW_TASK_3);

        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertEquals(3, tasks.size());
    }

    @Test
    public void insertAndUpdateTask() throws InterruptedException {
        // BEFORE : Adding demo user & demo items. Next, update item added & re-save it
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(NEW_TASK_1);
        Task taskAdded = LiveDataTestUtil.getValue(this.database.taskDao().getTasks()).get(0);
        taskAdded.setProjectId(1);
        this.database.taskDao().updateTask(taskAdded);

        //TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertTrue(tasks.size() == 1 && tasks.get(0).getProjectId() == 1 );
    }

    @Test
    public void insertAndDeleteTask() throws InterruptedException {

        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(NEW_TASK_1);
        Task taskAdded = LiveDataTestUtil.getValue(this.database.taskDao().getTasks()).get(0);
        this.database.taskDao().deleteTask(taskAdded);

        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertTrue(tasks.isEmpty());
    }

}
