package com.github.seregaryz.testdatagenerator.action.request_history

import com.github.seregaryz.testdatagenerator.action.request_history.view.RequestListViewImpl
import com.github.seregaryz.testdatagenerator.action.injector.InjectorImpl
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import icons.SdkIcons

class RequestHistoryAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val editor: Editor? = event.getData(CommonDataKeys.EDITOR)
        event.presentation.isEnabled = editor != null
        // Take this opportunity to set an icon for the group.
        // Take this opportunity to set an icon for the group.
        event.presentation.icon = SdkIcons.Sdk_default_icon
        val eventProject = event.project

        val dialog = eventProject?.let {
            RequestListViewImpl(it, InjectorImpl())
        }
        dialog?.show()
    }
}