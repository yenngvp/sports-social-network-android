package vn.datsan.datsan.ui.customwidgets.Alert;

import android.content.DialogInterface;

public interface AlertInterface {

    public int CANCEL_BUTTON_INDEX = DialogInterface.BUTTON_NEGATIVE;

    public int NEUTRAL_BUTTON_INDEX = DialogInterface.BUTTON_NEUTRAL;

    public int POSITIVE_BUTTON_INDEX = DialogInterface.BUTTON_POSITIVE;

    interface OnTapListener {

        public void onTap(SimpleAlert alert, int buttonIndex);

    }
    interface OnSetupListener {

        public void onSetup(SimpleAlert alert, int numberOfEditBox);

    }
}
