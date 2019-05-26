package com.wkz.listdialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButton;
    private ListDialog listDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                if (listDialog == null) {
                    listDialog = new ListDialog.Builder(this)
                            .setDatas(new String[]{"默认", "从上到下", "从下到上", "由高到低", "由低到高"})
//                            .setDialogWidth(ViewUtils.dp2px(this,300))
//                            .setDialogHeight(ViewUtils.dp2px(this,400))
                            .setDialogBackground(Color.CYAN, 10f)
                            .setDialogGravity(Gravity.BOTTOM)
                            .setDialogMargins(15, 5, 15, 5)
                            .setDividerDrawable(new ColorDrawable(Color.RED))
                            .setDialogEnterAnimation(R.anim.dialog_enter)
                            .setDialogExitAnimation(R.anim.dialog_exit)
//                            .setTextGravity(Gravity.START | Gravity.CENTER_VERTICAL)
//                            .setTextPadding(15, 0, 0, 0)
                            .setTextSize(15f)
                            .setTextNormalColor(Color.argb(40, 230, 152, 98))
                            .setTextSelectedColor(Color.argb(70, 130, 52, 198))
                            .setItemHeight(48f)
                            .setSelectedPosition(2)
                            .setOnItemClickListener(new ListDialog.OnItemClickListener() {
                                @Override
                                public void onItemClick(Dialog dialog, List<String> datas, int position) {
                                    Toast.makeText(MainActivity.this, datas.get(position), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .build();
                }
                listDialog.show();
                break;
            default:
                break;
        }
    }
}
