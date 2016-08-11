package vn.datsan.datsan.ui.customwidgets;

import android.app.ProgressDialog;
import android.content.Context;

import vn.datsan.datsan.R;

/**
 * Created by xuanpham on 6/20/16.
 */
public class SimpleProgress {
    static ProgressDialog progressDialog;
    private SimpleProgress(Context context, String message) {
        progressDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
        if (message == null)
            message = context.getString(R.string.in_progress);
        progressDialog.setMessage(message);
    }

    public static void show(Context context, String message) {
        new SimpleProgress(context, message);
        progressDialog.show();
    }

    public static void dismiss() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}
