package m.h.testapp.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.ScreenPoint;
import com.yandex.mapkit.geometry.BoundingBox;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.SearchType;
import com.yandex.mapkit.search.Session;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

import m.h.testapp.R;

public class MapActivity extends AppCompatActivity  implements UserLocationObjectListener {

    ImageButton imgbtngps,imgbtnpolygon,imgbtnrefresh;
    private MapView mapview;
    private final String MAPKIT_API_KEY = "8dd7a633-9374-44dc-871b-b0223887d9af";
    private UserLocationLayer userLocationLayer;
    public static final int PERMISON_ACCESS_FINE_LOCATION = 123;
    private static final String TAG = "MapFragment";


   // private static final String TAG = MapFragment.class.getSimpleName();
   // private Unbinder unbinder;
    private static final int PERMISSIONS_CODE = 109;

    private static final double DESIRED_ACCURACY = 0;
    private static final long MINIMAL_TIME = 0;
    private static final double MINIMAL_DISTANCE = 50;
    private static final boolean USE_IN_BACKGROUND = false;

    private Session.SearchListener mSearchAddressListener;
    private Session.SearchListener mSearchPointListener;
    private LocationListener myLocationListener;
    private SearchManager.SuggestListener suggestListener;
    private CameraListener cameraListener;

    public static final int COMFORTABLE_ZOOM_LEVEL = 18;//если уровень будет ниже, получить улицу с номером дома не получится
    private static final float START_ZOOM_LEVEL = 14.0f;

    private static final Point START_LOCATION = new Point(53.35, 83.76);
    private final double BOX_SIZE = 0.2;
    private final BoundingBox BOUNDING_BOX = new BoundingBox(
            new Point(START_LOCATION.getLatitude() - BOX_SIZE, START_LOCATION.getLongitude() - BOX_SIZE),
            new Point(START_LOCATION.getLatitude() + BOX_SIZE, START_LOCATION.getLongitude() + BOX_SIZE));
    private final int RESULT_NUMBER_LIMIT = 5;
    private final SearchOptions SEARCH_OPTIONS = new SearchOptions().setSearchTypes(
            SearchType.GEO.value);

    private Session searchSession;
    private SearchManager searchManager;

    private boolean isUseSuggestions;

    //private QueryHelper queryHelper;
    //private RegionHelper regionHelper;
    private Point myLocation;

    private ScreenPoint centerPointOfScreen;

    private LocationManager locationManager;
   // private SuggestAdapter suggestAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_map);


//        mapview = (MapView)findViewById(R.id.mapview);
//        mapview.getMap().move(
//                new CameraPosition(new Point(40.409262, 49.867092), 11.0f, 0.0f, 0.0f),
//                new Animation(Animation.Type.SMOOTH, 0),
//                null);
        mapview = (MapView) findViewById(R.id.mapview);
//        mapview.getMap().setRotateGesturesEnabled(false);
//        mapview.getMap().move(new CameraPosition(new Point(0, 0), 14, 0, 0));


            userlocation();
            imgbtngps=findViewById(R.id.imgbtngps);
            imgbtngps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (myLocation == null) {
                       // Toast.makeText(rootCoordinatorLayout, "yuklenir", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    moveCamera(myLocation, COMFORTABLE_ZOOM_LEVEL);

                }
            });


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
                   // Snackbar.make(rootCoordinatorLayout, R.string.error_cant_get_my_location, Snackbar.LENGTH_LONG).show();
                }
            }
        };


        //btn polygon-----------------------------------------------------------
        imgbtnrefresh=findViewById(R.id.imgbtnrefresh);
        imgbtnpolygon=findViewById(R.id.imgbtnpolygon);
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

    }

//user location---------------------------------

    void userlocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onMapReady: ");
          mapview.getMap().move(new CameraPosition(new Point(0, 0), 14, 0, 0));
        userLocationLayer =  mapview.getMap().getUserLocationLayer();
        userLocationLayer.setEnabled(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISON_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapview.onStop();
        locationManager.unsubscribe(myLocationListener);
        MapKitFactory.getInstance().onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapview.onStart();
        MapKitFactory.getInstance().onStart();
    }


    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
        userLocationLayer.setAnchor(
                new PointF((float)(mapview.getWidth() * 0.5), (float)(mapview.getHeight() * 0.5)),
                new PointF((float)(mapview.getWidth() * 0.5), (float)(mapview.getHeight() * 0.83)));

        userLocationView.getPin().setIcon(ImageProvider.fromResource(
                this, R.drawable.user_arrow));
        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                this, R.drawable.user_arrow));
        userLocationView.getAccuracyCircle().setFillColor(Color.GREEN);
    }

    @Override
    public void onObjectRemoved(UserLocationView userLocationView) {
    }

    @Override
    public void onObjectUpdated(UserLocationView userLocationView, ObjectEvent objectEvent) {
    }

    private void subscribeLocationUpdate() {
        if (locationManager != null && myLocationListener != null) {
            locationManager.subscribeForLocationUpdates(DESIRED_ACCURACY, MINIMAL_TIME, MINIMAL_DISTANCE, USE_IN_BACKGROUND, myLocationListener);
        }
    }

    private void moveCamera(Point point, float zoom) {
        mapview.getMap().move(
                new CameraPosition(point, zoom, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1),
                null);
    }
}
