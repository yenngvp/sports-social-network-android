package vn.datsan.datsan.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.MarkerOptions;
import com.androidmapsextensions.OnMapReadyCallback;
import com.androidmapsextensions.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import vn.datsan.datsan.R;
import vn.datsan.datsan.activities.FieldDetailActivity;
import vn.datsan.datsan.models.Field;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.FieldManager;
import vn.datsan.datsan.ui.adapters.DividerItemDecoration;
import vn.datsan.datsan.ui.adapters.FlexListAdapter;
import vn.datsan.datsan.ui.adapters.RecyclerTouchListener;
import vn.datsan.datsan.ui.customwidgets.Alert.SimpleAlert;

/**
 * Created by xuanpham on 7/25/16.
 */

public class SportFieldFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private GoogleMap mMap;
    FlexListAdapter adapter;
    private View searchResultView;

    private OnFragmentInteractionListener mListener;

    public SportFieldFragment() {
        // Required empty public constructor
    }

    public static SportFieldFragment newInstance(String param1, String param2) {
        SportFieldFragment fragment = new SportFieldFragment();
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
        View view = inflater.inflate(R.layout.fragment_sport_field, null);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        searchResultView = view.findViewById(R.id.searchResultView);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getExtendedMapAsync(this);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FlexListAdapter();
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getActivity(), "Touch " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), FieldDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        ((ImageButton) view.findViewById(R.id.btn_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchResultView.setVisibility(View.GONE);
            }
        });
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    private void addMarkers(List<Field> fieldList) {
        for (Field field : fieldList) {
            String latlon = field.getLocation();
            if (latlon != null && latlon.length() > 6) {
                String arr[] = latlon.split(",");
                MarkerOptions marker = new MarkerOptions();
                marker.position(new LatLng(Double.parseDouble(arr[0]), Double.parseDouble(arr[1])));
                marker.title(field.getName());
                marker.snippet(field.getAddress());
                marker.data(field);
                //marker.
                mMap.addMarker(marker);
            }
        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.getData() != null && marker.getData() instanceof Field) {
            Intent intent = new Intent(getActivity(), FieldDetailActivity.class);
            intent.putExtra("data", (Field) marker.getData());
            startActivity(intent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(10.777098, 106.695487);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Quan 1, tp.HCM"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnInfoWindowClickListener(this);

        FieldManager.getInstance().getFields(new CallBack.OnResultReceivedListener() {
            @Override
            public void onResultReceived(Object result) {
                if (result != null) {
                    List<Field> fieldList = (List<Field>) result;
                    addMarkers(fieldList);
                }
            }
        });
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void showSearchResultView(boolean open) {
        if (open)
            searchResultView.setVisibility(View.VISIBLE);
        else
            searchResultView.setVisibility(View.GONE);
    }

    public void showSearchResult(Object object) {
        if (object == null)
            return;
        List<Field> fields = (List<Field>) object;
        if (fields.isEmpty()) {
            SimpleAlert.showAlert(getActivity(), "Tìm sân", "Không tìm thấy kết quả !", getString(R.string.close));
            return;
        }


        searchResultView.setVisibility(View.VISIBLE);
        List<FlexListAdapter.FlexItem> list = new ArrayList<>();
        for (Field field : fields) {
            FlexListAdapter.FlexItem item = adapter.createItem(null, field.getName(), field.getAddress(), null);
            list.add(item);
        }
        adapter.update(list);
    }
}
