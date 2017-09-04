package com.elumenapp.elumenapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.elumenapp.elumenapp.R;
import com.elumenapp.elumenapp.models.Score;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.RecyclerViewHolder> {
    private List<Score> dataList;


    public List<Score> getDataList() {
        return dataList;
    }

    public void setDataList(List<Score> dataList) {
        this.dataList = dataList;
    }

    public ScoreAdapter(List<Score> dataList) {
        this.dataList = dataList;
    }


    @Override
    public ScoreAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_list_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Score score = dataList.get(position);
        BigDecimal result = new BigDecimal(0);
        result.setScale(3, RoundingMode.HALF_UP);
        result = score.getScore().divide(new BigDecimal(score.getRoundNumber()), 3, RoundingMode.HALF_UP);
        holder.scoreRating.setRating(Float.parseFloat(result.toString()));
        holder.subjectName.setText(score.getSubject().getName());
        holder.roundNumber.setText(String.valueOf(score.getRoundNumber()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public RatingBar scoreRating;
        public TextView subjectName;
        public TextView roundNumber;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            scoreRating = (RatingBar) itemView.findViewById(R.id.scoreRating);
            subjectName = (TextView) itemView.findViewById(R.id.subjectName);
            roundNumber = (TextView) itemView.findViewById(R.id.roundNumber);
        }
    }
}