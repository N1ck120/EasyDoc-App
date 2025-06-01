package com.n1ck120.easydoc

import kotlinx.serialization.Serializable

@Serializable
data class DocumentModels(
    val documents: List<DocModel>
)

@Serializable
data class DocModel(
    val type: String,
    val title: String,
    val description: String,

    // Campos opcionais, pois nem todos os documentos possuem os mesmos
    val client: Client? = null,
    val provider: Provider? = null,
    val service: Service? = null,
    val obligations: String? = null,
    val termination: String? = null,
    val jurisdiction: String? = null,
    val signature_date: String? = null,
    val payer: Payer? = null,
    val location: String? = null,
    val receipt_date: String? = null,
    val proposal_valid_until: String? = null,
    val order_number: String? = null,
    val party1: String? = null,
    val party2: String? = null,
    val confidentiality_terms: String? = null,
    val valid_until: String? = null,
    val original_contract_date: String? = null,
    val service_description: String? = null,
    val termination_reason: String? = null,
    val pending_settlements: String? = null
)

@Serializable
data class Client(
    val name: String = "",
    val tax_id: String = "",
    val address: String = ""
)

@Serializable
data class Provider(
    val name: String = "",
    val tax_id: String? = null,
    val address: String? = null
)

@Serializable
data class Service(
    val description: String = "",
    val start_date: String? = null,
    val end_date: String? = null,
    val amount: String = "",
    val payment_terms: String? = null,
    val execution_date: String? = null,
    val duration: String? = null,
    val payment_method: String? = null,
    val responsible_person: String? = null,
    val open_date: String? = null,
    val expected_completion_date: String? = null
)

@Serializable
data class Payer(
    val name: String = "",
    val tax_id: String = ""
)
