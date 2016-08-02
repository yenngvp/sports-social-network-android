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

import java.util.ArrayList;
import java.util.List;

import vn.datsan.datsan.R;
import vn.datsan.datsan.activities.GroupDetailActivity;
import vn.datsan.datsan.models.Group;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.GroupManager;
import vn.datsan.datsan.ui.adapters.DividerItemDecoration;
import vn.datsan.datsan.ui.adapters.FlexListAdapter;
import vn.datsan.datsan.ui.adapters.RecyclerTouchListener;
import vn.datsan.datsan.ui.appviews.NewFCPopup;

/**
 * Created by xuanpham on 7/25/16.
 */

public class SportClubFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    FlexListAdapter adapter;
    private OnFragmentInteractionListener mListener;

    public SportClubFragment() {
        // Required empty public constructor
    }

    public static SportClubFragment newInstance(String param1, String param2) {
        SportClubFragment fragment = new SportClubFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_club, null);
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
                NewFCPopup popup = new NewFCPopup(getActivity());
                popup.show();
            }
        });

        populateData();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void populateData() {
        GroupManager.getInstance().getUserGroups(new CallBack.OnResultReceivedListener() {
            @Override
            public void onResultReceived(Object result) {
                List<Group> groupList = (List<Group> )  result;
                if (groupList != null) {
                    List<FlexListAdapter.FlexItem> list = new ArrayList<>();
                    for (Group group : groupList) {
                        FlexListAdapter.FlexItem item = adapter.createItem(null, group.getName(), group.getCity(), null);
                        list.add(item);
                    }
                    adapter.update(list);
                }
            }
        });
    }
}
