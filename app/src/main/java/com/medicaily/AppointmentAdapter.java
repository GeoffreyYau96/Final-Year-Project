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


public class AppointmentAdapter extends FirestoreRecyclerAdapter<Appointment,
        AppointmentAdapter.AppointmentHolder> {

    private OnItemClickListener listener;

    public AppointmentAdapter(@NonNull FirestoreRecyclerOptions<Appointment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AppointmentHolder holder, int position,
                                    @NonNull Appointment model) {
        holder.textViewAppointmentTitle.setText(model.getTitle());
        holder.textViewDate.setText(model.getDate());
        holder.textViewTime.setText(model.getTime());
        holder.textViewLocation.setText(model.getLocation());
    }

    @NonNull
    @Override
    public AppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item,
                parent,false);
        return new AppointmentHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class AppointmentHolder extends RecyclerView.ViewHolder {
        TextView textViewAppointmentTitle;
        TextView textViewDate;
        TextView textViewTime;
        TextView textViewLocation;


        public AppointmentHolder(@NonNull View itemView) {
            super(itemView);
            textViewAppointmentTitle = itemView.findViewById(R.id.text_view_appointment_title);
            textViewDate = itemView.findViewById(R.id.text_view_appointment_date);
            textViewTime = itemView.findViewById(R.id.text_view_appointment_time);
            textViewLocation = itemView.findViewById(R.id.text_view_appointment_location);

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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
