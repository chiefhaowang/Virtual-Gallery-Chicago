package com.example.gallerychicago.Screen

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.credentials.GetCredentialException
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.gallerychicago.Data.ArtworkViewModel
import com.example.gallerychicago.Data.UserViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
//import com.stevdzasan.onetap.OneTapSignInState

import com.example.gallerychicago.Data.GoogleUser
import com.example.gallerychicago.Data.User
import com.example.gallerychicago.Data.UserRepository
import com.example.gallerychicago.Data.getUserFromTokenId
import com.example.gallerychicago.googleLogin.OneTapSignInState
//import com.stevdzasan.onetap.OneTapSignInStateSaver
//import com.stevdzasan.onetap.openGoogleAccountSettings

// from oneTop project
//import com.stevdzasan.onetap.OneTapSignInWithGoogle
//import com.stevdzasan.onetap.getUserFromTokenId
//import com.stevdzasan.onetap.handleCredentialsNotAvailable
//import com.stevdzasan.onetap.handleSignIn
//import com.stevdzasan.onetap.rememberOneTapSignInState



@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun LoginScreen(
    navController: NavHostController,
    userViewModel: UserViewModel = viewModel(),
    )
{
    // define the variables
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    fun validateLoginForm(): Boolean {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_LONG).show()
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Invalid email format", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8D8C4))
    ){
        Box(  //for red background
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF952323))
        ) {
            Text(
                text = "Login",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text("Welcome to the Art Gallery",
                color = Color(0xFF952323),
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 25.sp
                ),)
            Spacer(modifier = Modifier.height(30.dp))

            // field for email
            TextField(
                value = email,
                onValueChange = {
                    email = it
                   },
                label = { Text("Email Address") }
            )
            Spacer(modifier = Modifier.height(20.dp))
            // field for password
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle password visibility")
                    }
                }
            )
            Spacer(modifier = Modifier.height(30.dp))

            // button to login in, if login in successfully, it will go to homepage
            Button(
                onClick = {
                    if (validateLoginForm()) {
                        userViewModel.loginUser(email, password) { user ->
                            if (user != null) {
                                navController.navigate("Home")  // Navigate on success
                            } else {
                                Toast.makeText(context, "Incorrect email or password", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFF952323)),
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(15.dp))

            // Button to RegistrationScreen
            Button(
                onClick = {
                    println("Button clicked to RegistrationScreen!")
                    navController.navigate("Registration") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFF952323)),
            ) {
                Text("Create Account")
            }
            // Button to choose to login in with google account
            Spacer(modifier = Modifier.height(15.dp))


            val state = rememberOneTapSignInState()
            var user: GoogleUser? by remember { mutableStateOf(null) }
            OneTapSignInWithGoogle(
                state = state,
                rememberAccount = false,
                clientId = "508519310831-5jou70a9oo3sgt4adi1e965e2u6692ph.apps.googleusercontent.com",  // Google Cloud Platform Client ID

                onTokenIdReceived = { tokenId ->
                    user = getUserFromTokenId(tokenId)
                    println(user)
                    val userEmail: String = user?.email.toString()
                    // deal with token id
                    println("LoginScreen, Token ID received: $tokenId")
                    // if log in successful, navigate to home
                    userViewModel.addUser(email = userEmail, name = "gmail", birthday = null, password = "123")


                    
                    //userViewModel.setLoggedActive()
                    navController.navigate("Home")
                },
                onDialogDismissed = { message ->
                    Log.d("LoginScreen", "One-Tap dialog dismissed: $message")
                }
            )
            // Button to trigger google login in
            Button(
                onClick = { state.open() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFF952323)),
            ) {
                Text("Login in with Google")
            }
            /**Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFF952323)),
            ) {
                Text("Login in with Google")
            }*/

        }
    }
}

@Composable
fun rememberOneTapSignInState(): OneTapSignInState {
    return rememberSaveable(
        saver = OneTapSignInStateSaver
    ) { OneTapSignInState() }
}

private val OneTapSignInStateSaver: Saver<OneTapSignInState, Boolean> = Saver(
    save = { state -> state.opened },
    restore = { opened -> OneTapSignInState(open = opened) },
)

//
@Composable
fun OneTapSignInWithGoogle(
    state: OneTapSignInState,
    clientId: String,
    rememberAccount: Boolean = true,
    nonce: String? = null,
    onTokenIdReceived: (String) -> Unit,
    onDialogDismissed: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val credentialManager = remember { CredentialManager.create(context) }

    val googleIdOption = remember {
        GetGoogleIdOption.Builder()
            .setServerClientId(clientId)
            .setNonce(nonce)
            .setFilterByAuthorizedAccounts(rememberAccount)
            .build()
    }

    val request = remember {
        GetCredentialRequest.Builder()
            .setCredentialOptions(listOf(googleIdOption))
            .build()
    }

    LaunchedEffect(key1 = state.opened) {
        if (state.opened) {
            scope.launch {
                try {
                    val response = credentialManager.getCredential(
                        request = request,
                        context = context,
                    )
                    handleSignIn(
                        credentialResponse = response,
                        onTokenIdReceived = {
                            onTokenIdReceived(it)
                            state.close()
                        },
                        onDialogDismissed = {
                            onDialogDismissed(it)
                            state.close()
                        }
                    )
                } catch (e: androidx.credentials.exceptions.GetCredentialException) {
                    if (e.message != null) {
                        if (e.message!!.contains("No credentials available")) {
                            handleCredentialsNotAvailable(
                                context = context,
                                state = state,
                                credentialManager = credentialManager,
                                clientId = clientId,
                                nonce = nonce,
                                onTokenIdReceived = onTokenIdReceived,
                                onDialogDismissed = onDialogDismissed
                            )
                        }
                    } else {
                        try {
                            val errorMessage = if (e.message != null) {
                                if (e.message!!.contains("activity is cancelled by the user.")) {
                                    "Dialog Closed."
                                } else if (e.message!!.contains("Caller has been temporarily blocked")) {
                                    "Sign in has been Temporarily Blocked due to too many Closed Prompts."
                                } else {
                                    e.message.toString()
                                }
                            } else "Unknown Error."
                            onDialogDismissed(errorMessage)
                            state.close()
                        } catch (e: Exception) {
                            onDialogDismissed("${e.message}")
                            state.close()
                        }
                    }
                } catch (e: Exception) {
                    if (e.message != null) {
                        if (e.message!!.contains("No credentials available")) {
                            handleCredentialsNotAvailable(
                                context = context,
                                state = state,
                                credentialManager = credentialManager,
                                clientId = clientId,
                                nonce = nonce,
                                onTokenIdReceived = onTokenIdReceived,
                                onDialogDismissed = onDialogDismissed
                            )
                        }
                    } else {
                        onDialogDismissed("${e.message}")
                        state.close()
                    }
                }
            }
        }
    }
}

