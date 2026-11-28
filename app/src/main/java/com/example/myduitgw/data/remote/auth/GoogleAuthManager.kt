package com.example.myduitgw.data.remote.auth

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.myduitgw.R
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleAuthManager(
    private val activity: Activity
) {
    suspend fun signIn(): String {
        val credentialManager = CredentialManager.Companion.create(activity)

        val googleOption = GetSignInWithGoogleOption.Builder(
            serverClientId = activity.getString(
                R.string.google_web_client_id
            )
        ).build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleOption)
            .build()

        val result = credentialManager.getCredential(
            context = activity,
            request = request
        )

        val credential = result.credential

        if (
            credential is CustomCredential &&
            credential.type ==
            GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            return GoogleIdTokenCredential.Companion
                .createFrom(credential.data)
                .idToken
        }

        throw IllegalStateException(
            "Google ID token tidak ditemukan."
        )
    }
}