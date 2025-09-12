package com.n1ck120.easydoc.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.n1ck120.easydoc.R
import com.n1ck120.easydoc.activities.LoginActivity
import com.n1ck120.easydoc.utils.DialogBuilder

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        //Declaração de variaveis globais
        val logout = view.findViewById<MaterialButton>(R.id.btnLogout)
        val login = view.findViewById<MaterialButton>(R.id.btnLoginRedirect)

        val dialog = DialogBuilder(view.context,  genericCallback = { a ->
            if (a) {
                //TODO() Função apagar token
                activity?.finish()
            }
        })

        val dialog1 = DialogBuilder(view.context, genericCallback = { a ->
            if (a) {
                activity?.finish()
            }
        })

        logout.setOnClickListener {
            if (false){
                dialog.genericDialog(getString(R.string.logout_account),
                    getString(R.string.logout_warning),requireActivity(), getString(R.string.exit))
            }else{
                dialog1.genericDialog(
                    getString(R.string.exit_app), null, requireActivity(),
                    getString(R.string.exit))
            }
        }

        login.setOnClickListener{
            val intent = Intent(this.context, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        return view
    }
}