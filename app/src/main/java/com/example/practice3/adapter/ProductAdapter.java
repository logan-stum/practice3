package com.example.practice3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice3.R;
import com.example.practice3.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Single RecyclerView adapter reused by both activities.
 * - In MainActivity (showCheckbox = true) the user can tap rows to select products.
 * - In SecondActivity (showCheckbox = false) the list is read-only, just for review.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    /** Interface definition for a callback to be invoked when the selection count changes. */
    public interface OnSelectionChangedListener {
        /**
         * Called when a product is selected or deselected.
         * @param selectedCount The current number of selected products.
         */
        void onSelectionChanged(int selectedCount);
    }

    private final Context context;
    private final List<Product> productList;
    private final List<Product> selectedProducts = new ArrayList<>();
    private final boolean showCheckbox;
    private OnSelectionChangedListener listener;

    /**
     * Constructs a new ProductAdapter.
     *
     * @param context      The context of the calling activity.
     * @param productList  The list of products to display.
     * @param showCheckbox Whether to display a checkbox for selection.
     */
    public ProductAdapter(Context context, List<Product> productList, boolean showCheckbox) {
        this.context = context;
        this.productList = productList;
        this.showCheckbox = showCheckbox;
    }

    /**
     * Sets the listener for selection changes.
     * @param listener The listener to notify.
     */
    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.listener = listener;
    }

    /**
     * @return The list of currently selected products.
     */
    public List<Product> getSelectedProducts() {
        return selectedProducts;
    }

    /** Clears all items from the list (used by SecondActivity after a successful email send). */
    public void clearAll() {
        productList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.name.setText(product.getName());
        holder.description.setText(product.getDescription());
        holder.seller.setText(context.getString(R.string.seller_label, product.getSeller()));
        holder.price.setText(String.format(Locale.US, "$%.2f", product.getPrice()));
        int imageId = context.getResources().getIdentifier(
                product.getImageResName(),
                "drawable",
                context.getPackageName()
        );

        if (imageId != 0) {
            holder.image.setImageResource(imageId);
        } else {
            holder.image.setImageResource(R.drawable.ic_product_placeholder);
        }
        if (showCheckbox) {
            holder.checkBox.setVisibility(View.VISIBLE);
            // Avoid stale listener firing during view recycling
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(selectedProducts.contains(product));
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (!selectedProducts.contains(product)) {
                        selectedProducts.add(product);
                    }
                } else {
                    selectedProducts.remove(product);
                }
                if (listener != null) {
                    listener.onSelectionChanged(selectedProducts.size());
                }
            });
            holder.itemView.setOnClickListener(v -> holder.checkBox.toggle());
        } else {
            holder.checkBox.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    /**
     * ViewHolder for product items, containing UI references.
     */
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, seller, price;
        ImageView image;
        CheckBox checkBox;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            description = itemView.findViewById(R.id.tvDescription);
            seller = itemView.findViewById(R.id.tvSeller);
            price = itemView.findViewById(R.id.tvPrice);
            image = itemView.findViewById(R.id.ivProduct);
            checkBox = itemView.findViewById(R.id.cbSelect);
        }
    }
}
