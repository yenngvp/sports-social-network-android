package vn.datsan.datsan.ui.customwidgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import vn.datsan.datsan.ui.fonts.AppFont;


public class HelveticTextView extends TextView {

	public HelveticTextView(Context context) {
		super(context);
		init();
	}
	
	public HelveticTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void init() {
		
		if (AppFont.helveticNormal == null) {
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
					"fonts/helveticaneue.ttf");
			setTypeface(tf);
			AppFont.helveticNormal = tf;
		} else {
			setTypeface(AppFont.helveticNormal);
		}
	}

}
