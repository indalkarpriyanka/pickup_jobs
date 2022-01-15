package com.fulfillment.pickupkjobapplication.models

data class Article(
    val attributes: List<Any>,
    val imageUrl: String,
    val tenantArticleId: String,
    val title: String
)