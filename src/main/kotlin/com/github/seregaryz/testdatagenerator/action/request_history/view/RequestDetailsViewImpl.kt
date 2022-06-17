package com.github.seregaryz.testdatagenerator.action.request_history.view

import com.github.seregaryz.testdatagenerator.action.request_history.model.Request
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JTextArea

class RequestDetailsViewImpl (
    private val project: Project,
    private val request: Request?
) : DialogWrapper(project), RequestDetailsView {

    private val descriptionLabel: JLabel = JLabel(request.toString())
    private val dataLabel: JTextArea = JTextArea(request?.data)

    init {
        init()
    }

    override fun createCenterPanel(): JComponent = panel {
        row {
            descriptionLabel(grow)
        }
        row {
            dataLabel(grow)
        }
    }.apply {
        minimumSize = Dimension(600, 150)
        maximumSize = Dimension(600, 150)
    }
}