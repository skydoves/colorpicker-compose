/*
 * Designed and developed by 2022 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package docs.component

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import docs.theme.CodeHighlightColors
import docs.theme.DocsTheme

enum class CodeLanguage {
  KOTLIN,
  GROOVY,
  TOML,
  PLAIN,
}

@Composable
fun CodeBlock(
  code: String,
  modifier: Modifier = Modifier,
  language: CodeLanguage = CodeLanguage.KOTLIN,
) {
  val colors = DocsTheme.colors.codeHighlight
  val highlightedCode = remember(code, language, colors) {
    when (language) {
      CodeLanguage.KOTLIN -> highlightKotlin(code, colors)
      CodeLanguage.GROOVY -> highlightGroovy(code, colors)
      CodeLanguage.TOML -> highlightToml(code, colors)
      CodeLanguage.PLAIN -> AnnotatedString(code)
    }
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(DocsTheme.colors.codeBackground)
      .horizontalScroll(rememberScrollState())
      .padding(16.dp),
  ) {
    Text(
      text = highlightedCode,
      style = DocsTheme.typography.code,
      fontFamily = FontFamily.Monospace,
    )
  }
}

private fun highlightKotlin(code: String, colors: CodeHighlightColors): AnnotatedString {
  return buildAnnotatedString {
    val keywords = setOf(
      "fun", "val", "var", "class", "object", "interface", "enum", "sealed",
      "if", "else", "when", "for", "while", "do", "return", "break", "continue",
      "import", "package", "private", "public", "internal", "protected", "open",
      "override", "abstract", "final", "companion", "data", "inline", "suspend",
      "by", "in", "out", "is", "as", "try", "catch", "finally", "throw", "true",
      "false", "null", "this", "super", "it", "get", "set", "const", "lateinit",
      "expect", "actual",
    )

    var i = 0
    while (i < code.length) {
      when {
        // Multi-line comment
        code.startsWith("/*", i) -> {
          val end = code.indexOf("*/", i + 2).let { if (it == -1) code.length else it + 2 }
          withStyle(SpanStyle(color = colors.comment)) {
            append(code.substring(i, end))
          }
          i = end
        }
        // Single-line comment
        code.startsWith("//", i) -> {
          val end = code.indexOf('\n', i).let { if (it == -1) code.length else it }
          withStyle(SpanStyle(color = colors.comment)) {
            append(code.substring(i, end))
          }
          i = end
        }
        // Triple-quoted string
        code.startsWith("\"\"\"", i) -> {
          val end = code.indexOf("\"\"\"", i + 3).let { if (it == -1) code.length else it + 3 }
          withStyle(SpanStyle(color = colors.string)) {
            append(code.substring(i, end))
          }
          i = end
        }
        // String
        code[i] == '"' -> {
          val end = findStringEnd(code, i + 1, '"')
          withStyle(SpanStyle(color = colors.string)) {
            append(code.substring(i, end))
          }
          i = end
        }
        // Char
        code[i] == '\'' -> {
          val end = findStringEnd(code, i + 1, '\'')
          withStyle(SpanStyle(color = colors.string)) {
            append(code.substring(i, end))
          }
          i = end
        }
        // Annotation
        code[i] == '@' -> {
          val end = findIdentifierEnd(code, i + 1)
          withStyle(SpanStyle(color = colors.annotation)) {
            append(code.substring(i, end))
          }
          i = end
        }
        // Number
        code[i].isDigit() || (code[i] == '.' && i + 1 < code.length && code[i + 1].isDigit()) -> {
          val end = findNumberEnd(code, i)
          withStyle(SpanStyle(color = colors.number)) {
            append(code.substring(i, end))
          }
          i = end
        }
        // Identifier or keyword
        code[i].isLetter() || code[i] == '_' -> {
          val end = findIdentifierEnd(code, i)
          val word = code.substring(i, end)
          when {
            word in keywords -> withStyle(SpanStyle(color = colors.keyword)) { append(word) }
            word.first().isUpperCase() -> withStyle(SpanStyle(color = colors.type)) { append(word) }
            else -> withStyle(SpanStyle(color = colors.plain)) { append(word) }
          }
          i = end
        }
        // Other characters
        else -> {
          withStyle(SpanStyle(color = colors.plain)) {
            append(code[i])
          }
          i++
        }
      }
    }
  }
}