private suspend fun handleCredentialsNotAvailable(
    context: Context,
    state: OneTapSignInState,
    credentialManager: CredentialManager,
    clientId: String,
    nonce: String?,
    onTokenIdReceived: (String) -> Unit,
    onDialogDismissed: (String) -> Unit
) {
    val googleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId(clientId)
        .setNonce(nonce)
        .setFilterByAuthorizedAccounts(false)
        .build()

    val request = GetCredentialRequest.Builder()
        .setCredentialOptions(listOf(googleIdOption))
        .build()

    try {
        val response = credentialManager.getCredential(
            request = request,
            context = context,
        )
        handleSignIn(
            credentialResponse = response,
            onTokenIdReceived = {
                onTokenIdReceived(it)
                state.close()
            },
            onDialogDismissed = {
                onDialogDismissed(it)
                state.close()
            }
        )
    } catch (e: androidx.credentials.exceptions.GetCredentialException) {
        try {
            if (e.message!!.contains("No credentials available")) {
                openGoogleAccountSettings(context = context)
            }
            val errorMessage = if (e.message != null) {
                if (e.message!!.contains("activity is cancelled by the user.")) {
                    "Dialog Closed."
                } else if (e.message!!.contains("Caller has been temporarily blocked")) {
                    "Sign in has been Temporarily Blocked due to too many Closed Prompts."
                } else {
                    e.message.toString()
                }
            } else "Unknown Error."

            onDialogDismissed(errorMessage)
            state.close()
        } catch (e: Exception) {

            onDialogDismissed("${e.message}")
            state.close()
        }
    } catch (e: Exception) {

        onDialogDismissed("${e.message}")
        state.close()
    }
}



fun openGoogleAccountSettings(context: Context) {
    try {
        val addAccountIntent = Intent(Settings.ACTION_ADD_ACCOUNT).apply {
            putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
        }
        context.startActivity(addAccountIntent)
    } catch (e: Exception) {
    }
}
private fun handleSignIn(
    credentialResponse: GetCredentialResponse,
    onTokenIdReceived: (String) -> Unit,
    onDialogDismissed: (String) -> Unit,
) {
    when (val credential = credentialResponse.credential) {
        is CustomCredential -> {
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                try {
                    val googleIdTokenCredential = GoogleIdTokenCredential
                        .createFrom(credential.data)
                    onTokenIdReceived(googleIdTokenCredential.idToken)
                } catch (e: GoogleIdTokenParsingException) {
                    onDialogDismissed("Invalid Google tokenId response: ${e.message}")
                }
            } else {
                onDialogDismissed("Unexpected Type of Credential.")
            }
        }

        else -> {
            onDialogDismissed("Unexpected Type of Credential.")
        }
    }
}

























@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun GoogleSignInButton(navController: NavHostController)
{
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val onClick: () -> Unit =
        {

            Toast.makeText(context, "Button clicked!", Toast.LENGTH_SHORT).show()
            val credentialManager = CredentialManager.create(context)

            val rawNonce = UUID.randomUUID().toString()
            val bytes = rawNonce.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            val hashedNonce = digest.fold("") {str, it -> str + "%02x".format(it)}


            // create GetGoogleIdOption object
            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("513800573110-dbileqgqt86u0rkdvptsfe6ravq1eapf.apps.googleusercontent.com")
                .setNonce(hashedNonce)
                .build()

            println("instantiate a Google sign in request: $googleIdOption")

            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            println("Credential has got: $request")


            coroutineScope.launch {
                try {
                    val result = credentialManager.getCredential(
                        request = request,
                        context = context,
                    )


                    val credential = result.credential

                    println("The result is: $result")

                    val googleIdTokenCredential = GoogleIdTokenCredential
                        .createFrom(credential.data)

                    println("The google TokenCredential: $googleIdTokenCredential")
                    val googleIdToken = googleIdTokenCredential.idToken


                    // Log and show success message
                    println("GoogleSignIn: $googleIdToken")
                    Toast.makeText(context, "You are signed in with Google", Toast.LENGTH_SHORT).show()

                    // Navigate to Home screen on success
                    navController.navigate("Home")
                }catch (e: GetCredentialException) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    println("Credential not found")
                }catch(e: GoogleIdTokenParsingException){
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    println("GoogleIdToken not found")
                }

            }

        }
    Button(
        onClick = {onClick()},
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(Color(0xFF952323)))
    {
        Text("Login in with Google")
    }
}
