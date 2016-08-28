package vn.datsan.datsan.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.datsan.datsan.R;
import vn.datsan.datsan.activities.NewAccountActivity;
import vn.datsan.datsan.ui.appviews.LoginPopup;
import vn.datsan.datsan.ui.customwidgets.SimpleProgress;
import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.utils.Constants;

public class LoginFragment extends Fragment {
    private GoogleApiClient mGoogleApiClient;

    @BindView(R.id.email_login)
    EditText emailEdt;
    @BindView(R.id.password_login)
    EditText passwordEdt;
    @BindView(R.id.sign_in_button_login)
    Button signInBtn;
    @BindView(R.id.forgot_pwd_button_login)
    TextView forgotPwdBtn;
    @BindView(R.id.new_account_button_login)
    TextView newAccBtn;
    @BindView(R.id.fb_login_button)
    LoginButton fbLoginBtn;
    @BindView(R.id.gg_login_button)
    SignInButton ggLoginBtn;


    private LoginPopup.LoginPopupCallBack callBack;
    private CallbackManager callbackManager;

    public LoginFragment() {
        // Required empty public constructor
    }

    public void setCallBack(LoginPopup.LoginPopupCallBack callBack) {
        this.callBack = callBack;
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, view);

        fbLoginBtn.setReadPermissions("email", "public_profile","user_photos", "user_birthday");
        fbLoginBtn.setFragment(this);

        callbackManager = CallbackManager.Factory.create();
        fbLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Log.e("LoginPopup", "facebook:onCancel");
                // ...
            }
            @Override
            public void onError(FacebookException error) {
                Log.e("LoginPopup", "facebook:onError", error);
                // ...
            }
        });



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getActivity().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        AppLog.log(AppLog.LogType.LOG_ERROR, "LoginFragment", "onActivityResult");

        if (requestCode == Constants.GOOGLE_SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            AppLog.log(AppLog.LogType.LOG_ERROR, "LoginFragment", "Google login finish " + result.isSuccess());
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately

            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if (callBack != null)
                                callBack.dismiss(0);
                        }
                    }
                });
    }


    @OnClick(R.id.sign_in_button_login)
    void loginBtnClicked() {
        attemptLogin();
    }

    @OnClick(R.id.forgot_pwd_button_login)
    void resetPwdBtnClicked() {

    }

    @OnClick(R.id.new_account_button_login)
    void newAccountBtnClicked() {
        Intent intent = new Intent(getActivity(), NewAccountActivity.class);
        startActivity(intent);
        if (callBack != null)
            callBack.dismiss(0);
    }

    @OnClick(R.id.gg_login_button)
    void loginGoogleBtnClicked() {
        signInGoogle();
    }

    private void signInGoogle() {
        SimpleProgress.show(getActivity(), null);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, Constants.GOOGLE_SIGN_IN_CODE);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        SimpleProgress.show(getActivity(), null);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        SimpleProgress.dismiss();
                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if (callBack != null)
                                callBack.dismiss(0);
                        }
                    }
                });
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
            passwordEdt.setError(getActivity().getString(R.string.error_invalid_password));
        }

        // Check for a valid email address.
        if (email.isEmpty()) {
            emailEdt.setError(getActivity().getString(R.string.error_field_required));
        } else if (!isEmailValid(email)) {
            emailEdt.setError(getActivity().getString(R.string.error_invalid_email));
        }

        loginEmail(email, password);
    }

    private void loginEmail(final String email, String password) {
        SimpleProgress.show(getActivity(), null);

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        SimpleProgress.dismiss();
                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Login Failed !",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Login Successful !",
                                    Toast.LENGTH_SHORT).show();
                            if (callBack != null)
                                callBack.dismiss(0);
                        }
                    }
                });
    }

    private boolean isPhoneNumberValid(String phone) {
        return phone.startsWith("0");// && phone.length() > 8 && phone.length() < 12;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");// && phone.length() > 8 && phone.length() < 12;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


}
