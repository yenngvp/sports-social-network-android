package vn.datsan.datsan.utils.localization;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


/**
 * Created by yennguyen on 7/30/16.
 */
public class VietnameseUnsignedTranslatorTest {

    @Test
    public void testGetTranslation() throws Exception {
        assertEquals("Translation result is wrong 1",
                "San Bong Da",
                VietnameseUnsignedTranslator.getInstance().getTranslation("Sân Bóng Đá"));
        assertEquals("Translation result is wrong 2",
                "San Bong Da Cau lac bo van hoa - TDTT Nguyen Du",
                VietnameseUnsignedTranslator.getInstance().getTranslation("Sân Bóng Đá Câu lạc bộ văn hóa - TDTT Nguyễn Du"));
        assertEquals("Translation result is wrong 3",
                "118/80 Huynh Thien Loc, Phuong Hoa Thanh, Quan Tan Phu (Vuon Ngau), Tp HCM",
                VietnameseUnsignedTranslator.getInstance().getTranslation("118/80 Hùynh Thiện Lộc, Phường Hòa Thạnh, Quận Tân Phú (Vườn Ngâu), Tp HCM"));
        assertEquals("Translation result is wrong 4",
                "Huyen Binh Chanh",
                VietnameseUnsignedTranslator.getInstance().getTranslation("Huyện Bình Chánh"));
        assertEquals("Translation result is wrong 5",
                "San Bong Da Tao Dan",
                VietnameseUnsignedTranslator.getInstance().getTranslation("San Bong Da Tao Dan"));
        assertNotEquals("Translation result is wrong 6",
                "72A Nguyễn Văn Yến, P.Tân Thới Hòa, Q.Tân Phú",
                VietnameseUnsignedTranslator.getInstance().getTranslation("72A Nguyễn Văn Yến, P.Tân Thới Hòa, Q.Tân Phú"));
    }
}
