package com.Lee_34393862.nutritrack.shared

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePicker(
    context: Context,
    time: LocalTime,
    onTimeChange: (LocalTime) -> Unit,
) {

    val listener = TimePickerDialog.OnTimeSetListener { timePickerDialog, hour, min ->
        onTimeChange(LocalTime.of(hour, min))
    }
    val timePickerDialog = TimePickerDialog(
        context,
        listener,
        time.hour,
        time.minute,
        false
    )

    Button(
        onClick = {
            timePickerDialog.show()
        },
        shape = RoundedCornerShape(size = 8.dp)
    ) {
        Text(
            time.toString(),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }

}


