import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiComment
import com.intellij.ui.JBColor
import java.awt.Color
import java.awt.Font

class ToDo_Highlighter : Annotator {

    companion object {
        //custom text attributes for TODO highlighting
        val TODO_ATTRIBUTES = TextAttributesKey.createTextAttributesKey(
            "TODO_COMMENT",
            TextAttributes().apply {
                backgroundColor = JBColor(Color(255, 255, 0, 80), Color(255, 255, 0, 60)) // Yellow background
                foregroundColor = JBColor(Color(0, 100, 0), Color(0, 150, 0)) // Dark green text
                fontType = Font.BOLD
            }
        )
    }

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        //process comments in Kotlin files
        if (element is PsiComment && element.containingFile.name.endsWith(".kt")) {
            val commentText = element.text

            //if this comment contains TODO
            val todoRegex = Regex("//\\s*TODO:?\\s*(.*)", RegexOption.IGNORE_CASE)
            val blockTodoRegex = Regex("/\\*\\s*TODO:?\\s*(.*?)\\s*\\*/", RegexOption.IGNORE_CASE)

            when {
                todoRegex.containsMatchIn(commentText) -> {
                    //Highlight the entire TODO comment
                    holder.newAnnotation(HighlightSeverity.INFORMATION, "TODO Comment")
                        .range(element.textRange)
                        .textAttributes(TODO_ATTRIBUTES)
                        .create()
                }
                blockTodoRegex.containsMatchIn(commentText) -> {
                    //Highlight block TODO comments
                    holder.newAnnotation(HighlightSeverity.INFORMATION, "TODO Comment")
                        .range(element.textRange)
                        .textAttributes(TODO_ATTRIBUTES)
                        .create()
                }
            }
        }
    }
}