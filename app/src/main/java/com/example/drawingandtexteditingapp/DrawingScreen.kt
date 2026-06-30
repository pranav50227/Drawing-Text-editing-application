package com.example.drawingandtexteditingapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DrawingScreen() {
    var currentColor by remember { mutableStateOf(Color.Red) }
    var currentStrokeWidth by remember { mutableStateOf(8.dp) }

    // State to control the visibility of the Color Picker
    var showColorPicker by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            // Your Bottom Bar with tools
            BottomAppBar {
                Button(onClick = { showColorPicker = true }) {
                    Text("Pick Color")
                }
            }
        }
    ) { paddingValues ->
        // The Canvas
        DrawingCanvas(
            currentColor = currentColor,
            currentStrokeWidth = currentStrokeWidth,
            modifier = Modifier.padding(paddingValues)
        )

        // The Overlay Component
        if (showColorPicker) {
            ColorPickerDialog(
                initialColor = currentColor,
                onColorSelected = { selectedColor ->
                    currentColor = selectedColor // Update the hoisted state
                    showColorPicker = false      // Close the dialog
                },
                onDismiss = {
                    showColorPicker = false      // Close without saving
                }
            )
        }
    }
}