package com.zhang.baselib.http.progress;


import com.zhang.baselib.http.progress.body.ProgressInfo;

/**
 * ================================================
 * Created by JessYan on 02/06/2017 18:23
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface ProgressListener {
    /**
     * 进度监听
     *
     * @param progressInfo 关于进度的所有信息
     */
    void onProgress(ProgressInfo progressInfo);

    /**
     * 错误监听
     *
     * @param id 进度信息的 id
     * @param e 错误
     */
    void onError(long id, Exception e);
}
