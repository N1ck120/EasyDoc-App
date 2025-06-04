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

    // Campos opcionais, substituindo objetos por strings com nomes representativos
    val clientName: String? = null,
    val clientTaxId: String? = null,
    val clientAddress: String? = null,

    val providerName: String? = null,
    val providerTaxId: String? = null,
    val providerAddress: String? = null,

    val serviceDescription: String? = null,
    val serviceStartDate: String? = null,
    val serviceEndDate: String? = null,
    val serviceAmount: String? = null,
    val servicePaymentTerms: String? = null,
    val serviceExecutionDate: String? = null,
    val serviceDuration: String? = null,
    val servicePaymentMethod: String? = null,
    val serviceResponsiblePerson: String? = null,
    val serviceOpenDate: String? = null,
    val serviceExpectedCompletionDate: String? = null,

    val obligations: String? = null,
    val termination: String? = null,
    val jurisdiction: String? = null,
    val signature_date: String? = null,

    val payerName: String? = null,
    val payerTaxId: String? = null,

    val location: String? = null,
    val receipt_date: String? = null,
    val proposal_valid_until: String? = null,
    val order_number: String? = null,
    val party1: String? = null,
    val party2: String? = null,
    val confidentiality_terms: String? = null,
    val valid_until: String? = null,
    val original_contract_date: String? = null,
    val termination_reason: String? = null,
    val pending_settlements: String? = null
)
