package com.github.seregaryz.testdatagenerator.action.data_analyze.presenter

import com.github.seregaryz.testdatagenerator.action.data_analyze.Constants
import com.github.seregaryz.testdatagenerator.action.data_analyze.model.FieldType
import com.github.seregaryz.testdatagenerator.action.data_analyze.model.FileParser
import com.github.seregaryz.testdatagenerator.action.data_analyze.repository.DataAnalyzeRepository
import com.github.seregaryz.testdatagenerator.action.data_analyze.view.DataAnalyzeView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementWalkingVisitor
import org.apache.commons.lang3.tuple.MutablePair
import org.jetbrains.kotlin.asJava.namedUnwrappedElement
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.psiUtil.startOffset

class DataAnalyzePresenterImpl(
    private val view: DataAnalyzeView,
    private val repository: DataAnalyzeRepository
) : DataAnalyzePresenter {

    override fun analyzeModelData(
        project: Project,
        selectedElement: PsiElement,
        method: String,
        language: String,
        isStatic: Boolean,
        isRepresentative: Boolean,
        isList: Boolean?,
        elementsCount: Int?
    ) {
        val rootElementName = selectedElement.namedUnwrappedElement?.name
        val resultMap = getInfo(rootElementName, selectedElement, 1, null, hashMapOf())

        val innerClasses = defineTypes(resultMap, project, mutableListOf())
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

        val builder = GsonBuilder().setPrettyPrinting().serializeNulls()
        val gson = builder.create()
        //Result for original selected element
        val modelJsonData = gson.toJson(modelMap)

        //Result for internal classes of original element
        val internalClassesJsonData = gson.toJson(resClasses.toSet())

        //send data on Mock-server
        repository.sendParsedData(
            modelJsonData,
            internalClassesJsonData,
            method,
            language,
            isStatic,
            isRepresentative,
            rootElementName,
            isList,
            elementsCount
        ).subscribe(
                { endpoint ->
                    view.success(endpoint)
                },
                { error ->
                    view.error(error)
                }
            )
    }

    override fun getInfo(
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

    override fun evaluateNextGenAvailable(psiElement: PsiElement, epoch: Int): Boolean {
        if (psiElement.children.isEmpty()) return false
        if (Constants.STOP_WORDS.contains(psiElement.findElementAt(psiElement.startOffset)?.text) && epoch != 1)
            return false
        return true
    }

    override fun findFieldClass(
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

    override fun defineTypes(
        sourceMap: HashMap<String?, MutablePair<MutableList<String?>, FieldType?>>,
        eventProject: Project?,
        result: MutableList<HashMap<String?, HashMap<String?, MutablePair<MutableList<String?>, FieldType?>>>>
    ): List<HashMap<String?, HashMap<String?, MutablePair<MutableList<String?>, FieldType?>>>> {
        sourceMap.keys.forEach { key ->
            sourceMap[key]?.left?.forEach {
                if (!Constants.PRIMITIVES.contains(it) && !Constants.LISTS_TYPES.contains(it) && !Constants.MAP_TYPES.contains(it)) {
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

    override fun proceedType(type: String?): String? {
        var result = type
        result = result?.replace(" ", "")
        result = result?.replace("<", "[")
        result = result?.replace(">", "]")
        Constants.LISTS_TYPES.forEach {
            if (type?.contains(it, ignoreCase = false) == true) {
                result = result?.replace(it, "")
            }
        }
        Constants.MAP_TYPES.forEach {
            if (type?.contains(it, ignoreCase = false) == true) {
                result = result?.replace(it, "")
            }
        }
        return result
    }

}