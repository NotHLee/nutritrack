package com.Lee_34393862.nutritrack.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay

@Composable
fun StreamingText(
    text: String,
    delayMillis: Long = 20, // speed of text appearance
    modifier: Modifier = Modifier,
) {
    var displayedText by remember { mutableStateOf("") }
    val charsPerFrame = 2 // number of characters to reveal per frame

    LaunchedEffect(text) {
        // if the new target 'text' doesn't start with the currently 'displayedText',
        // or if the new 'text' is shorter, it implies a reset or a completely new message.
        // we reset 'displayedText' to animate from scratch.
        if (!text.startsWith(displayedText) || text.length < displayedText.length) {
            displayedText = ""
        }

        // animate from the current 'displayedText' length to the target 'text' length.
        var currentLength = displayedText.length
        while (currentLength < text.length) {
            val nextLength = (currentLength + charsPerFrame).coerceAtMost(text.length)
            // update displayedText with the new substring
            // this ensures that we are building upon the existing displayedText if it's a continuation,
            // or building from scratch if it was reset.
            displayedText = text.substring(0, nextLength)
            currentLength = nextLength
            delay(delayMillis)
        }
        // ensure the final text is displayed if the loop finishes or text was already complete
        // this handles cases where text might be shorter than charsPerFrame or already fully displayed.
        if (displayedText != text) {
            displayedText = text
        }
    }

    BoldAnnotatedText(
        text = displayedText,
        modifier = modifier,
    )
}