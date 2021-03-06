package io.countryInfo.wiki.presentation;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.countryInfo.wiki.R;
import io.countryInfo.wiki.model.Row;

public class CountryInfoAdapter extends RecyclerView.Adapter<CountryInfoAdapter.ViewHolder> {

    private List<Row> rows = new ArrayList<>();

    CountryInfoAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.item_info, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Row fact = rows.get(position);
        if (fact.getTitle() == null && fact.getDescription() == null) {
            CardView.LayoutParams params = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = 0;
            holder.cardView.setLayoutParams(params);
        } else {
            holder.titleTextView.setVisibility(View.VISIBLE);
            holder.descTextView.setVisibility(View.VISIBLE);
            holder.titleTextView.setText(fact.getTitle());
            holder.descTextView.setText(fact.getDescription());
            Glide.with(holder.articleImageView.getContext())
                    .load(fact.getImageHref())
                    .into(holder.articleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descTextView;
        ImageView articleImageView;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_card);
            titleTextView = itemView.findViewById(R.id.text_title);
            descTextView = itemView.findViewById(R.id.text_desc);
            articleImageView = itemView.findViewById(R.id.image_article);
        }
    }
}
