package com.example.encryptedchat.menu;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.encryptedchat.R;
import com.example.encryptedchat.adapter.AdapterTab;
import com.example.encryptedchat.databinding.FragmentTabBinding;
import com.example.encryptedchat.model.Tab;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatFragment extends Fragment {
    private FragmentTabBinding binding;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private FirebaseFirestore firestore;
    private AdapterTab adapter;
    private List<Tab> list;
    private ArrayList<String> daftarID;
    private Handler handler = new Handler();


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_tab, container, false);

        list = new ArrayList<>();
        daftarID = new ArrayList<>();
        adapter = new AdapterTab(list,getContext());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();

        if (firebaseUser !=null){
            daftarChat();
        }

        return binding.getRoot();
    }

    private void daftarChat() {
        reference.child("Daftar Chat").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                daftarID.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String ID = Objects.requireNonNull(snapshot.child("IDChat").getValue().toString());

                    daftarID.add(ID);
                }

                bacaAkun();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void bacaAkun() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (String id : daftarID){
                    firestore.collection("Akun").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String ID = documentSnapshot.getString("id");
                            Tab chat = new Tab(ID,
                                    documentSnapshot.getString("noTelp"),
                                    documentSnapshot.getString("nama"),
                                    documentSnapshot.getString("keterangan"),
                                    documentSnapshot.getString("tanggal"));

                            if (ID !=null && !ID.equals(firebaseUser.getUid())){
                                list.add(chat);
                            }

                            if (adapter !=null){
                                adapter.notifyItemInserted(0);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });
    }

}