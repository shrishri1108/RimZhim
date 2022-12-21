package com.rimzhim.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rimzhim.Activities.JoinContestActivity;
import com.rimzhim.Adapters.joinContestRankAdapter;
import com.rimzhim.Fragments.MyContests.Up_ComingFragment;
import com.rimzhim.ModelClasses.ContestResponse.DistributionItem;
import com.rimzhim.R;

import java.util.ArrayList;
import java.util.List;


public class WinningkFragment extends Fragment {
    String contestId;
    RecyclerView recyclerView;



    public WinningkFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_winningk, container, false);
        contestId = getArguments().getString("contestId");
        recyclerView = (RecyclerView)view.findViewById(R.id.recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        joinContestRankAdapter adapter = new joinContestRankAdapter(getContext(), JoinContestActivity.list);
        recyclerView.setAdapter(adapter);

        return view;
    }


}