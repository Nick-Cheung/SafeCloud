package com.nick.safecloud.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.nick.safecloud.R;
import com.nick.safecloud.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sparrow on 2017/3/17.
 */

public class FileListActivity extends BaseActivity {

    private static final int FILE_SELECT_CODE = 0;
    public static final String EXTRA_PARAM_DIR = "dir";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.contentLayout)
    FrameLayout mContentLayout;

    FileListFragment fileListFragment;


    public static void startMe(Context context, String dir) {
        Intent intent = new Intent(context, FileListActivity.class);
        intent.putExtra(EXTRA_PARAM_DIR, dir);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        ButterKnife.bind(this);

        initToolbar();
        setContent();
    }


    private String getDir() {
        return getIntent().getStringExtra(EXTRA_PARAM_DIR);
    }


    private void initToolbar() {
        mToolbar.setTitle("百度网盘");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    private void setContent() {

        fileListFragment = FileListFragment.newInstance(getDir());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentLayout, fileListFragment);
        transaction.commit();
    }


}
