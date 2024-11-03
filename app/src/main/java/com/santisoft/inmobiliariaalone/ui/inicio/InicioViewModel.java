package com.santisoft.inmobiliariaalone.ui.inicio;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class InicioViewModel extends AndroidViewModel {

    public InicioViewModel(@NonNull Application application) {
        super(application);
    }

    public class MapaActual implements OnMapReadyCallback {

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            // Coordenadas para San Luis, La Punta
            LatLng laPunta = new LatLng(-33.2950, -66.3356);
            googleMap.addMarker(new MarkerOptions()
                    .position(laPunta)
                    .title("San Luis, La Punta"));


            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(laPunta, 15));
        }
    }
}
