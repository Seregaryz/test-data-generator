package com.github.seregaryz.testdatagenerator.action.data_analyze

import com.github.seregaryz.testdatagenerator.action.data_analyze.model.FileParser
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.asJava.namedUnwrappedElement
import org.jetbrains.kotlin.idea.structuralsearch.visitor.KotlinRecursiveElementVisitor
import org.jetbrains.kotlin.idea.structuralsearch.visitor.KotlinRecursiveElementWalkingVisitor
import org.jetbrains.kotlin.nj2k.postProcessing.type
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.psiUtil.getChildrenOfType
import org.jetbrains.kotlin.psi.psiUtil.startOffset
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getType


class DataAnalyzeAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val eventProject = event.project
        val data = eventProject?.let { PsiManager.getInstance(it) }
        val str = data?.project?.basePath ?: "Empty"
//        val classList = hashMapOf<String?, List<PsiElement>>()
        val classList = hashMapOf<String?, String>()
        // -------- Getting classes & their references
        val objectValue = StringBuilder()
        eventProject?.let { project ->
            FileParser(project).getAllKotlinDbClasses().forEach { psiFile ->
                println(psiFile.toString())
                print("     fields")
//                objectValue.append(psiFile.toString()).append("\n")
//                objectValue.append("    fields: ")
                psiFile.accept(object : PsiRecursiveElementWalkingVisitor() {
                    override fun visitElement(element: PsiElement) {
                        super.visitElement(element)
                        if (element is KtClassOrObject) {
                            objectValue.append("    $element")
                            objectValue.append("\n")
                            getInfo(psiFile)
                        }
//                            element.accept(object : KotlinRecursiveElementVisitor() {
//                                override fun visitKtElement(element2: KtElement) {
//                                    super.visitKtElement(element2)
//                                    element2.getChildrenOfType<KtClass>().asList()
//                                    if (element2.name != null) {
//                                        objectValue.append(element2.name).append(" type: ")
//                                        objectValue.append(element2.getChildrenOfType<KtClass>().asList().toString())
//                                        //objectValue.append(element2.context?.node?.elementType.toString())
//                                        objectValue.append(", \n")
//                                    }
//                                }
//                            })
                    }
                })
            }
        }


        val notificationGroup = NotificationGroup(
            displayId = "myActionId",
            displayType = NotificationDisplayType.BALLOON,
            isLogByDefault = true
        )
        notificationGroup.createNotification(
            title = "My title",
            content = objectValue.toString(),
            type = NotificationType.INFORMATION,
            listener = null
        ).notify(eventProject)
    }

    fun getInfo(psiElement: PsiElement) {
        //builder.append(psiElement).append("\n")
        println("${psiElement.namedUnwrappedElement?.name} type: ${psiElement.findElementAt(psiElement.startOffset)?.text}")
        if (psiElement.children.isNotEmpty()) {
            psiElement.children.forEach {
                getInfo(it)
            }
        }
    }
}