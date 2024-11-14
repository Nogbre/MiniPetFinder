package com.example.minipetfinder;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PetDao {
    @Insert
    void insert(PetEntity pet);  // Método para insertar una nueva mascota

    @Update
    void update(PetEntity pet);

    @Delete
    void delete(PetEntity pet);

    @Query("SELECT * FROM pets")
    LiveData<List<PetEntity>> getAllPets();

    @Query("SELECT * FROM pets WHERE id = :id LIMIT 1")
    LiveData<PetEntity> getPetById(int id);
}
