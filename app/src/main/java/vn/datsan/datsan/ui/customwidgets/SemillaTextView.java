package vn.datsan.datsan.ui.customwidgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import vn.datsan.datsan.ui.fonts.AppFont;

public class SemillaTextView extends TextView {

	
	
	public SemillaTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void init() {
		
		if (AppFont.semilla == null) {
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
							"fonts/vnf-semilla.ttf");
			setTypeface(tf);
			AppFont.semilla = tf;
		} else {
			setTypeface(AppFont.semilla);
		}
	}

}
