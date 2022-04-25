package com.github.seregaryz.testdatagenerator.action.data_analyze

import com.github.seregaryz.testdatagenerator.action.data_analyze.model.FileParser
import com.google.gson.Gson
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementWalkingVisitor
import org.jetbrains.kotlin.asJava.namedUnwrappedElement
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.psiUtil.startOffset

class DataAnalyzeAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val eventProject = event.project
        val message = StringBuilder(event.presentation.description + " Selected!")

        val selectedElement: PsiElement? = event.getData(CommonDataKeys.PSI_ELEMENT)
        if (selectedElement != null) {
            message.append("\nSelected Element: \n").append(selectedElement.namedUnwrappedElement?.name)

            eventProject?.let { project ->
                FileParser(project).getAllKotlinDbClasses().forEach { psiFile ->
                    println(psiFile.toString())
                    println("fields: ")
                    psiFile.accept(object : PsiRecursiveElementWalkingVisitor() {
                        override fun visitElement(element: PsiElement) {
                            super.visitElement(element)
                            if (element is KtClassOrObject) {
                                getInfo(
                                    selectedElement.namedUnwrappedElement?.name,
                                    psiFile,
                                    1,
                                    null,
                                    hashMapOf()
                                )
                            }
                        }
                    })
                }
            }

            val resultJson = Gson().toJson(
                getInfo(
                    selectedElement.namedUnwrappedElement?.name,
                    selectedElement,
                    1,
                    null,
                    hashMapOf()
                )
            )
            NotificationGroupManager.getInstance()
                .getNotificationGroup("Custom Notification Group")
                .createNotification(
                    resultJson, NotificationType.INFORMATION
                ).notify(eventProject)

        }
    }

    fun getInfo(
        rootElementName: String?,
        psiElement: PsiElement,
        epoch: Int,
        parent: PsiElement?,
        resultMap: HashMap<String?, MutableList<String?>>
    ) : HashMap<String?, MutableList<String?>> {
        val elementName = psiElement.namedUnwrappedElement?.name
        val elementTypeInfo = psiElement.findElementAt(psiElement.startOffset)?.text
        if (parent != null && !elementTypeInfo.equals(parent.findElementAt(psiElement.startOffset)?.text) &&
            !elementName.equals(rootElementName)) {
            println("$elementName type: $elementTypeInfo epoch: $epoch")
            if (resultMap.containsKey(elementName)) {
                val currentList = resultMap[elementName]
                if (elementTypeInfo != "\u003e") currentList?.add(elementTypeInfo)
                resultMap[elementName] = currentList!!
            } else if (elementTypeInfo != "\u003e")resultMap[elementName] = mutableListOf(elementTypeInfo)
        }
        if (evaluateNextGenAvailable(psiElement, epoch)) {
            psiElement.children.forEach {
                getInfo(rootElementName, it, epoch + 1, psiElement, resultMap)
            }
        }
        return resultMap
    }

    private fun evaluateNextGenAvailable(psiElement: PsiElement, epoch: Int): Boolean {
        if (psiElement.children.isEmpty()) return false
        if (Constants.STOP_WORDS.contains(psiElement.findElementAt(psiElement.startOffset)?.text) && epoch != 1)
            return false
        return true
    }
}