package vn.datsan.datsan.ui.appviews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import vn.datsan.datsan.R;
import vn.datsan.datsan.activities.HomeActivity;
import vn.datsan.datsan.activities.NewAccountActivity;
import vn.datsan.datsan.fragments.LoginFragment;
import vn.datsan.datsan.setting.UserDefine;
import vn.datsan.datsan.ui.customwidgets.SimpleProgress;

/**
 * Created by xuanpham on 6/27/16.
 */
public class LoginPopup {
    private Dialog popup;
    private AppCompatActivity context;
    private FirebaseAuth firebaseAuth;
    // UI references.

    private EditText emailEdt;
    private EditText passwordEdt;
    private Button signInBtn;
    private TextView forgotPwdBtn;
    private TextView newAccBtn;
    private LoginButton fbLoginBtn;
    private SignInButton ggLoginBtn;
    private String TAG = "LOGIN POPUP";

    private GoogleApiClient mGoogleApiClient;

    public LoginPopup(final AppCompatActivity context) {
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

        LoginFragment loginFragment = (LoginFragment) context.getSupportFragmentManager().findFragmentById(R.id.login_fragment);
        loginFragment.setCallBack(new LoginPopupCallBack() {
            @Override
            public void dismiss(int code) {
                hidePopUp();
            }
        });
//        emailEdt = (EditText) popup.findViewById(R.id.email);
//        passwordEdt = (EditText) popup.findViewById(R.id.password);
//        signInBtn = (Button) popup.findViewById(R.id.email_sign_in_button);
//        forgotPwdBtn = (TextView) popup.findViewById(R.id.forgotPwdBtn);
//        newAccBtn = (TextView) popup.findViewById(R.id.newAccountBtn);
//        fbLoginBtn = (LoginButton) popup.findViewById(R.id.fbLoginBtn);
//        ggLoginBtn = (SignInButton) popup.findViewById(R.id.ggLoginBtn);
//
//        signInBtn.setOnClickListener(signInClicked);
//        forgotPwdBtn.setOnClickListener(forgotPwdBtnClick);
//        newAccBtn.setOnClickListener(newAccPwdBtnClick);
//
//        fbLoginBtn.setReadPermissions("email", "public_profile","user_photos", "user_birthday");
//
//        fbLoginBtn.registerCallback(fbCallBack, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                handleFacebookAccessToken(loginResult.getAccessToken());
//            }
//            @Override
//            public void onCancel() {
//                Log.e("LoginPopup", "facebook:onCancel");
//                // ...
//            }
//            @Override
//            public void onError(FacebookException error) {
//                Log.e("LoginPopup", "facebook:onError", error);
//                // ...
//            }
//        });
//
//        ggLoginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signInGoogle();
//            }
//        });
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(context.getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(context)
//                .enableAutoManage(context /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//                    }
//                } /* OnConnectionFailedListener */)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//
//        firebaseAuth = FirebaseAuth.getInstance();
    }



    private View.OnClickListener signInClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            attemptLogin();
        }
    };

    private View.OnClickListener forgotPwdBtnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener newAccPwdBtnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, NewAccountActivity.class);
            context.startActivity(intent);
        }
    };

    public void show() {
        popup.show();
    }

    private void attemptLogin() {

        // Reset errors.
        emailEdt.setError(null);
        passwordEdt.setError(null);

        // Store values at the time of the login attempt.
        String email = emailEdt.getText().toString();
        String password = passwordEdt.getText().toString();

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordEdt.setError(context.getString(R.string.error_invalid_password));
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailEdt.setError(context.getString(R.string.error_field_required));
        } else if (!isPhoneNumberValid(email)) {
            emailEdt.setError(context.getString(R.string.error_invalid_email));
        }

        loginEmail(email, password);
    }

    private void loginEmail(final String email, String password) {
        SimpleProgress.show(context);

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        SimpleProgress.dismiss();
                        if (!task.isSuccessful()) {
                        } else {
                            Toast.makeText(context, "Login Successful !",
                                    Toast.LENGTH_SHORT).show();
                            popup.dismiss();
                        }
                    }
                });
    }

    private boolean isPhoneNumberValid(String phone) {
        return phone.startsWith("0");// && phone.length() > 8 && phone.length() < 12;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public boolean isShowing() {
        return popup.isShowing();
    }

    public void hidePopUp() {
        if (popup.isShowing()) {
            popup.dismiss();
            SimpleProgress.dismiss();
        }
    }


    public interface LoginPopupCallBack {
        void dismiss(int code);
    }
}
