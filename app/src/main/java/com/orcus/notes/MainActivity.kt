package com.orcus.notes

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.orcus.notes.lifecycle.NotesViewModel
import com.orcus.notes.utils.NoteApplication
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var  notesViewModel: NotesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        

        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
        notesViewModel.allNotes?.observe(this, Observer {

            val note = it[0]

            noteName.text = note.content

        })


    }
}