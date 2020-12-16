package com.orcus.notes.ui

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.orcus.notes.R
import com.orcus.notes.adapters.NoteAdapter
import com.orcus.notes.data.Note
import com.orcus.notes.lifecycle.NotesViewModel
import com.orcus.notes.utils.NoteApplication
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    companion object {
        const val TAG = "homeFragment"
        const val ADD_NOTE_REQUEST_CODE = 1
        const val EDIT_NOTE_REQUEST_CODE = 2
        const val ARGS_NOTE = "NoteArgument"
    }


    private val notesViewModel: NotesViewModel by viewModels {
        NotesViewModel.NotesViewModelFactory((context?.applicationContext as NoteApplication).repository)
    }

    private var notes: ArrayList<Note> = ArrayList()
    private lateinit var adapter: NoteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        setUpRecyclerView()



        adapter.setOnItemClickListener(object : NoteAdapter.OnItemClick {
            override fun setOnItemClickListener(note: Note, position: Int) {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                    EDIT_NOTE_REQUEST_CODE, note
                )

                findNavController().navigate(action)
            }

        })

        addNoteButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                ADD_NOTE_REQUEST_CODE, null
            )

            findNavController().navigate(action)
        }
    }

    override fun onStart() {
        super.onStart()


        //Getting all the notes from the database (if any)
        notesViewModel.allNotes?.observe(requireActivity(), Observer {

            if (it.isEmpty()) {
                tv_no_notes_saved?.visibility = View.VISIBLE
            } else {
                tv_no_notes_saved?.visibility = View.GONE
            }

            notes.clear()
            adapter.notifyDataSetChanged()


            for (i in it.indices) {

                notes.add(it[i])
                adapter.notifyItemInserted(i)
            }

        })

    }

    //TODO fix the delete all function
    private fun deleteAll() {
        for (i in 0 until notes.size) {
            try {
                val note = notes[i]
                notes.removeAt(i)
                //notesViewModel.delete(note)
                adapter.notifyItemRemoved(i)

            } catch (e: Exception) {
                Log.d(TAG, "deleteAll: ${e.message} and  the current i = $i")

            }

        }
        notesViewModel.deleteAll()
    }

    private fun setUpRecyclerView() {
        notesRecyclerView.setHasFixedSize(true)
        adapter = NoteAdapter(context, notes)
        notesRecyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(simpleCallback())
        itemTouchHelper.attachToRecyclerView(notesRecyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.delete_all_btn -> {

                deleteAll()
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun simpleCallback(): ItemTouchHelper.SimpleCallback =
        (object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT and ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }


            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return createSwipeFLags(viewHolder.adapterPosition)
            }


            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val position = viewHolder.adapterPosition
                val swipeFlags = createSwipeFLags(position)
                return makeMovementFlags(0, swipeFlags)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.adapterPosition

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        val noteToDelete = notes[position]
                        notes.removeAt(position)
                        adapter.notifyItemRemoved(position)

                        notesViewModel.delete(noteToDelete)

                        Snackbar.make(
                            notesRecyclerView,
                            noteToDelete.content,
                            Snackbar.LENGTH_LONG
                        ).setAction("Undo") {
                            notesViewModel.insert(noteToDelete)
                            notes.add(position, noteToDelete)
                            adapter.notifyItemInserted(position)
                        }
                            .setAnchorView(addNoteButton)
                            .show()

                    }

                    ItemTouchHelper.RIGHT -> {
                        val noteToDelete = notes[position]
                        notes.removeAt(position)
                        adapter.notifyItemRemoved(position)

                        notesViewModel.delete(noteToDelete)

                        Snackbar.make(
                            notesRecyclerView,
                            noteToDelete.content,
                            Snackbar.LENGTH_LONG
                        ).setAction("Undo") {
                            notesViewModel.insert(noteToDelete)
                            notes.add(position, noteToDelete)
                            adapter.notifyItemInserted(position)
                        }
                            .setAnchorView(addNoteButton)
                            .show()
                    }
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                val foregroundView: View = (viewHolder as NoteAdapter.NoteHolder).viewForeground

                getDefaultUIUtil().clearView(foregroundView)
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {

                if (viewHolder != null) {
                    val foregroundView: View = (viewHolder as NoteAdapter.NoteHolder).viewForeground

                    getDefaultUIUtil().onSelected(foregroundView)
                }

            }

            override fun onChildDrawOver(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder?,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                val foregroundView: View = (viewHolder as NoteAdapter.NoteHolder).viewForeground

                getDefaultUIUtil().onDrawOver(
                    c,
                    recyclerView,
                    foregroundView,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                /*
                super.onChildDrawOver(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                 */
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                val foregroundView: View = (viewHolder as NoteAdapter.NoteHolder).viewForeground
                getDefaultUIUtil().onDraw(
                    c,
                    recyclerView,
                    foregroundView,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                /*
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                 */

            }

        })

    private fun createSwipeFLags(position: Int): Int {
        return if (position % 2 == 0) {
            ItemTouchHelper.LEFT
        } else {
            ItemTouchHelper.RIGHT
        }
    }


}