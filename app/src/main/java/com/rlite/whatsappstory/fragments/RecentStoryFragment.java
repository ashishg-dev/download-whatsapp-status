package com.rlite.whatsappstory.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.rlite.whatsappstory.R;
import com.rlite.whatsappstory.activities.DisplayStoryActivity;
import com.rlite.whatsappstory.adapter.StoryAdapter;
import com.rlite.whatsappstory.constant.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentStoryFragment extends Fragment {

    private View view;
    private ArrayList<String> arrayListPath;
    private RecyclerView recyclerView;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private StoryAdapter imageAdapter;
    private InterstitialAd mInterstitialAd;

    public RecentStoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recent_story, container, false);
        initilize();
        return view;
    }

    private void initilize(){
        GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 2);
        arrayListPath = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

        if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
            setAdapter("allstories");
        } else {
            setAdapter("download");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        adsInit();

        arrayListPath.clear();
        if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
            displayStories(Constant.WHATSAPP_URI);
        } else {
            displayStories(Constant.MY_URI);
        }
        imageAdapter.notifyDataSetChanged();
    }

    private void adsInit() {
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial));
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    public void displayStories(String from) {
        File folder = new File(from);

        File[] allFiles = folder.listFiles();
        if (allFiles != null) {
            int count = allFiles.length;
            if (folder.exists()) {
                for (int i = 0; i < count; i++) {
                    arrayListPath.add(i,allFiles[i].getPath());
                }

            } else {
                Toast.makeText(getActivity(), "Invalid Url", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "No Stories", Toast.LENGTH_SHORT).show();
        }
    }

    public void setAdapter(final String calledFrom){
        imageAdapter = new StoryAdapter(getActivity(), arrayListPath);
        recyclerView.setAdapter(imageAdapter);

        imageAdapter.setOnItemClickListener(new StoryAdapter.StoryClickListener() {
            @Override
            public void itemClicked(View view, int position) {

                Intent intent = new Intent(getActivity(), DisplayStoryActivity.class);
                intent.putExtra("uri", arrayListPath.get(position));
                intent.putExtra("stories", calledFrom);
                startActivity(intent);

                Random random = new Random();
                int num = random.nextInt(2);
                if (num == 1) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                }

            }
        });
    }

}
