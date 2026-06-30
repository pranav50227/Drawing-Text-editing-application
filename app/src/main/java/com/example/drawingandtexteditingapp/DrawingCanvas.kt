package com.example.drawingandtexteditingapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DrawingCanvas(
    currentColor : Color ,
    currentStrokeWidth : Dp ,
    modifier: Modifier
){
    val lines = remember { mutableStateListOf<Line>() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ){
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit){
                    detectDragGestures(
                        onDragStart = {startOffset ->
                            lines.add(Line(
                                points = listOf(startOffset),
                                color = currentColor ,
                                strokeWidth = currentStrokeWidth))
                        },
                        onDrag = {change, dragAmount ->
                            change.consume()
                            if (lines.isNotEmpty()){
                                val lastIndex = lines.lastIndex
                                val lastline = lines[lastIndex]
                                val newPoint = lastline.points.last() + dragAmount
                                lines[lastIndex] = lastline.copy(points = lastline.points + newPoint)
                            }
                        }
                    )
                }
        ) {
            lines.forEach { line->
                for (i in 0 until line.points.size - 1){
                    drawLine(
                        color = line.color ,
                        start = line.points[i],
                        end = line.points[i+1] ,
                        strokeWidth = line.strokeWidth.toPx() ,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}