package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class Page3 extends Fragment {

    public Page3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page3, container, false);
        TextView textView = view.findViewById(R.id.textViewEmail);

        SharedPreferences preferences = getActivity().getSharedPreferences("AppPrefs", getActivity().MODE_PRIVATE);
        String email = preferences.getString("email", "No Email Found");
        textView.setText("Welcome " + email);

        return view;
    }
}
