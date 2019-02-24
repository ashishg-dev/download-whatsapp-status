package com.rlite.whatsappstory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rlite.whatsappstory.R;

import java.util.ArrayList;

/**
 * Created by le no vo on 07-06-2017.
 */

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ItemViewHolder> {
    private LayoutInflater mInflater;
    private Context context;
//    private String [] path;
    private ArrayList<String> path;
    private StoryClickListener clicklistener = null;

    public StoryAdapter(Context ctx, ArrayList<String> path) {
        context = ctx;
        mInflater = LayoutInflater.from(context);
        this.path = path;
    }
    /*    public StoryAdapter(Context ctx, String[] path) {
        context = ctx;
        mInflater = LayoutInflater.from(context);
        this.path = path;
    }*/

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private ImageView videoThumbnail;

        public ItemViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            image = (ImageView) itemView.findViewById(R.id.image);
            videoThumbnail = (ImageView) itemView.findViewById(R.id.videoThumbnail);
        }

        @Override
        public void onClick(View v) {

            if (clicklistener != null) {
                clicklistener.itemClicked(v, getAdapterPosition());
            }
        }
    }

    public void setOnItemClickListener(StoryClickListener listener) {
        this.clicklistener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_whatsapp_stories, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        if (path.get(position).contains("mp4")){
            holder.videoThumbnail.setVisibility(View.VISIBLE);
        } else {
            holder.videoThumbnail.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load(path.get(position))
                .thumbnail(0.01f)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        if (path == null){
            return 0;
        } else {
            return path.size();
        }
    }


    public interface StoryClickListener {
        void itemClicked(View view, int position);
    }

}

