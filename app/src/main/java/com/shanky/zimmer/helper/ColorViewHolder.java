package com.shanky.zimmer.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.shanky.zimmer.R;

/**
 * Created by USER on 03-05-2016.
 * Helper Class for color recyclerview
 */
public class ColorViewHolder extends RecyclerView.ViewHolder {
    public ImageView colorImageView;

    public ColorViewHolder(View itemView) {
        super(itemView);
        colorImageView = (ImageView) itemView.findViewById(R.id.colorImage);
    }
}