private fun highlightGroovy(code: String, colors: CodeHighlightColors): AnnotatedString {
  return buildAnnotatedString {
    val keywords = setOf(
      "def", "class", "interface", "enum", "if", "else", "for", "while", "do",
      "switch", "case", "default", "break", "continue", "return", "try", "catch",
      "finally", "throw", "import", "package", "new", "true", "false", "null",
      "this", "super", "static", "final", "public", "private", "protected",
      "implementation", "api", "dependencies", "plugins", "id", "version",
    )

    var i = 0
    while (i < code.length) {
      when {
        // Multi-line comment
        code.startsWith("/*", i) -> {
          val end = code.indexOf("*/", i + 2).let { if (it == -1) code.length else it + 2 }
          withStyle(SpanStyle(color = colors.comment)) {
            append(code.substring(i, end))
          }
          i = end
        }
        // Single-line comment
        code.startsWith("//", i) -> {
          val end = code.indexOf('\n', i).let { if (it == -1) code.length else it }
          withStyle(SpanStyle(color = colors.comment)) {
            append(code.substring(i, end))
          }
          i = end
        }
        // String (double quote)
        code[i] == '"' -> {
          val end = findStringEnd(code, i + 1, '"')
          withStyle(SpanStyle(color = colors.string)) {
            append(code.substring(i, end))
          }
          i = end
        }
        // String (single quote)
        code[i] == '\'' -> {
          val end = findStringEnd(code, i + 1, '\'')
          withStyle(SpanStyle(color = colors.string)) {
            append(code.substring(i, end))
          }
          i = end
        }
        // Number
        code[i].isDigit() -> {
          val end = findNumberEnd(code, i)
          withStyle(SpanStyle(color = colors.number)) {
            append(code.substring(i, end))
          }
          i = end
        }
        // Identifier or keyword
        code[i].isLetter() || code[i] == '_' -> {
          val end = findIdentifierEnd(code, i)
          val word = code.substring(i, end)
          when {
            word in keywords -> withStyle(SpanStyle(color = colors.keyword)) { append(word) }
            word.first().isUpperCase() -> withStyle(SpanStyle(color = colors.type)) { append(word) }
            else -> withStyle(SpanStyle(color = colors.plain)) { append(word) }
          }
          i = end
        }
        // Other characters
        else -> {
          withStyle(SpanStyle(color = colors.plain)) {
            append(code[i])
          }
          i++
        }
      }
    }
  }
}

private fun highlightToml(code: String, colors: CodeHighlightColors): AnnotatedString {
  return buildAnnotatedString {
    var i = 0
    while (i < code.length) {
      when {
        // Comment
        code[i] == '#' -> {
          val end = code.indexOf('\n', i).let { if (it == -1) code.length else it }
          withStyle(SpanStyle(color = colors.comment)) {
            append(code.substring(i, end))
          }
          i = end
        }
        // Section header [section]
        code[i] == '[' -> {
          val end = code.indexOf(']', i).let { if (it == -1) code.length else it + 1 }
          withStyle(SpanStyle(color = colors.keyword)) {
            append(code.substring(i, end))
          }
          i = end
        }
        // String
        code[i] == '"' -> {
          val end = findStringEnd(code, i + 1, '"')
          withStyle(SpanStyle(color = colors.string)) {
            append(code.substring(i, end))
          }
          i = end
        }
        // Number
        code[i].isDigit() -> {
          val end = findNumberEnd(code, i)
          withStyle(SpanStyle(color = colors.number)) {
            append(code.substring(i, end))
          }
          i = end
        }
        // Key (identifier before =)
        code[i].isLetter() || code[i] == '_' || code[i] == '-' -> {
          val end = findTomlKeyEnd(code, i)
          val word = code.substring(i, end)
          val isKey = code.substring(end).trimStart().startsWith("=")
          if (isKey) {
            withStyle(SpanStyle(color = colors.property)) { append(word) }
          } else {
            withStyle(SpanStyle(color = colors.plain)) { append(word) }
          }
          i = end
        }
        // Other characters
        else -> {
          withStyle(SpanStyle(color = colors.plain)) {
            append(code[i])
          }
          i++
        }
      }
    }
  }
}

private fun findStringEnd(code: String, start: Int, quote: Char): Int {
  var i = start
  while (i < code.length) {
    when {
      code[i] == '\\' && i + 1 < code.length -> i += 2
      code[i] == quote -> return i + 1
      code[i] == '\n' -> return i
      else -> i++
    }
  }
  return code.length
}

private fun findIdentifierEnd(code: String, start: Int): Int {
  var i = start
  while (i < code.length && (code[i].isLetterOrDigit() || code[i] == '_')) {
    i++
  }
  return i
}

private fun findTomlKeyEnd(code: String, start: Int): Int {
  var i = start
  while (i < code.length && (code[i].isLetterOrDigit() || code[i] == '_' || code[i] == '-')) {
    i++
  }
  return i
}

private fun Char.isHexDigit(): Boolean =
  this.isDigit() || this in 'a'..'f' || this in 'A'..'F'

private fun findNumberEnd(code: String, start: Int): Int {
  var i = start
  var hasDecimal = false
  var hasExponent = false

  while (i < code.length) {
    when {
      code[i].isDigit() -> i++
      code[i] == '.' && !hasDecimal && !hasExponent -> {
        hasDecimal = true
        i++
      }
      (code[i] == 'e' || code[i] == 'E') && !hasExponent -> {
        hasExponent = true
        i++
        if (i < code.length && (code[i] == '+' || code[i] == '-')) i++
      }
      code[i] == 'f' || code[i] == 'F' || code[i] == 'L' || code[i] == 'd' || code[i] == 'D' -> {
        i++
        break
      }
      code[i] == 'x' || code[i] == 'X' -> {
        i++
        while (i < code.length && code[i].isHexDigit()) {
          i++
        }
        break
      }
      else -> break
    }
  }
  return i
}
