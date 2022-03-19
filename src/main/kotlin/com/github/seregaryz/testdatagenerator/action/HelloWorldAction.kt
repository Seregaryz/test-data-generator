package com.github.seregaryz.testdatagenerator.action

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import icons.SdkIcons


class HelloWorldAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val editor: Editor? = event.getData(CommonDataKeys.EDITOR)
        event.presentation.isEnabled = editor != null
        // Take this opportunity to set an icon for the group.
        // Take this opportunity to set an icon for the group.
        event.presentation.icon = SdkIcons.Sdk_default_icon
        val notificationGroup = NotificationGroup(
            displayId = "myActionId",
            displayType = NotificationDisplayType.BALLOON,
            isLogByDefault = true
        )
        notificationGroup.createNotification(
            title = "My title",
            content = "Hello world",
            type = NotificationType.INFORMATION,
            listener = null
        ).notify(event.project)
    }
}