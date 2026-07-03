package com.example.drawingandtexteditingapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drawings")
data class DrawingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lines: List<Line>,
    val dateSaved: Long
)
