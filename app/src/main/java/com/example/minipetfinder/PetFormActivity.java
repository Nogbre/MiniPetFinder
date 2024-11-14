package com.example.minipetfinder;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class PetFormActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_form);

        int petId = getIntent().getIntExtra("petId", -1);

        if (savedInstanceState == null) {
            PetFormFragment fragment = new PetFormFragment();
            if (petId != -1) {
                Bundle args = new Bundle();
                args.putInt("petId", petId);
                fragment.setArguments(args);
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
