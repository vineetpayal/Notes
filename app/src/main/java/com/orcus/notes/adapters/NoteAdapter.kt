package com.orcus.notes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.orcus.notes.R
import com.orcus.notes.data.Note
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(var context: Context?, var notes: ArrayList<Note>) :
    RecyclerView.Adapter<NoteAdapter.NoteHolder>() {

    private lateinit var clickListener: OnItemClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)
        return NoteHolder(view)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.contentView.text = notes[position].content
        holder.timeCreated.text = formatTimeCreated(notes[position].timeCreated)

        if (position % 2 != 0) {
            holder.leftToRightBgView.visibility = View.VISIBLE
            holder.rightToLeftBgView.visibility = View.GONE
        } else {
            holder.rightToLeftBgView.visibility = View.VISIBLE
            holder.leftToRightBgView.visibility = View.GONE
        }

    }

    private fun formatTimeCreated(timeCreated: Long): String? {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeCreated
        calendar.timeZone = null

        val date = DateFormat.getDateInstance().format(calendar.time)
        val time = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(timeCreated)

        return "$date,\n$time"
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentView: TextView = itemView.findViewById(R.id.contentView)
        val timeCreated: TextView = itemView.findViewById(R.id.timeCreated)

        val viewBackground: RelativeLayout = itemView.findViewById(R.id.background_view)
        val viewForeground: MaterialCardView = itemView.findViewById(R.id.foreground_view)

        val leftToRightBgView: RelativeLayout =
            viewBackground.findViewById(R.id.leftToRightSwipeView)

        val rightToLeftBgView: RelativeLayout =
            viewBackground.findViewById(R.id.rightToLeftSwipeView)

        init {
            viewForeground.setOnClickListener {
                if (clickListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                    clickListener.setOnItemClickListener(notes[adapterPosition], adapterPosition)
                }
            }
        }
    }

    interface OnItemClick {
        fun setOnItemClickListener(note: Note, position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClick) {
        this.clickListener = clickListener
    }
}


