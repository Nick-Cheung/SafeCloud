package com.nick.safecloud.activity;

import com.google.gson.Gson;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nick.safecloud.R;
import com.nick.safecloud.adapter.FileListAdapter;
import com.nick.safecloud.api.ApiScheduler;
import com.nick.safecloud.api.BaiduApi;
import com.nick.safecloud.base.BaseActivity;
import com.nick.safecloud.model.FileListModel;
import com.nick.safecloud.util.CookieUtil;
import com.nick.safecloud.util.DensityUtil;
import com.nick.safecloud.util.ToastUtil;
import com.trello.rxlifecycle.ActivityEvent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

/**
 * Created by Sparrow on 2017/3/17.
 */

public class FileListActivity extends BaseActivity {

    public static final String EXTRA_PARAM_DIR = "dir";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;


    FileListAdapter mAdapter;


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
        setListener();
    }


    private String getDir() {
        return getIntent().getStringExtra(EXTRA_PARAM_DIR);
    }


    private void initToolbar() {
        mToolbar.setTitle("百度网盘");
        if (getDir().length() != 1) {
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }


    private void setContent() {

        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = DensityUtil.dip2px(10);
                outRect.left = DensityUtil.dip2px(10);
                outRect.right = DensityUtil.dip2px(10);
            }
        });

        getFileList(false);
    }


    private void setListener() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFileList(true);
            }
        });
    }

    private FileListAdapter.ItemClicklListener mItemClicklListener = new FileListAdapter.ItemClicklListener() {
        @Override
        public void onClick(final FileListModel.ListBean item) {
            if (item.getIsdir() == 1) {
                FileListActivity.startMe(FileListActivity.this, item.getPath());
            } else {
                Snackbar.make(mCoordinatorLayout, "下载" + item.getServer_filename(), Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downLoadFile(item.getPath(), item.getServer_filename());
                                progressDialog = new ProgressDialog(FileListActivity.this);
                                progressDialog.setMessage("正在下载"+item.getServer_filename());
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                progressDialog.setMax(100);
                                progressDialog.show();
                            }
                        }).show();
            }
        }
    };



    ProgressDialog progressDialog;

    private void downLoadFile(String path, String fileName) {
        OkHttpUtils.get().url(String.format(BaiduApi.DOWNLAOD_FILE_URL_FORMAT, path))
                .addHeader("cookie", CookieUtil.getCookie())
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName) {

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        progressDialog.setProgress((int) (progress / total * 100));
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        ToastUtil.showText("下载成功");
                    }
                });


    }


    private void getFileList(final boolean refresh) {

        BaiduApi.ApiBuilder.build().getFileList(getDir())
                .map(new Func1<String, FileListModel>() {
                    @Override
                    public FileListModel call(String s) {
                        Gson gson = new Gson();
                        FileListModel model = gson.fromJson(s, FileListModel.class);
                        if (model.getErrno() == 0) {
                            return model;
                        } else {
                            throw Exceptions.propagate(new Exception());
                        }
                    }
                }).compose(this.<FileListModel>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new ApiScheduler<FileListModel>())
                .subscribe(new Subscriber<FileListModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtil.showText("获取文件列表失败！");
                        if (refresh) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onNext(FileListModel model) {
                        if (refresh) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (mAdapter == null) {
                            mAdapter = new FileListAdapter(model.getList(), mItemClicklListener);
                            mRecyclerview.setAdapter(mAdapter);
                        } else {
                            mAdapter.setNewData(model.getList());
                        }
                    }
                });
    }


}
