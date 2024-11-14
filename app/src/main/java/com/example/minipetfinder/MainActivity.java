package com.example.minipetfinder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements PetAdapter.OnItemClickListener, PetAdapter.OnEditClickListener, PetAdapter.OnDeleteClickListener {

    private PetAdapter adapter;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private PetDao petDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        petDao = MiniPetDatabase.getInstance(this).petDao();
        adapter = new PetAdapter(this, this, this);
        recyclerView.setAdapter(adapter);

        // Observa los cambios en la lista de mascotas y actualiza el RecyclerView
        petDao.getAllPets().observe(this, new Observer<List<PetEntity>>() {
            @Override
            public void onChanged(List<PetEntity> pets) {
                adapter.setPets(pets);
            }
        });

        FloatingActionButton fabAddPet = findViewById(R.id.fab_add_pet);
        fabAddPet.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PetFormActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onItemClick(PetEntity pet) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("petId", pet.getId());
        startActivity(intent);
    }

    @Override
    public void onEditClick(PetEntity pet) {
        Intent intent = new Intent(this, PetFormActivity.class);
        intent.putExtra("petId", pet.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(PetEntity pet) {
        executor.execute(() -> {
            petDao.delete(pet);
            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, "Mascota eliminada", Toast.LENGTH_SHORT).show();
            });
        });
    }
}
