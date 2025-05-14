package com.example.movie_ticket_app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_ticket_app.R;

import java.util.ArrayList;
import java.util.List;

public class CastFragment extends Fragment {

    private RecyclerView castRecyclerView;
    private CastAdapter castAdapter;
    private List<String> castList = new ArrayList<>();

    public CastFragment() {
        // Required empty public constructor
    }

    public static CastFragment newInstance() {
        return new CastFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        castRecyclerView = view.findViewById(R.id.castRecyclerView);
        castRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample data - replace with actual data from your API
        if (castList.isEmpty()) {
            castList.add("Jason Momoa");
            castList.add("Jack Black");
            castList.add("Emma Stone");
        }

        castAdapter = new CastAdapter(castList);
        castRecyclerView.setAdapter(castAdapter);
    }

    // Method to update cast list from outside
    public void updateCastList(List<String> newCastList) {
        if (newCastList != null) {
            castList.clear();
            castList.addAll(newCastList);
            if (castAdapter != null) {
                castAdapter.notifyDataSetChanged();
            }
        }
    }
}