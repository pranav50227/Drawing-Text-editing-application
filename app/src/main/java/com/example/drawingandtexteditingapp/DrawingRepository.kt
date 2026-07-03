package com.example.drawingandtexteditingapp

import kotlinx.coroutines.flow.Flow

class DrawingRepository(private val drawingDao: DrawingDao) {
    val allDrawings: Flow<List<DrawingEntity>> = drawingDao.getAllDrawings()

    suspend fun insert(drawing: DrawingEntity): Long {
        return drawingDao.insertDrawing(drawing)
    }

    suspend fun delete(drawing: DrawingEntity) {
        drawingDao.deleteDrawing(drawing)
    }

    suspend fun getDrawingById(id: Int): DrawingEntity? {
        return drawingDao.getDrawingById(id)
    }
}
