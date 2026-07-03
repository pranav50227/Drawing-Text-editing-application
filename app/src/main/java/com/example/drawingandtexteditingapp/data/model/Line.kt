package com.example.drawingandtexteditingapp.data.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Line(
    val points : List<Offset>,
    val color: Color = Color.Black ,
    val strokeWidth : Dp = 4.dp
)
