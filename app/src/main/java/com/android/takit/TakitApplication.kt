package com.android.takit

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class TakitApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.app_key))
    }
}