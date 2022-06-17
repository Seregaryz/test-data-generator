package com.github.seregaryz.testdatagenerator.action.request_history.view

import com.github.seregaryz.testdatagenerator.action.request_history.model.Request
import com.github.seregaryz.testdatagenerator.action.injector.InjectorImpl
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.ui.layout.panel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBList
import java.awt.Dimension
import javax.swing.DefaultListModel

import javax.swing.JComponent
import javax.swing.JLabel

class RequestListViewImpl(
    private val project: Project?,
    private val injector: InjectorImpl
) : DialogWrapper(project), RequestListView {

    private val endpointListPresenter by lazy {
        injector.endpointListPresenter(this)
    }

    private var historyList: JBList<Request?>? = JBList<Request?>().apply {

    }

    private val historyModel = DefaultListModel<Request?>()
    private val listLabel: JLabel? = JLabel("History of requests")

    init {
        init()
        endpointListPresenter.getRequestList()
    }

    override fun createCenterPanel(): JComponent = panel {
        row {
            listLabel?.let { it() }
        }
        row {
            historyList?.let { it(grow) }
        }
    }.apply {
        minimumSize = Dimension(600, 150)
        maximumSize = Dimension(600, 150)
        historyList?.model = historyModel
    }

    override fun successListLoading(endpointsList: List<Request?>?) {
        historyModel.addAll(endpointsList)
        historyList?.selectionModel?.addListSelectionListener {
            val selected = historyList?.selectedValue
            val dialog = project?.let {
                RequestDetailsViewImpl(it, selected)
            }
            dialog?.show()
//            NotificationGroupManager.getInstance()
//                .getNotificationGroup("Custom Notification Group")
//                .createNotification(
//                    "Success!", selected.toString(), NotificationType.INFORMATION
//                ).notify(project)
        }
    }

    override fun error(error: Throwable) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Custom Notification Group")
            .createNotification(
                "Error!", error.localizedMessage, NotificationType.ERROR
            ).notify(project)
    }
}