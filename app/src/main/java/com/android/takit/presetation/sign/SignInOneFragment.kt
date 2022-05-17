package com.android.takit.presetation.sign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android.takit.MainActivity
import com.android.takit.R
import com.android.takit.data.local.TakitAuthSharedPreferences
import com.android.takit.databinding.FragmentSignInOneBinding
import com.android.takit.presetation.base.BaseFragment
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient


class SignInOneFragment : BaseFragment<FragmentSignInOneBinding>(R.layout.fragment_sign_in_one) {

    private val signInViewModel: SignInViewModel by viewModels()

    override fun initView() {
        setListeners()
        accessTokenObserver()
        //setAutoKakaoLogin()
    }

    private fun setListeners(){
        binding.btnSignLogin.setOnClickListener {
            setKakaoLogin()
        }
    }
    private fun setAutoKakaoLogin() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        //로그인 필요
                        setKakaoLogin()
                    }
                }
                else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                    startMainActivity()
                }
            }
        }
        else {
            //로그인 필요
            setKakaoLogin()
        }
    }

    private fun setKakaoLogin() {
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = signInViewModel.callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    signInViewModel.kakaoSignIn(token.accessToken)

                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = signInViewModel.callback)
        }
    }
    private fun startMainActivity() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }

    private fun accessTokenObserver() {
        signInViewModel.accessToken.observe(viewLifecycleOwner) {
            TakitAuthSharedPreferences.setAccessToken(it)
            startMainActivity()
        }
    }

    companion object{
        const val TAG = "SignInOneFragment"
    }

}