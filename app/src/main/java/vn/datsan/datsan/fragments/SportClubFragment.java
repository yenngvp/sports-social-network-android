package vn.datsan.datsan.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import vn.datsan.datsan.R;
import vn.datsan.datsan.activities.GroupDetailActivity;
import vn.datsan.datsan.activities.NewGroupActivity;
import vn.datsan.datsan.models.Group;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.GroupService;
import vn.datsan.datsan.ui.adapters.DividerItemDecoration;
import vn.datsan.datsan.ui.adapters.FlexListAdapter;
import vn.datsan.datsan.ui.adapters.RecyclerTouchListener;
import vn.datsan.datsan.utils.AppLog;

/**
 * Created by xuanpham on 7/25/16.
 */

public class SportClubFragment extends Fragment {
    FlexListAdapter adapter;
    List<Group> groupList;

    public SportClubFragment() {
        // Required empty public constructor
    }

    public static SportClubFragment newInstance(String param1, String param2) {
        SportClubFragment fragment = new SportClubFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_club, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FlexListAdapter(getActivity()) {

            @Override
            public void setImage(Context context, ImageView imageView, String url) {
                AppLog.log(AppLog.LogType.LOG_ERROR, "Loadurl", url);
                Picasso.with(getActivity())
                        .load(url).error(R.drawable.ball).placeholder(R.drawable.ball)
                        .resize(100, 100)
                        .centerCrop()
                        .into(imageView);
            }
        };
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getActivity(), "Touch " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
                Group group = groupList.get(position);
                intent.putExtra("data", group);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NewFCPopup popup = new NewFCPopup(getActivity());
//                popup.show();
                Intent intent = new Intent(getActivity(), NewGroupActivity.class);
                startActivity(intent);

            }
        });

        populateData();
        return view;
    }

    private void populateData() {
        GroupService.getInstance().getUserGroups(new CallBack.OnResultReceivedListener() {
            @Override
            public void onResultReceived(Object result) {
                groupList = (List<Group> )  result;
                if (groupList != null) {
                    adapter.update(groupList);
                }
            }
        });
    }
}
