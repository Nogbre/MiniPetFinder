package com.example.minipetfinder;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PetEntity.class}, version = 2)  // Incrementa el número de versión aquí
public abstract class MiniPetDatabase extends RoomDatabase {

    public abstract PetDao petDao();

    private static volatile MiniPetDatabase INSTANCE;

    public static MiniPetDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MiniPetDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MiniPetDatabase.class, "mini_pet_database")
                            .fallbackToDestructiveMigration()  // Opción para desarrollo
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
