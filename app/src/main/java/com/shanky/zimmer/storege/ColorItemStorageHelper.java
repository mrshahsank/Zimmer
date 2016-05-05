package com.shanky.zimmer.storege;

import android.content.Context;
import android.content.SharedPreferences;

import com.shanky.zimmer.Utils.CommonVarUtils;

/**
 * Created by USER on 06-05-2016.
 */
public class ColorItemStorageHelper {
    private SharedPreferences colorItemStore;
    private SharedPreferences.Editor colorItemStoreEditor;
    private Context context;

    public ColorItemStorageHelper(Context context) {
        this.context = context;
        colorItemStore = context.getSharedPreferences(CommonVarUtils.COLOR_STORE_SHAREDPREF, Context.MODE_PRIVATE);
    }
    
}
