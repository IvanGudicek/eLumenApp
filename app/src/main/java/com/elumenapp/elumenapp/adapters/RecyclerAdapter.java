package com.elumenapp.elumenapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.elumenapp.elumenapp.R;
import com.elumenapp.elumenapp.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by IvanGudiček on 7/29/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private List<User> dataList;


    public List<User> getDataList() {
        return dataList;
    }

    public void setDataList(List<User> dataList) {
        this.dataList = dataList;
    }

    public RecyclerAdapter(List<User> dataList) {
        this.dataList = dataList;
    }


    @Override
    public RecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        User user = dataList.get(position);
        holder.username.setText(user.getFullName());
        String imgUrl = "https://graph.facebook.com/" + user.getFacebookId() + "/picture?type=large";
        Picasso.with(holder.imageView.getContext())
                .load(imgUrl)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView username;
        public RatingBar ratingBar;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.itemImageView);
            username = (TextView) itemView.findViewById(R.id.itemUsername);
            ratingBar = (RatingBar) itemView.findViewById(R.id.itemRatingBar);
            ratingBar.setEnabled(false);
        }
    }
}