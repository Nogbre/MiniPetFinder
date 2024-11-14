package com.example.minipetfinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    private List<PetEntity> pets = new ArrayList<>();
    private OnItemClickListener itemClickListener;
    private OnEditClickListener editClickListener;
    private OnDeleteClickListener deleteClickListener;

    public PetAdapter(OnItemClickListener itemClickListener, OnEditClickListener editClickListener, OnDeleteClickListener deleteClickListener) {
        this.itemClickListener = itemClickListener;
        this.editClickListener = editClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pet, parent, false);
        return new PetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        PetEntity currentPet = pets.get(position);
        holder.textViewName.setText(currentPet.getName() == null ? "Sin nombre" : currentPet.getName());
        holder.textViewBreed.setText(currentPet.getBreed());
        holder.textViewSize.setText(currentPet.getSize());
        holder.bind(currentPet, itemClickListener, editClickListener, deleteClickListener);
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public void setPets(List<PetEntity> pets) {
        this.pets = pets;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(PetEntity pet);
    }

    public interface OnEditClickListener {
        void onEditClick(PetEntity pet);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(PetEntity pet);
    }

    class PetViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewBreed;
        private TextView textViewSize;
        private Button btnEdit, btnDelete;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewBreed = itemView.findViewById(R.id.text_view_breed);
            textViewSize = itemView.findViewById(R.id.text_view_size);
            btnEdit = itemView.findViewById(R.id.btn_edit_pet);
            btnDelete = itemView.findViewById(R.id.btn_delete_pet);
        }

        public void bind(final PetEntity pet, final OnItemClickListener itemListener, final OnEditClickListener editListener, final OnDeleteClickListener deleteListener) {
            itemView.setOnClickListener(v -> itemListener.onItemClick(pet));
            btnEdit.setOnClickListener(v -> editListener.onEditClick(pet));
            btnDelete.setOnClickListener(v -> deleteListener.onDeleteClick(pet));
        }
    }
}
