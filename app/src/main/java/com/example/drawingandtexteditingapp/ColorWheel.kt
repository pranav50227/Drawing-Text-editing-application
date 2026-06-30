package com.example.drawingandtexteditingapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.*

@Composable
fun ColorWheel(
    hue: Float,
    saturation: Float,
    modifier: Modifier = Modifier,
    onColorChanged: (hue: Float, saturation: Float) -> Unit
) {
    val sweepColors = listOf(
        Color.Red, Color.Yellow, Color.Green, Color.Cyan,
        Color.Blue, Color.Magenta, Color.Red
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f) // Keeps it a perfect circle
            .pointerInput(Unit) {
                // Handle continuous dragging
                detectDragGestures { change, _ ->
                    change.consume()
                    calculateHueAndSaturation(change.position, size.width / 2f, onColorChanged)
                }
            }
            .pointerInput(Unit) {
                // Handle single taps
                detectTapGestures { offset ->
                    calculateHueAndSaturation(offset, size.width / 2f, onColorChanged)
                }
            }
    ) {
        val radius = size.width / 2f
        val center = Offset(radius, radius)

        // 1. Draw the Rainbow Hue Ring
        drawCircle(
            brush = Brush.sweepGradient(colors = sweepColors, center = center),
            radius = radius
        )

        // 2. Draw the White Center (Saturation)
        // Fades from solid white in the middle to transparent at the edges
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White, Color.Transparent),
                center = center,
                radius = radius
            ),
            radius = radius
        )

        // 3. Draw the Selection Indicator (The little circle showing current pick)
        val selectionRadius = saturation * radius
        val angleRad = hue * (PI / 180f).toFloat()
        val x = center.x + selectionRadius * cos(angleRad)
        val y = center.y + selectionRadius * sin(angleRad)

        drawCircle(
            color = Color.Black,
            radius = 6.dp.toPx(),
            center = Offset(x, y),
            style = Stroke(width = 2.dp.toPx())
        )
        drawCircle(
            color = Color.White,
            radius = 4.dp.toPx(),
            center = Offset(x, y),
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

// --- The Core Math Logic ---
private fun calculateHueAndSaturation(
    touchPosition: Offset,
    radius: Float,
    onColorChanged: (Float, Float) -> Unit
) {
    // Distance from center on X and Y axes
    val dx = touchPosition.x - radius
    val dy = touchPosition.y - radius

    // 1. Calculate the Angle (Hue) using atan2
    // atan2 returns radians between -PI and PI. We convert to 0-360 degrees.
    var angle = (atan2(dy, dx) * (180f / PI)).toFloat()
    if (angle < 0) angle += 360f

    // 2. Calculate the Distance from center (Saturation) using Pythagorean theorem
    val distance = hypot(dx, dy)
    // Coerce the distance so it doesn't exceed the radius if the user drags outside the box
    val saturation = (distance / radius).coerceIn(0f, 1f)

    onColorChanged(angle, saturation)
}