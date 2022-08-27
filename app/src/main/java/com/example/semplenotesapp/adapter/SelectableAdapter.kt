package com.example.semplenotesapp.adapter

import android.util.SparseBooleanArray
import androidx.core.util.contains

import androidx.recyclerview.widget.RecyclerView
import com.example.semplenotesapp.data.Note


abstract class SelectableAdapter<VH : RecyclerView.ViewHolder?> :
    RecyclerView.Adapter<VH>() {
    private val selectedItems : SparseBooleanArray = SparseBooleanArray()
    private val selectedNotes = ArrayList<Note>()

    fun toggleSelection(position : Int, note : Note){
        if (selectedNotes.contains(note)){
            selectedNotes.remove(note)
        }else{
            selectedNotes.add(note)
        }
        if (selectedItems.get(position,false)){
            selectedItems.delete(position)
        }else{
            selectedItems.put(position,true)
        }
        notifyItemChanged(position)
    }

    private fun getSelectedItems() : List<Int>{
        val items = ArrayList<Int>(selectedItems.size())
        for (i in 0 until selectedItems.size()){
            items.add(selectedItems.keyAt(i))
        }
        return items
    }

    fun getSelectedNotes() : List<Note>{
        return selectedNotes
    }

    fun getSelectedItemsCount() : Int{
        return selectedItems.size()
    }

    fun isSelected(position: Int) : Boolean{
        return getSelectedItems().contains(position)
    }

    fun clearSelection(){
        val items = getSelectedItems()
        selectedItems.clear()
        for (i in items){
            notifyItemChanged(i)
        }
    }
}