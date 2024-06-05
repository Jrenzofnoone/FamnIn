package com.example.farmin;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Locale;

public class fragmentOverview extends Fragment {
    private viewHolderDisplay viewHolderDisplay;
    private EditText etName,etCount,etNote;
    private ImageView ivEdit, ivVoiceNotes, ivVoiceDescrip;
    private Boolean editState = false;
    private String csKey, key, stringUrl, stringQr;
    private TextToSpeech t1;
    private Spinner spinStatus, spinType;
    private String[] items;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        t1 = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                }
            }
        });
        ivVoiceNotes = rootView.findViewById(R.id.ivVoiceNotes);
        ivVoiceDescrip = rootView.findViewById(R.id.ivVoiceDescrip);
        viewHolderDisplay = new ViewModelProvider(requireActivity()).get(viewHolderDisplay.class);
        ivVoiceNotes = rootView.findViewById(R.id.ivVoiceNotes);
        ivVoiceDescrip = rootView.findViewById(R.id.ivVoiceDescrip);
        ivEdit = rootView.findViewById(R.id.ivEdit);
        etName = rootView.findViewById(R.id.etName);
        spinType = rootView.findViewById(R.id.spinType);
        spinStatus = rootView.findViewById(R.id.spinStatus);
        etCount = rootView.findViewById(R.id.etCount);
        etNote = rootView.findViewById(R.id.etNote);
        String[] statusitems = getResources().getStringArray(R.array.status);
        int statusindex = Arrays.asList(statusitems).indexOf(statusitems);
        if(viewHolderDisplay.getCsType().equals("Crops")) {
            items= getResources().getStringArray(R.array.typeCrop);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_text, items);
            spinType.setAdapter(adapter);
        } else if (viewHolderDisplay.getCsType().equals("Seeds")) {
            items= getResources().getStringArray(R.array.typeSeed);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_text, items);
            spinType.setAdapter(adapter);
        }
        ArrayAdapter<String> adapterstats = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_text, statusitems);
        spinStatus.setAdapter(adapterstats);

        int index = Arrays.asList(items).indexOf(items);
        spinStatus.setSelection(statusindex);
        spinType.setSelection(index);
        etName.setText(viewHolderDisplay.getName());
        etCount.setText(viewHolderDisplay.getCount());
        etNote.setText(viewHolderDisplay.getNotes());
        csKey = viewHolderDisplay.getCsType();
        key = viewHolderDisplay.getKey();
        stringQr = viewHolderDisplay.getQrcode();
        stringUrl = viewHolderDisplay.getImageurl();
        etName.setEnabled(false);
        etCount.setEnabled(false);
        etNote.setEnabled(false);
        spinStatus.setEnabled(false);
        spinType.setEnabled(false);
        ivEdit.setOnClickListener(view -> {
            if(editState == false) {
                etName.setEnabled(true);
                etCount.setEnabled(true);
                spinType.setEnabled(true);
                etNote.setEnabled(true);
                spinStatus.setEnabled(true);
                ivEdit.setImageResource(R.drawable.check);
                editState = true;
                displayingInterface addingInterface = (displayingInterface) getActivity();
                addingInterface.setOnclick("false");
            } else {
                etName.setText(viewHolderDisplay.getName());
                etCount.setText(viewHolderDisplay.getCount());
                etNote.setText(viewHolderDisplay.getNotes());
                csKey = viewHolderDisplay.getCsType();
                key = viewHolderDisplay.getKey();
                stringQr = viewHolderDisplay.getQrcode();
                stringUrl = viewHolderDisplay.getImageurl();
                update(csKey, key, etName.getText().toString(), spinType.getSelectedItem().toString(),spinStatus.getSelectedItem().toString(), etCount.getText().toString(), etNote.getText().toString(), stringUrl, stringQr);
                editState = false;
                etName.setEnabled(false);
                etCount.setEnabled(false);
                spinType.setEnabled(false);
                etNote.setEnabled(false);
                spinStatus.setEnabled(false);
                ivEdit.setImageResource(R.drawable.edit);
                displayingInterface addingInterface = (displayingInterface) getActivity();
                addingInterface.setOnclick("true");
            }
        });
        ivVoiceNotes.setOnClickListener(view -> {
            String voice = etNote.getText().toString();
            t1.speak(voice, TextToSpeech.QUEUE_FLUSH, null);
        });
        ivVoiceDescrip.setOnClickListener(view -> {
            String voice = spinType.getSelectedItem().toString();
            t1.speak(voice, TextToSpeech.QUEUE_FLUSH, null);
        });
        return rootView;
    }
    private void update(String csType, String key, String name, String type,String status, String descrip, String notes, String stringUrl, String stringQr) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference(csType);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        addingUploads addingUploads = new addingUploads(csType, key, user.getEmail(), name, type, status, descrip, notes, stringUrl, stringQr);
        productRef.child(key).setValue(addingUploads).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity(), "Successfully Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }


}