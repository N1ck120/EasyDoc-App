package com.n1ck120.easydoc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class CustomAdapter(private val dataSet: MutableList<Doc>, private val  callDel: (Doc) -> Unit, private val  callUpd: (Doc, Doc) -> Unit) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val dataSet: MutableList<Doc> , private val  callbackDel: (Doc) -> Unit, private val  callbackUpd: (Doc, Doc) -> Unit) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val content: TextView = view.findViewById(R.id.content)
        val date: TextView = view.findViewById(R.id.modDate)
        val card: MaterialCardView = view.findViewById(R.id.recyclerCard)
        val delete: Button = view.findViewById(R.id.deleteButton)

        init {
            val dialog = DialogBuilder(view.context, {doc ->
                callbackUpd(dataSet[adapterPosition],doc)
            },{a ->
                if (a ==true){
                    callbackDel(dataSet[adapterPosition])
                }
            })

            // Define click listener for the ViewHolder's View
            card.setOnClickListener {
                dialog.docDialog("Editar documento",dataSet[adapterPosition].title.toString(),dataSet[adapterPosition].content.toString().toString(),"","")
            }

            delete.setOnClickListener {
                dialog.genericDialog("Apagar?", view.context,"Apagar")
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_layout, viewGroup, false)

        return ViewHolder(view, dataSet, callbackDel = {doc1 ->
            callDel(doc1)
        }, callbackUpd = {doc1, doc2 ->
            callUpd(doc1,doc2)
        })
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.title.text = "Título: " + dataSet[position].title
        viewHolder.content.text = "Conteúdo: " + dataSet[position].content
        viewHolder.date.text = "Última modificação: " + dataSet[position].date.toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}