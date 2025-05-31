package com.n1ck120.easydoc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlin.String

class ModelsAdapter(private val dataSet1: Array<String>,private val dataSet2: Array<String>) :
    RecyclerView.Adapter<ModelsAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val dataSet:Array<String>) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val desc: TextView = view.findViewById(R.id.desc)
        val card: MaterialCardView = view.findViewById(R.id.recyclerCard)

        init {
            card.setOnClickListener {
                //dialog.docDialog("Editar documento",dataSet[adapterPosition].title.toString(),dataSet[adapterPosition].content.toString().toString(),"",dataSet[adapterPosition].doc_name.toString().toString())
            }

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_models_layout, viewGroup, false)

        return ViewHolder(view, dataSet1)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.title.text = dataSet1[position]
        viewHolder.desc.text = dataSet2[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet1.size

}