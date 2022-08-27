package com.example.semplenotesapp.notelistscreen

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.AbsListViewBindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.semplenotesapp.R
import com.example.semplenotesapp.adapter.NoteListAdapter
import com.example.semplenotesapp.data.Note
import com.example.semplenotesapp.data.NoteRepository
import com.example.semplenotesapp.data.NotesDatabase
import com.example.semplenotesapp.databinding.FragmentNoteListBinding
import java.util.*


class NoteListFragment : Fragment() {
    private lateinit var binding: FragmentNoteListBinding

    private lateinit var noteListViewModel: NoteListViewModel
    private lateinit var manager: StaggeredGridLayoutManager
    private lateinit var adapter: NoteListAdapter
    private  lateinit var noteList : ArrayList<Note>
    private lateinit var searchText : String


    private var mActionMode: ActionMode? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_note_list, container, false)

        val mActivity = activity as AppCompatActivity?
        mActivity?.setSupportActionBar(binding.toolbar)
        val application = requireNotNull(this.activity).application
        val noteRepository = NoteRepository(NotesDatabase.getInstance(application))
        val noteListViewModelFactory = NoteListViewModelFactory(noteRepository)
        noteListViewModel = ViewModelProvider(this,noteListViewModelFactory).get(NoteListViewModel::class.java)

//        noteList = (noteListViewModel.getAllNotes().value as ArrayList<Note>?)!!
        noteList = ArrayList()


        manager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        adapter = NoteListAdapter(listener)

        binding.noteRv.notesRecyclerView.adapter = adapter
        binding.noteRv.notesRecyclerView.layoutManager = manager


        noteListViewModel.getAllNotes().observe(viewLifecycleOwner){ notelist->
            noteList = notelist as ArrayList<Note>
            adapter.updateList(notelist as ArrayList<Note>)
        }

        binding.addNoteFab.setOnClickListener {
            this.findNavController().navigate(
                NoteListFragmentDirections.actionNoteListFragmentToUpdateOrAddNoteFragment()
            )
        }

        binding.noteRv.notesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy>0){
                    binding.addNoteFab.extend()
                }else{
                    binding.addNoteFab.shrink()
                }
            }
        })


        binding.btnLayout.setOnClickListener {
            noteListViewModel.toggleLayout()
        }
        noteListViewModel.isStaggered.observe(viewLifecycleOwner){
            if (it){
                binding.noteRv.notesRecyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                binding.btnLayout.setImageResource(R.drawable.ic_staggered_layout)
            }else{
                binding.noteRv.notesRecyclerView.layoutManager = LinearLayoutManager(context)
                binding.btnLayout.setImageResource(R.drawable.ic_linear_layout)
            }
        }
        notesSearch()
        return  binding.root
    }


   private val listener = object: NoteListAdapter.ItemClickListener {
      override fun onClick(note: Note , position : Int) {
          if (mActionMode == null){
              findNavController().navigate(NoteListFragmentDirections.actionNoteListFragmentToUpdateOrAddNoteFragment().setNote(note))
          }else{
              toggleSelection(position,note)
          }
      }

       override fun onLongClick(note : Note ,position : Int): Boolean {
           if (mActionMode==null){
               mActionMode = activity?.startActionMode(myActModeCallback)

           }
           toggleSelection(position,note)

           return true
       }

       fun toggleSelection(position : Int , note: Note){
           adapter.toggleSelection(position , note)
           val count = adapter.getSelectedItemsCount()
           if (count >= 0){
               mActionMode?.title = "$count item selected"
               mActionMode?.invalidate();
           }
       }
   }
    private val myActModeCallback = object : ActionMode.Callback {

        override fun onCreateActionMode(mode: ActionMode, menu: Menu?): Boolean {
            mode.menuInflater.inflate(R.menu.example_menu, menu)
//            mode.title = "Choose your option"
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.option_1 -> {
                    val selectedNotes = adapter.getSelectedNotes()
                    for (note in selectedNotes){
                        noteListViewModel.deleteNote(note)
                    }
                    adapter.notifyDataSetChanged()
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT)
                        .show()
                    mode.finish()
                    true
                }
                R.id.option_2 -> {
                    Toast.makeText(context, "Shared", Toast.LENGTH_SHORT)
                        .show()
                    mode.finish()
                    true
                }
                else -> false
            }

        }

        override fun onDestroyActionMode(mode: ActionMode) {
            adapter.clearSelection()
            mActionMode = null
        }
    }

    private fun notesSearch(){
        binding.noteSearchEdittext.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                searchText = p0.toString().lowercase(Locale.getDefault())
                updateNoteRecyclerView(searchText)
                Log.d("NoteListFragment", "afterTextChanged: ")
            }

        })
    }

    fun updateNoteRecyclerView(searchText : String){
        val data = ArrayList<Note>()

            for (item in noteList){
                val title = item.title.lowercase(Locale.getDefault())
                val description = item.content.lowercase(Locale.getDefault())

                Log.d("NoteListFragment", "updateNoteRecyclerView: ")

                if (title.contains(searchText) || description.contains(searchText)){
                    data.add(item)
                }
            }

        adapter.filterList(data)
    }
}