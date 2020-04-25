package com.cleanup.todoc.repositories;

import android.arch.lifecycle.LiveData;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.model.Project;

public class ProjectDataRepository {

    private final ProjectDao mProjectDao;

    public ProjectDataRepository(ProjectDao projectDao) { this.mProjectDao = projectDao; }

    // --- GET ---
    public LiveData<Project> getProjects() { return this.mProjectDao.getProject(); }

}
