package com.n1ck120.easydoc

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        val logout = view.findViewById<ImageButton>(R.id.btnLogout)
        val login = view.findViewById<Button>(R.id.btnLoginRedirect)

        logout.setOnClickListener {
            val dialogView = LayoutInflater.from(this.context).inflate(R.layout.exit_dialog, null)
            val dialog = AlertDialog.Builder(this.context)
                .setView(dialogView)
                .create()
            val exit = dialogView.findViewById<Button>(R.id.exit)
            val cancel = dialogView.findViewById<Button>(R.id.cancel)
            exit.setOnClickListener{
                activity?.finish()
            }
            cancel.setOnClickListener{
                dialog.dismiss()
            }
                dialog.show()
        }

        login.setOnClickListener{
            val intent = Intent(this.context,LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }
}