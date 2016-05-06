package com.shanky.zimmer;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.shanky.zimmer.Utils.CommonVarUtils;
import com.shanky.zimmer.adapter.ColorAdapter;
import com.shanky.zimmer.model.ColorCircularButton;
import com.shanky.zimmer.storege.ColorItemStorageHelper;
import com.shanky.zimmer.storege.model.ColorCircleItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView colorRecyclerView;
    private RecyclerView.Adapter mColorAdapter;
    private RecyclerView.LayoutManager mColorLayoutManager;
    private RelativeLayout bodyLayout;
    private Button resetButton;
    private Button undoButton;

    private ArrayList<Integer> colorArrayList;// to maintain colors codes
    private int currentSelectedCode = -1;// represents current seleced color from list
    private ArrayList<ColorCircularButton> colorButtons;// maintains reference of added color circular buttons
    private int colorButtonCount = -1;// count of existing color circular buttons
    private ColorItemStorageHelper colorItemStorageHelper;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        colorItemStorageHelper = new ColorItemStorageHelper(HomeActivity.this);
        //currentSelectedCode = getResources().getColor(R.color.colorWhite);
        resetButton = (Button) findViewById(R.id.resetButton);
        undoButton = (Button) findViewById(R.id.undoButton);
        colorButtons = new ArrayList<>();
        bodyLayout = (RelativeLayout) findViewById(R.id.body);
        colorRecyclerView = (RecyclerView) findViewById(R.id.colorRecyclerView);
        colorRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mColorLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        colorRecyclerView.setLayoutManager(mColorLayoutManager);
        initColorList();
        mColorAdapter = new ColorAdapter(HomeActivity.this, colorArrayList);
        colorRecyclerView.setAdapter(mColorAdapter);

        // on touch of white background adds new circular color button
        bodyLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (currentSelectedCode != -1 && event.getAction() == MotionEvent.ACTION_DOWN) {
                    //get position of touch
                    final int x = (int) event.getX();
                    final int y = (int) event.getY();
                    final int size = (int) getResources().getDimension(R.dimen.color_item_size);//default initial size

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            size,
                            size);
                    final Button colorButton = new Button(getApplicationContext());
                    colorButton.setTag(colorButtonCount + 1);
                    // on long click increases radious be delta radious
                    colorButton.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int index = (int) colorButton.getTag();
                            ColorCircularButton colorCircularButton = colorButtons.get(index);
                            int newSize = colorCircularButton.SIZE + CommonVarUtils.DELTA_RADIOUS;
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                    newSize,
                                    newSize);
                            lp.setMargins(x - newSize / 2, y - newSize / 2, 0, 0);
                            colorButton.setLayoutParams(lp);
                            colorCircularButton.SIZE = newSize;
                            colorButtons.set(colorCircularButton.INDEX, colorCircularButton);
                            colorItemStorageHelper.updateSizeNewColorButton(index, newSize);
                            return false;
                        }
                    });

                    lp.setMargins(x - size / 2, y - size, 0, 0);
                    colorButton.setLayoutParams(lp);
                    colorButton.setBackground(getResources().getDrawable(
                            R.drawable.circular_button));
                    GradientDrawable drawable = (GradientDrawable) colorButton.getBackground();
                    drawable.setColor(currentSelectedCode);
                    colorButtonCount++;

                    ColorCircularButton colorCircularButton = new ColorCircularButton();
                    colorCircularButton.COLOR_BUTTON = colorButton;
                    colorCircularButton.INDEX = colorButtonCount;
                    colorCircularButton.POSITION_X = x;
                    colorCircularButton.POSITION_Y = y;
                    colorCircularButton.SIZE = size;

                    colorButtons.add(colorButtonCount, colorCircularButton);

                    ColorCircleItem colorCircleItem = new ColorCircleItem();
                    colorCircleItem.INDEX = colorButtonCount;
                    colorCircleItem.COLOR_CODE = currentSelectedCode;
                    colorCircleItem.POSITION_X = x;
                    colorCircleItem.POSITION_Y = y;
                    colorCircleItem.SIZE = size;
                    colorItemStorageHelper.insertNewColorButton(colorCircleItem);
                    ((ViewGroup) v).addView(colorButton);
                }
                return false;
            }
        });
        // on reset button click removes all circular color buttons
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetView(bodyLayout);
                colorButtons = new ArrayList<>();
                colorButtonCount = -1;
                colorItemStorageHelper.resetAllColorButton();
            }
        });

        // on undo button click removes last added circular color button
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (colorButtonCount != -1) {
                    bodyLayout.removeView(colorButtons.get(colorButtonCount).COLOR_BUTTON);
                    colorButtons.remove(colorButtonCount);
                    colorItemStorageHelper.deleteNewColorButton(colorButtonCount);
                    colorButtonCount--;
                }
            }
        });
    }

    /**
     * Method to initialize color array and loads saves circular color buttons
     */
    public void initColorList() {
        colorArrayList = new ArrayList<>();
        colorArrayList.add(getResources().getColor(R.color.colorWhite));//white
        colorArrayList.add(getResources().getColor(R.color.colorRed));//red
        colorArrayList.add(getResources().getColor(R.color.colorPink));//pink
        colorArrayList.add(getResources().getColor(R.color.colorPurple));//purple
        colorArrayList.add(getResources().getColor(R.color.colorBlue));//blue
        colorArrayList.add(getResources().getColor(R.color.colorGreen));//green
        colorArrayList.add(getResources().getColor(R.color.colorYellow));//yellow
        colorArrayList.add(getResources().getColor(R.color.colorOrenge));//orenge
        colorArrayList.add(getResources().getColor(R.color.colorBrown));//browm
        colorArrayList.add(getResources().getColor(R.color.colorGray));//gray
        colorArrayList.add(getResources().getColor(R.color.colorBlack));//black
        initColorButtons();
    }

    /**
     * Method to get current seleced color
     *
     * @param colorCode
     */
    public void currentSelectedColor(int colorCode) {
        currentSelectedCode = colorCode;
    }

    /**
     * Method to remove all chield views
     *
     * @param v
     */
    private void resetView(ViewGroup v) {
        boolean doBreak = false;
        while (!doBreak) {
            int childCount = v.getChildCount();
            int i;
            for (i = 0; i < childCount; i++) {
                View currentChild = v.getChildAt(i);
                // Change ImageView with your desired type view
                if (currentChild instanceof Button) {
                    v.removeView(currentChild);
                    break;
                }
            }

            if (i == childCount) {
                doBreak = true;
            }
        }
    }

    /**
     * Method to load previously saved circular color  buttons
     */
    private void initColorButtons() {

        JSONArray colorSavedRecord = colorItemStorageHelper.getColorItemRecords();
        try {
            for (int i = 0; i < colorSavedRecord.length(); i++) {

                JSONObject colorRecordJsonObject = colorSavedRecord.getJSONObject(i);
                int colorCode = colorRecordJsonObject.getInt("color_code");
                final int x = colorRecordJsonObject.getInt("position_x");
                final int y = colorRecordJsonObject.getInt("position_y");
                final int size = colorRecordJsonObject.getInt("size");

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        size,
                        size);
                final Button colorButton = new Button(getApplicationContext());
                colorButton.setTag(colorButtonCount + 1);
                colorButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int index = (int) colorButton.getTag();
                        ColorCircularButton colorCircularButton = colorButtons.get(index);
                        int newSize = colorCircularButton.SIZE + CommonVarUtils.DELTA_RADIOUS;
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                newSize,
                                newSize);
                        lp.setMargins(x - newSize / 2, y - newSize / 2, 0, 0);
                        colorButton.setLayoutParams(lp);
                        colorCircularButton.SIZE = newSize;
                        colorButtons.set(colorCircularButton.INDEX, colorCircularButton);
                        colorItemStorageHelper.updateSizeNewColorButton(index, newSize);
                        return false;
                    }
                });

                lp.setMargins(x - size / 2, y - size, 0, 0);
                colorButton.setLayoutParams(lp);
                colorButton.setBackground(getResources().getDrawable(
                        R.drawable.circular_button));
                GradientDrawable drawable = (GradientDrawable) colorButton.getBackground();
                drawable.setColor(colorCode);
                colorButtonCount++;

                ColorCircularButton colorCircularButton = new ColorCircularButton();
                colorCircularButton.COLOR_BUTTON = colorButton;
                colorCircularButton.INDEX = colorButtonCount;
                colorCircularButton.POSITION_X = x;
                colorCircularButton.POSITION_Y = y;
                colorCircularButton.SIZE = size;

                colorButtons.add(colorButtonCount, colorCircularButton);

                ColorCircleItem colorCircleItem = new ColorCircleItem();
                colorCircleItem.INDEX = colorButtonCount;
                colorCircleItem.COLOR_CODE = colorCode;
                colorCircleItem.POSITION_X = x;
                colorCircleItem.POSITION_Y = y;
                colorCircleItem.SIZE = size;
                colorItemStorageHelper.insertNewColorButton(colorCircleItem);
                ((ViewGroup) bodyLayout).addView(colorButton);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
