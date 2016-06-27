package vn.datsan.datsan.ui.appviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import vn.datsan.datsan.R;

/**
 * Created by xuanpham on 6/27/16.
 */
public class LoginPopup {
    private Dialog popup;

    public LoginPopup(Context context) {
        popup = new Dialog(context);

        popup.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window popupWindow = popup.getWindow();
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setContentView(R.layout.activity_login);

        WindowManager.LayoutParams wlp = popupWindow.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.dimAmount = 0.30f;

        popupWindow.setAttributes(wlp);
    }

    public void show() {
        popup.show();
    }
}
