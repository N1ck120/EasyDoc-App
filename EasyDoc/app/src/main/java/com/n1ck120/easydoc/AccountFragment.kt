package com.n1ck120.easydoc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        val logout = view.findViewById<ImageButton>(R.id.btnLogout)
        logout.setOnClickListener {
            Toast.makeText(this.context, "Fazendo logout", Toast.LENGTH_SHORT).show()
        }
        return view
    }
}