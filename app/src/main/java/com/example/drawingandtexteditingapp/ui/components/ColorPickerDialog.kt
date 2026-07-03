package com.example.drawingandtexteditingapp.ui.components

import android.graphics.Color.colorToHSV
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ColorPickerDialog(
    initialColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    // 1. Convert the incoming Compose RGB Color to HSV values
    val hsvArray = FloatArray(3)
    colorToHSV(initialColor.toArgb(), hsvArray)

    // 2. Initialize the state variables with the current color's HSV breakdown
    var hue by remember { mutableStateOf(hsvArray[0]) }
    var saturation by remember { mutableStateOf(hsvArray[1]) }
    var value by remember { mutableStateOf(hsvArray[2]) } // Lightness/Toner

    // 3. Reactively combine them back into a Compose Color
    val currentColor = Color.hsv(hue, saturation, value)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .width(IntrinsicSize.Min) // Keeps the dialog tightly wrapped around the wheel
            ) {
                Text("Choose a Color", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(16.dp))

                // Optional: A preview box showing the live color updating
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(currentColor, shape = MaterialTheme.shapes.small)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 4. Implement the Color Wheel component
                ColorWheel(
                    hue = hue,
                    saturation = saturation,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f), // Keeps the wheel perfectly round
                    onColorChanged = { newHue, newSaturation ->
                        hue = newHue
                        saturation = newSaturation
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 5. Implement the Toner Slider component
                TonerSlider(
                    currentHue = hue,
                    currentSaturation = saturation,
                    currentValue = value,
                    onValueChanged = { newValue ->
                        value = newValue
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 6. Action Buttons to pass the data back to the DrawingScreen
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onColorSelected(currentColor) }) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}