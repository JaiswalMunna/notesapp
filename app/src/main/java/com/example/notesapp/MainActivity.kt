package com.example.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notesapp.ui.theme.NotesAppTheme
import com.example.notesapp.ui_layer.NoteScreen
import com.example.notesapp.ui_layer.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.Serializable


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        setContent {
            NotesAppTheme {

                Scaffold(modifier=Modifier.fillMaxSize()){
                    innerPadding->
                    val viewModel = hiltViewModel<NoteViewModel>()
                    val state by viewModel.state.collectAsState()
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)) {

                        val navController= rememberNavController()
                        NavHost(
                            navController=navController,
                            startDestination = Screen.NoteScreen
                        ){
                            composable<Screen.NoteScreen> {
                                NoteScreen(navController=navController,state = state, onEvent=viewModel::onEvent)
                            }
                            composable<Screen.AddNoteScreen> {
                                AddNoteScreen(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed class Screen(){
    @Serializable
    object NoteScreen

    @Serializable
    object AddNoteScreen
}