package com.example.minipetfinder;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private TextView textViewName, textViewBreed, textViewSize, textViewPhone;
    private PetEntity pet;
    private int petId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textViewName = findViewById(R.id.text_view_name);
        textViewBreed = findViewById(R.id.text_view_breed);
        textViewSize = findViewById(R.id.text_view_size);
        textViewPhone = findViewById(R.id.text_view_phone);

        petId = getIntent().getIntExtra("petId", -1);

        // Iniciar la carga del mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        if (petId != -1) {
            // Obtenemos los datos de la mascota de la base de datos
            MiniPetDatabase.getInstance(this).petDao().getPetById(petId).observe(this, new Observer<PetEntity>() {
                @Override
                public void onChanged(PetEntity petEntity) {
                    pet = petEntity;
                    if (pet != null) {
                        displayPetInfo();
                        // Actualizar el marcador del mapa
                        updateMapWithPetLocation();
                    }
                }
            });
        }
    }

    private void displayPetInfo() {
        textViewName.setText(pet.getName() == null ? "Sin nombre" : pet.getName());
        textViewBreed.setText(pet.getBreed());
        textViewSize.setText(pet.getSize());
        textViewPhone.setText(pet.getPhoneNumber());
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        // Si los datos de la mascota ya están cargados, actualizamos el mapa con la ubicación
        if (pet != null) {
            updateMapWithPetLocation();
        }
    }

    private void updateMapWithPetLocation() {
        if (googleMap != null && pet != null) {
            LatLng petLocation = new LatLng(pet.getLatitude(), pet.getLongitude());
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(petLocation).title("Ubicación de la mascota"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(petLocation, 15));
        }
    }
}
