package com.crmapp.data.models

import java.util.UUID

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val senderId: String,
    val content: String,
    val chatId: String,
    val isGroup: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class Interaction(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val type: String,
    val content: String
)

data class Coupon(
    val id: String = UUID.randomUUID().toString(),
    val code: String,
    val description: String,
    val expiryDate: String
)

data class Client(
    val id: String,
    val name: String,
    val status: String,
    val segment: String,
    val lastContact: String,
    val score: Int,
    val tags: List<String>,
    val interactions: List<Interaction>
)

data class User(
    val id: String,
    val name: String,
    val isOperator: Boolean,
    val coupons: List<Coupon> = emptyList()
)