package com.example.farmin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class fragmentOverview extends Fragment {
    private viewHolderDisplay viewHolderDisplay;
    private EditText etName,etType,etDescrip,etNote;
    private ImageView ivEdit, ivVoiceNotes, ivVoiceDescrip;
    private Boolean editState = false;
    private String csKey, key, stringUrl, stringQr;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        viewHolderDisplay = new ViewModelProvider(requireActivity()).get(viewHolderDisplay.class);
        ivVoiceNotes = rootView.findViewById(R.id.ivVoiceNotes);
        ivVoiceDescrip = rootView.findViewById(R.id.ivVoiceDescrip);
        ivEdit = rootView.findViewById(R.id.ivEdit);
        etName = rootView.findViewById(R.id.etName);
        etType = rootView.findViewById(R.id.etType);
        etDescrip = rootView.findViewById(R.id.etDescrip);
        etNote = rootView.findViewById(R.id.etNote);
        etName.setText(viewHolderDisplay.getName());
        etDescrip.setText(viewHolderDisplay.getDescrip());
        etType.setText(viewHolderDisplay.getType());
        etNote.setText(viewHolderDisplay.getNotes());
        csKey = viewHolderDisplay.getCsType();
        key = viewHolderDisplay.getKey();
        stringQr = viewHolderDisplay.getQrcode();
        stringUrl = viewHolderDisplay.getImageurl();
        etName.setEnabled(false);
        etDescrip.setEnabled(false);
        etType.setEnabled(false);
        etNote.setEnabled(false);
        ivEdit.setOnClickListener(view -> {
            if(editState == false) {
                etName.setEnabled(true);
                etDescrip.setEnabled(true);
                etType.setEnabled(true);
                etNote.setEnabled(true);
                ivEdit.setImageResource(R.drawable.check);
                editState = true;
            } else {
                update(csKey, key, etName.getText().toString(), etType.getText().toString(), etDescrip.getText().toString(), etNote.getText().toString(), stringUrl, stringQr);
                editState = false;
                etName.setEnabled(false);
                etDescrip.setEnabled(false);
                etType.setEnabled(false);
                etNote.setEnabled(false);
                ivEdit.setImageResource(R.drawable.edit);
            }
        });
        return rootView;
    }
    private void update(String csType, String key, String name, String type, String descrip, String notes, String stringUrl, String stringQr) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference(csType);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        addingUploads addingUploads = new addingUploads(csType, key, user.getEmail(), name, type, descrip, notes, stringUrl, stringQr);
        productRef.child(key).setValue(addingUploads).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity(), "It works if this appear", Toast.LENGTH_SHORT).show();
            }
        });
    }
}