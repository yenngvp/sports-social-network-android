package vn.datsan.datsan.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import vn.datsan.datsan.R;
import vn.datsan.datsan.ui.adapters.SubjectInfoAdapter;
import vn.datsan.datsan.utils.AppLog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubjectInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubjectInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubjectInfoFragment extends Fragment {
    private static final String PARAM1 = "param1";
    private static final String PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private SubjectInfoAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public SubjectInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubjectInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubjectInfoFragment newInstance(String param1, String param2) {
        SubjectInfoFragment fragment = new SubjectInfoFragment();
        Bundle args = new Bundle();
        args.putString(PARAM1, param1);
        args.putString(PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(PARAM1);
            mParam2 = getArguments().getString(PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        AppLog.log(AppLog.LogType.LOG_ERROR, "SubjectInfoFragment", "onCreateView");
        View view = inflater.inflate(R.layout.subject_info_list, container, false);
        adapter = new SubjectInfoAdapter(new ArrayList<SubjectInfoAdapter.DataItem>());
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        }

        reloadUI();
        return view;
    }

    private void reloadUI() {
        List<SubjectInfoAdapter.DataItem> itemList = new ArrayList<>();
        for(int i = 0; i < 50; i++) {
            itemList.add(adapter.createDataItem("SubJect " + i, "my info " + i,
                    SubjectInfoAdapter.DataType.TEXT));
        }
        adapter.updateDataSet(itemList);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
