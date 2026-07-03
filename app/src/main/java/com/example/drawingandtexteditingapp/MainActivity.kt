package com.example.drawingandtexteditingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.drawingandtexteditingapp.ui.theme.DrawingAndTextEditingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrawingAndTextEditingAppTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val application = context.applicationContext as DrawingApplication
                val viewModel: DrawingViewModel = viewModel(
                    factory = DrawingViewModelFactory(application.repository)
                )

                NavHost(navController = navController, startDestination = "drawings_list") {
                    composable("drawings_list") {
                        DrawingListScreen(
                            viewModel = viewModel,
                            onNavigateToDrawing = {
                                navController.navigate("drawing_canvas")
                            }
                        )
                    }
                    composable("drawing_canvas") {
                        DrawingScreen(
                            viewModel = viewModel,
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DrawingAndTextEditingAppTheme {
        Greeting("Android")
    }
}