package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Page2 extends Fragment {
    private Button scanButton;
    private TextView qrCodeInfo;
    private NotificationHelper notificationHelper;
    private FirebaseFirestore db;

    public Page2() {
        // Required empty public constructor
    }

    public interface NotificationHelper {
        void sendNotification(String message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        db = FirebaseFirestore.getInstance();
        if (context instanceof NotificationHelper) {
            notificationHelper = (NotificationHelper) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement NotificationHelper");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page2, container, false);
        scanButton = view.findViewById(R.id.scanButton);
        qrCodeInfo = view.findViewById(R.id.qrCodeInfo);
        scanButton.setOnClickListener(v -> {
            IntentIntegrator integrator = IntentIntegrator.forSupportFragment(Page2.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scan a QR Code or Barcode");
            integrator.initiateScan();
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String scanResult = result.getContents();
            qrCodeInfo.setText("Scan Result: " + scanResult);
            addProductToUser(scanResult);
            notificationHelper.sendNotification("Item scanned: " + scanResult);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void addProductToUser(String productId) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("userid", "addProductToUser: "+userId);
        db.collection("ProductID").document(productId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                db.collection("users").document(userId)
                        .update("productIDs", FieldValue.arrayUnion(productId))
                        .addOnSuccessListener(aVoid -> qrCodeInfo.append("\nProduct added to your list!"))
                        .addOnFailureListener(e -> qrCodeInfo.append("\nFailed to add product to your list."));
            } else {
                qrCodeInfo.append("\nNo such product found!");
            }
        });
    }
}
