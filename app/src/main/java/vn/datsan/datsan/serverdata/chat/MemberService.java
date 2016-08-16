package vn.datsan.datsan.serverdata.chat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import vn.datsan.datsan.models.chat.Member;
import vn.datsan.datsan.utils.Constants;

/**
 * Created by yennguyen on 8/13/16.
 */
public class MemberService {

    private DatabaseReference memberDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_MEMBERS);
    private static MemberService instance = new MemberService();

    private MemberService() {}

    public static MemberService getInstance() {
        return instance;
    }

    public void add(String chatId, Member member) {
        getMemberDatabaseRef(chatId).setValue(member.toUserRoleMap());
    }

    public DatabaseReference getMemberDatabaseRef(String chatId) {
        return memberDatabaseRef.child(chatId);
    }
}
