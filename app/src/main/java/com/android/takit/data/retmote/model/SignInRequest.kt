package com.android.takit.data.retmote.model

import com.google.gson.annotations.SerializedName

data class SignInRequest(
    @SerializedName(value = "accessToken")
    val kakaoToken: String
)
