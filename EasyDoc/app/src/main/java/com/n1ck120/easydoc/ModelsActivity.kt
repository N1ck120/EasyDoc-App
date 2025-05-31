package com.n1ck120.easydoc

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout.VERTICAL
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class ModelsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_models)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val backBtn = findViewById<Button>(R.id.backButton)

        backBtn.setOnClickListener {
            finish()
        }


        val dataset1 = arrayOf("Contrato de Prestação de Serviços","Recibo de Pagamento de Serviço","Proposta Comercial de Serviço","Ordem de Serviço (OS)","Termo de Confidencialidade (NDA)","Termo de Rescisão de Contrato")
        val dataset2 = arrayOf("Instrumento jurídico que formaliza um acordo entre duas partes para a execução de um serviço. Define obrigações, prazos, valores e cláusulas de rescisão, garantindo segurança jurídica para ambas as partes.","Documento simples que comprova o pagamento realizado por um serviço prestado. Serve como prova de quitação, contendo dados do prestador, valor, descrição do serviço e data do pagamento.","Documento enviado antes da contratação que detalha o serviço ofertado, incluindo escopo, prazos, valores e formas de pagamento. Serve como base para negociação e posterior formalização contratual.","Documento operacional que autoriza a execução de um serviço específico, geralmente com número de controle. Utilizado para organizar e rastrear atividades técnicas ou operacionais com prazos definidos.","Acordo entre as partes que garante o sigilo de informações sensíveis ou estratégicas compartilhadas durante a relação profissional. Protege dados comerciais, técnicos ou pessoais de divulgação indevida.","Documento que oficializa o encerramento de um contrato previamente firmado. Estabelece as razões da rescisão, data de encerramento e acertos finais entre as partes, evitando disputas futuras.")
        val modelsAdapter = ModelsAdapter(dataset1, dataset2)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_models)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        recyclerView.adapter = modelsAdapter
    }
}