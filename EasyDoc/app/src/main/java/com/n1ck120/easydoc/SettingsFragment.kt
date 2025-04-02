package com.n1ck120.easydoc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.materialswitch.MaterialSwitch


class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val github = view.findViewById<ImageButton>(R.id.github)
        val teste = view.findViewById<MaterialSwitch>(R.id.teste)
        
        github.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/N1ck120/EasyDoc-App"))
            startActivity(browserIntent)
        }

        teste.setOnCheckedChangeListener { compoundButton, b ->
            if (teste.isChecked){
                Toast.makeText(this.context, "Ativado", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this.context, "Desativado", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}