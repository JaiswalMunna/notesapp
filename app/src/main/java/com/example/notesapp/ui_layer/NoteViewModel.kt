package com.example.notesapp.ui_layer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data_layer.Note
import com.example.notesapp.data_layer.NoteDB
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteViewModel @Inject constructor(noteDB: NoteDB) : ViewModel() {
    val dao=noteDB.noteDao
    private val isSortedByDataAdded= MutableStateFlow(true)
    private var notes= isSortedByDataAdded.flatMapLatest {
        if (it){
            dao.getAllOrderedDateAdded()
        }else{
            dao.getAllOrderedTitle()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )
    val _state= MutableStateFlow(NoteState())
    val state= combine(_state,isSortedByDataAdded,notes){
        state,isSortedByDateAdded,notes->state.copy(
            notes = notes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())

    fun onEvent(event:NoteEvent){
        when(event){
            is NoteEvent.DeleteNote->{
                viewModelScope.launch {
                    dao.delete(event.note)
                }
            }
            is NoteEvent.SaveNote -> {
                viewModelScope.launch {
                    val note = Note(
                        title = state.value.title.value,
                        disp = state.value.disp.value,
                        dateDated = System.currentTimeMillis()
                    )
                    viewModelScope.launch { dao.upsert(note) }
                }
            }
            NoteEvent.SortNotes-> {
                isSortedByDataAdded.value=!isSortedByDataAdded.value
            }
        }
    }
}