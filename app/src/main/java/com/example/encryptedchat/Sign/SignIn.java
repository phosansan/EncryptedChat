package com.example.encryptedchat.Sign;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.encryptedchat.MainActivity;
import com.example.encryptedchat.R;
import com.example.encryptedchat.databinding.ActivitySignInBinding;
import com.example.encryptedchat.model.Tab;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class SignIn extends AppCompatActivity {

    private static final String TAG = "PhoneAuthActivity";

    ActivitySignInBinding binding;

    private FirebaseAuth mAuth;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private ProgressDialog progressDialog;

    private FirebaseFirestore mFireStore;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_in);


        mFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        progressDialog = new ProgressDialog(this);
        binding.btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.btnMasuk.getText().toString().equals("Masuk")) {
                    progressDialog.setMessage("Mengirim Kode Verifikasi");
                    progressDialog.show();

                    String nponsel = binding.edPonsel.getText().toString();
                    startPhoneNumberVerification(nponsel);
                } else {
                    progressDialog.setMessage("Memverifikasi Kode");
                    progressDialog.show();

                    verifyPhoneNumberWithCode(mVerificationId, binding.edVerifikasi.getText().toString());
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "Verifikasi BERHASIL");
                signInWithPhoneAuthCredential(phoneAuthCredential);
                progressDialog.dismiss();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d(TAG, "Verifikasi GAGAL: " + e.getMessage());
                progressDialog.dismiss();
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

                // The SMS Verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                binding.btnMasuk.setText("Verifikasi");
                progressDialog.dismiss();
            }
        };
    }

    private void startPhoneNumberVerification (String phoneNumber) {
        //[START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)          // Phone number to verify
                .setPhoneNumber(phoneNumber)                // Timeout and unit
                .setTimeout(60L, TimeUnit.SECONDS)  // Activity (for callback binding)
                .setActivity(this)                          // OnVerificationStateChangedCallbacks
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]

        //mVerificationProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
    mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign In success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");

                        FirebaseUser user = task.getResult().getUser();

                        if(user != null){
                            String ID = user.getUid();
                            Tab akun = new Tab(ID,user.getPhoneNumber(),"","","");

                            mFireStore.collection("Akun").document(user.getUid()).collection(ID)
                                    .add(akun).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    startActivity(new Intent(SignIn.this, InfoAkun.class));
                                    finish();
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(),"Terdapat Kesalahan",Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            // [START_EXCLUDE_ silent]
                            Log.w(TAG,"Kode Salah");
                            // [END_EXCLUDE]
                        }

                    }
                }
            });
    }
}