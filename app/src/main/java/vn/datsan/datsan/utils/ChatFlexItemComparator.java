package vn.datsan.datsan.utils;

import java.util.Comparator;

import vn.datsan.datsan.ui.adapters.FlexListAdapter;

/**
 * Created by yennguyen on 9/10/16.
 */
public class ChatFlexItemComparator implements Comparator<FlexListAdapter.FlexItem> {

    @Override
    public int compare(FlexListAdapter.FlexItem i1, FlexListAdapter.FlexItem i2) {
        if (i1.getSortingWeight() == i2.getSortingWeight()) {
            return 0;
        } else if (i1.getSortingWeight() > i2.getSortingWeight()) {
            return -1;
        } else {
            return 1;
        }
    }
}
