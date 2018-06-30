package m.h.testapp.map;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.yandex.mapkit.MapKitFactory;

import butterknife.ButterKnife;
import m.h.testapp.R;
public class MapActivity extends AppCompatActivity {

    private final String MAPKIT_API_KEY = "8dd7a633-9374-44dc-871b-b0223887d9af";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);

        MapFragment mapFragment = new MapFragment();

        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.content_frame, mapFragment);
        fTrans.commit();
    }


}
