package vn.datsan.datsan.ui.customwidgets.Alert;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class SimpleAlert {

    private AlertDialog alertDialog;
    private List<EditText> editTextList;

    private SimpleAlert(final Context context, final String title, final String message,
                        final String cancelButton, final String neutralButton, final String positiveButton,
                        final String[] otherButtons,
                        final int numberOfEditField, final AlertInterface.OnSetupListener setupListener,
                        final AlertInterface.OnTapListener listener) {

        final SimpleAlert simpleAlert = this;
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context)
                        .setCancelable(false);
        if (title != null) {
            builder.setTitle(title);
        }
        if (message != null) {
            builder.setMessage(message);
        }
        if (cancelButton != null) {
            builder.setNegativeButton(cancelButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onTap(simpleAlert, AlertInterface.CANCEL_BUTTON_INDEX);
                    }
                }
            });
        }
        if (neutralButton != null) {
            builder.setNeutralButton(neutralButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onTap(simpleAlert, AlertInterface.NEUTRAL_BUTTON_INDEX);
                    }
                }
            });
        }
        if (positiveButton != null) {
            builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onTap(simpleAlert, AlertInterface.POSITIVE_BUTTON_INDEX);
                    }
                }
            });
        }
        if (otherButtons != null) {
            builder.setItems(otherButtons, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onTap(simpleAlert, which);
                    }
                }
            });
        }
        if (numberOfEditField > 0) {
            editTextList = new ArrayList<>();
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            for (int i = 0; i < numberOfEditField; i++) {
                final EditText editBox = new EditText(context);
                layout.addView(editBox);
                editTextList.add(editBox);
            }
            builder.setView(layout);
            if (setupListener != null) {
                setupListener.onSetup(this, numberOfEditField);
            }
        }

        this.alertDialog = builder.create();
    }

    private void show() {
        alertDialog.show();
    }

    public EditText getEditText (int index) {
        return editTextList.get(index);
    }

    public static void showAlert(final Context context, final String title, final String message,
                                 final String cancelButton, final String neutralButton,
                                 int numberOfEditField, final AlertInterface.OnSetupListener setupListener,
                                 final AlertInterface.OnTapListener listener) {
        new SimpleAlert(context, title, message, cancelButton, neutralButton, null, null, numberOfEditField, setupListener, listener).show();
    }

    public static void showAlert(final Context context, final String title, final String message,
                                 final String cancelButton) {
        new SimpleAlert(context, title, message, cancelButton, null, null, null, 0, null, null).show();
    }

    public static void showAlert(final Context context, final String title, final String message,
                                 final String cancelButton, final String neutralButton, final String positiveButton,
                                 final AlertInterface.OnTapListener listener) {
        new SimpleAlert(context, title, message, cancelButton, neutralButton, positiveButton, null, 0, null, listener).show();
    }

    public static void showAlert(final Context context, final String title,
                                 final String cancelButton,
                                 final String[] otherButtons,
                                 final AlertInterface.OnTapListener listener) {
        new SimpleAlert(context, title, null, cancelButton, null, null, otherButtons, 0, null, listener).show();
    }

}
