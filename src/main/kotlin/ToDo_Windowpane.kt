import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.psi.PsiManager
import com.intellij.util.messages.MessageBusConnection
import javax.swing.*
import java.awt.BorderLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class ToDo_Windowpane : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val todoToolWindow = Todo_Too_Window(project)
        val content = ContentFactory.getInstance().createContent(todoToolWindow.getContent(), "", false)
        toolWindow.contentManager.addContent(content)
    }
}

class Todo_Too_Window(private val project: Project) {
    private val listModel = DefaultListModel<TodoItem>()
    private val todoList = JList(listModel)
    private val scanner = ToDo_Scanner()
    private var connection: MessageBusConnection? = null

    fun getContent(): JPanel {
        val panel = JPanel(BorderLayout())

        // Create the TODO list with custom renderer
        todoList.cellRenderer = TodoListCellRenderer()
        todoList.selectionMode = ListSelectionModel.SINGLE_SELECTION

        // Add click listener for navigation
        todoList.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2) { // Double-click
                    val selectedTodo = todoList.selectedValue
                    if (selectedTodo != null) {
                        navigateToTodo(selectedTodo)
                    }
                }
            }
        })

        val scrollPane = JScrollPane(todoList)
        panel.add(scrollPane, BorderLayout.CENTER)

        // Add refresh button
        val refreshButton = JButton("Refresh TODOs")
        refreshButton.addActionListener { refreshTodos() }
        panel.add(refreshButton, BorderLayout.SOUTH)

        // Initial scan
        refreshTodos()

        // Listen for file changes
        setupFileChangeListener()

        return panel
    }

    private fun refreshTodos() {
        listModel.clear()

        // Get currently opened editor
        val fileEditorManager = FileEditorManager.getInstance(project)
        val selectedFiles = fileEditorManager.selectedFiles

        for (file in selectedFiles) {
            if (file.extension == "kt") { // Only Kotlin files
                try {
                    val content = String(file.contentsToByteArray())
                    val todos = scanner.scanTodos(content, file.name)
                    for (todo in todos) {
                        listModel.addElement(todo)
                    }
                } catch (e: Exception) {
                    // Handle file reading errors gracefully
                    println("Error reading file ${file.name}: ${e.message}")
                }
            }
        }

        // Update list display
        todoList.revalidate()
        todoList.repaint()
    }

    private fun navigateToTodo(todo: TodoItem) {
        val fileEditorManager = FileEditorManager.getInstance(project)
        val selectedFiles = fileEditorManager.selectedFiles

        // Find the file containing this TODO
        val targetFile = selectedFiles.find { it.name == todo.file_name }
        if (targetFile != null) {
            // Open the file if not already open
            val editors = fileEditorManager.openFile(targetFile, true)
            if (editors.isNotEmpty()) {
                val editor = editors[0]
                if (editor is com.intellij.openapi.fileEditor.TextEditor) {
                    val textEditor = editor.editor

                    // Navigate to the specific line
                    val logicalPosition = LogicalPosition(todo.line_number - 1, todo.column_start)
                    textEditor.caretModel.moveToLogicalPosition(logicalPosition)
                    textEditor.scrollingModel.scrollToCaret(com.intellij.openapi.editor.ScrollType.CENTER)

                    // Request focus
                    textEditor.contentComponent.requestFocus()
                }
            }
        }
    }

    private fun setupFileChangeListener() {
        connection = project.messageBus.connect()
        connection?.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, object : FileEditorManagerListener {
            override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
                if (file.extension == "kt") {
                    SwingUtilities.invokeLater { refreshTodos() }
                }
            }

            override fun fileClosed(source: FileEditorManager, file: VirtualFile) {
                if (file.extension == "kt") {
                    SwingUtilities.invokeLater { refreshTodos() }
                }
            }
        })
    }
}

// Custom cell renderer for better TODO display
class TodoListCellRenderer : DefaultListCellRenderer() {
    override fun getListCellRendererComponent(
        list: JList<*>?,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): java.awt.Component {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)

        if (value is TodoItem) {
            text = "${value.file_name}:${value.line_number} - ${value.text}"
            toolTipText = "Double-click to navigate to line ${value.line_number}"
        }

        return this
    }
}