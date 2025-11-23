package com.example.email

data class Email(
    val sender: String,
    val subject: String,
    val preview: String,
    val time: String,
    val avatarColor: String,
    var isStarred: Boolean = false,
    val isUnread: Boolean = true
)