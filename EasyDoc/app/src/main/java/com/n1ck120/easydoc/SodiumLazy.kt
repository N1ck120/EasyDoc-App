package com.n1ck120.easydoc

import com.goterl.lazysodium.LazySodiumAndroid
import com.goterl.lazysodium.SodiumAndroid
import java.nio.charset.StandardCharsets.UTF_8

class SodiumLazy {
    val lazySodium = LazySodiumAndroid(SodiumAndroid("mysodium"), UTF_8)
}