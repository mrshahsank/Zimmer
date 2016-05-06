package com.shanky.zimmer.storege;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.shanky.zimmer.Utils.CommonVarUtils;
import com.shanky.zimmer.storege.model.ColorCircleItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by USER on 06-05-2016.
 * Helper class to manage storege of circular color buttons in shared preference
 */
public class ColorItemStorageHelper {
    private SharedPreferences colorItemStore;
    private SharedPreferences.Editor colorItemStoreEditor;
    private Context context;
    private JSONArray colorRecordJsonArray;

    public ColorItemStorageHelper(Context context) {
        this.context = context;
        colorItemStore = context.getSharedPreferences(CommonVarUtils.COLOR_STORE_SHAREDPREF, Context.MODE_PRIVATE);
        colorItemStoreEditor = colorItemStore.edit();
    }

    /**
     * Method to insert circular color button info
     *
     * @param colorCircleItem
     */
    public void insertNewColorButton(ColorCircleItem colorCircleItem) {

        try {
            colorRecordJsonArray = new JSONArray(colorItemStore.getString(CommonVarUtils.COLOR_RECORDS, new JSONArray().toString()));
            JSONObject colorRecordJsonObject = new JSONObject();
            colorRecordJsonObject.put("color_code", colorCircleItem.COLOR_CODE);
            colorRecordJsonObject.put("position_x", colorCircleItem.POSITION_X);
            colorRecordJsonObject.put("position_y", colorCircleItem.POSITION_Y);
            colorRecordJsonObject.put("size", colorCircleItem.SIZE);
            colorRecordJsonObject.put("index", colorCircleItem.INDEX);
            colorRecordJsonArray.put(colorCircleItem.INDEX, colorRecordJsonObject);
            colorItemStoreEditor.putString(CommonVarUtils.COLOR_RECORDS, colorRecordJsonArray.toString()).commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to update size of circular color button according to index
     *
     * @param colorCircleItemIndex
     * @param newSize
     */
    public void updateSizeNewColorButton(int colorCircleItemIndex, int newSize) {

        try {
            colorRecordJsonArray = new JSONArray(colorItemStore.getString(CommonVarUtils.COLOR_RECORDS, new JSONArray().toString()));
            JSONObject colorButtonJsonObject = colorRecordJsonArray.getJSONObject(colorCircleItemIndex);
            colorButtonJsonObject.put("size", newSize);
            colorRecordJsonArray.put(colorCircleItemIndex, colorButtonJsonObject);
            colorItemStoreEditor.putString(CommonVarUtils.COLOR_RECORDS, colorRecordJsonArray.toString()).commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to delete circular color button item from shardpref
     *
     * @param colorCircleItemIndex
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void deleteNewColorButton(int colorCircleItemIndex) {

        try {
            colorRecordJsonArray = new JSONArray(colorItemStore.getString(CommonVarUtils.COLOR_RECORDS, new JSONArray().toString()));
            colorRecordJsonArray.remove(colorCircleItemIndex);
            colorItemStoreEditor.putString(CommonVarUtils.COLOR_RECORDS, colorRecordJsonArray.toString()).commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to reset all color buttons ie it deletes all circular color items from shred preferences
     */
    public void resetAllColorButton() {
        colorItemStoreEditor.putString(CommonVarUtils.COLOR_RECORDS, new JSONArray().toString()).commit();
    }

    /**
     * Method to get all circular color buttons items
     *
     * @return
     */
    public JSONArray getColorItemRecords() {
        try {
            return new JSONArray(colorItemStore.getString(CommonVarUtils.COLOR_RECORDS, new JSONArray().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }
}
