package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Page3 extends Fragment {

    private ListView listViewProducts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page3, container, false);
        TextView textViewEmail = view.findViewById(R.id.textViewEmail);
        listViewProducts = view.findViewById(R.id.listViewProducts);

        SharedPreferences preferences = getActivity().getSharedPreferences("AppPrefs", getActivity().MODE_PRIVATE);
        String email = preferences.getString("email", "No Email Found");
        if (email.contains("@")) {
            email = email.substring(0, email.indexOf("@"));
        }
        textViewEmail.setText("Welcome " + email);

        loadProducts();
        return view;
    }

    private void loadProducts() {
        List<Product> products = new ArrayList<>();
        // Mock data, replace this with your actual Firestore data fetch logic
        products.add(new Product("Beurre", "Happy", "2024-12-31"));
        products.add(new Product("Biscuit", "Charcutier", "2024-05-01"));
        products.add(new Product("TutiFruity", "Metro", "2024-05-08"));

        ProductAdapter adapter = new ProductAdapter(getActivity(), products);
        listViewProducts.setAdapter(adapter);
    }

    class ProductAdapter extends ArrayAdapter<Product> {
        public ProductAdapter(Context context, List<Product> products) {
            super(context, 0, products);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_item, parent, false);
            }
            TextView textViewProductName = convertView.findViewById(R.id.textViewProductName);
            TextView textViewProductExpiry = convertView.findViewById(R.id.textViewProductExpiry);

            Product product = getItem(position);
            Log.d("branches", "getView: "+getItem(position).getBranch());

            textViewProductName.setText(product.getName() + " - " + product.getBranch());
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date expiryDate = sdf.parse(product.getExpiryDate());
                Date today = new Date();
                if (expiryDate.before(today)) {
                    textViewProductExpiry.setTextColor(Color.RED);
                } else if ((expiryDate.getTime() - today.getTime()) <= (1000 * 60 * 60 * 24 * 7)) {
                    textViewProductExpiry.setTextColor(Color.YELLOW);
                } else {
                    textViewProductExpiry.setTextColor(Color.BLACK);
                }
            } catch (Exception e) {
                textViewProductExpiry.setTextColor(Color.BLACK);
                e.printStackTrace();
            }
            textViewProductExpiry.setText("Expires on: " + product.getExpiryDate());

            return convertView;
        }
    }
}
