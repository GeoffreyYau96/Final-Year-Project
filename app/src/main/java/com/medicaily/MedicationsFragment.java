package com.medicaily;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static com.medicaily.MainActivity.Appointment_ID;
import static com.medicaily.MainActivity.Medication_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class MedicationsFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference medicationRef = db.collection("medication");

    private MedicationAdapter adapter;


    public MedicationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_medications, container, false);

        Query query = medicationRef.whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirestoreRecyclerOptions<Medication> options = new FirestoreRecyclerOptions.Builder<Medication>()
                .setQuery(query, Medication.class)
                .build();

        adapter = new MedicationAdapter(options);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view_medication);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new MedicationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Medication medication = documentSnapshot.toObject(Medication.class);
                String id = documentSnapshot.getId();

                Toast.makeText(getActivity(), "Position: " + position + "ID: " + id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ViewMedication.class);
                intent.putExtra(Medication_ID, id);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}

