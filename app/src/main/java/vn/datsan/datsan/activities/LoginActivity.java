package vn.datsan.datsan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.datsan.datsan.R;
import vn.datsan.datsan.serverdata.UserManager;
import vn.datsan.datsan.setting.UserDefine;
import vn.datsan.datsan.ui.customwidgets.SimpleProgress;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    // UI references.

    @BindView(R.id.phoneNumber)
    EditText phoneEdt;
    @BindView(R.id.password)
    EditText passwordEdt;
    @BindView(R.id.email_sign_in_button)
    Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            firebaseAuth.removeAuthStateListener(authListener);
        }
    }

    @OnClick(R.id.email_sign_in_button)
    public void signIn() {
        attemptLogin();


//        User user = new User();
//        user.setEmail("abc@gmail.com");
//        user.setName("Mr.A");
//        user.setPhone("01662583067");
//        UserManager.getInstance().addUser(user);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        phoneEdt.setError(null);
        passwordEdt.setError(null);

        // Store values at the time of the login attempt.
        String phone = phoneEdt.getText().toString();
        String password = passwordEdt.getText().toString();

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordEdt.setError(getString(R.string.error_invalid_password));
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(phone)) {
            phoneEdt.setError(getString(R.string.error_field_required));
        } else if (!isPhoneNumberValid(phone)) {
            phoneEdt.setError(getString(R.string.error_invalid_email));
        }

        login(phone, password);
    }

    private void login(final String phone, String password) {
        SimpleProgress.show(LoginActivity.this);

        String email = phone + UserDefine._Default_Email_Sufix;

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        SimpleProgress.dismiss();

                        Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "signInWithEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login Successful !",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            intent.putExtra("id", phone);
                            startActivity(intent);
                        }
                    }
                });
    }

    private boolean isPhoneNumberValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.startsWith("0");// && phone.length() > 8 && phone.length() < 12;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public void onForgotBtnClicked(View view) {

       // UserManager.getInstance().getUser("01662583067");
    }

    public void onNewAccountBtnClicked(View view) {
        Intent intent = new Intent(this, NewAccountActivity.class);
        startActivity(intent);
    }

    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.e("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.e("TAG", "onAuthStateChanged:signed_out");
            }
            // ...
        }
    };
}

