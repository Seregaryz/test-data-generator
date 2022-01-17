package com.github.seregaryz.testdatagenerator.services

import com.intellij.openapi.project.Project
import com.github.seregaryz.testdatagenerator.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
