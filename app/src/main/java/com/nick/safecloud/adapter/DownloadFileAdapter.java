package com.nick.safecloud.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nick.safecloud.R;
import com.nick.safecloud.utils.TimeUtils;

import java.io.File;
import java.util.List;

/**
 * 已下载文件列表的Adapter
 */

public class DownloadFileAdapter extends BaseQuickAdapter<File, DownloadFileAdapter.ViewHolder> {


    public DownloadFileAdapter(List<File> data) {
        super(R.layout.item_file_list, data);
    }

    @Override
    protected void convert(ViewHolder helper, File item) {
        helper.setContent(item);
    }

    @Override
    protected ViewHolder createBaseViewHolder(View view) {
        return new ViewHolder(view);
    }

    public static class ViewHolder extends BaseViewHolder {

        ImageView ivIcon;
        TextView tvTime;
        TextView tvName;

        public ViewHolder(View view) {
            super(view);
            ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvName = (TextView) view.findViewById(R.id.tv_name);
        }

        private void setContent(File file) {
            tvName.setText(file.getName());
            tvTime.setText(TimeUtils.stamp2Date(file.lastModified()));
            if(file.isDirectory()) {
                ivIcon.setImageResource(R.drawable.ic_folder_special);
            } else {
                ivIcon.setImageResource(R.drawable.ic_insert_drive_file);
            }
        }


    }
}
