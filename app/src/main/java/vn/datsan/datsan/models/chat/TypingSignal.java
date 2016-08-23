package vn.datsan.datsan.models.chat;

import com.google.firebase.database.Exclude;

/**
 * Created by yennguyen on 7/31/16.
 */
public class TypingSignal {

    private String userName1;
    private String userName2;
    private int otherUsersCounter;

    public String getUserName1() {
        return userName1;
    }

    public void setUserName1(String userName1) {
        this.userName1 = userName1;
    }

    public String getUserName2() {
        return userName2;
    }

    public void setUserName2(String userName2) {
        this.userName2 = userName2;
    }

    public int getOtherUsersCounter() {
        return otherUsersCounter;
    }

    public void setOtherUsersCounter(int otherUsersCounter) {
        this.otherUsersCounter = otherUsersCounter;
    }

    @Exclude
    public String toString() {

        String signal;
        if (userName1 == null) {
            // Nobody typing
            signal = null;
        } else if (userName2 == null) {
            // Just one user typing
            signal = String.format("%s typing ...", userName1);
        } else if (otherUsersCounter > 0) {
            // User1 and user2 are typing
            signal = String.format("%s and %s typing ...", userName1, userName2);
        } else {
            // User1, user2 and somebody else typing
            signal = String.format("%s, %s and %i others typing ...", userName1, userName2, otherUsersCounter);
        }

        return signal;
    }

    @Exclude
    public void setUserTyping(String userName) {
        if (userName.equals(userName1) || userName.equals(userName2)) {
            return;
        }

        if (userName1 == null) {
            userName1 = userName;
        } else if (userName2 == null) {
            userName2 = userName;
        } else {
            otherUsersCounter++;
        }
    }
}
