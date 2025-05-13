package com.n1ck120.easydoc

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CustomAdapter(private val dataSet: Array<String>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.title)
        val card: MaterialCardView = view.findViewById(R.id.recyclerCard)
        val delete: Button = view.findViewById(R.id.deleteButton)

        init {
            // Define click listener for the ViewHolder's View
            delete.setOnClickListener {
                val dialogView = LayoutInflater.from(view.context).inflate(R.layout.delete_dialog, null)
                val dialog = MaterialAlertDialogBuilder(dialogView.context)
                    .setView(dialogView)
                    .create()
                val exit = dialogView.findViewById<Button>(R.id.confirm)
                val cancel = dialogView.findViewById<Button>(R.id.cancel)
                exit.setOnClickListener{
                    card.visibility = GONE
                    dialog.dismiss()
                }
                cancel.setOnClickListener{
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_layout, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}