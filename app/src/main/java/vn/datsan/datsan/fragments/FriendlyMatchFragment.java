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

import java.util.List;

import vn.datsan.datsan.R;
import vn.datsan.datsan.activities.FriendlyMatchDetailActivity;
import vn.datsan.datsan.activities.NewFriendlyMatchActivity;
import vn.datsan.datsan.models.FriendlyMatch;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.MatchService;
import vn.datsan.datsan.ui.adapters.DividerItemDecoration;
import vn.datsan.datsan.ui.adapters.FlexListAdapter;
import vn.datsan.datsan.ui.adapters.RecyclerTouchListener;

/**
 * Created by xuanpham on 7/25/16.
 */

public class FriendlyMatchFragment extends Fragment {
    FlexListAdapter adapter;
    List<FriendlyMatch> matches;
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
        adapter = new FlexListAdapter(getActivity()) {
            @Override
            public void setImage(Context context, ImageView imageView, String imageUrl) {

            }
        };
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (matches == null || matches.isEmpty())
                    return;
                Intent intent = new Intent(getActivity(), FriendlyMatchDetailActivity.class);
                intent.putExtra("data", matches.get(position));
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
        MatchService.getInstance().getMatches(new CallBack.OnResultReceivedListener() {
            @Override
            public void onResultReceived(Object result) {
                matches = (List<FriendlyMatch> )  result;
                if (matches != null) {
                    adapter.update(matches);
                }
            }
        });
    }
}
