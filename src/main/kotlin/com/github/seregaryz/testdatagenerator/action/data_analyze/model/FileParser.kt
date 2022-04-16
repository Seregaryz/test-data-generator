package com.github.seregaryz.testdatagenerator.action.data_analyze.model

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager

data class FileParser (val myProject: Project) {

    fun getAllKotlinDbClasses(): List<PsiFile> {
        val roots = ProjectRootManager.getInstance(myProject).contentSourceRoots
        val dirs = roots.map { PsiManager.getInstance(myProject).findDirectory(it) }
        val result = mutableListOf<PsiFile>()
        getKotlinDbClassesInPsiDir(result, dirs)
        return result.toList()
    }

    private tailrec fun getKotlinDbClassesInPsiDir(aResult: MutableList<PsiFile>, aPsiDirectoryList: List<PsiDirectory?>) {
        if (aPsiDirectoryList.isEmpty()) return

        val subDirectories = mutableListOf<PsiDirectory>()
        aPsiDirectoryList.filter { it != null }.forEach {
            val children = it!!.children
            val files = children.filter {
                it is PsiFile && it.language.toString().toLowerCase() == "language: kotlin"
            }.map { it as PsiFile }
            subDirectories += children.filter { it is PsiDirectory }.map { it as PsiDirectory }
            aResult += files.toList()
        }
        getKotlinDbClassesInPsiDir(aResult, subDirectories)
    }
}