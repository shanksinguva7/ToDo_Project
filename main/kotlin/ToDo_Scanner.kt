data class TodoItem(
    val text: String,
    val line_number: Int,
    val column_start: Int,
    val column_end: Int,
    val file_name: String
)

class ToDo_Scanner {
    fun scanTodos(fileContent: String, file_name: String): List<TodoItem> {
        val todos = mutableListOf<TodoItem>()
        val lines = fileContent.split('\n')

        lines.forEachIndexed { lineIndex, line ->
            // Find single line TODO comments: // TODO: text
            val single_line_todo = Regex("//\\s*TODO:?\\s*(.*)").find(line)
            if (single_line_todo != null) {
                todos.add(TodoItem(
                    text = single_line_todo.groupValues[1].trim(),
                    line_number = lineIndex + 1,
                    column_start = single_line_todo.range.first,
                    column_end = single_line_todo.range.last + 1,
                    file_name = file_name
                ))
            }
        }

        return todos
    }
}