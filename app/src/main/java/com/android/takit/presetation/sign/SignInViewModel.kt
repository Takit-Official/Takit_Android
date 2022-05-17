package com.android.takit.presetation.sign

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.takit.data.retmote.RetrofitBuilder
import com.android.takit.data.retmote.model.SignInRequest
import com.kakao.sdk.auth.model.OAuthToken
import kotlinx.coroutines.launch

class SignInViewModel: ViewModel() {

    private var _kakaoToken = MutableLiveData<String>()
    var kakaoToken: LiveData<String> = _kakaoToken

    private var _accessToken = MutableLiveData<String>()
    var accessToken: LiveData<String> = _accessToken

    // 카카오계정으로 로그인 공통 callback 구성
// 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("TAG", "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i("TAG", "카카오계정으로 로그인 성공 ${token.accessToken}")
            kakaoSignIn(token.accessToken)
        }
    }

    fun kakaoSignIn(token: String){
        _kakaoToken.value = token
        viewModelScope.launch {
            kotlin.runCatching {
                RetrofitBuilder.takitService.signIn(
                    SignInRequest(requireNotNull(_kakaoToken.value))
                )
            }.onSuccess {
                _accessToken.postValue(it.data.token)
            }
        }
    }
}