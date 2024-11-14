package com.example.minipetfinder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PetFormFragment extends Fragment implements OnMapReadyCallback {

    private EditText inputName, inputPhone;
    private Spinner spinnerBreed, spinnerSize;
    private PetDao petDao;
    private PetEntity pet;
    private GoogleMap googleMap;
    private Marker selectedMarker;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_form, container, false);

        inputName = view.findViewById(R.id.edit_text_name);
        inputPhone = view.findViewById(R.id.edit_text_phone);
        spinnerBreed = view.findViewById(R.id.spinner_breed);
        spinnerSize = view.findViewById(R.id.spinner_size);

        petDao = MiniPetDatabase.getInstance(requireContext()).petDao();

        ArrayAdapter<CharSequence> breedAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.pet_breeds, android.R.layout.simple_spinner_item);
        breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBreed.setAdapter(breedAdapter);

        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.pet_sizes, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(sizeAdapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        int petId = getArguments() != null ? getArguments().getInt("petId", -1) : -1;
        if (petId != -1) {
            petDao.getPetById(petId).observe(getViewLifecycleOwner(), new Observer<PetEntity>() {
                @Override
                public void onChanged(PetEntity petEntity) {
                    pet = petEntity;
                    loadPetData();
                }
            });
        }

        view.findViewById(R.id.save_pet_button).setOnClickListener(v -> savePet());
        return view;
    }

    private void loadPetData() {
        inputName.setText(pet.getName());
        inputPhone.setText(pet.getPhoneNumber());
        selectSpinnerItemByValue(spinnerBreed, pet.getBreed());
        selectSpinnerItemByValue(spinnerSize, pet.getSize());

        if (googleMap != null) {
            LatLng petLocation = new LatLng(pet.getLatitude(), pet.getLongitude());
            if (selectedMarker != null) selectedMarker.remove();
            selectedMarker = googleMap.addMarker(new MarkerOptions().position(petLocation).title("Ubicación de la mascota"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(petLocation, 15));
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Listener para seleccionar una ubicación en el mapa
        googleMap.setOnMapClickListener(latLng -> {
            if (selectedMarker != null) {
                selectedMarker.remove();
            }
            selectedMarker = googleMap.addMarker(new MarkerOptions().position(latLng).title("Nueva Ubicación"));

            // Inicializar `pet` si es nulo (para nuevas mascotas) y asignar la latitud y longitud
            if (pet == null) {
                pet = new PetEntity();
            }
            pet.setLatitude(latLng.latitude);
            pet.setLongitude(latLng.longitude);
        });

        // Si estamos editando una mascota existente, cargar la ubicación
        if (pet != null) {
            loadPetData();
        }
    }

    private void savePet() {
        if (pet == null) {
            // Crear una nueva mascota si `pet` es nulo
            pet = new PetEntity();
        }

        pet.setName(inputName.getText().toString().trim());
        pet.setPhoneNumber(inputPhone.getText().toString().trim());
        pet.setBreed(spinnerBreed.getSelectedItem().toString());
        pet.setSize(spinnerSize.getSelectedItem().toString());

        // Verificar que la ubicación esté seleccionada
        if (selectedMarker != null) {
            pet.setLatitude(selectedMarker.getPosition().latitude);
            pet.setLongitude(selectedMarker.getPosition().longitude);
        } else {
            Toast.makeText(getContext(), "Por favor, selecciona una ubicación en el mapa.", Toast.LENGTH_SHORT).show();
            return;
        }

        executor.execute(() -> {
            if (pet.getId() == 0) {
                // Si es una nueva mascota, usa insert
                petDao.insert(pet);
            } else {
                // Si es una mascota existente, usa update
                petDao.update(pet);
            }
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Mascota guardada", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            });
        });
    }

    private void selectSpinnerItemByValue(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++) {
            if (adapter.getItem(position).toString().equals(value)) {
                spinner.setSelection(position);
                return;
            }
        }
    }
}
