package vn.datsan.datsan.ui.customwidgets;

import android.app.ProgressDialog;
import android.content.Context;

import vn.datsan.datsan.R;

/**
 * Created by xuanpham on 6/20/16.
 */
public class SimpleProgress {
    // It's NOT safe when using static progressDialog in this instance
    // It could make the UI blocked forever if there are more than one context was trying to show the dialog
    private ProgressDialog progressDialog;

    private SimpleProgress() {}

    public SimpleProgress(Context context, String message) {
        progressDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
        if (message == null) {
            message = context.getString(R.string.in_progress);
        }
        progressDialog.setMessage(message);
    }

    public SimpleProgress(Context context, String message, boolean cancelable) {
        progressDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
        if (message == null) {
            message = context.getString(R.string.in_progress);
        }
        progressDialog.setMessage(message);
        progressDialog.setCancelable(cancelable);
    }

    public void show() {
        progressDialog.show();
    }

    public void dismiss() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
