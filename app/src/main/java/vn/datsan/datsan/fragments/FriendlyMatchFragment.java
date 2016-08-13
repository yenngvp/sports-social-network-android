package vn.datsan.datsan.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vn.datsan.datsan.R;
import vn.datsan.datsan.activities.ChatActivity;
import vn.datsan.datsan.activities.GroupDetailActivity;
import vn.datsan.datsan.activities.NewFriendlyMatchActivity;
import vn.datsan.datsan.models.FriendlyMatch;
import vn.datsan.datsan.models.Group;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.FriendlyMatchManager;
import vn.datsan.datsan.serverdata.GroupManager;
import vn.datsan.datsan.ui.adapters.DividerItemDecoration;
import vn.datsan.datsan.ui.adapters.FlexListAdapter;
import vn.datsan.datsan.ui.adapters.RecyclerTouchListener;
import vn.datsan.datsan.utils.Utils;

/**
 * Created by xuanpham on 7/25/16.
 */

public class FriendlyMatchFragment extends Fragment {
    FlexListAdapter adapter;
    public FriendlyMatchFragment() {
        // Required empty public constructor
    }

    public static FriendlyMatchFragment newInstance(String param1, String param2) {
        FriendlyMatchFragment fragment = new FriendlyMatchFragment();
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
        View view = inflater.inflate(R.layout.fragment_friendly_match, null);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FlexListAdapter();
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getActivity(), "Touch " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
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
                startActivity(new Intent(getActivity(), NewFriendlyMatchActivity.class));
            }
        });

        populateData();
        return view;
    }

    private void populateData() {
        FriendlyMatchManager.getInstance().getMaths(new CallBack.OnResultReceivedListener() {
            @Override
            public void onResultReceived(Object result) {
                List<FriendlyMatch> matches = (List<FriendlyMatch> )  result;
                if (matches != null) {
                    List<FlexListAdapter.FlexItem> list = new ArrayList<>();
                    for (FriendlyMatch match : matches) {

                        DateTime startTime = new DateTime(match.getStartTime());
                        DateTime endTime = new DateTime(match.getEndTime());
                        String dayWeek = Utils.convertWeekDayToString(startTime.getDayOfWeek());
                        String dayMonth = " Ngày " + Utils.DAYMONTH_FORMAT.print(startTime);
                        String timeRange = "Thời gian " + startTime.getHourOfDay() + "h:" + startTime.getMinuteOfHour() + " - " +
                                endTime.getHourOfDay() + "h:" + endTime.getMinuteOfHour();
                        String field = "\nSân : ";
                        if (match.getFields() == null || match.getFields().isEmpty()) {
                            field += "Thoả thuận sau";
                        } else {
                            field += match.getFields();
                        }
                        String content = dayWeek + "," + dayMonth + "\n" + timeRange
                                + field;

                        FlexListAdapter.FlexItem item = adapter.createItem(null, match.getTitle(),content, null);
                        list.add(item);
                    }
                    adapter.update(list);
                }
            }
        });
    }
}
