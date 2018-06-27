package m.h.testapp;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

public class MapActivity extends AppCompatActivity implements UserLocationObjectListener {

    ImageButton imgbtngps,imgbtnpolygon,imgbtnrefresh;
    private MapView mapview;
    private final String MAPKIT_API_KEY = "8dd7a633-9374-44dc-871b-b0223887d9af";
    private UserLocationLayer userLocationLayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_map);


        mapview = (MapView)findViewById(R.id.mapview);
//        mapview.getMap().move(
//                new CameraPosition(new Point(40.409262, 49.867092), 11.0f, 0.0f, 0.0f),
//                new Animation(Animation.Type.SMOOTH, 0),
//                null);

        userlocation();
        imgbtngps=findViewById(R.id.imgbtngps);
        imgbtngps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLocationLayer.isAutoZoomEnabled();

            }
        });

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
        userLocationLayer = mapview.getMap().getUserLocationLayer();
        userLocationLayer.setEnabled(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);
        userLocationLayer.setAutoZoomEnabled(true);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mapview.onStop();
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
}
