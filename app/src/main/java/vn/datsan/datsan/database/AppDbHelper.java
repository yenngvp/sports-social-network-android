//package vn.datsan.datsan.database;
//
//import android.content.Context;
//
//import net.sqlcipher.database.SQLiteDatabase;
//import net.sqlcipher.database.SQLiteOpenHelper;
//
//import vn.datsan.datsan.models.Field;
//
///**
// * Created by xuanpham on 8/6/16.
// */
//public class AppDbHelper extends SQLiteOpenHelper {
//    private static AppDbHelper instance;
//    public static final int DATABSE_VERSION = 1;
//    public static final String DATABSE_NAME = "local.html";
//    private static final String TEXT_TYPE = " TEXT";
//
//    private static final String SQL_CREATE_FIELD_TABLE = "CREATE TABLE " +
//            Field.class.getName() + "(";
//
//    public AppDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//
//    }
//}
