package vn.datsan.datsan.ui.appviews;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import vn.datsan.datsan.R;

/**
 * Created by xuanpham on 7/29/16.
 */
public class BasePopup {
    private Dialog popup;
    public BasePopup(final Activity context, boolean fullScreen, int contentView) {
        popup = new Dialog(context);
        popup.requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Window popupWindow = popup.getWindow();

        popupWindow.getAttributes().windowAnimations = R.style.left_to_right;
        popup.setContentView(contentView);

        WindowManager.LayoutParams wlp = popupWindow.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        if (fullScreen)
            wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        else {
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        wlp.dimAmount = 0.30f;

        popupWindow.setAttributes(wlp);

        ImageButton closeBtn = (ImageButton) popup.findViewById(R.id.btn_close);
        if (closeBtn != null) {
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup.dismiss();
                }
            });
        }
    }

    public Dialog getPopup() {
        return popup;
    }

    public void show() {
        popup.show();
    }

    public void hide() {
        popup.dismiss();
    }
}
