package vn.datsan.datsan.fragments;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import vn.datsan.datsan.BR;
import vn.datsan.datsan.R;
import vn.datsan.datsan.models.SubjectInfo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LinearInfoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LinearInfoListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout layoutContent;


    public LinearInfoListFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static LinearInfoListFragment newInstance(String param1, String param2) {
        LinearInfoListFragment fragment = new LinearInfoListFragment();
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
        View view = inflater.inflate(R.layout.fragment_linear_info_list, container, false);
        layoutContent = (LinearLayout) view.findViewById(R.id.layout_content);
        populateView();
        return view;
    }

    private void populateView() {
        for (int i=0; i<10; i++) {
            SubjectInfo item = new SubjectInfo("Subject " + i, "Info " + i);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.linear_info_item, null);
            ViewDataBinding binding = DataBindingUtil.bind(view);
            binding.setVariable(BR.dataItem, item);

            layoutContent.addView(view);
        }
    }
}
