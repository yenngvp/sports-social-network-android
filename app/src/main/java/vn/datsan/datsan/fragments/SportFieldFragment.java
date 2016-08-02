package vn.datsan.datsan.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import vn.datsan.datsan.R;
import vn.datsan.datsan.models.Field;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.FieldManager;

/**
 * Created by xuanpham on 7/25/16.
 */

public class SportFieldFragment extends Fragment implements OnMapReadyCallback {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private GoogleMap mMap;

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

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FieldManager.getInstance().getFields(new CallBack.OnResultReceivedListener() {
            @Override
            public void onResultReceived(Object result) {
                if (result != null) {
                    List<Field> fieldList = (List<Field>) result;
                    addMarkers(fieldList);
                }
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
                mMap.addMarker(marker);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(10.777098, 106.695487);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Quan 1, tp.HCM"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
