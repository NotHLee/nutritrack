package com.Lee_34393862.nutritrack.shared

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun BoldAnnotatedText(
    text: String,
    modifier: Modifier = Modifier,
) {
    val annotatedString = buildAnnotatedString {
        val boldPattern = """(\*\*)(.*?)(\*\*)""".toRegex()
        var lastMatchEnd = 0

        boldPattern.findAll(text).forEach { matchResult ->
            // Add text before this match
            append(text.substring(lastMatchEnd, matchResult.range.first))

            // Extract and add the bold text (without the ** markers)
            val boldText = matchResult.groupValues[2]
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(boldText)
            }

            lastMatchEnd = matchResult.range.last + 1
        }

        // Append any remaining text after the last match
        if (lastMatchEnd < text.length) {
            append(text.substring(lastMatchEnd))
        }
    }
    Text(
        text = annotatedString,
        modifier = modifier,
    )
}