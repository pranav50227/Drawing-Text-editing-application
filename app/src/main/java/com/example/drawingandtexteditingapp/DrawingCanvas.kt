package com.example.drawingandtexteditingapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.geometry.Offset

@Composable
fun DrawingCanvas(
    lines: List<Line>,
    onAction: (DrawingAction) -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .testTag("DrawingCanvas")
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { startOffset ->
                            onAction(DrawingAction.OnDrawStart(startOffset))
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            onAction(DrawingAction.OnDraw(change.position))
                        },
                        onDragEnd = {
                            onAction(DrawingAction.OnDrawEnd)
                        },
                        onDragCancel = {
                            onAction(DrawingAction.OnDrawEnd)
                        }
                    )
                }
        ) {
            lines.forEach { line ->
                for (i in 0 until line.points.size - 1) {
                    drawLine(
                        color = line.color,
                        start = line.points[i],
                        end = line.points[i + 1],
                        strokeWidth = line.strokeWidth.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}
