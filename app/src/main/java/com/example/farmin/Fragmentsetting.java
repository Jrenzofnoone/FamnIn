package com.example.farmin;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class Fragmentsetting extends Fragment {
    private TextView tvShare, tvSupport, tvAccount, tvContact, tvLanguage;
    private Dialog supportDialog, contactsDialog;
    private TextInputEditText etMessage;
    private AppCompatButton btnReset, btnCancel;
    //    private int REQUEST_CODE_ASK_PERMISSIONS = 123;
//    private String[] permissions = new String[]{Manifest.permission.SEND_SMS};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fragmentsetting, container, false);
        Display display = requireActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        supportDialog = new Dialog(getActivity());
        supportDialog.setContentView(R.layout.dialog_support);
        supportDialog.setCancelable(true);
        contactsDialog = new Dialog(getActivity());
        contactsDialog.setContentView(R.layout.dailog_contacts);
        contactsDialog.setCancelable(true);
        Window window = supportDialog.getWindow();
        Window contaceswindow = contactsDialog.getWindow();
        float dialogPercentageWidth = 0.8f;
        float dialogPercentageHeight = 0.4f;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(window.getAttributes());
        layoutParams.width = (int) (screenWidth * dialogPercentageWidth);
        layoutParams.height = (int) (screenHeight * dialogPercentageHeight);
        window.setAttributes(layoutParams);
        contaceswindow.setAttributes(layoutParams);
        supportDialog.getWindow().setBackgroundDrawable(requireActivity().getDrawable(R.drawable.custom_dialogbg));
        contactsDialog.getWindow().setBackgroundDrawable(requireActivity().getDrawable(R.drawable.custom_dialogbg));
        etMessage = supportDialog.findViewById(R.id.etMessage);
        btnCancel = supportDialog.findViewById(R.id.btnCancel);
        btnReset = supportDialog.findViewById(R.id.btnReset);
        tvShare = rootView.findViewById(R.id.tvShare);
        tvSupport = rootView.findViewById(R.id.tvSupport);
        tvAccount = rootView.findViewById(R.id.tvAccount);
        tvContact = rootView.findViewById(R.id.tvContact);
        tvLanguage = rootView.findViewById(R.id.tvLanguage);
        tvShare.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ShareUs.class);
            startActivity(intent);
        });
        tvSupport.setOnClickListener(view -> {
            supportDialog.show();
        });
        tvContact.setOnClickListener(view -> {
            contactsDialog.show();
        });
        btnReset.setOnClickListener(view -> {
//            if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
//            } else {
            // Permission granted, proceed with email sending
            String text = etMessage.getText().toString();
            sendEmail(text);
            supportDialog.dismiss();
//            }
        });
        tvAccount.setOnClickListener(view -> {
            // Start the DeleteAccountActivity
            Intent intent = new Intent(getActivity(), DeleteAccountActivity.class);
            startActivity(intent);
        });
        btnCancel.setOnClickListener(view -> {
            supportDialog.dismiss();
        });
        return rootView;
    }

    private void sendEmail(String text) {
        String[] recipients = new String[]{"janrenzbasilio@gmail.com"};
        String subject = "Support Service(Do not Delete)";
        String message = "Issue: " + text;

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.putExtra(Intent.EXTRA_TEXT, message);


        startActivity(Intent.createChooser(intent, "choose one application"));
    }
}