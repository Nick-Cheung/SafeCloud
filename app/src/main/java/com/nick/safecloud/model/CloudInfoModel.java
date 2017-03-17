package com.nick.safecloud.model;

/**
 * Created by Sparrow on 2017/1/4.
 */

public class CloudInfoModel {


    public static final int SUCCESS_CODE = 0;

    /**
     * errno : 0
     * used : 168444844687
     * total : 2208686931968
     * request_id : 1753441520293836327
     */

    private int errno;
    private long used;
    private long total;
    private long request_id;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getRequest_id() {
        return request_id;
    }

    public void setRequest_id(long request_id) {
        this.request_id = request_id;
    }
}
