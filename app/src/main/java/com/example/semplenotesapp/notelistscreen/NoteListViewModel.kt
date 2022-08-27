package com.example.semplenotesapp.notelistscreen

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.semplenotesapp.data.Note
import com.example.semplenotesapp.data.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteListViewModel(private val noteRepository: NoteRepository) : ViewModel() {

    private val _isStaggered = MutableLiveData<Boolean>()
    val isStaggered : LiveData<Boolean>
    get() = _isStaggered

    init {
        _isStaggered.value = true
    }
    fun deleteNote(existingNote: Note) = viewModelScope.launch(Dispatchers.IO) {
        noteRepository.deleteNote(existingNote)
    }

    fun getAllNotes() : LiveData<List<Note>> = noteRepository.getAllNotes()

    fun toggleLayout(){
        _isStaggered.value =!_isStaggered.value!!
    }
}