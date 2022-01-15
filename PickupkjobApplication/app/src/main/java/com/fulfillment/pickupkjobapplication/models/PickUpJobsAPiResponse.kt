package com.fulfillment.pickupkjobapplication.models

data class PickUpJobsAPiResponse(
    val id: String,
    val pickLineItems: List<PickLineItem>,
    val status: String,
    val version: Int
)