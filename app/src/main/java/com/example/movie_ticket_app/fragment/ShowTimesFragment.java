package com.example.movie_ticket_app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_ticket_app.R;
import com.example.movie_ticket_app.adapter.ShowTimesAdapter;
import com.example.movie_ticket_app.data.model.ShowCase;

import java.util.ArrayList;
import java.util.List;

public class ShowTimesFragment extends Fragment {

    private List<ShowCase> showTimes = new ArrayList<>();
    private RecyclerView showTimesRecyclerView;
    private ShowTimesAdapter adapter;
    private TextView noShowTimesText;

    public ShowTimesFragment() {
        // Required empty public constructor
    }

    public void setShowTimes(List<ShowCase> showTimes) {
        if (showTimes != null) {
            this.showTimes.clear();
            this.showTimes.addAll(showTimes);

            // Update UI if the view has been created
            if (adapter != null) {
                adapter.notifyDataSetChanged();
                updateNoShowTimesVisibility();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_times, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showTimesRecyclerView = view.findViewById(R.id.showTimesRecyclerView);
        noShowTimesText = view.findViewById(R.id.noShowTimesText);

        // Set up RecyclerView
        showTimesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ShowTimesAdapter(getContext(),showTimes);
        showTimesRecyclerView.setAdapter(adapter);

        updateNoShowTimesVisibility();
    }

    private void updateNoShowTimesVisibility() {
        if (noShowTimesText != null) {
            noShowTimesText.setVisibility(showTimes.isEmpty() ? View.VISIBLE : View.GONE);
        }

        if (showTimesRecyclerView != null) {
            showTimesRecyclerView.setVisibility(showTimes.isEmpty() ? View.GONE : View.VISIBLE);
        }
    }
}
