package vn.datsan.datsan.utils.localization;

import org.apache.commons.lang3.StringUtils;


/**
 * Created by yennguyen on 7/30/16.
 */
public class VietnameseUnsignedTranslator {

    private static VietnameseUnsignedTranslator instance = null;

    private final String[] searchList = new String[] {
            "Á","À","Ả","Ã","Ạ","Ấ","Ầ","Ẩ","Ẫ","Ậ","Ă","Ắ","Ằ","Ẳ","Ẵ","Ặ",
            "á","à","ả","ã","ạ","â","ấ","ầ","ẩ","ẫ","ậ","ă","ắ","ằ","ẳ","ẵ","ặ",
            "Đ",
            "đ",
            "É","È","Ẻ","Ẽ","Ẹ","Ê","Ế","Ề","Ể","Ễ","Ệ",
            "é","è","ẻ","ẽ","ẹ","ê","ế","ề","ể","ễ","ệ",
            "Í","Ì","Ỉ","Ĩ","Ị",
            "í","ì","ỉ","ĩ","ị",
            "Ó","Ò","Ỏ","Õ","Ọ","Ô","Ố","Ồ","Ổ","Ỗ","Ộ","Ơ","Ớ","Ờ","Ở","Ỡ","Ợ",
            "ó","ò","ỏ","õ","ọ","ô","ố","ồ","ổ","ỗ","ộ","ơ","ớ","ờ","ở","ỡ","ợ",
            "Ú","Ù","Ủ","Ũ","Ụ","Ư","Ứ","Ừ","Ử","Ữ","Ự",
            "ú","ù","ủ","ũ","ụ","ư","ứ","ừ","ử","ữ","ự",
            "Ý","Ỳ","Ỷ","Ỹ","Ỵ",
            "ý","ỳ","ỷ","ỹ","ỵ"
    };
    private final String[] replacementList = new String[] {
            "A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A",
            "a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a",
            "D",
            "d",
            "E","E","E","E","E","E","E","E","E","E","E",
            "e","e","e","e","e","e","e","e","e","e","e",
            "I","I","I","I","I",
            "i","i","i","i","i",
            "O","O","O","O","O","O","O","O","O","O","O","O","O","O","O","O","O",
            "o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o","o",
            "U","U","U","U","U","U","U","U","U","U","U",
            "u","u","u","u","u","u","u","u","u","u","u",
            "Y","Y","Y","Y","Y",
            "y","y","y","y","y"
    };

    private VietnameseUnsignedTranslator() {}

    public static VietnameseUnsignedTranslator getInstance() {
        if (instance == null) {
            instance = new VietnameseUnsignedTranslator();
        }
        return instance;
    }

    /**
     * Translate string from input string
     *
     * @param input
     * @return a translated string
     */
    public String getTranslation(String input) {
        return  StringUtils.replaceEach(input, searchList, replacementList);
    }
}
