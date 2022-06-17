package com.github.seregaryz.testdatagenerator.action.data_analyze

import com.github.seregaryz.testdatagenerator.action.data_analyze.view.DataAnalyzeViewImpl
import com.github.seregaryz.testdatagenerator.action.injector.InjectorImpl
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiElement

class DataAnalyzeAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val eventProject = event.project
        val message = StringBuilder(event.presentation.description + " Selected!")
        // get element that was selected by user
        val selectedElement: PsiElement? = event.getData(CommonDataKeys.PSI_ELEMENT)
        if (selectedElement != null) {
//            val dialog = eventProject?.let {
//                DataAnalyzeViewImpl(
//                    it,
//                    selectedElement,
//                    DataAnalyzeInjectorImpl()
//                )
//            }
//            dialog?.show()
            val dialog = eventProject?.let {
                DataAnalyzeViewImpl(
                    it,
                    selectedElement,
                    InjectorImpl()
                )
            }
            dialog?.show()
        }
    }

}