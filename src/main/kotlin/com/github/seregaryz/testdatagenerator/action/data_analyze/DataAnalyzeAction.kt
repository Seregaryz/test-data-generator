package com.github.seregaryz.testdatagenerator.action.data_analyze

import com.github.seregaryz.testdatagenerator.action.data_analyze.Constants.LISTS_TYPES
import com.github.seregaryz.testdatagenerator.action.data_analyze.Constants.MAP_TYPES
import com.github.seregaryz.testdatagenerator.action.data_analyze.Constants.PRIMITIVES
import com.github.seregaryz.testdatagenerator.action.data_analyze.injector.DataAnalyzeInjectorImpl
import com.github.seregaryz.testdatagenerator.action.data_analyze.model.FieldType
import com.github.seregaryz.testdatagenerator.action.data_analyze.model.FileParser
import com.github.seregaryz.testdatagenerator.action.data_analyze.view.DataAnalyzeForm
import com.google.gson.Gson
import com.intellij.internal.statistic.DeviceIdManager
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

        if (selectedElement != null) {
            message.append("\nSelected Element: \n").append(selectedElement.namedUnwrappedElement?.name)

            val resultMap =
                getInfo(rootElementName, selectedElement, 1, null, hashMapOf())

            val innerClasses = defineTypes(resultMap, eventProject, mutableListOf())
            val resClasses = mutableListOf<HashMap<String?, HashMap<String?, FieldType?>>>()

            innerClasses.forEach {
                val resMap = hashMapOf<String?, HashMap<String?, FieldType?>>()
                it.keys.forEach { classKey ->
                    val fieldMap = hashMapOf<String?, FieldType?>()
                    it[classKey]?.keys?.forEach { fieldKey ->
                        fieldMap[fieldKey] = FieldType(
                            proceedType(it[classKey]?.get(fieldKey)?.right?.type),
                            it[classKey]?.get(fieldKey)?.right?.isPrimitive
                        )
                    }
                    resMap[classKey] = fieldMap
                }
                resClasses.add(resMap)
            }
            val modelMap = hashMapOf<String?, FieldType?>()
            resultMap.keys.forEach {
                resultMap[it]?.right?.type = proceedType(resultMap[it]?.right?.type)
                modelMap[it] = resultMap[it]?.right
            }
            //Result for original selected element
            val modelJsonData = Gson().toJson(modelMap)

            //Result for internal classes of original element
            val internalClassesJsonData = Gson().toJson(resClasses.toSet())

            val dialog = eventProject?.let {
                DataAnalyzeForm(
                    it,
                    modelJsonData,
                    internalClassesJsonData,
                    rootElementName,
                    DataAnalyzeInjectorImpl()
                )
            }
            dialog?.show()
//            NotificationGroupManager.getInstance()
//                .getNotificationGroup("Custom Notification Group")
//                .createNotification(
//                    resultJson, NotificationType.INFORMATION
//                ).notify(eventProject)

        }
    }

    private fun getInfo(
        rootElementName: String?,
        psiElement: PsiElement,
        epoch: Int,
        parent: PsiElement?,
        resultMap: HashMap<String?, MutablePair<MutableList<String?>, FieldType?>>
    ): HashMap<String?, MutablePair<MutableList<String?>, FieldType?>> {
        //name of current element
        val elementName = psiElement.namedUnwrappedElement?.name
        // part of type of current element
        val elementTypeInfo = psiElement.findElementAt(psiElement.startOffset)?.text
        // package of element
        val mPackage = psiElement.namedUnwrappedElement?.text
        // add in result if has parent element, parent type and current type are not equal
        // and current name is not equal root name
        if (parent != null && !elementTypeInfo.equals(parent.findElementAt(psiElement.startOffset)?.text) &&
            !elementName.equals(rootElementName)
        ) {
            println("$elementName type: $elementTypeInfo epoch: $epoch")
            println()

            if (!resultMap.containsKey(elementName) && elementTypeInfo != "\u003e") {
                println("$elementName epoch:$epoch package:$mPackage")
                println()
                val splitType = mPackage?.split(":")
                resultMap[elementName] = MutablePair(
                    mutableListOf(elementTypeInfo),
                    if (splitType != null && splitType.size > 1) FieldType(splitType[1]) else null
                )

            } else if (resultMap.containsKey(elementName)) {
                val currentList = resultMap[elementName]?.left
                if (elementTypeInfo != "\u003e") currentList?.add(elementTypeInfo)
                resultMap[elementName]?.left = currentList
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

    private fun findFieldClass(
        className: String?,
        eventProject: Project?
    ): PsiElement? {
        var result: PsiElement? = null
        eventProject?.let { project ->
            FileParser(project).getAllKotlinDbClasses().forEach { psiFile ->
                println(psiFile.namedUnwrappedElement?.name)
                println()
                if (psiFile.namedUnwrappedElement?.name.equals("$className.kt", ignoreCase = true)) {
                    psiFile.accept(object : PsiRecursiveElementWalkingVisitor() {
                        override fun visitElement(element: PsiElement) {
                            super.visitElement(element)
                            if (element is KtClassOrObject) {
                                result = element
                                return
                            }
                        }
                    })
                }
            }
        }
        return result
    }

    private fun defineTypes(
        sourceMap: HashMap<String?, MutablePair<MutableList<String?>, FieldType?>>,
        eventProject: Project?,
        result: MutableList<HashMap<String?, HashMap<String?, MutablePair<MutableList<String?>, FieldType?>>>>
    ): List<HashMap<String?, HashMap<String?, MutablePair<MutableList<String?>, FieldType?>>>> {
        sourceMap.keys.forEach { key ->
            sourceMap[key]?.left?.forEach {
                if (!PRIMITIVES.contains(it) && !LISTS_TYPES.contains(it) && !MAP_TYPES.contains(it)) {
                    sourceMap[key]?.right?.isPrimitive = false
                    findFieldClass(it, eventProject)?.let { element ->
                        val innerMap = getInfo(it, element, 1, null, hashMapOf())
                        val resultMap = hashMapOf<String?, HashMap<String?, MutablePair<MutableList<String?>, FieldType?>>>()
                        resultMap[element.namedUnwrappedElement?.name] = innerMap
                        result.add(resultMap)
                        defineTypes(innerMap, eventProject, result)
                    }
                }
            }
        }
        return result
    }

    private fun proceedType(type: String?): String? {
        var result = type
        result = result?.replace(" ", "")
        result = result?.replace("<", "[")
        result = result?.replace(">", "]")
        LISTS_TYPES.forEach {
            if (type?.contains(it, ignoreCase = false) == true) {
                result = result?.replace(it, "")
            }
        }
        MAP_TYPES.forEach {
            if (type?.contains(it, ignoreCase = false) == true) {
                result = result?.replace(it, "")
            }
        }
        return result
    }

}