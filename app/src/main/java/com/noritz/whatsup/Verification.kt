package com.noritz.whatsup

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import com.google.firebase.FirebaseTooManyRequestsException

class Verification : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var buttonVerify : Button
    private lateinit var textViewResend : TextView
    private lateinit var editTextNumber1 : EditText
    private lateinit var editTextNumber2 : EditText
    private lateinit var editTextNumber3 : EditText
    private lateinit var editTextNumber4 : EditText
    private lateinit var editTextNumber5 : EditText
    private lateinit var editTextNumber6 : EditText
    private lateinit var progressBar : ProgressBar

    private lateinit var OTP : String
    private lateinit var resendToken : PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneNumber : String


    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.verification)

            OTP = intent.getStringExtra("OTP").toString()
            resendToken = intent.getParcelableExtra("resendToken")!!
            phoneNumber = intent.getStringExtra("phoneNumber")!!

            initialisation()
            progressBar.visibility = View.INVISIBLE
            addTextChangedListener()
            resendOTPVisibility()


            textViewResend.setOnClickListener{
                resendOTP()
                resendOTPVisibility()
            }

            buttonVerify.setOnClickListener{
                //collecte otp depuis les edit textView
                val OTPsaisie = (editTextNumber1.text.toString() + editTextNumber2.text.toString() + editTextNumber3.text.toString() +
                                editTextNumber4.text.toString() + editTextNumber5.text.toString() + editTextNumber6.text.toString())

                if(OTPsaisie.isNotEmpty()) {
                    if(OTPsaisie.length == 6) {

                        val credits : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                            OTP, OTPsaisie
                        )
                        progressBar.visibility = View.VISIBLE
                        signInWithPhoneAuthCredential(credits)
                    }else {
                        Toast.makeText(this,"Please enter the correct OTP", Toast.LENGTH_SHORT).show()
                    }
                }else {
                    Toast.makeText(this,"Please type the given OTP", Toast.LENGTH_SHORT).show()
                 }
            }
    }

    private fun resendOTP() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(resendToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun resendOTPVisibility() {
        editTextNumber1.setText("")
        editTextNumber2.setText("")
        editTextNumber3.setText("")
        editTextNumber4.setText("")
        editTextNumber5.setText("")
        editTextNumber6.setText("")
        textViewResend.visibility = View.INVISIBLE
        textViewResend.isEnabled = false

        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            textViewResend.visibility = View.VISIBLE
            textViewResend.isEnabled = true
        }, 60000)
    }

     private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, "Authenticate Successfully", Toast.LENGTH_SHORT).show()
                        envoyerMainActivity()
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.d("TAG", "signInWithPhoneAuthCredential :${task.exception.toString()}")
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                        // Update UI
                    }
                }
        }

     private fun envoyerMainActivity() {
         startActivity(Intent(this,Create_profile::class.java))
         finish()
     }

    private fun addTextChangedListener() {
        editTextNumber1.addTextChangedListener(EditTextWatcher(editTextNumber1))
        editTextNumber2.addTextChangedListener(EditTextWatcher(editTextNumber2))
        editTextNumber3.addTextChangedListener(EditTextWatcher(editTextNumber3))
        editTextNumber4.addTextChangedListener(EditTextWatcher(editTextNumber4))
        editTextNumber5.addTextChangedListener(EditTextWatcher(editTextNumber5))
        editTextNumber6.addTextChangedListener(EditTextWatcher(editTextNumber6))
    }

    private fun initialisation() {

        auth = FirebaseAuth.getInstance()
        buttonVerify = findViewById(R.id.buttonVerify)
        textViewResend = findViewById(R.id.textViewResend)
        editTextNumber1 = findViewById(R.id.editTextNumber1)
        editTextNumber2 = findViewById(R.id.editTextNumber2)
        editTextNumber3 = findViewById(R.id.editTextNumber3)
        editTextNumber4 = findViewById(R.id.editTextNumber4)
        editTextNumber5 = findViewById(R.id.editTextNumber5)
        editTextNumber6 = findViewById(R.id.editTextNumber6)
        progressBar = findViewById(R.id.progressBar)

    }

    inner class EditTextWatcher(private val view : View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //TODO("Not yet implemented")
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            //TODO("Not yet implemented")
        }

        override fun afterTextChanged(s: Editable?) {
            val txt = s.toString()
            when(view.id) {
                R.id.editTextNumber1 -> if(txt.length == 1) editTextNumber2.requestFocus()
                R.id.editTextNumber2 -> if(txt.length == 1) editTextNumber3.requestFocus() else if(txt.isEmpty()) editTextNumber1.requestFocus()
                R.id.editTextNumber3 -> if(txt.length == 1) editTextNumber4.requestFocus() else if(txt.isEmpty()) editTextNumber2.requestFocus()
                R.id.editTextNumber4 -> if(txt.length == 1) editTextNumber5.requestFocus() else if(txt.isEmpty()) editTextNumber3.requestFocus()
                R.id.editTextNumber5 -> if(txt.length == 1) editTextNumber6.requestFocus() else if(txt.isEmpty()) editTextNumber4.requestFocus()
                R.id.editTextNumber6 -> if(txt.isEmpty()) editTextNumber5.requestFocus()
            }
        }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d("TAG","onVerificationFailed : ${e.toString()}")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("TAG","onVerificationFailed : ${e.toString()}")
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
                Log.d("TAG","onVerificationFailed : ${e.toString()}")
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            // Save verification ID and resending token so we can use them later

            OTP = verificationId
            resendToken = token
        }
        }
}