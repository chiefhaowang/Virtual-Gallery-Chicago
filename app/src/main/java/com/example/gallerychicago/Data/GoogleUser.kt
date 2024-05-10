package com.example.gallerychicago.Data

import android.util.Log
import androidx.compose.runtime.Immutable
import com.auth0.android.jwt.DecodeException
import com.auth0.android.jwt.JWT

@Immutable
data class GoogleUser(
    val sub: String?,
    val email: String?,
    val emailVerified: Boolean?,
    val fullName: String?
)

/**
 * Extract important information from token, and return a Google user object
 * */
fun getUserFromTokenId(tokenId: String): GoogleUser? {
    try {
        val jwt = JWT(tokenId)
        return GoogleUser(
            sub = jwt.claims["sub"]?.asString(),
            email = jwt.claims["email"]?.asString(),
            emailVerified = jwt.claims["email_verified"]?.asBoolean(),
            fullName = jwt.claims["name"]?.asString(),
        )
    } catch (e: Exception) {
        Log.e("google token", e.toString())
        return null
    } catch (e: DecodeException) {
        Log.e("google token", e.toString())
        return null
    }
}
