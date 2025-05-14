package com.example.movie_ticket_app.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_ticket_app.R;
import com.example.movie_ticket_app.data.model.ShowCase;
import com.example.movie_ticket_app.dto.movie.response.MovieDetailResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShowTimesAdapter extends RecyclerView.Adapter<ShowTimesAdapter.ShowTimeViewHolder> {

    private List<ShowCase> showTimes;
    private SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public ShowTimesAdapter(List<ShowCase> showTimes) {
        this.showTimes = showTimes;
    }

    @NonNull
    @Override
    public ShowTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_show_time, parent, false);
        return new ShowTimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowTimeViewHolder holder, int position) {
        ShowCase showTime = showTimes.get(position);

        try {
            // Parse date and time
            Date startDate = inputFormat.parse(showTime.getShowStartTime());
            Date endDate = inputFormat.parse(showTime.getShowEndTime());

            // Format for display
            String date = dateFormat.format(startDate);
            String startTime = timeFormat.format(startDate);
            String endTime = timeFormat.format(endDate);

            // Set cinema info
            holder.cinemaName.setText(showTime.getCinemaHall().getCinema().getCinemaName());
            holder.cinemaAddress.setText(showTime.getCinemaHall().getCinema().getCinemaStreet() + ", " +
                    showTime.getCinemaHall().getCinema().getCinemaWard() + ", " +
                    showTime.getCinemaHall().getCinema().getCinemaDistrict() + ", " +
                    showTime.getCinemaHall().getCinema().getCinemaProvince());

            // Set hall and time info
            holder.hallName.setText(showTime.getCinemaHall().getCinemaHallName());
            holder.showDate.setText(date);
            holder.showTime.setText(startTime + " - " + endTime);

            // Set click listener for booking button
            holder.bookButton.setOnClickListener(v -> {
                // Handle booking action
                // You would typically navigate to a booking screen here
            });

        } catch (ParseException e) {
            e.printStackTrace();
            // Fallback if date parsing fails
            holder.cinemaName.setText(showTime.getCinemaHall().getCinema().getCinemaName());
            holder.showTime.setText(showTime.getShowStartTime());
        }
    }

    @Override
    public int getItemCount() {
        return showTimes.size();
    }

    static class ShowTimeViewHolder extends RecyclerView.ViewHolder {
        TextView cinemaName;
        TextView cinemaAddress;
        TextView hallName;
        TextView showDate;
        TextView showTime;
        Button bookButton;

        public ShowTimeViewHolder(@NonNull View itemView) {
            super(itemView);
            cinemaName = itemView.findViewById(R.id.cinemaName);
            cinemaAddress = itemView.findViewById(R.id.cinemaAddress);
            hallName = itemView.findViewById(R.id.hallName);
            showDate = itemView.findViewById(R.id.showDate);
            showTime = itemView.findViewById(R.id.showTime);
            bookButton = itemView.findViewById(R.id.bookButton);
        }
    }
}