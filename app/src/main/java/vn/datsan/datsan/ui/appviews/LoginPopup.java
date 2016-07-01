package vn.datsan.datsan.ui.appviews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import vn.datsan.datsan.R;
import vn.datsan.datsan.activities.HomeActivity;
import vn.datsan.datsan.activities.NewAccountActivity;
import vn.datsan.datsan.setting.UserDefine;
import vn.datsan.datsan.ui.customwidgets.SimpleProgress;

/**
 * Created by xuanpham on 6/27/16.
 */
public class LoginPopup {
    private Dialog popup;
    private Context context;
    private FirebaseAuth firebaseAuth;
    // UI references.

    private EditText phoneEdt;
    private EditText passwordEdt;
    private Button signInBtn;
    private Button loginByFB;
    private Button loginByGG;


    public LoginPopup(Context context) {
        popup = new Dialog(context);

        this.context = context;

        popup.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window popupWindow = popup.getWindow();
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setContentView(R.layout.activity_login);

        WindowManager.LayoutParams wlp = popupWindow.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.dimAmount = 0.30f;

        popupWindow.setAttributes(wlp);

        phoneEdt = (EditText) popup.findViewById(R.id.phoneNumber);
        passwordEdt = (EditText) popup.findViewById(R.id.password);
        signInBtn = (Button) popup.findViewById(R.id.email_sign_in_button);
        loginByFB = (Button) popup.findViewById(R.id.loginFbBtn);
        loginByGG = (Button) popup.findViewById(R.id.loginGgBtn);

        signInBtn.setOnClickListener(signInClicked);
        loginByFB.setOnClickListener(loginGGClicked);
        loginByGG.setOnClickListener(loginFBClicked);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private View.OnClickListener signInClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            attemptLogin();
        }
    };

    private View.OnClickListener loginGGClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener loginFBClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    public void show() {
        popup.show();
    }

    private void attemptLogin() {

        // Reset errors.
        phoneEdt.setError(null);
        passwordEdt.setError(null);

        // Store values at the time of the login attempt.
        String phone = phoneEdt.getText().toString();
        String password = passwordEdt.getText().toString();

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordEdt.setError(context.getString(R.string.error_invalid_password));
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(phone)) {
            phoneEdt.setError(context.getString(R.string.error_field_required));
        } else if (!isPhoneNumberValid(phone)) {
            phoneEdt.setError(context.getString(R.string.error_invalid_email));
        }

        login(phone, password);
    }

    private void login(final String phone, String password) {
        SimpleProgress.show(context);

        String email = phone + UserDefine._Default_Email_Sufix;

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        SimpleProgress.dismiss();

                        Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "signInWithEmail", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Login Successful !",
                                    Toast.LENGTH_SHORT).show();
                            popup.dismiss();

                            //Intent intent = new Intent(context, HomeActivity.class);
                           // intent.putExtra("id", phone);
                           // context.startActivity(intent);
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
        Intent intent = new Intent(context, NewAccountActivity.class);
        context.startActivity(intent);
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
