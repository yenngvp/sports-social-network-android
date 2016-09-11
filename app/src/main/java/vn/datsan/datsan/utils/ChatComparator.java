package vn.datsan.datsan.utils;

import java.util.Comparator;

import vn.datsan.datsan.models.chat.Chat;

/**
 * Created by yennguyen on 9/11/16.
 */
public class ChatComparator implements Comparator<Chat> {

    @Override
    public int compare(Chat i1, Chat i2) {
        if (i1.getLastModifiedTimestampMillis() == i2.getLastModifiedTimestampMillis()) {
            return 0;
        } else if (i1.getLastModifiedTimestampMillis() > i2.getLastModifiedTimestampMillis()) {
            return -1;
        } else {
            return 1;
        }
    }
}
