package com.example.semplenotesapp.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.ThemeCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.semplenotesapp.R
import com.example.semplenotesapp.data.Note
import com.example.semplenotesapp.databinding.NoteItemLayoutBinding
import com.example.semplenotesapp.util.TimesAgo

class NoteListAdapter(private val clickListener: NoteListAdapter.ItemClickListener) :
    SelectableAdapter<NoteListAdapter.MyViewHolder>() {

    private var items = ArrayList<Note>()

    class MyViewHolder(private val binding : NoteItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val card = binding.noteCard
        val title = binding.noteItemTitle
        val content = binding.noteItemContent
        val date = binding.noteItemDate
    }

    interface ItemClickListener {
        fun onClick(note: Note , position : Int)
        fun onLongClick(note : Note ,position : Int ): Boolean
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater =  LayoutInflater.from(parent.context)
        val binding = NoteItemLayoutBinding.inflate(layoutInflater,parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.content.text = item.content
        holder.title.text = item.title
        holder.date.text = TimesAgo.getTimeAgo(item.time)
        holder.itemView.setOnClickListener {
            clickListener.onClick(item,position)
        }
        if (isSelected(position)){
            holder.card.strokeColor = ContextCompat.getColor(holder.card.context,R.color.orange)
        }else{
            holder.card.strokeColor = ContextCompat.getColor(holder.card.context,R.color.transparent)
        }
        holder.itemView.setOnLongClickListener {
            return@setOnLongClickListener clickListener.onLongClick(item,position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateList(list: ArrayList<Note>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun filterList(filterList: ArrayList<Note>) {
        // below line is to add our filtered
        // list in our course array list.
        items = filterList
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }
}