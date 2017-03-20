package com.nick.safecloud.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nick.safecloud.R;
import com.nick.safecloud.adapter.DownloadFileAdapter;
import com.nick.safecloud.base.BaseFragment;
import com.nick.safecloud.utils.DensityUtils;
import com.nick.safecloud.utils.FileUtils;
import com.nick.safecloud.utils.ToastUtils;

import java.io.File;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sparrow on 2017/3/20.
 */

public class DownloadFileFragment extends BaseFragment {


    View rootView;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;

    public static DownloadFileFragment newInstance() {
        return new DownloadFileFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_download_file, container, false);
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


    private void setContent() {
        showFileList();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerview.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = DensityUtils.dip2px(10);
                outRect.left = DensityUtils.dip2px(10);
                outRect.right = DensityUtils.dip2px(10);
            }
        });
    }

    private void showFileList() {
        try {
            File file = new File(FileUtils.getDownloadFolder());
            File[] fileList = file.listFiles();

            mRecyclerview.setAdapter(new DownloadFileAdapter(Arrays.asList(fileList)));
        } catch (Exception e) {
            ToastUtils.showText("获取文件失败！");
    }
}


    private void setListener() {

    }
}
