package com.example.movie_ticket_app.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_ticket_app.R;
import com.example.movie_ticket_app.data.model.FoodItem;


import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder> {

    private Context context;
    private List<FoodItem> foodItems;
    private OnFoodItemClickListener listener;
    private NumberFormat currencyFormatter;

    public interface OnFoodItemClickListener {
        void onIncreaseQuantity(FoodItem foodItem);
        void onDecreaseQuantity(FoodItem foodItem);
    }

    public FoodItemAdapter(Context context, List<FoodItem> foodItems, OnFoodItemClickListener listener) {
        this.context = context;
        this.foodItems = foodItems;
        this.listener = listener;
        this.currencyFormatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
    }

    @NonNull
    @Override
    public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new FoodItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);

        holder.tvFoodName.setText(foodItem.getName());
        holder.tvFoodDescription.setText(foodItem.getDescription());
        holder.tvFoodPrice.setText("Giá: " + currencyFormatter.format(foodItem.getPrice()) + " đ");
        holder.tvQuantity.setText(String.valueOf(foodItem.getQuantity()));

        // Set food image
        holder.ivFoodImage.setImageResource(foodItem.getImageResourceId());

        // Set click listeners
        holder.btnIncrease.setOnClickListener(v -> {
            listener.onIncreaseQuantity(foodItem);
            holder.tvQuantity.setText(String.valueOf(foodItem.getQuantity()));
        });

        holder.btnDecrease.setOnClickListener(v -> {
            listener.onDecreaseQuantity(foodItem);
            holder.tvQuantity.setText(String.valueOf(foodItem.getQuantity()));
        });
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public static class FoodItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoodImage;
        TextView tvFoodName, tvFoodDescription, tvFoodPrice, tvQuantity;
        ImageButton btnDecrease, btnIncrease;

        public FoodItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoodImage = itemView.findViewById(R.id.ivFoodImage);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvFoodDescription = itemView.findViewById(R.id.tvFoodDescription);
            tvFoodPrice = itemView.findViewById(R.id.tvFoodPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
        }
    }
}
