package m.h.testapp.map;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.ScreenPoint;
import com.yandex.mapkit.geometry.Geometry;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.Response;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchManagerType;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.SearchType;
import com.yandex.mapkit.search.Session;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import m.h.testapp.R;

public class MapFragment extends Fragment implements UserLocationObjectListener {

    ImageButton imgbtnpolygon,imgbtnrefresh;

    private static final String TAG = MapFragment.class.getSimpleName();
    private Unbinder unbinder;
    private static final int PERMISSIONS_CODE = 109;
    private static final double DESIRED_ACCURACY = 0;
    private static final long MINIMAL_TIME = 0;
    private static final double MINIMAL_DISTANCE = 50;
    private static final boolean USE_IN_BACKGROUND = false;

 private Session.SearchListener mSearchPointListener;
    private LocationListener myLocationListener;

    public static final int COMFORTABLE_ZOOM_LEVEL = 9;
    private static final Point START_LOCATION = new Point(53.35, 83.76);
    private final SearchOptions SEARCH_OPTIONS = new SearchOptions().setSearchTypes(
            SearchType.GEO.value);

    private Session searchSession;
    private SearchManager searchManager;
    private boolean isUseSuggestions;
    private Point myLocation;
   private ScreenPoint centerPointOfScreen;
    private LocationManager locationManager;

    @BindView(R.id.mapview)
    MapView mapView;
    @BindView(R.id.sv_search_address)
    SearchView svSearchAddress;
    @BindView(R.id.root_coordinator)
    CoordinatorLayout rootCoordinatorLayout;
    @BindView(R.id.fab_current_location)
    FloatingActionButton fabCurrentLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSearchPointListener = new Session.SearchListener() {
            @Override
            public void onSearchResponse(Response response) {
                for (GeoObjectCollection.Item searchResult : response.getCollection().getChildren()) {
                    Point resultLocation = searchResult.getObj().getGeometry().get(0).getPoint();
                    if (resultLocation != null) {
                        moveCamera(resultLocation, COMFORTABLE_ZOOM_LEVEL);
                        svSearchAddress.clearFocus();
                        break;
                    }
                }
            }

            @Override
            public void onSearchError(Error error) {
                Log.w(TAG, "onSearchError ERROR when try search point by address");
                Toast.makeText(getContext(), R.string.error_cant_get_coordinates, Toast.LENGTH_SHORT).show();
            }
        };

        myLocationListener = new LocationListener() {
            @Override
            public void onLocationUpdated(Location location) {
                if (myLocation == null) {
                    moveCamera(location.getPosition(), COMFORTABLE_ZOOM_LEVEL);
                }
                myLocation = location.getPosition();
                Log.w(TAG, "my location - " + myLocation.getLatitude() + "," + myLocation.getLongitude());
            }

            @Override
            public void onLocationStatusUpdated(LocationStatus locationStatus) {
                if (locationStatus == LocationStatus.NOT_AVAILABLE) {
                    Toast.makeText(getContext(), R.string.error_cant_get_my_location, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this, view);

        searchManager = MapKitFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);
       prepareSearchView();

        locationManager = MapKitFactory.getInstance().createLocationManager();

        setupCenterPointOfScreen();

        addiconlocation();

        //btn polygon-----------------------------------------------------------
        imgbtnrefresh=view.findViewById(R.id.imgbtnrefresh);
        imgbtnpolygon=view.findViewById(R.id.imgbtnpolygon);
        imgbtnpolygon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgbtnrefresh.getVisibility()==View.GONE)
                {
                    imgbtnpolygon.setBackgroundResource(R.drawable.btnredbckr);
                    imgbtnpolygon.setImageResource(R.drawable.ic_clancel);
                    imgbtnrefresh.setVisibility(View.VISIBLE);
                }else {
                    imgbtnpolygon.setBackgroundResource(R.drawable.btnwhitebckr);
                    imgbtnpolygon.setImageResource(R.drawable.ic_polygon);
                    imgbtnrefresh.setVisibility(View.GONE);
                }
            }
        });
        //------------------------------------------------------------------------------

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();

        if(!canDetermineLocation()){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_CODE);
        }else {
        subscribeLocationUpdate();
        }

        if (svSearchAddress != null) {
            svSearchAddress.clearFocus();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        locationManager.unsubscribe(myLocationListener);
        MapKitFactory.getInstance().onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    subscribeLocationUpdate();

                } else {
                    new AlertDialog.Builder(this.getContext())
                            .setMessage(R.string.rights_are_required_message)
                            .setPositiveButton(R.string.btn_name_grant_permission,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            if(!canDetermineLocation()){
                                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_CODE);
                                            }
                                        }
                                    })
                            .setCancelable(false)
                            .create()
                            .show();
                }
                return;
            }
        }
    }
    private UserLocationLayer userLocationLayer;
    @OnClick(R.id.fab_current_location)
    public void onFabCurrentLocation() {
        if (myLocation == null) {
            Toast.makeText(getContext(), R.string.coordinates_are_not_yet_determinate,Toast.LENGTH_SHORT).show();
            return;
        }
        moveCamera(myLocation, 18);
    }

    void addiconlocation(){
        userLocationLayer =  mapView.getMap().getUserLocationLayer();
        userLocationLayer.setEnabled(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);
    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
        userLocationLayer.setAnchor(
                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.5)),
                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.83)));
        userLocationView.getPin().setIcon(ImageProvider.fromResource(
                getContext(), R.drawable.user_arrow));
        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                getContext(), R.drawable.user_arrow));
        userLocationView.getAccuracyCircle().setFillColor(Color.GREEN);
    }

    @Override
    public void onObjectRemoved(UserLocationView userLocationView) {
    }

    @Override
    public void onObjectUpdated(UserLocationView userLocationView, ObjectEvent objectEvent) {
    }

    private boolean canDetermineLocation() {
        int perm = ActivityCompat.checkSelfPermission(MapFragment.this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    private void requestGeoPoint(String query) {
        searchSession = searchManager.submit(
                query,
                Geometry.fromPoint(START_LOCATION),
                SEARCH_OPTIONS,
               mSearchPointListener);
    }

    private void prepareSearchView() {
        svSearchAddress.setQueryHint(getString(R.string.search_view_hint));
        svSearchAddress.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fabCurrentLocation.show();
               requestGeoPoint(svSearchAddress.getQuery().toString());
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    fabCurrentLocation.show();
                    isUseSuggestions = true;
                    return false;
                }
                isUseSuggestions = true;
                return false;
            }
        });
    }

    private void moveCamera(Point point, float zoom) {
        mapView.getMap().move(
                new CameraPosition(point, zoom, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1),
                null);
    }

    private void subscribeLocationUpdate() {
        if (locationManager != null && myLocationListener != null) {
            locationManager.subscribeForLocationUpdates(DESIRED_ACCURACY, MINIMAL_TIME, MINIMAL_DISTANCE, USE_IN_BACKGROUND, myLocationListener);
        }
    }
    private void setupCenterPointOfScreen() {
        int actionBarHeight = calculateActionBarHeight();
        int mWidth = this.getResources().getDisplayMetrics().widthPixels;
        int mHeight = this.getResources().getDisplayMetrics().heightPixels;
        float x = mWidth / 2.f;
        float y = mHeight / 2.f - actionBarHeight;
        centerPointOfScreen = new ScreenPoint(x, y);
    }

    private int calculateActionBarHeight() {
        TypedValue tv = new TypedValue();
        return (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) ?
                TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics()) : 0;
    }
}