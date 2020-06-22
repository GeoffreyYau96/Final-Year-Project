package com.medicaily;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


public class MedicationAdapter extends FirestoreRecyclerAdapter<Medication,
        MedicationAdapter.MedicationHolder> {

    private OnItemClickListener listener;

    public MedicationAdapter(@NonNull FirestoreRecyclerOptions<Medication> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MedicationHolder holder, int position, @NonNull Medication model) {
        holder.textViewName.setText(model.getName());
        holder.textViewDate.setText(model.getEnd());
        holder.textViewRemaining.setText(model.getRemain());
        holder.textViewInterval.setText(model.getInterval());
        holder.textViewDosage.setText(model.getDosage());
        holder.textViewType.setText(model.getType());
    }

    @NonNull
    @Override
    public MedicationHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.medication_item,
                parent,false);
        return new MedicationHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class MedicationHolder extends RecyclerView.ViewHolder {
         TextView textViewName;
         TextView textViewDate;
         TextView textViewRemaining;
         TextView textViewInterval;
         TextView textViewDosage;
         TextView textViewType;

         public MedicationHolder(@NonNull View itemView) {
             super(itemView);
             textViewName = itemView.findViewById(R.id.text_view_medicine_name);
             textViewDate = itemView.findViewById(R.id.text_view_medicine_date);
             textViewRemaining =itemView.findViewById(R.id.text_view_medicine_remaining);
             textViewInterval =itemView.findViewById(R.id.text_view_medicine_interval);
             textViewDosage=itemView.findViewById(R.id.text_view_medicine_dosage);
             textViewType=itemView.findViewById(R.id.text_view_medicine_type);

             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     int position = getAdapterPosition();
                     if (position!= RecyclerView.NO_POSITION && listener !=null) {
                         listener.onItemClick(getSnapshots().getSnapshot(position),position);
                     }
                 }
             });
         }
     }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(MedicationAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
