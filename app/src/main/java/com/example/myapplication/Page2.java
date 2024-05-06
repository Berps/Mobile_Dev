package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Page2 extends Fragment {
    Button scanButton;
    TextView qrCodeInfo;
    private NotificationHelper notificationHelper;

    public Page2() {
        // Required empty public constructor
    }

    public interface NotificationHelper {
        void sendNotification(String message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NotificationHelper) {
            notificationHelper = (NotificationHelper) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement NotificationHelper");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page2, container, false);

        scanButton = view.findViewById(R.id.scanButton);
        qrCodeInfo = view.findViewById(R.id.qrCodeInfo);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(Page2.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan a QR Code or Barcode");
                integrator.initiateScan();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String scanResult = result.getContents();
            qrCodeInfo.setText("Scan Result: " + scanResult);
            notificationHelper.sendNotification("Item scanned: " + scanResult);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
