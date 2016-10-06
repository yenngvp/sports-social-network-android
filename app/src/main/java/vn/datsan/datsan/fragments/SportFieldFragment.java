package vn.datsan.datsan.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowCloseListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import vn.datsan.datsan.R;
import vn.datsan.datsan.activities.FieldDetailActivity;
import vn.datsan.datsan.models.Field;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.FieldService;
import vn.datsan.datsan.ui.adapters.DividerItemDecoration;
import vn.datsan.datsan.ui.adapters.FlexListAdapter;
import vn.datsan.datsan.ui.adapters.RecyclerTouchListener;
import vn.datsan.datsan.ui.customwidgets.Alert.SimpleAlert;
import vn.datsan.datsan.utils.AppLog;

/**
 * Created by xuanpham on 7/25/16.
 */

public class SportFieldFragment extends Fragment implements
        OnMarkerClickListener,
        OnInfoWindowClickListener,
        OnMarkerDragListener,
        SeekBar.OnSeekBarChangeListener,
        OnMapReadyCallback,
        OnInfoWindowLongClickListener,
        OnInfoWindowCloseListener,
        ConnectionCallbacks,
        OnConnectionFailedListener {

    private static final String TAG = SportFieldFragment.class.getSimpleName();

    private GoogleMap mMap;

    private Map<String, Marker> markers;

    private List<Field> fields;

    /**
     * Provides the entry point to Google Play services.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */
    private Location mLastLocation;

    private FlexListAdapter adapter;
    private View searchResultView;

    private OnFragmentInteractionListener mListener;

    public SportFieldFragment() {
        // Required empty public constructor
    }

    public static SportFieldFragment newInstance(String param1, String param2) {
        SportFieldFragment fragment = new SportFieldFragment();
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
        View view = inflater.inflate(R.layout.fragment_sport_field, null);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        searchResultView = view.findViewById(R.id.searchResultView);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        buildGoogleApiClient();

        FloatingActionButton currentLocationFab = (FloatingActionButton) view.findViewById(R.id.currentLocationFab);
        currentLocationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppLog.d(TAG, "Request my last known location");
                requestCurrentLocation();
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

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Field field = getFieldFromMaker(marker);
        if (field == null) return;

        Intent intent = new Intent(getActivity(), FieldDetailActivity.class);
        intent.putExtra("data", field);
        startActivity(intent);
    }

    private Field getFieldFromMaker(Marker marker) {
        if (fields == null || !markers.containsValue(marker)) {
            return null;
        }

        String fieldId = null;
        for (Map.Entry<String, Marker> entry : markers.entrySet()) {
            if (entry.equals(marker)) {
                fieldId = entry.getKey();
                break;
            }
        }

        for (Field field : fields) {
            if (field.getId().equals(fieldId)) {
                return field;
            }
        }

        return null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // We will provide our own zoom controls.
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        // Add a marker in Sydney and move the camera
        if (mLastLocation != null) {
            LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 20));
            mMap.setOnInfoWindowClickListener(this);
        }

        fields = FieldService.getInstance().getFields(new CallBack.OnResultReceivedListener() {
            @Override
            public void onResultReceived(Object result) {
                if (result != null) {
                    List<Field> fields = (List<Field>) result;
                    if (fields != null) {
                        addMarkers(fields);
                    }
                }
            }
        });
        if (fields != null) {
            addMarkers(fields);
        }
    }

    private void addMarkers(List<Field> fieldList) {
        if (markers == null) {
            markers = new HashMap<>();
        }
        for (Field field : fieldList) {
            String location = field.getLocation();
            if (location != null && location.length() > 6) {
                String arr[] = location.split(",");
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(arr[0]), Double.parseDouble(arr[1])))
                        .title(field.getName())
                        .snippet(field.getAddress())
                        .snippet("Suc chua: 16.300")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.football_field))
                        .infoWindowAnchor(0.5f, 0.5f);
                Marker marker = mMap.addMarker(markerOptions);
                markers.put(field.getId(), marker);
            }
        }
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onInfoWindowClose(Marker marker) {

    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

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
        adapter.update(fields);
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        requestCurrentLocation();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        AppLog.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        AppLog.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    private void requestCurrentLocation() {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Toast.makeText(getActivity(), "Requesting current location...", Toast.LENGTH_LONG).show();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            AppLog.d(TAG, String.format("Location (%s, %s)", mLastLocation.getLatitude(), mLastLocation.getLongitude()));

            LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(currentLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location))
                    .title("Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            mMap.setOnInfoWindowClickListener(this);
        } else {
            Toast.makeText(getActivity(), R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }
    }
}
