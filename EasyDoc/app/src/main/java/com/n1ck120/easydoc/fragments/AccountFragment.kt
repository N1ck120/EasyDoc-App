package com.n1ck120.easydoc.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.n1ck120.easydoc.utils.DialogBuilder
import com.n1ck120.easydoc.activities.LoginActivity
import com.n1ck120.easydoc.R

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        val logout = view.findViewById<Button>(R.id.btnLogout)
        val login = view.findViewById<Button>(R.id.btnLoginRedirect)

        val dialog = DialogBuilder(view.context, {}, { a ->
            if (a) {
                //TODO() Função apagar token
                activity?.finish()
            }
        },{})

        val dialog1 = DialogBuilder(view.context, {}, { a ->
            if (a) {
                activity?.finish()
            }
        },{})

        logout.setOnClickListener {
            if (false){
                dialog.genericDialog("Sair da sua conta?", "Você terá que fazer login novamente para usar a sincronização de documentos",requireActivity(), "Sair")
            }else{
                dialog1.genericDialog("Sair do app?", null, requireActivity(), "Sair")
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