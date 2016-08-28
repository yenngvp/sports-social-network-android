package vn.datsan.datsan.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.datsan.datsan.R;
import vn.datsan.datsan.activities.GroupDetailActivity;
import vn.datsan.datsan.models.Group;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.DataType;
import vn.datsan.datsan.serverdata.storage.AppCloudDataManager;
import vn.datsan.datsan.utils.AppLog;

public class GroupDetailFragment extends Fragment {
    private Group group;
    @BindView(R.id.fc_avatar)
    ImageView fcAvatar;
    @BindView(R.id.fc_name)
    TextView fcName;

    public GroupDetailFragment() {
        // Required empty public constructor
    }

    public static GroupDetailFragment newInstance(Group group, String param2) {
        GroupDetailFragment fragment = new GroupDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("data", group);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group = getArguments().getParcelable("data");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_detail, container, false);
//        ButterKnife.bind(view, getActivity());
        fcName = (TextView) view.findViewById(R.id.fc_name);
        fcAvatar = (ImageView) view.findViewById(R.id.fc_logo);
        populateData(group);
        return view;
    }

    private void populateData(Group group) {
        fcName.setText(group.getName());
        AppCloudDataManager.getInstance().getFileUrl(group.getId() + "/avatar.png", DataType.IMAGE, new CallBack.OnResultReceivedListener() {
            @Override
            public void onResultReceived(Object result) {
                if (result != null) {
                    Uri avatarUri = (Uri) result;
                    AppLog.log(AppLog.LogType.LOG_ERROR, "avatar", avatarUri.toString());
                    Picasso.with(getActivity())
                            .load(avatarUri.toString())
                            .resize(100, 100)
//                            .centerCrop()
                            .into(fcAvatar);
                }
            }
        });
    }
}
