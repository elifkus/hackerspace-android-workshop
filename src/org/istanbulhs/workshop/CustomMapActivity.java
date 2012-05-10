package org.istanbulhs.workshop;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class CustomMapActivity extends MapActivity {

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        //Arayuzun tanimlandigi xml'i yukluyoruz
        setContentView(R.layout.mapview);  
        
        //Sayfanin altindaki zoom controlleri gostertiyoruz
        //Cok da gerekli degil aslinda
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
    }

    //Implemente edilmesi zorunlu metod 
    @Override
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }

}
