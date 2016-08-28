package vn.datsan.datsan.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.datsan.datsan.R;
import vn.datsan.datsan.models.Group;
import vn.datsan.datsan.models.UserRole;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.GroupManager;
import vn.datsan.datsan.serverdata.UserManager;
import vn.datsan.datsan.serverdata.storage.AppCloudDataManager;
import vn.datsan.datsan.serverdata.storage.CloudDataStorage;
import vn.datsan.datsan.ui.customwidgets.Alert.AlertInterface;
import vn.datsan.datsan.ui.customwidgets.Alert.SimpleAlert;
import vn.datsan.datsan.ui.customwidgets.SimpleProgress;
import vn.datsan.datsan.utils.ActivityUtils;
import vn.datsan.datsan.utils.AppLog;

public class NewGroupActivity extends SimpleActivity {

    @BindView(R.id.group_name)
    EditText name;
    @BindView(R.id.phone)
    EditText phoneTv;
    @BindView(R.id.spinner_city)
    Spinner citySpinner;
    @BindView(R.id.take_photo)
    ImageButton takePhotoBtn;
    @BindView(R.id.fc_avatar)
    ImageView groupAvatar;
    private Bitmap avatarBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        super.initToolBar();

        setTitle("Tạo đội bóng");
        ButterKnife.bind(this);
//        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(NewGroupActivity.this, "click", Toast.LENGTH_SHORT).show();
//                openImageIntent();
//            }
//        });
    }

    @OnClick(R.id.take_photo)
    public void onTakePhotoBtnClicked() {
        Toast.makeText(NewGroupActivity.this, "click", Toast.LENGTH_SHORT).show();
        openImageIntent();
    }
    @OnClick(R.id.register_btn)
    public void onRegisterBtnClicked() {
        final Group group = createGroupObject();
        if (group != null) {
//            SimpleProgress.show(NewGroupActivity.this, "Tạo FC..");
            if (avatarBitmap != null) {
                AppCloudDataManager.getInstance().uploadImage(avatarBitmap,
                        group.getId() + "/avatar.png", new CallBack.OnResultReceivedListener() {
                            @Override
                            public void onResultReceived(Object result) {
                                String imageUrl = (String) result;
                                if (imageUrl != null) {
                                    group.setLogoUrl(imageUrl);
                                    doAddNewGroup(group);
                                } else {
                                    SimpleProgress.dismiss();
                                    SimpleAlert.showAlert(NewGroupActivity.this, getString(R.string.error),
                                            getString(R.string.failed_doagain), getString(R.string.close));
                                }
                            }
                        });
            } else {
                doAddNewGroup(group);
            }
        }
    }

    private void doAddNewGroup(Group group) {
        GroupManager.getInstance().addGroup(group, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                SimpleProgress.dismiss();
                if (databaseError == null) {
                    SimpleAlert.showAlert(NewGroupActivity.this, "Đăng ký thành công",
                            getString(R.string.close), null, new AlertInterface.OnTapListener() {
                                @Override
                                public void onTap(SimpleAlert alert, int buttonIndex) {
                                    finish();
                                }
                            });
                } else {
                    SimpleAlert.showAlert(NewGroupActivity.this, getString(R.string.error),
                            getString(R.string.failed_doagain),getString(R.string.close));
                }
            }
        });
    }

    private Group createGroupObject() {
        String groupName = name.getText().toString();
        if (groupName.isEmpty()) {
            SimpleAlert.showAlert(NewGroupActivity.this, getString(R.string.error),
                    "Chưa nhập đủ thông tin !!!", getString(R.string.close));
            return null;
        }

        String location = citySpinner.getSelectedItem().toString();

        if (location.isEmpty()) {
            SimpleAlert.showAlert(NewGroupActivity.this, getString(R.string.error),
                    "Chưa nhập đủ thông tin !!!", getString(R.string.close));
            return null;
        }

        String phoneNumber = phoneTv.getText().toString();
        if (phoneNumber.isEmpty()) {
            SimpleAlert.showAlert(NewGroupActivity.this, getString(R.string.error),
                    "Chưa nhập đủ thông tin !!!", getString(R.string.close));
            return null;
        }

        List<String> phones = new ArrayList<>();
        phones.add(phoneNumber);

        Group group = new Group(groupName, 0, null);
        group.setCity(location);
        group.setPhones(phones);
        group.addMember(FirebaseAuth.getInstance().getCurrentUser().getUid(), UserRole.SUPER_ADMIN);
        return group;
    }

    private void openImageIntent() {
        ActivityUtils.startImageIntent(NewGroupActivity.this, 111, getString(R.string.select_avatar));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 111) {
                final boolean isCamera;
                if (data == null || data.getData() == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = ActivityUtils.getDefaultCameraUri();
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }

                //AppLog.log(AppLog.LogType.LOG_ERROR, "data", data.getExtras().toString());
                AppLog.log(AppLog.LogType.LOG_ERROR, "image url", selectedImageUri.toString());
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    avatarBitmap = bitmap;
//                    CloudDataStorage.getInstance().uploadPhoto(bitmap,
//                            UserManager.getInstance().getUserInfo().getId() + "/avatar/");
                    groupAvatar.setImageBitmap(avatarBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
