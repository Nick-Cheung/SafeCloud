package com.nick.safecloud.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nick.safecloud.R;
import com.nick.safecloud.model.FileListModel;
import com.nick.safecloud.utils.TimeUtils;

import java.util.List;

/**
 * Created by Sparrow on 2017/3/17.
 */

public class FileListAdapter extends BaseQuickAdapter<FileListModel.ListBean, FileListAdapter.FileViewHolder> {


    private ItemClicklListener mItemClicklListener;

    public FileListAdapter(List<FileListModel.ListBean> data, ItemClicklListener mItemClicklListener) {
        super(R.layout.item_file_list, data);
        this.mItemClicklListener = mItemClicklListener;
    }


    @Override
    protected FileViewHolder createBaseViewHolder(View view) {
        return new FileViewHolder(view);
    }

    @Override
    protected void convert(FileViewHolder helper, FileListModel.ListBean item) {
        helper.setContent(item);
        helper.setListener(item, mItemClicklListener);
    }


    public interface ItemClicklListener {
        void onClick(FileListModel.ListBean item);
    }


    public static class FileViewHolder extends BaseViewHolder {

        ImageView ivIcon;
        TextView tvTime;
        TextView tvName;

        public FileViewHolder(View view) {
            super(view);
            ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvName = (TextView) view.findViewById(R.id.tv_name);
        }


        public void setContent(FileListModel.ListBean data) {

            tvName.setText(data.getServer_filename());
            tvTime.setText(TimeUtils.stamp2Date(data.getServer_mtime()));

            if(data.getIsdir() == 1) {
                ivIcon.setImageResource(R.drawable.ic_folder_special);
            } else {
                ivIcon.setImageResource(R.drawable.ic_insert_drive_file);
            }

        }


        public void setListener(final FileListModel.ListBean item, final ItemClicklListener itemClicklListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicklListener.onClick(item);
                }
            });
        }
    }
}
