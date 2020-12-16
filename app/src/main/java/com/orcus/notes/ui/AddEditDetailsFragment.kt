package com.orcus.notes.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.orcus.notes.R
import com.orcus.notes.data.Note
import com.orcus.notes.lifecycle.NotesViewModel
import com.orcus.notes.ui.HomeFragment.Companion.TAG
import com.orcus.notes.utils.NoteApplication
import kotlinx.android.synthetic.main.fragment_add_edit_details.*

class AddEditDetailsFragment : Fragment(R.layout.fragment_add_edit_details) {

    val args: AddEditDetailsFragmentArgs by navArgs()
    private var note: Note? = null
    private var isEditMode: Boolean = false

    lateinit var imm: InputMethodManager


    private val notesViewModel: NotesViewModel by viewModels {
        NotesViewModel.NotesViewModelFactory((context?.applicationContext as NoteApplication).repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        note = args.note
        if (args.requestCode == HomeFragment.EDIT_NOTE_REQUEST_CODE) {
            et_content.setText(note?.content)
            isEditMode = true
        }

        et_content.requestFocus()
        imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(et_content, InputMethodManager.SHOW_IMPLICIT)
        setHasOptionsMenu(true)

    }

    override fun onStart() {
        super.onStart()
        if (!isEditMode) {

        }
    }


    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Fragment stopped")

        imm.hideSoftInputFromWindow(et_content.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.details_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            //Saving note to the database
            R.id.save_note_btn -> {
                val noteString = et_content.text.toString()

                val action =
                    AddEditDetailsFragmentDirections.actionDetailsFragmentToHomeFragment()

                //Checking if the user is adding the note for the first time or updating the old note
                if (!isEditMode) {
                    if (noteString.isNotEmpty()) {

                        val note = Note(noteString.trim())
                        notesViewModel.insert(note)


                        findNavController().navigate(action)

                    } else {
                        Toast.makeText(context, "Please enter a note first", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    if (!note?.content.equals(noteString)) {
                        note?.let {
                            note?.content = ""
                            note?.content = noteString.trim()
                            notesViewModel.update(note!!)
                        }
                    } else {
                        Log.d(TAG, "onOptionsItemSelected: No changes found in the note ")
                    }

                    findNavController().navigate(action)
                }
            }

            //Sharing the note to other apps
            R.id.share_note_btn -> {
                val noteString = et_content.text.toString()
                if (noteString.isNotEmpty()) {
                    Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, noteString)
                        startActivity(Intent.createChooser(this, null))
                    }
                }
            }
        }


        return super.onOptionsItemSelected(item)
    }
}