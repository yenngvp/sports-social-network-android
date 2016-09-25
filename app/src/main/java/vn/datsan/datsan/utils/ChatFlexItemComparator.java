package vn.datsan.datsan.utils;

import java.util.Comparator;

import vn.datsan.datsan.ui.adapters.FlexListAdapter;

/**
 * Created by yennguyen on 9/10/16.
 */
public class ChatFlexItemComparator implements Comparator<FlexListAdapter.ViewItem> {

    @Override
    public int compare(FlexListAdapter.ViewItem i1, FlexListAdapter.ViewItem i2) {
        if (i1.getSortingValue() == i2.getSortingValue()) {
            return 0;
        } else if (i1.getSortingValue() > i2.getSortingValue()) {
            return -1;
        } else {
            return 1;
        }
    }
}
