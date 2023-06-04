package com.noritz.whatsup

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

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

    private lateinit var OTP : String
    private lateinit var resendToken : PhoneAuthProvider.ForceResendingToken
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.verification)
    }
}