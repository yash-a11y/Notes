package com.example.notes

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.databinding.ItemnoteBinding


class noteadapter(private var notes : ArrayList<notes_entity>
,private val updateListner : (id:Int)->Unit
,private val DeleteListner : (id:Int)->Unit
) : RecyclerView.Adapter<noteadapter.viewholder>() {

    inner class viewholder(bind: ItemnoteBinding): RecyclerView.ViewHolder(bind.root)
    {
        val itemlayout = bind?.itemlay
        val text : TextView = bind?.textView
        val delete : ImageView = bind?.di
        val update : ImageView = bind?.pi
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(ItemnoteBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
      val context = holder.itemView.context
        val note = notes[position]

        holder.text.text = note.note.toString()

        if(position % 2 ==0)
        {
            holder.itemlayout.setBackgroundColor(Color.parseColor("#EBEBEBEB"))
        }
        else
        {
            holder.itemlayout.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
        }

        holder.update.setOnClickListener{
            updateListner.invoke(note.id)
        }

        holder.delete.setOnClickListener {
            DeleteListner.invoke(note.id)
        }

    }

    override fun getItemCount(): Int {
        return notes.size
    }
}