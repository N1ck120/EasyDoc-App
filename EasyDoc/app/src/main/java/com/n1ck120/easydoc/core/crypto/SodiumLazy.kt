package com.n1ck120.easydoc.core.crypto

import com.goterl.lazysodium.LazySodiumAndroid
import com.goterl.lazysodium.SodiumAndroid
import java.nio.charset.StandardCharsets

class SodiumLazy {
    val lazySodium = LazySodiumAndroid(SodiumAndroid("mysodium"), StandardCharsets.UTF_8)
}