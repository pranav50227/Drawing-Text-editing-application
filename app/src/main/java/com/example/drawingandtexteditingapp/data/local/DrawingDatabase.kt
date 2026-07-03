package com.example.drawingandtexteditingapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.drawingandtexteditingapp.data.local.dao.DrawingDao
import com.example.drawingandtexteditingapp.data.local.entity.DrawingEntity

@Database(entities = [DrawingEntity::class], version = 1, exportSchema = false)
@TypeConverters(DrawingConverters::class)
abstract class DrawingDatabase : RoomDatabase() {
    abstract fun drawingDao(): DrawingDao
}
