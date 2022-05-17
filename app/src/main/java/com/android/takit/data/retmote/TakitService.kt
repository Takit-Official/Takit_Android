package com.android.takit.data.retmote

import com.android.takit.data.retmote.model.SignInRequest
import com.android.takit.data.retmote.model.SignInResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TakitService {

    @POST("oauth/kakao")
    suspend fun signIn(
        @Body body: SignInRequest
    ): SignInResponse
}