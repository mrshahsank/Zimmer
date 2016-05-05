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
import com.shanky.zimmer.storege.ColorItemStorageHelper;
import com.shanky.zimmer.storege.model.ColorCircleItem;

import org.json.JSONArray;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView colorRecyclerView;
    private RecyclerView.Adapter mColorAdapter;
    private RecyclerView.LayoutManager mColorLayoutManager;
    private RelativeLayout bodyLayout;
    private Button resetButton;
    private Button undoButton;

    private ArrayList<Integer> colorArrayList;
    private int currentSelectedCode = -1;
    private ArrayList<Button> colorButtons;
    private int colorButtonCount = -1;
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
        bodyLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (currentSelectedCode != -1 && event.getAction() == MotionEvent.ACTION_DOWN) {
                    final int x = (int) event.getX();
                    final int y = (int) event.getY();
                    final int size = (int) getResources().getDimension(R.dimen.color_item_size);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            size,
                            size);
                    final Button colorButton = new Button(getApplicationContext());
                    colorButton.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                    size + CommonVarUtils.DELTA_RADIOUS,
                                    size + CommonVarUtils.DELTA_RADIOUS);
                            lp.setMargins(x - (size + CommonVarUtils.DELTA_RADIOUS) / 2, y - (size + CommonVarUtils.DELTA_RADIOUS) / 2, 0, 0);
                            colorButton.setLayoutParams(lp);
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
                    colorButtons.add(colorButtonCount, colorButton);
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

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetView(bodyLayout);
                colorButtons = new ArrayList<>();
                colorButtonCount = -1;
                colorItemStorageHelper.resetAllColorButton();
            }
        });

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (colorButtonCount != -1) {
                    bodyLayout.removeView(colorButtons.get(colorButtonCount));
                    colorButtons.remove(colorButtonCount);
                    colorItemStorageHelper.deleteNewColorButton(colorButtonCount);
                    colorButtonCount--;
                }
            }
        });
    }

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

        /*JSONArray colorSavedRecord = colorItemStorageHelper.getColorItemRecords();
        for (int i = 0; i < colorSavedRecord.length(); i++) {

            colorRecordJsonObject.put("color_code", colorCircleItem.COLOR_CODE);
            colorRecordJsonObject.put("position_x", colorCircleItem.POSITION_X);
            colorRecordJsonObject.put("position_Y", colorCircleItem.POSITION_Y);
            colorRecordJsonObject.put("size", colorCircleItem.SIZE);
            colorRecordJsonObject.put("index", colorCircleItem.INDEX);
        }*/
    }

    public void currentSelectedColor(int colorCode) {
        currentSelectedCode = colorCode;
    }

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
}
