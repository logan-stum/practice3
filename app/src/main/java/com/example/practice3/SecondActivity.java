package com.example.practice3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice3.adapter.ProductAdapter;
import com.example.practice3.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The second activity of the application.
 * Displays the list of products selected in MainActivity and provides
 * functionality to send this list via email.
 */
public class SecondActivity extends AppCompatActivity {

    /** Hardcoded recipient email address for the product list. */
    private static final String RECIPIENT_EMAIL = "sweng888mobileapps@gmail.com";

    private ProductAdapter adapter;
    private List<Product> selectedProducts;

    /**
     * ActivityResultLauncher to handle the email application intent.
     * When the user returns from the email app, it shows a confirmation toast
     * and clears the list.
     */
    private final ActivityResultLauncher<Intent> emailLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                // Most email clients return RESULT_CANCELED even after a successful send,
                // so we treat "user returned to the app" as confirmation the intent was handled.
                Toast.makeText(SecondActivity.this,
                        R.string.email_sent_toast, Toast.LENGTH_LONG).show();
                adapter.clearAll();
            });

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button btnSendEmail = findViewById(R.id.btnSendEmail);
        RecyclerView recyclerView = findViewById(R.id.rvSelectedProducts);

        // Retrieve the selected products passed from MainActivity
        Intent incoming = getIntent();
        List<Product> received = (List<Product>) incoming.getSerializableExtra(MainActivity.EXTRA_SELECTED_PRODUCTS);
        selectedProducts = received != null ? new ArrayList<>(received) : new ArrayList<>();

        // Initialize the adapter in read-only mode (no checkboxes)
        adapter = new ProductAdapter(this, selectedProducts, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Handle send email button click
        btnSendEmail.setOnClickListener(v -> sendEmail());
    }

    /**
     * Constructs and sends an email with the details of the selected products.
     */
    private void sendEmail() {
        if (selectedProducts.isEmpty()) {
            Toast.makeText(this, R.string.no_products_to_send, Toast.LENGTH_SHORT).show();
            return;
        }

        // Build the email body with product details
        StringBuilder body = new StringBuilder();
        body.append("Selected Products:\n\n");
        for (Product p : selectedProducts) {
            body.append("Name: ").append(p.getName()).append("\n");
            body.append("Description: ").append(p.getDescription()).append("\n");
            body.append("Seller: ").append(p.getSeller()).append("\n");
            body.append(String.format(Locale.US, "Price: $%.2f", p.getPrice())).append("\n");
            body.append("---------------------------\n");
        }

        // Configure the email intent
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // limit to email apps
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{RECIPIENT_EMAIL});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, body.toString());

        try {
            // Launch the email app chooser
            emailLauncher.launch(Intent.createChooser(emailIntent, getString(R.string.send_email_chooser_title)));
        } catch (Exception e) {
            Toast.makeText(this, R.string.no_email_app_found, Toast.LENGTH_SHORT).show();
        }
    }
}
