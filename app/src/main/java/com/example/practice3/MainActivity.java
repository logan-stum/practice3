package com.example.practice3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice3.adapter.ProductAdapter;
import com.example.practice3.db.DBHelper;
import com.example.practice3.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * The main activity of the application.
 * Displays a list of products and allows the user to select at least 3
 * before proceeding to the checkout/review screen.
 */
public class MainActivity extends AppCompatActivity {

    /** Intent extra key for the list of selected products. */
    public static final String EXTRA_SELECTED_PRODUCTS = "selected_products";
    
    /** Minimum number of products that must be selected to proceed. */
    private static final int MIN_SELECTION = 3;

    private ProductAdapter adapter;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.rvProducts);
        btnNext = findViewById(R.id.btnNext);

        // Load data from the SQLite database
        DBHelper dbHelper = new DBHelper(this);
        List<Product> products = dbHelper.getAllProducts();

        // Setup RecyclerView with the product adapter
        adapter = new ProductAdapter(this, products, true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Update button state based on selection count
        adapter.setOnSelectionChangedListener(selectedCount ->
                btnNext.setEnabled(selectedCount >= MIN_SELECTION));
        btnNext.setEnabled(false);

        // Handle next button click
        btnNext.setOnClickListener(v -> {
            List<Product> selected = adapter.getSelectedProducts();
            if (selected.size() < MIN_SELECTION) {
                Toast.makeText(this,
                        getString(R.string.select_at_least, MIN_SELECTION),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // Transition to SecondActivity passing the selected products
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra(EXTRA_SELECTED_PRODUCTS, new ArrayList<>(selected));
            startActivity(intent);
        });
    }
}
