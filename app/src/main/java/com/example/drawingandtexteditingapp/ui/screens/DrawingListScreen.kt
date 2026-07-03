package com.example.drawingandtexteditingapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.drawingandtexteditingapp.R
import com.example.drawingandtexteditingapp.data.model.Line
import com.example.drawingandtexteditingapp.ui.viewmodel.DrawingAction
import com.example.drawingandtexteditingapp.ui.viewmodel.DrawingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawingListScreen(
    viewModel: DrawingViewModel,
    onNavigateToDrawing: () -> Unit
) {
    val savedDrawings by viewModel.savedDrawings.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Drawing List Screen") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { paddingValues ->
            if (savedDrawings.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Let's Draw",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    OutlinedCard(
                        onClick = {
                            viewModel.onAction(DrawingAction.OnClearCanvas)
                            onNavigateToDrawing()
                        },
                        modifier = Modifier.size(160.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Create Drawing",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // First square: Create Drawing
                    item {
                        OutlinedCard(
                            onClick = {
                                viewModel.onAction(DrawingAction.OnClearCanvas)
                                onNavigateToDrawing()
                            },
                            modifier = Modifier.aspectRatio(1f),
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "Create Drawing",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }

                    // Subsequent squares: Drawing Previews
                    itemsIndexed(savedDrawings) { index, drawing ->
                        ElevatedCard(
                            onClick = {
                                viewModel.onAction(DrawingAction.OnLoadDrawing(drawing))
                                onNavigateToDrawing()
                            },
                            modifier = Modifier.aspectRatio(1f),
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                            )
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth()
                                            .background(Color.White)
                                            .padding(8.dp)
                                    ) {
                                        DrawingPreview(lines = drawing.lines)
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Drawing ${index + 1}",
                                            style = MaterialTheme.typography.labelMedium,
                                            maxLines = 1
                                        )
                                        IconButton(
                                            onClick = { viewModel.onAction(DrawingAction.OnDeleteDrawing(drawing)) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                modifier = Modifier.size(16.dp),
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawingPreview(lines: List<Line>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (lines.isEmpty()) return@Canvas

        // Calculate bounds to scale the drawing to fit the thumbnail
        var minX = Float.MAX_VALUE
        var minY = Float.MAX_VALUE
        var maxX = Float.MIN_VALUE
        var maxY = Float.MIN_VALUE

        lines.forEach { line ->
            line.points.forEach { point ->
                minX = minOf(minX, point.x)
                minY = minOf(minY, point.y)
                maxX = maxOf(maxX, point.x)
                maxY = maxOf(maxY, point.y)
            }
        }

        val drawingWidth = (maxX - minX).coerceAtLeast(1f)
        val drawingHeight = (maxY - minY).coerceAtLeast(1f)

        val scale = minOf(size.width / drawingWidth, size.height / drawingHeight) * 0.8f

        val offsetX = (size.width - drawingWidth * scale) / 2f - minX * scale
        val offsetY = (size.height - drawingHeight * scale) / 2f - minY * scale

        lines.forEach { line ->
            for (i in 0 until line.points.size - 1) {
                drawLine(
                    color = line.color,
                    start = Offset(line.points[i].x * scale + offsetX, line.points[i].y * scale + offsetY),
                    end = Offset(line.points[i + 1].x * scale + offsetX, line.points[i + 1].y * scale + offsetY),
                    strokeWidth = (line.strokeWidth.toPx() * scale).coerceAtLeast(1f),
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DrawingPreviewPreview() {
    Box(modifier = Modifier.size(100.dp).padding(8.dp)) {
        DrawingPreview(
            lines = listOf(
                Line(
                    points = listOf(Offset(0f, 0f), Offset(100f, 100f), Offset(200f, 0f)),
                    color = Color.Red,
                    strokeWidth = 4.dp
                ),
                Line(
                    points = listOf(Offset(0f, 200f), Offset(100f, 100f), Offset(200f, 200f)),
                    color = Color.Blue,
                    strokeWidth = 4.dp
                )
            )
        )
    }
}
