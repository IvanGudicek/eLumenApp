package com.elumenapp.elumenapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.elumenapp.elumenapp.R;
import com.elumenapp.elumenapp.models.Score;
import com.elumenapp.elumenapp.models.User;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.RecyclerViewHolder> {

    private List<User> dataList;
    private List<Score> scoreList;

    public List<Score> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Score> scoreList) {
        this.scoreList = scoreList;
    }

    public List<User> getDataList() {
        return dataList;
    }

    public void setDataList(List<User> dataList) {
        this.dataList = dataList;
    }

    public UserAdapter(List<User> dataList, List<Score> scoreList) {
        this.dataList = dataList;
        this.scoreList = scoreList;
    }


    @Override
    public UserAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        User user = dataList.get(position);
        BigDecimal totalScore = new BigDecimal(0);
        totalScore.setScale(3, RoundingMode.HALF_UP);
        holder.username.setText(user.getFullName());
        String imgUrl = "https://graph.facebook.com/" + user.getFacebookId() + "/picture?type=large";
        Picasso.with(holder.imageView.getContext())
                .load(imgUrl)
                .into(holder.imageView);
        List<Score> userScores = scoreList.stream().filter(score -> score.getUser().getId() == user.getId()).collect(Collectors.toList());
        for (Score score : userScores) {
            totalScore = totalScore.add(score.getScore().divide(new BigDecimal(score.getRoundNumber()), 3, RoundingMode.HALF_UP));
        }
        if (userScores.size() == 0) {
            holder.userRating.setRating(Float.parseFloat(new BigDecimal(0).toString()));
        } else {
            holder.userRating.setRating(Float.parseFloat(totalScore.divide(new BigDecimal(userScores.size()), 3, RoundingMode.HALF_UP).toString()));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView username;
        public RatingBar userRating;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.itemImageView);
            username = (TextView) itemView.findViewById(R.id.itemUsername);
            userRating = (RatingBar) itemView.findViewById(R.id.userRating);
        }
    }
}