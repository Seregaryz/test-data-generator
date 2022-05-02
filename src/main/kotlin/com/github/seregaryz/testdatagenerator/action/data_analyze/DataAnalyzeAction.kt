package com.github.seregaryz.testdatagenerator.action.data_analyze

import com.github.seregaryz.testdatagenerator.action.data_analyze.Constants.PRIMITIVES
import com.github.seregaryz.testdatagenerator.action.data_analyze.model.FileParser
import com.google.gson.Gson
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementWalkingVisitor
import org.apache.commons.lang3.tuple.MutablePair
import org.jetbrains.kotlin.asJava.namedUnwrappedElement
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.psiUtil.startOffset

class DataAnalyzeAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val eventProject = event.project
        val message = StringBuilder(event.presentation.description + " Selected!")
        // get element that was selected by user
        val selectedElement: PsiElement? = event.getData(CommonDataKeys.PSI_ELEMENT)
        val rootElementName = selectedElement?.namedUnwrappedElement?.name

        eventProject?.let { project ->
            FileParser(project).getAllKotlinDbClasses().forEach { psiFile ->
                println(psiFile.namedUnwrappedElement?.name)
                println()
                if (psiFile.namedUnwrappedElement?.name.equals("AdditionalInfo.kt", ignoreCase = true)) {
                    psiFile.accept(object : PsiRecursiveElementWalkingVisitor() {
                        override fun visitElement(element: PsiElement) {
                            super.visitElement(element)
                            if (element is KtClassOrObject) {
                                getInfo(rootElementName, element, 1, null, hashMapOf())
                            }
                        }
                    })
                }
            }
        }

        if (selectedElement != null) {
            message.append("\nSelected Element: \n").append(selectedElement.namedUnwrappedElement?.name)

            val resultMap =
                getInfo(rootElementName, selectedElement, 1, null, hashMapOf())
//            resultMap.keys.forEach { key ->
//                val listOfTypes = resultMap[key]?.left
//                val resultList = mutableListOf<String?>()
//                listOfTypes?.forEach { type ->
//                    resultList.add(type)
//                    if (type != null && PRIMITIVES.contains(type)) {
//                        resultList.add(type)
//                    } else {
//                        evaluateFieldType(type, eventProject, resultMap[key]?.right, rootElementName)?.let { element ->
//                            val longMap = getInfo(rootElementName, element, 1, null, hashMapOf())
//                            val shortMap = hashMapOf<String?, MutableList<String?>>()
//                            longMap.forEach { (key, value) ->
//                                shortMap[key] = value.left
//                            }
//                            resultList.add(
//                                Gson().toJson(shortMap)
//                            )
//                        }
//                    }
//                }
//                resultMap[key]?.left = resultList
//            }
            val preparedResultMap = hashMapOf<String?, MutableList<String?>>()
            resultMap.forEach { (key, value) ->
                preparedResultMap[key] = value.left
            }
            val resultJson = Gson().toJson(preparedResultMap)
            NotificationGroupManager.getInstance()
                .getNotificationGroup("Custom Notification Group")
                .createNotification(
                    resultJson, NotificationType.INFORMATION
                ).notify(eventProject)

        }
    }

    private fun getInfo(
        rootElementName: String?,
        psiElement: PsiElement,
        epoch: Int,
        parent: PsiElement?,
        resultMap: HashMap<String?, MutablePair<MutableList<String?>, String?>>
    ) : HashMap<String?, MutablePair<MutableList<String?>, String?>> {
        //name of current element
        val elementName = psiElement.namedUnwrappedElement?.name
        // part of type of current element
        val elementTypeInfo = psiElement.findElementAt(psiElement.startOffset)?.text
        // add in result if has parent element, parent type and current type are not equal
        // and current name is not equal root name
        if (parent != null && !elementTypeInfo.equals(parent.findElementAt(psiElement.startOffset)?.text) &&
            !elementName.equals(rootElementName)) {
            println("$elementName type: $elementTypeInfo epoch: $epoch")
            println()
            // if map has current name key, add type in exciting type array
            if (resultMap.containsKey(elementName)) {
                val currentList = resultMap[elementName]?.left
                if (elementTypeInfo != "\u003e") currentList?.add(elementTypeInfo)
                resultMap[elementName]?.left = currentList
                // if package info about element is empty, add info
                if (resultMap[elementName]?.right == null)
                    resultMap[elementName]?.right = psiElement.containingFile.virtualFile.path
            } else if (elementTypeInfo != "\u003e") {
                resultMap[elementName] =
                    MutablePair(mutableListOf(elementTypeInfo), psiElement.containingFile.virtualFile.path)
            }
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

    private fun evaluateFieldType(
        className: String?,
        eventProject: Project?,
        packageName: String?,
        rootElementName: String?
    ): PsiElement? {
        var result: PsiElement? = null
        eventProject?.let { project ->
            FileParser(project).getAllKotlinDbClasses().forEach { psiFile ->
                if (psiFile.namedUnwrappedElement?.name.equals("$className.kt", ignoreCase = true)) {
                    if (psiFile.namedUnwrappedElement?.name.equals("AdditionalInfo.kt", ignoreCase = true)) {
                        psiFile.accept(object : PsiRecursiveElementWalkingVisitor() {
                            override fun visitElement(element: PsiElement) {
                                super.visitElement(element)
                                if (element is KtClassOrObject) {
                                    getInfo(rootElementName, element, 1, null, hashMapOf())
                                }
                            }
                        })
                    }
                }
            }
        }
        return result
    }

//    private fun evaluateTypes(
//        sourceMap: HashMap<String?, MutablePair<MutableList<String?>, String?>>,
//        eventProject: Project?
//    ): HashMap<String?, MutablePair<MutableList<String?>, String?>> {
//        sourceMap.keys.forEach { key ->
//            val listOfTypes = sourceMap[key]?.left
//            val resultList = mutableListOf<String?>()
//            listOfTypes?.forEach { type ->
//                resultList.add(type)
//                if (type != null && PRIMITIVES.contains(type)) {
//                    resultList.add(type)
//                } else {
//                    evaluateFieldType(type, eventProject, sourceMap[key]?.right)?.let { element ->
//                        val longMap = getInfo(rootElementName, element, 1, null, hashMapOf())
//                        val shortMap = hashMapOf<String?, MutableList<String?>>()
//                        longMap.forEach { (key, value) ->
//                            shortMap[key] = value.left
//                        }
//                        resultList.add(
//                            Gson().toJson(shortMap)
//                        )
//                    }
//                }
//            }
//            sourceMap[key]?.left = resultList
//        }
//    }

}