package com.android.takit.data.retmote.model

data class SignInResponse(
    val data: Data,
    val message: String,
    val status: Int
) {
    data class Data(
        val email: String,
        val nickname: String,
        val token: String
    )
}