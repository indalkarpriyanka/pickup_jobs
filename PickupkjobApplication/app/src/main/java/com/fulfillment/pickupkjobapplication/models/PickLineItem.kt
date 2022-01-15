package com.fulfillment.pickupkjobapplication.models

data class PickLineItem(
    val article: Article,
    val id: String,
    val picked: Int,
    val quantity: Int,
    val scannableCodes: List<String>,
    val status: String
)