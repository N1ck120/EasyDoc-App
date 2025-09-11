package com.n1ck120.easydoc.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.n1ck120.easydoc.R
import com.n1ck120.easydoc.activities.MainActivity
import com.n1ck120.easydoc.adapters.HomeAdapter
import com.n1ck120.easydoc.database.datastore.SettingsDataStore
import com.n1ck120.easydoc.database.room.AppDatabase
import com.n1ck120.easydoc.database.room.Doc
import com.n1ck120.easydoc.utils.DialogBuilder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MyDocumentsFragment : Fragment() {
    lateinit var db : AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_documents, container, false)
        //Declaração de variaveis globais
        val nothing = view.findViewById<ScrollView>(R.id.nothing)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        val createDoc = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)

        try {
            db = (requireActivity() as MainActivity).db
        }catch (e : Exception){
            Toast.makeText(requireActivity(), getString(R.string.error) + "$e", Toast.LENGTH_SHORT).show()
        }

        val dataStore = SettingsDataStore.getDataStorePrefs(requireContext())
        val saveExported = intPreferencesKey("saveExported")


        val dialog = DialogBuilder(requireContext(), { doc ->
            val a = lifecycleScope.launch {
                db.userDao().insertAll(doc)
            }
            a.invokeOnCompletion {
                Toast.makeText(requireContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show()
            }
        }, {},{ doc ->
            lifecycleScope.launch {
                if ((dataStore.data.first()[saveExported] ?: 1) == 1){
                    val a = lifecycleScope.launch {
                        db.userDao().insertAll(doc)
                    }
                    a.invokeOnCompletion {
                        Toast.makeText(requireContext(),
                            getString(R.string.saved), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        createDoc.setOnClickListener {
            dialog.docDialog(getString(R.string.create_a_new_document))
        }

        lifecycleScope.launch {
            db.userDao().getAll().collect { docs ->
                val dataset = mutableListOf<Doc>()
                val iterator = docs.listIterator()
                while (iterator.hasNext()){
                    dataset.add(iterator.next())
                }
                val homeAdapter = HomeAdapter(dataset, callDel = { docDel ->
                    val a = lifecycleScope.launch {
                        db.userDao().delete(docDel)
                    }
                    a.invokeOnCompletion {
                        Snackbar.make(view, "${docDel.title} "+ getString(R.string.deleted), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.undo)) {
                                lifecycleScope.launch {
                                    db.userDao().insertAll(docDel)
                                }
                            }
                            .show()
                    }

                }, callUpd = { docUpd1, docUpd2 ->
                    val a = lifecycleScope.launch {
                        var data: String
                        if (LocalDateTime.now().dayOfMonth < 10) {
                            data = "0" + LocalDateTime.now().dayOfMonth.toString()
                        } else {
                            data = LocalDateTime.now().dayOfMonth.toString()
                        }
                        if (LocalDateTime.now().monthValue < 10) {
                            data =
                                data + "/0" + LocalDateTime.now().monthValue.toString() + "/" + LocalDateTime.now().year.toString()
                        } else {
                            data =
                                data + "/" + LocalDateTime.now().monthValue.toString() + "/" + LocalDateTime.now().year.toString()
                        }
                        if (LocalDateTime.now().hour < 10) {
                            data = data + " às 0" + LocalDateTime.now().hour.toString()
                        } else {
                            data = data + " às " + LocalDateTime.now().hour.toString()
                        }
                        if (LocalDateTime.now().minute < 10) {
                            data = data + ":0" + LocalDateTime.now().minute.toString()
                        } else {
                            data = data + ":" + LocalDateTime.now().minute.toString()
                        }
                        db.userDao().update(
                            docUpd1.uid,
                            docUpd2.title.toString(),
                            docUpd2.content.toString(),
                            docUpd2.doc_name.toString(),
                            data
                        )
                    }
                    a.invokeOnCompletion {
                        Toast.makeText(view.context, getString(R.string.updated), Toast.LENGTH_SHORT).show()
                    }
                })
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = homeAdapter
                if (recyclerView.adapter?.itemCount.toString().toInt() > 0) {
                    nothing.visibility = GONE
                }else{
                    nothing.visibility = VISIBLE
                }
            }
        }
        return view
    }
}