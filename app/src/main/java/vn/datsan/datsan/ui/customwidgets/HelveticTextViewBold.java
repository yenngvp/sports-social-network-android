package vn.datsan.datsan.ui.customwidgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import vn.datsan.datsan.ui.fonts.AppFont;

public class HelveticTextViewBold extends TextView {

	public HelveticTextViewBold(Context context) {
		super(context);
		init();
	}
	
	public HelveticTextViewBold(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void init() {
		
		if (AppFont.helveticBold == null) {
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
					"fonts/helveticaneuelight.ttf");
			setTypeface(tf, Typeface.BOLD);
			AppFont.helveticBold = tf;
		} else {
			setTypeface(AppFont.helveticBold, Typeface.BOLD);
		}
	}

}
