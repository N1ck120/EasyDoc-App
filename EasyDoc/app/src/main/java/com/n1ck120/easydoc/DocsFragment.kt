package com.n1ck120.easydoc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DocsFragment : Fragment() {

    lateinit var db : AppDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_docs, container, false)


        try {
            db = (requireActivity() as MainActivity).db
        }catch (e : Exception){
            Toast.makeText(requireActivity(), "Erro: $e", Toast.LENGTH_SHORT).show()
        }

        //val tex1 = view.findViewById<TextView>(R.id.textView7)
        //val tex2 = view.findViewById<TextView>(R.id.textView8)


        val a = lifecycleScope.launch {
            db.userDao().getAll().collect { docs ->
                //tex2.text = docs.size.toString()
            }
        }
        //a.invokeOnCompletion {
        //    Toast.makeText(requireActivity(), "Recuperado", Toast.LENGTH_SHORT).show()
        //}


        return view
    }
}