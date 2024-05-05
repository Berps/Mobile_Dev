package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;


public class Page1 extends Fragment {

    // Define the location details for Happy Hazmiyeh and Charcutier Aoun Baabda
    private static final double HAPPY_HAZMIYEH_LATITUDE = 33.8457;
    private static final double HAPPY_HAZMIYEH_LONGITUDE = 35.5845;
    private static final String HAPPY_HAZMIYEH_LABEL = "Happy Hazmiyeh";

    private static final double CHARCUTIER_AOUN_BAABDA_LATITUDE = 33.8266;
    private static final double CHARCUTIER_AOUN_BAABDA_LONGITUDE = 35.5484;
    private static final String CHARCUTIER_AOUN_BAABDA_LABEL = "Charcutier Aoun Baabda";

    public Page1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page1, container, false);

        // Find the buttons by their ids
        Button openHappyHazmiyehButton = rootView.findViewById(R.id.open_happy_hazmiyeh_button);
        Button openCharcutierAounBaabdaButton = rootView.findViewById(R.id.open_charcutier_aoun_baabda_button);

        // Set the OnClickListener for the Happy Hazmiyeh button
        openHappyHazmiyehButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLocationOnMaps(HAPPY_HAZMIYEH_LATITUDE, HAPPY_HAZMIYEH_LONGITUDE, HAPPY_HAZMIYEH_LABEL);
            }
        });

        // Set the OnClickListener for the Charcutier Aoun Baabda button
        openCharcutierAounBaabdaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLocationOnMaps(CHARCUTIER_AOUN_BAABDA_LATITUDE, CHARCUTIER_AOUN_BAABDA_LONGITUDE, CHARCUTIER_AOUN_BAABDA_LABEL);
            }
        });

        return rootView;
    }

    private void openLocationOnMaps(double latitude, double longitude, String label) {
        // Create the location string with latitude, longitude, and label
        String locationString = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(" + label + ")";

        // Create an Intent with the action to view the location on Google Maps
        Uri gmmIntentUri = Uri.parse(locationString);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

        // Set the package to ensure only the Google Maps app is opened
        mapIntent.setPackage("com.google.android.apps.maps");

        // Check if there is an activity to handle this intent
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Start the Google Maps activity
            startActivity(mapIntent);
        } else {
            // If there is no Google Maps app available, you can open the location in a web browser
            String webUrl = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
            startActivity(webIntent);
        }
    }
}
