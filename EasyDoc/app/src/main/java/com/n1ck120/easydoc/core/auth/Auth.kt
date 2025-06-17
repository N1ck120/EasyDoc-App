package com.n1ck120.easydoc.core.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.Base64

class Auth {
    fun verifyToken(key: String, token: String): Jws<Claims?>? {
        val publicKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(key))
            val content = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
        return content
    }
}