package com.example.semplenotesapp.updateoraddnotescreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.semplenotesapp.R
import com.example.semplenotesapp.data.Note
import com.example.semplenotesapp.data.NoteRepository
import com.example.semplenotesapp.data.NotesDatabase
import com.example.semplenotesapp.databinding.FragmentUpdateOrAddNoteBinding
import com.example.semplenotesapp.util.TimesAgo
import java.text.SimpleDateFormat
import java.util.*


class UpdateOrAddNoteFragment : Fragment() {

    private var note : Note? = null
    private val currentDate = SimpleDateFormat.getInstance().format(Date())
    private val args : UpdateOrAddNoteFragmentArgs by navArgs()
    private lateinit var binding: FragmentUpdateOrAddNoteBinding
    private lateinit var updateOrAddNoteViewModel : UpdateOrAddNoteViewModel
    private lateinit var navController : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = DataBindingUtil.inflate(inflater,R.layout.fragment_update_or_add_note,container,false)

        binding.etTitle.requestFocus()
//        if (binding.etTitle.text.equals("")){
//            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
//        }

        val application = requireNotNull(this.activity).application
        val noteRepository = NoteRepository(NotesDatabase.getInstance(application))
        val updateOrAddNoteViewModelFactory = UpdateOrAddNoteViewModelFactory(noteRepository)
        updateOrAddNoteViewModel = ViewModelProvider(this,updateOrAddNoteViewModelFactory).get(UpdateOrAddNoteViewModel::class.java)
        navController = this.findNavController()

        binding.backImage.setOnClickListener {
            navController.popBackStack()
        }

        binding.saveImage.setOnClickListener {
            saveNote()
        }

        setUpNote()

        return binding.root
    }

    private fun setUpNote() {
        val note = args.note
        val title = binding.etTitle
        val content = binding.etContent
        val lastEdited = binding.editTime

        if (note == null){
            binding.editTime.text = getString(R.string.edited_on , SimpleDateFormat.getDateInstance().format(Date()))
        }
        if (note != null){
            title.setText(note.title)
            content.setText(note.content)
            lastEdited.text = getString(R.string.edited_on , TimesAgo.getTimeAgo(note.time))
        }
    }


    private fun saveNote() {
        if (binding.etTitle.text.toString().isNotEmpty() && binding.etContent.text.toString().isNotEmpty()){
            note = args.note

            when(note) {
                null -> {
                    updateOrAddNoteViewModel.saveNote(
                        Note(
                            0,
                            binding.etTitle.text.toString(),
                            binding.etContent.text.toString(),
//                            currentDate
                        System.currentTimeMillis()
                        )
                    )
                    navController.navigate(UpdateOrAddNoteFragmentDirections.actionUpdateOrAddNoteFragmentToNoteListFragment())
                }
                else->{
                    updateNote()
                    navController.popBackStack()
                }
            }
        }
    }

    private fun updateNote() {
        if (note != null){
            updateOrAddNoteViewModel.updateNote(
                Note(
                    note!!.id,
                    binding.etTitle.text.toString(),
                    binding.etContent.text.toString(),
//                    currentDate
                    System.currentTimeMillis()
                )
            )
        }
    }

}