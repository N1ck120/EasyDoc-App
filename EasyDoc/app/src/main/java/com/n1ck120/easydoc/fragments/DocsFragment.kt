package com.n1ck120.easydoc.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.card.MaterialCardView
import com.n1ck120.easydoc.activities.ConverterActivity
import com.n1ck120.easydoc.activities.EditorActivity
import com.n1ck120.easydoc.activities.ModelsActivity
import com.n1ck120.easydoc.R

class DocsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_docs, container, false)
        val models = view.findViewById<MaterialCardView>(R.id.doc_models)
        val converter = view.findViewById<MaterialCardView>(R.id.doc_converter)
        val editor = view.findViewById<MaterialCardView>(R.id.doc_mdeditor)

        models.setOnClickListener {
            val intent = Intent(requireActivity(), ModelsActivity::class.java)
            startActivity(intent)
        }

        converter.setOnClickListener {
            val intent = Intent(requireActivity(), ConverterActivity::class.java)
            startActivity(intent)
        }

        editor.setOnClickListener {
            val intent = Intent(requireActivity(), EditorActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}