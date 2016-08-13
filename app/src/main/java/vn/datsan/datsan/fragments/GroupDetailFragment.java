package vn.datsan.datsan.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.datsan.datsan.R;

public class GroupDetailFragment extends Fragment {

    public GroupDetailFragment() {
        // Required empty public constructor
    }

    public static GroupDetailFragment newInstance(String param1, String param2) {
        GroupDetailFragment fragment = new GroupDetailFragment();
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
        return inflater.inflate(R.layout.fragment_group_detail, container, false);
    }

}
