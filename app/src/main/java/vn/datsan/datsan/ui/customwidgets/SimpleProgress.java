package vn.datsan.datsan.ui.customwidgets;

import android.app.ProgressDialog;
import android.content.Context;

import vn.datsan.datsan.R;

/**
 * Created by xuanpham on 6/20/16.
 */
public class SimpleProgress {
    static ProgressDialog progressDialog;
    private SimpleProgress(Context context) {
        progressDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(context.getString(R.string.in_progress));
    }

    public static void show(Context context) {
        new SimpleProgress(context);
        progressDialog.show();
    }

    public static void dismiss() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}
