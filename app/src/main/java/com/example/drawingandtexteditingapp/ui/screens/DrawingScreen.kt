package com.example.drawingandtexteditingapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.drawingandtexteditingapp.ui.components.ColorPickerDialog
import com.example.drawingandtexteditingapp.ui.components.DrawingCanvas
import com.example.drawingandtexteditingapp.ui.viewmodel.DrawingAction
import com.example.drawingandtexteditingapp.ui.viewmodel.DrawingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawingScreen(
    viewModel: DrawingViewModel,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (viewModel.currentDrawingId == null) "New Drawing" else "Edit Drawing #${viewModel.currentDrawingId}") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(onClick = { 
                        viewModel.onAction(DrawingAction.OnSaveDrawing)
                        onNavigateBack()
                    }) {
                        Text("Save")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                IconButton(onClick = { viewModel.onAction(DrawingAction.OnToggleColorPicker) }) {
                    Surface(
                        modifier = Modifier.size(28.dp),
                        shape = CircleShape,
                        color = viewModel.currentColor,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {}
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = { viewModel.onAction(DrawingAction.OnUndo) }) {
                    Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = "Undo")
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = { viewModel.onAction(DrawingAction.OnClearCanvas) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Clear")
                }

                Spacer(modifier = Modifier.weight(1f))

                // Stroke width controls
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        IconButton(
                            onClick = { viewModel.onAction(DrawingAction.OnStrokeWidthChange(-2f)) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease Stroke Width", modifier = Modifier.size(18.dp))
                        }
                        Text(
                            text = "${viewModel.currentStrokeWidth.value.toInt()}",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        IconButton(
                            onClick = { viewModel.onAction(DrawingAction.OnStrokeWidthChange(2f)) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase Stroke Width", modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            // The Canvas receives the state and sends back actions
            DrawingCanvas(
                lines = viewModel.lines,
                onAction = viewModel::onAction,
                modifier = Modifier.fillMaxSize()
            )
        }

        // UI logic (showing dialogs) is driven by ViewModel state
        if (viewModel.isColorPickerVisible) {
            ColorPickerDialog(
                initialColor = viewModel.currentColor,
                onColorSelected = { selectedColor ->
                    viewModel.onAction(DrawingAction.OnColorSelected(selectedColor))
                },
                onDismiss = {
                    viewModel.onAction(DrawingAction.OnToggleColorPicker)
                }
            )
        }
    }
}
