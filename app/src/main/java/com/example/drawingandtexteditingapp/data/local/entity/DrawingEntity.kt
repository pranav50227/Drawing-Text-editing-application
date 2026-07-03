package com.example.drawingandtexteditingapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.drawingandtexteditingapp.data.model.Line

@Entity(tableName = "drawings")
data class DrawingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lines: List<Line>,
    val dateSaved: Long
)
