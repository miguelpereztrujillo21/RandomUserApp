package com.example.randomuserapp.models

data class UserResponse(
    val results: List<User>,
    val info: Info
)