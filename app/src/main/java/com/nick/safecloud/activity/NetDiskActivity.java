package com.nick.safecloud.activity;

import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nick.safecloud.R;
import com.nick.safecloud.api.ApiScheduler;
import com.nick.safecloud.api.BaiduApi;
import com.nick.safecloud.base.BaseActivity;
import com.nick.safecloud.model.CloudInfoModel;
import com.nick.safecloud.utils.StringUtils;
import com.trello.rxlifecycle.ActivityEvent;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.functions.Func1;

import static com.nick.safecloud.activity.FileListActivity.EXTRA_PARAM_DIR;

/**
 * Created by Sparrow on 2017/3/20.
 */

public class NetDiskActivity extends BaseActivity {

    public static final int INDEX_FILE_LIST_FRAGMENT = 0;
    public static final int INDEX_DOWNLOAD_FILE_FRAGMENT = 1;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.contentLayout)
    FrameLayout mContentLayout;
    @Bind(R.id.navigationView)
    NavigationView mNavigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    FileListFragment mFileListFragment;
    DownloadFileFragment mDownloadFileFragment;

    TextView tvTotal;
    TextView tvUsed;


    MenuItem checkedMenuItem;


    public static void startMe(Context context, String dir) {
        Intent intent = new Intent(context, NetDiskActivity.class);
        intent.putExtra(EXTRA_PARAM_DIR, dir);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_disk);
        ButterKnife.bind(this);

        initToolbar();
        setContent();
        setListener();
    }


    private void initToolbar() {
        mToolbar.setNavigationIcon(R.drawable.ic_menu);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mToolbar.setTitle("我的网盘");
    }


    private void setContent() {

        tvUsed = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tv_used);
        tvTotal = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tv_total);

        checkedMenuItem = mNavigationView.getMenu().getItem(0);
        checkedMenuItem.setChecked(true);
        showFragment(INDEX_FILE_LIST_FRAGMENT);
        getQuote();
    }

    private void showFragment(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragment(ft);

        switch (index) {
            case INDEX_DOWNLOAD_FILE_FRAGMENT:
                mToolbar.setTitle("下载文件");
                if (mDownloadFileFragment == null) {
                    mDownloadFileFragment = DownloadFileFragment.newInstance();
                    ft.add(R.id.contentLayout, mDownloadFileFragment);
                } else {
                    ft.show(mDownloadFileFragment);
                }
                break;

            case INDEX_FILE_LIST_FRAGMENT:
                mToolbar.setTitle("我的网盘");
                if (mFileListFragment == null) {
                    mFileListFragment = FileListFragment.newInstance("/");
                    ft.add(R.id.contentLayout, mFileListFragment);
                } else {
                    ft.show(mFileListFragment);
                }
                break;
        }
        ft.commit();

    }

    public void hideFragment(FragmentTransaction ft) {
        if (mDownloadFileFragment != null) {
            ft.hide(mDownloadFileFragment);
        }
        if (mFileListFragment != null) {
            ft.hide(mFileListFragment);
        }
    }


    private void setListener() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                checkedMenuItem.setChecked(false);
                checkedMenuItem = item;
                item.setChecked(true);
                if (item.getItemId() == R.id.item_download) {
                    showFragment(INDEX_DOWNLOAD_FILE_FRAGMENT);
                } else if (item.getItemId() == R.id.item_net_disk) {
                    showFragment(INDEX_FILE_LIST_FRAGMENT);
                }

                mDrawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });
    }


    private void getQuote() {

        BaiduApi.ApiBuilder.build().getQuote()
                .map(new Func1<String, CloudInfoModel>() {
                    @Override
                    public CloudInfoModel call(String s) {
                        Gson gson = new Gson();
                        return gson.fromJson(s, CloudInfoModel.class);
                    }
                }).compose(this.<CloudInfoModel>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new ApiScheduler<CloudInfoModel>())
                .subscribe(new Subscriber<CloudInfoModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CloudInfoModel s) {
                        if (s.getErrno() == CloudInfoModel.SUCCESS_CODE) {
                            tvTotal.setText("总共：" + StringUtils.getCapacityToshow(s.getTotal()));
                            tvUsed.setText("已用：" + StringUtils.getCapacityToshow(s.getUsed()));
                        } else {

                        }
                    }
                });
    }
}
