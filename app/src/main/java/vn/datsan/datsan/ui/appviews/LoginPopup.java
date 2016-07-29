package vn.datsan.datsan.ui.appviews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import vn.datsan.datsan.R;
import vn.datsan.datsan.activities.NewAccountActivity;
import vn.datsan.datsan.fragments.LoginFragment;
import vn.datsan.datsan.ui.customwidgets.SimpleProgress;

/**
 * Created by xuanpham on 6/27/16.
 */
public class LoginPopup {
    private Dialog popup;

    public LoginPopup(final AppCompatActivity context) {
        popup = new Dialog(context);
        popup.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window popupWindow = popup.getWindow();
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setContentView(R.layout.login_popup);

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
    }

    public void show() {
        popup.show();
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
