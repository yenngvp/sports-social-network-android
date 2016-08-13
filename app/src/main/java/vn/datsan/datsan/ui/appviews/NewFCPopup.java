package vn.datsan.datsan.ui.appviews;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import vn.datsan.datsan.R;
import vn.datsan.datsan.models.Group;
import vn.datsan.datsan.serverdata.GroupManager;

/**
 * Created by xuanpham on 7/29/16.
 */
public class NewFCPopup extends BasePopup {
    private Context context;
    EditText groupName;
    EditText groupLocation;
    EditText groupPhone;

    public NewFCPopup(Activity context) {
        super(context, true, R.layout.new_fc_layout);
        this.context = context;

        groupName = (EditText) getPopup().findViewById(R.id.group_name);
        groupLocation = (EditText) getPopup().findViewById(R.id.group_location);
        groupPhone = (EditText) getPopup().findViewById(R.id.group_phone);
        Button button = (Button) getPopup().findViewById(R.id.register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Group group = createGroup();
                if (group != null)
                    GroupManager.getInstance().addGroup(group);
            }
        });
    }

    private Group createGroup() {
        String name = groupName.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(context, "Missing group name", Toast.LENGTH_SHORT).show();
            return null;
        }

        String location = groupLocation.getText().toString();
        if (location.isEmpty()) {
            Toast.makeText(context, "Missing group name", Toast.LENGTH_SHORT).show();
            return null;
        }

        String phone = groupPhone.getText().toString();
        if (phone.isEmpty()) {
            Toast.makeText(context, "Missing group name", Toast.LENGTH_SHORT).show();
            return null;
        }

        List<String> admin = new ArrayList<>();
        admin.add(FirebaseAuth.getInstance().getCurrentUser().getUid());

        return new Group(null, 0, null);
    }

}
