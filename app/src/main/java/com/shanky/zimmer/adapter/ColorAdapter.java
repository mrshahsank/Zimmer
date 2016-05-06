package com.shanky.zimmer.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shanky.zimmer.HomeActivity;
import com.shanky.zimmer.R;
import com.shanky.zimmer.helper.ColorViewHolder;

import java.util.ArrayList;

/**
 * Created by Shashank on 03-05-2016.
 *  Adapter helps to manage color items in recycler view
 */
public class ColorAdapter extends RecyclerView.Adapter<ColorViewHolder> {

    ArrayList<Integer> colors;
    Context context;

    public ColorAdapter(Context context, ArrayList<Integer> colors) {
        this.colors = colors;
        this.context = context;
    }

    @Override
    public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.color_item_layout, parent, false);
        ColorViewHolder colorViewHolder = new ColorViewHolder(v);
        return colorViewHolder;
    }

    @Override
    public void onBindViewHolder(ColorViewHolder holder, final int position) {
        final ImageView colorImage = holder.colorImageView;
        GradientDrawable drawable = (GradientDrawable) colorImage.getBackground();
        drawable.setColor(colors.get(position));
        holder.colorImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 05-05-2016
                ((HomeActivity) context).currentSelectedColor(colors.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }
}
