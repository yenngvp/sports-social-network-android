package vn.datsan.datsan.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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
import vn.datsan.datsan.models.User;
import vn.datsan.datsan.serverdata.UserManager;
import vn.datsan.datsan.setting.UserDefine;
import vn.datsan.datsan.ui.customwidgets.Alert.SimpleAlert;
import vn.datsan.datsan.ui.customwidgets.SimpleProgress;
import vn.datsan.datsan.utils.AppLog;

/**
 * A login screen that offers login via email/password.
 */
public class NewAccountActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    // UI references.
    @BindView(R.id.fullNameEdt)
    EditText nameEdt;
    @BindView(R.id.addressEdt)
    EditText addressEdt;
    @BindView(R.id.phoneNumber)
    EditText phoneEdt;
    @BindView(R.id.emailEdt)
    EditText emailEdt;
    @BindView(R.id.password)
    EditText pwdEdt;
    @BindView(R.id.rePassword)
    EditText rePwdEdt;
    @BindView(R.id.email_sign_up_button)
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
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

    @OnClick(R.id.email_sign_up_button)
    public void signUp() {
        attemptCreateAccount();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptCreateAccount() {

        // Reset errors.
        emailEdt.setError(null);
        pwdEdt.setError(null);
        rePwdEdt.setError(null);
        nameEdt.setError(null);
        phoneEdt.setError(null);

        // Store values at the time of the login attempt.
        String name = nameEdt.getText().toString();
        String email = emailEdt.getText().toString();
        String password = pwdEdt.getText().toString();
        String repassword = pwdEdt.getText().toString();
        String phoneNumber = phoneEdt.getText().toString();

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            pwdEdt.setError(getString(R.string.error_invalid_password));
        }
        if (repassword == null || !repassword.equals(password)) {
            rePwdEdt.setError("Password not match !!!");
        }

        if (phoneNumber == null || phoneNumber.isEmpty()) {
            phoneEdt.setError("couldn't empty");
        }

        // Check for a valid email address.
        if (email != null && !isEmailValid(email)) {
            emailEdt.setError(getString(R.string.error_invalid_email));
        }

        User user = new User(name, email, phoneNumber, addressEdt.getText().toString(), null, null);
        createAccount(user, password);
    }

    private void createAccount(final User user, String password) {
        SimpleProgress.show(NewAccountActivity.this);
        String email = user.getEmail();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        SimpleProgress.dismiss();
                        if (!task.isSuccessful()) {
                            SimpleAlert.showAlert(NewAccountActivity.this, getString(R.string.error), task.getException().getMessage() + "",
                                    getString(R.string.close));
                        } else {
                            Toast.makeText(NewAccountActivity.this, "Register successfully !",
                                    Toast.LENGTH_SHORT).show();

                            user.setId(task.getResult().getUser().getUid());
                            UserManager.getInstance().addUser(user);
                        }
                    }
                });
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
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

