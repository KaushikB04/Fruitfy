package com.example.fruitify_offcial;

import android.widget.Filter;


import com.example.fruitify_offcial.Adapter.ProductAdapter;
import com.example.fruitify_offcial.models.Product;

import java.util.ArrayList;
import java.util.Locale;

public class FilteringProducts extends Filter {
    private final ArrayList<Product> originalList;
    private final ProductAdapter adapter;

    public FilteringProducts(ProductAdapter productAdapter, ArrayList<Product> originalList) {
        this.adapter = productAdapter;
        this.originalList = originalList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        ArrayList<Product> filteredList = new ArrayList<>();

        if (constraint != null) {
            String[] query = constraint.toString().trim().toLowerCase(Locale.getDefault()).split(" ");

            for (Product product : originalList) {
                boolean matches = false;
                for (String term : query) {
                    if (product.getProductTitle().toLowerCase(Locale.getDefault()).contains(term) ||
                            product.getProductCategory().toLowerCase(Locale.getDefault()).contains(term)) {
                        matches = true;
                        break;
                    }
                }

                if (matches) {
                    filteredList.add(product);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        // Correct casting
        adapter.submitList((ArrayList<Product>) results.values);
    }
}
