package io.countryInfo.wiki.presentation;

import android.support.annotation.NonNull;
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

    public CountryInfoAdapter() {

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
            holder.caretView.setVisibility(View.GONE);
            holder.titleTextView.setVisibility(View.GONE);
            holder.descTextView.setVisibility(View.GONE);
            holder.separatorView.setVisibility(View.GONE);
        } else {
            holder.caretView.setVisibility(View.VISIBLE);
            holder.titleTextView.setVisibility(View.VISIBLE);
            holder.descTextView.setVisibility(View.VISIBLE);
            holder.separatorView.setVisibility(View.VISIBLE);

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
        TextView caretView;
        View separatorView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_title);
            descTextView = itemView.findViewById(R.id.text_desc);
            caretView = itemView.findViewById(R.id.caret);
            articleImageView = itemView.findViewById(R.id.image_article);
            separatorView = itemView.findViewById(R.id.separator);
        }


    }

}
