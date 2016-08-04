package vn.datsan.datsan.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.datsan.datsan.R;
import vn.datsan.datsan.activities.GalleryDetailActivity;
import vn.datsan.datsan.serverdata.DummyData;
import vn.datsan.datsan.ui.adapters.GalleryAdapter;
import vn.datsan.datsan.ui.adapters.RecyclerTouchListener;

public class GaleryFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    GalleryAdapter adapter;
    RecyclerView recyclerView;


    public GaleryFragment() {
        // Required empty public constructor
    }

    public static GaleryFragment newInstance(String param1, String param2) {
        GaleryFragment fragment = new GaleryFragment();
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
        View view = inflater.inflate(R.layout.fragment_galery, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setHasFixedSize(true);


        adapter = new GalleryAdapter(getActivity(), DummyData.getImageModels());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), GalleryDetailActivity.class);
                        intent.putParcelableArrayListExtra("data", DummyData.getImageModels());
                        intent.putExtra("pos", position);
                        startActivity(intent);

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
        return view;
    }

}
