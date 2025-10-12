package com.santisoft.inmobiliariaalone.ui.inicio;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class InicioViewModel extends AndroidViewModel {

    public InicioViewModel(@NonNull Application application) {
        super(application);
    }

    public class MapaActual implements OnMapReadyCallback {

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            configurarMapa(googleMap);
        }

        private void configurarMapa(GoogleMap googleMap) {
            // 🌍 Tipo de mapa: híbrido (satélite + nombres de calles)
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setMapToolbarEnabled(false);

            // 📍 Coordenadas: Inmobiliaria Alone (La Punta, San Luis)
            LatLng villaAguadita = new LatLng(-33.209755, -66.300211);

            // 🏠 Marker personalizado
            googleMap.addMarker(new MarkerOptions()
                    .position(villaAguadita)
                    .title("🏠 Inmobiliaria Alone")
                    .snippet("Villa Aguadita – La Punta, San Luis")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(villaAguadita, 16f));
        }
    }
}
