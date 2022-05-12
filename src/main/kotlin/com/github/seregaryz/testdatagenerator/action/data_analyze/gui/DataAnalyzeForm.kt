package com.github.seregaryz.testdatagenerator.action.data_analyze.gui

import com.github.seregaryz.testdatagenerator.action.data_analyze.DataAnalyzeInjector
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import com.github.seregaryz.testdatagenerator.action.data_analyze.view.DataAnalyzeView
import java.awt.Dimension
import javax.swing.*

class DataAnalyzeForm(
    private val project: Project,
    val injector: DataAnalyzeInjector
) : DialogWrapper(project), DataAnalyzeView {

    private var endpointTextField: JTextField? = JTextField().apply {
        name = "endpointTextField"
    }
    private var buttonConfirm: JButton? = JButton().apply {
        name = "buttonConfirm"
        text = "Confirm"
    }

    init {
        init()
    }


    override fun createCenterPanel(): JComponent = panel {
        row("Edit the endpoint") {
            endpointTextField?.let { it() }
        }
        row {
            buttonConfirm?.let { it() }
        }
    }.apply {
        minimumSize = Dimension(360, 240)
        maximumSize = Dimension(360, 240)
    }

    override fun onButtonConfirmClick() {
        TODO("Not yet implemented")
    }

    override fun success() {
        TODO("Not yet implemented")
    }

    override fun error(error: Throwable) {
        TODO("Not yet implemented")
    }
}