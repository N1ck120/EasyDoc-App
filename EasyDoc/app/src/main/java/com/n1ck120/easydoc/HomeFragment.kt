package com.n1ck120.easydoc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    lateinit var db : AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val nothingText = view.findViewById<TextView>(R.id.nothing_to_show)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        try {
            db = (requireActivity() as MainActivity).db
        }catch (e : Exception){
            Toast.makeText(requireActivity(), "Erro: $e", Toast.LENGTH_SHORT).show()
        }

        lifecycleScope.launch {
            db.userDao().getAll().collect { docs ->

                val dataset = mutableListOf<Doc>()
                val iterator = docs.listIterator()
                while (iterator.hasNext()){
                    dataset.add(iterator.next())
                }
                val customAdapter = CustomAdapter(dataset)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = customAdapter
                if (recyclerView.adapter?.itemCount.toString().toInt() > 0) {
                    nothingText.visibility = GONE
                }else{
                    nothingText.visibility = VISIBLE
                }
            }
        }

        return view
    }

    fun deleteItem(docitem: Doc){
        try {
            db = (requireActivity() as MainActivity).db
        }catch (e : Exception){
            Toast.makeText(requireActivity(), "Erro: $e", Toast.LENGTH_SHORT).show()
        }

        lifecycleScope.launch{
            db.userDao().delete(docitem)
        }
    }
}