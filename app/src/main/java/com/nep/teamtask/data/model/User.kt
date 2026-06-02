package com.nep.teamtask.data.model

data class User(
    val id: String,
    val name: String,
    val role: String,
    val avatarUrl: String? = null
)