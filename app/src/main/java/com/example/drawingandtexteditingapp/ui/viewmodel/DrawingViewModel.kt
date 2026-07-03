package com.example.drawingandtexteditingapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drawingandtexteditingapp.data.local.entity.DrawingEntity
import com.example.drawingandtexteditingapp.data.model.Line
import com.example.drawingandtexteditingapp.data.repository.DrawingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface DrawingAction {
    data class OnDrawStart(val startPoint: Offset) : DrawingAction
    data class OnDraw(val newPoint: Offset) : DrawingAction
    object OnClearCanvas : DrawingAction
    object OnToggleColorPicker : DrawingAction
    data class OnColorSelected(val color: Color) : DrawingAction
    object OnUndo : DrawingAction
    object OnSaveDrawing : DrawingAction
    data class OnDeleteDrawing(val drawing: DrawingEntity) : DrawingAction
    data class OnLoadDrawing(val drawing: DrawingEntity) : DrawingAction
    object OnDrawEnd : DrawingAction
    data class OnStrokeWidthChange(val delta: Float) : DrawingAction
}

class DrawingViewModel(private val repository: DrawingRepository) : ViewModel() {
    // State
    val lines = mutableStateListOf<Line>()
    var currentColor by mutableStateOf(Color.Red)
    var currentStrokeWidth by mutableStateOf(8.dp)
    var isColorPickerVisible by mutableStateOf(false)
    var currentDrawingId by mutableStateOf<Int?>(null)

    val savedDrawings: StateFlow<List<DrawingEntity>> = repository.allDrawings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun autoSaveIfExisting() {
        val id = currentDrawingId
        if (id != null) {
            viewModelScope.launch {
                val entity = DrawingEntity(
                    id = id,
                    lines = lines.toList(),
                    dateSaved = System.currentTimeMillis()
                )
                repository.insert(entity)
            }
        }
    }

    fun onAction(action: DrawingAction) {
        when (action) {
            is DrawingAction.OnDrawStart -> {
                lines.add(
                    Line(
                        points = listOf(action.startPoint),
                        color = currentColor,
                        strokeWidth = currentStrokeWidth
                    )
                )
            }
            is DrawingAction.OnDraw -> {
                if (lines.isNotEmpty()) {
                    val lastIndex = lines.lastIndex
                    val lastLine = lines[lastIndex]
                    lines[lastIndex] = lastLine.copy(points = lastLine.points + action.newPoint)
                }
            }
            DrawingAction.OnClearCanvas -> {
                lines.clear()
                autoSaveIfExisting()
            }
            DrawingAction.OnToggleColorPicker -> {
                isColorPickerVisible = !isColorPickerVisible
            }
            is DrawingAction.OnColorSelected -> {
                currentColor = action.color
                isColorPickerVisible = false
                autoSaveIfExisting()
            }
            DrawingAction.OnUndo -> {
                if (lines.isNotEmpty()) {
                    lines.removeAt(lines.lastIndex)
                    autoSaveIfExisting()
                }
            }
            DrawingAction.OnSaveDrawing -> {
                viewModelScope.launch {
                    val entity = DrawingEntity(
                        id = currentDrawingId ?: 0,
                        lines = lines.toList(),
                        dateSaved = System.currentTimeMillis()
                    )
                    val newId = repository.insert(entity)
                    // If it was a new drawing, we now have an ID, so future edits auto-save.
                    if (currentDrawingId == null) {
                        currentDrawingId = newId.toInt()
                    }
                }
            }
            is DrawingAction.OnDeleteDrawing -> {
                viewModelScope.launch {
                    repository.delete(action.drawing)
                }
            }
            is DrawingAction.OnLoadDrawing -> {
                lines.clear()
                lines.addAll(action.drawing.lines)
                currentDrawingId = action.drawing.id
            }
            DrawingAction.OnDrawEnd -> {
                autoSaveIfExisting()
            }
            is DrawingAction.OnStrokeWidthChange -> {
                val newWidth = (currentStrokeWidth.value + action.delta).coerceIn(2f, 50f)
                currentStrokeWidth = newWidth.dp
            }
        }
    }

    // Call this when the user finishes a stroke (up event)
    fun onDrawEnd() {
        autoSaveIfExisting()
    }
}
