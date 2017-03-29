package com.nick.safecloud.model;

import java.util.List;

/**
 * 文件列表的Model类
 */

public class FileListModel {

    private int errno;
    private String guid_info;
    private long request_id;
    private int guid;
    private List<ListBean> list;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getGuid_info() {
        return guid_info;
    }

    public void setGuid_info(String guid_info) {
        this.guid_info = guid_info;
    }

    public long getRequest_id() {
        return request_id;
    }

    public void setRequest_id(long request_id) {
        this.request_id = request_id;
    }

    public int getGuid() {
        return guid;
    }

    public void setGuid(int guid) {
        this.guid = guid;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {

        private int category;
        private int unlist;
        private long fs_id;
        private int oper_id;
        private int server_ctime;
        private int isdir;
        private int local_mtime;
        private long size;
        private String server_filename;
        private String path;
        private int local_ctime;
        private int server_mtime;
        private String md5;

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public int getUnlist() {
            return unlist;
        }

        public void setUnlist(int unlist) {
            this.unlist = unlist;
        }

        public long getFs_id() {
            return fs_id;
        }

        public void setFs_id(long fs_id) {
            this.fs_id = fs_id;
        }

        public int getOper_id() {
            return oper_id;
        }

        public void setOper_id(int oper_id) {
            this.oper_id = oper_id;
        }

        public int getServer_ctime() {
            return server_ctime;
        }

        public void setServer_ctime(int server_ctime) {
            this.server_ctime = server_ctime;
        }

        public int getIsdir() {
            return isdir;
        }

        public void setIsdir(int isdir) {
            this.isdir = isdir;
        }

        public int getLocal_mtime() {
            return local_mtime;
        }

        public void setLocal_mtime(int local_mtime) {
            this.local_mtime = local_mtime;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getServer_filename() {
            return server_filename;
        }

        public void setServer_filename(String server_filename) {
            this.server_filename = server_filename;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getLocal_ctime() {
            return local_ctime;
        }

        public void setLocal_ctime(int local_ctime) {
            this.local_ctime = local_ctime;
        }

        public int getServer_mtime() {
            return server_mtime;
        }

        public void setServer_mtime(int server_mtime) {
            this.server_mtime = server_mtime;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }
    }
}
