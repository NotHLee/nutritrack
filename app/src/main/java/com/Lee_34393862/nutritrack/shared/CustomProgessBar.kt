package com.Lee_34393862.nutritrack.shared

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun CustomProgressBar(
    label: String,
    progressValue: Float,
    progressMax: Float
) {

    var progress by remember { mutableFloatStateOf(progressValue/progressMax) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            modifier = Modifier.fillMaxWidth(0.3f)
        )
        Spacer(modifier = Modifier.size(8.dp))

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.fillMaxWidth(0.7f),
        ) {
            Row(
                modifier = Modifier
                    .zIndex(1f)
            ) {
                Spacer(modifier = Modifier.fillMaxWidth(progress * 0.86f))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(24.dp)
                        .background(Color.White)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            shape = CircleShape
                        )
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.onPrimaryContainer)
                )
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            "$progressValue/${progressMax.toInt()}",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )
    }
}