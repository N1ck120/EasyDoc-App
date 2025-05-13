package com.n1ck120.easydoc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val nothingText = view.findViewById<TextView>(R.id.nothing_to_show)

        //val dataset = emptyArray<String>()

        var dataset = arrayOf("SFVWED","DSFGASEDR","SFVWED","DSFGASEDR","DSFGASEDR","SFVWED","DSFGASEDR","DSFGASEDR","SFVWED","DSFGASEDR")

        val customAdapter = CustomAdapter(dataset)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = customAdapter

        if (recyclerView.adapter?.itemCount.toString().toInt() > 0) {
            nothingText.visibility = GONE
        }else{
            nothingText.visibility = VISIBLE
        }
        return view
    }
}