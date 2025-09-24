package com.n1ck120.easydoc.core.document

import kotlinx.serialization.Serializable

@Serializable
data class DocumentModels(
    val documents: List<DocModel>
)

@Serializable
data class DocModel(
    //val type: String,
    val title: String,
    val description: String,
    val content: String,

    val clientName: String? = null,        // {0}
    val clientTaxId: String? = null,       // {1}
    val clientAddress: String? = null,     // {2}

    val providerName: String? = null,      // {3}
    val providerTaxId: String? = null,     // {4}
    val providerAddress: String? = null,   // {5}

    val serviceDescription: String? = null,   // {6}
    val serviceStartDate: String? = null,     // {7}
    val serviceEndDate: String? = null,       // {8}
    val serviceAmount: String? = null,        // {9}
    val servicePaymentTerms: String? = null,  // {10}

    val obligations: String? = null,       // {11}
    val termination: String? = null,       // {12}
    val jurisdiction: String? = null,      // {13}
    val signaturePlaceDate: String? = null,   // {14}

    val receiptDate: String? = null,       // {15}
    val proposalValidity: String? = null,  // {16}

    val serviceOrderNumber: String? = null,   // {17}
    val serviceOrderNotes: String? = null,    // {18}

    val ndaConfidentialInfo: String? = null,  // {19}
    val ndaDurationYears: String? = null,     // {20}

    val terminationReason: String? = null    // {21}

)
