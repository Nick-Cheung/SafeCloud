package com.nick.safecloud.activity;

import com.google.gson.Gson;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nick.safecloud.R;
import com.nick.safecloud.adapter.FileListAdapter;
import com.nick.safecloud.api.ApiScheduler;
import com.nick.safecloud.api.BaiduApi;
import com.nick.safecloud.api.DownloadService;
import com.nick.safecloud.api.UploadService;
import com.nick.safecloud.base.BaseFragment;
import com.nick.safecloud.model.FileListModel;
import com.nick.safecloud.utils.DensityUtils;
import com.nick.safecloud.utils.FileUtils;
import com.nick.safecloud.utils.ToastUtils;
import com.trello.rxlifecycle.FragmentEvent;

import java.net.URISyntaxException;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

import static android.app.Activity.RESULT_OK;

/**
 * 文件列表Fragment
 */

public class FileListFragment extends BaseFragment {

    private static final int FILE_SELECT_CODE = 0;
    public static final String ARGUMENT_KEY_DIR = "dir";


    View rootView;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.floatingActionButton)
    FloatingActionButton mFloatingActionButton;


    FileListAdapter mAdapter;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;


    public static FileListFragment newInstance(String dir) {
        FileListFragment fragment = new FileListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_KEY_DIR, dir);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_file_list, container, false);
        ButterKnife.bind(this, rootView);
        setContent();
        setListener();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private String getDir() {
        return getArguments().getString(ARGUMENT_KEY_DIR);
    }

    private void setContent() {

        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置列表之前的间距
        mRecyclerview.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = DensityUtils.dip2px(10);
                outRect.left = DensityUtils.dip2px(10);
                outRect.right = DensityUtils.dip2px(10);
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
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }

    //调整到文件选择页面
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (ActivityNotFoundException ex) {

            ToastUtils.showText("获取文件失败！");
        }
    }


    //处理选择文件的结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                String path = FileUtils.getPath(getContext(), uri);
                ToastUtils.showText(path);
                UploadService.startMe(getContext(), path, getDir(), false);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //每一个Item的点击事件
    private FileListAdapter.ItemClicklListener mItemClicklListener = new FileListAdapter.ItemClicklListener() {
        @Override
        public void onClick(final FileListModel.ListBean item) {
            if (item.getIsdir() == 1) {
                FileListActivity.startMe(getContext(), item.getPath());
            } else {
                Snackbar snackbar = Snackbar.make(mCoordinatorLayout, "下载" + item.getServer_filename(), Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DownloadService.startMe(getContext(), item.getPath(), item.getServer_filename(), false);
                            }
                        });
                snackbar.getView().setBackgroundResource(R.color.colorAccent);
                snackbar.show();

            }
        }
    };


    /**
     * 获取文件列表
     * @param refresh 是否刷新
     */
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
                }).compose(this.<FileListModel>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .compose(new ApiScheduler<FileListModel>())
                .subscribe(new Subscriber<FileListModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.showText("获取文件列表失败！");
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