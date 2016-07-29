package vn.datsan.datsan.ui.appviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.List;

import vn.datsan.datsan.R;

/**
 * Created by xuanpham on 7/29/16.
 */
public class ViewHelper {
    public static Spinner populateSpinner(Context context, Spinner spinner, List<String> listContent) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listContent);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return spinner;
    }

    public static View genSpinnerLayout(Context context, String name, List<String> content) {
        View view = LayoutInflater.from(context).inflate(R.layout.simple_spinner_layout, null, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        TextView nameTv = (TextView) view.findViewById(R.id.spinner_name);
        nameTv.setText(name);
        populateSpinner(context, spinner, content);
        return view;
    }
}
