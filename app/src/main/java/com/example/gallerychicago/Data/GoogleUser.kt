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
 * Use this function to extract [GoogleUser] information from a token id that you're
 * getting from One-Tap Sign in. The token id is usually valid for only one hour after
 * a successful authentication.
 * @return This function returns a [GoogleUser] object, or null if an exception occurred,
 * due to invalid token decoding, or because your token id has expired.
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
        Log.e("OneTapCompose", e.toString())
        return null
    } catch (e: DecodeException) {
        Log.e("OneTapCompose", e.toString())
        return null
    }
}
