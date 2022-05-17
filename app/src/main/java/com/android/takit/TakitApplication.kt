package com.android.takit

import android.app.Application
import com.android.takit.data.local.TakitAuthSharedPreferences
import com.kakao.sdk.common.KakaoSdk

class TakitApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        TakitAuthSharedPreferences.init(this)
        KakaoSdk.init(this, getString(R.string.app_key))
    }
}