package com.github.seregaryz.testdatagenerator.action.data_analyze.presenter

import com.github.seregaryz.testdatagenerator.action.data_analyze.model.FieldType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.apache.commons.lang3.tuple.MutablePair

interface DataAnalyzePresenter {
    fun analyzeModelData(
        project: Project,
        selectedElement: PsiElement,
        method: String,
        language: String,
        isStatic: Boolean,
        isRepresentative: Boolean,
        isList: Boolean?,
        elementsCount: Int?
    )

    fun getInfo(
        rootElementName: String?,
        psiElement: PsiElement,
        epoch: Int,
        parent: PsiElement?,
        resultMap: HashMap<String?, MutablePair<MutableList<String?>, FieldType?>>
    ): HashMap<String?, MutablePair<MutableList<String?>, FieldType?>>

    fun evaluateNextGenAvailable(psiElement: PsiElement, epoch: Int): Boolean

    fun findFieldClass(className: String?, eventProject: Project?): PsiElement?

    fun defineTypes(
        sourceMap: HashMap<String?, MutablePair<MutableList<String?>, FieldType?>>,
        eventProject: Project?,
        result: MutableList<HashMap<String?, HashMap<String?, MutablePair<MutableList<String?>, FieldType?>>>>
    ): List<HashMap<String?, HashMap<String?, MutablePair<MutableList<String?>, FieldType?>>>>

    fun proceedType(type: String?): String?
}