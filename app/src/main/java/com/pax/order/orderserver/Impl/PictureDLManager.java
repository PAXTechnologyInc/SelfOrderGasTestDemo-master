package com.pax.order.orderserver.Impl;

import android.os.Handler;
import android.os.Message;

import com.pax.dlfileslib.bizs.DLManager;
import com.pax.dlfileslib.bizs.MySSLSocketFactory;
import com.pax.dlfileslib.interfaces.IDListener;
import com.pax.order.FinancialApplication;
import com.pax.order.logger.AppLog;
import com.pax.order.orderserver.entity.DownloadJob;
import com.pax.order.orderserver.inf.DLPictureListener;
import com.pax.order.util.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.pax.dlfileslib.bizs.DLError.ERROR_NOT_NETWORK;

/**
 * Created by chenyr on 2018/12/19.
 */

public class PictureDLManager {
    private static final String TAG = "PictureDLManager";
    private static final int START_SUB_DOWNLOAD = 0;
    private static PictureDLManager instance = null;
    private DLManager mDLManager;
    private List<DownloadJob> mTotalJobList;
    private List<DownloadJob> mRoundJobList;             // current round job
    private DLPictureListener listener;
    private int mTotalCount;                             // total download job count
    private int mSucceedCount;                           // total succeed job count
    private int mFailRoundProCount;
    private int mRoundNum;                               // current round num
    private static final int MAX_ROUND = 3;              // max round number including retry times.
    private int mRoundProCount;                          // processed count including failure ones in current round
    private int mRoundTotalCount;                        // total job count in current round

    public static synchronized PictureDLManager getInstance() {
        if (instance == null) {
            instance = new PictureDLManager();
        }
        return instance;
    }

    private PictureDLManager() {
        mDLManager = DLManager.getInstance(FinancialApplication.getApp());
        mDLManager.setMaxTask(10);
        MySSLSocketFactory.getInstance().fixHttpsURLConnection();
    }

    public void init(DLPictureListener listener) {
        this.listener = listener;
        this.mSucceedCount = 0;
        mFailRoundProCount = 0;
        this.mTotalCount = 0;
        this.mTotalJobList = new ArrayList<>();
        this.mRoundJobList = new ArrayList<>();
        this.mRoundNum = 1;
        this.mRoundProCount = 0;
        this.mRoundTotalCount = 0;
    }

    private int roundNumPlusPlus() {
        return ++mRoundNum;
    }

    public void setDownloadJobList(List<DownloadJob> downloadJobList) {
        this.mTotalJobList.clear();
        this.mTotalJobList.addAll(downloadJobList);
        this.mTotalCount = this.mTotalJobList.size();
    }

    public void runAsyncDownload() {
        listener.onProgress(mSucceedCount, mTotalCount);
        // preProcessJobs only for first round
        deleteInvalidPicture();   // pre-process jobs
//        subBlockLoop();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_SUB_DOWNLOAD:
                    subBlockLoop();
                    break;
                default:

                    break;
            }
        }
    };

    private void subBlockLoop() {
        mFailRoundProCount = 0;
        mRoundProCount = 0;

        mRoundTotalCount = mTotalJobList.size();
        mRoundJobList.clear();
        mRoundJobList.addAll(mTotalJobList);

        for (DownloadJob tempJob : mRoundJobList) {
            mDLManager.dlStart(tempJob.getUrl(), tempJob.getLocalDir(),
                    null, null, new SimpleDListener());
        }
    }

    private class SimpleDListener implements IDListener {
        @Override
        public void onPrepare() {
        }

        @Override
        public void onStart(String fileName, String realUrl, int fileLength) {
        }

        @Override
        public void onProgress(int progress) {
        }

        @Override
        public void onStop(int progress) {
        }

        @Override
        public void onFinish(File file, String realUrl) {
            synchronized (PictureDLManager.class) {
                ++mRoundProCount;
                ++mSucceedCount;
                listener.onProgress(mSucceedCount, mTotalCount);
                AppLog.d(TAG, Tools._FILE_FUNC_LINE_() + "mSucceedCount:" + mSucceedCount + ", mTotalCount:" + mTotalCount);

                for (DownloadJob downloadJob : mRoundJobList) {
                    if (realUrl.equals(downloadJob.getUrl())) {
                        mTotalJobList.remove(downloadJob);
                        break;
                    }
                }

                if (mSucceedCount == mTotalCount) {
                    listener.onDownloadSuccess();
                    return;
                }

                if (mRoundProCount == mRoundTotalCount && mFailRoundProCount != 0) { // new round
                    if (roundNumPlusPlus() > MAX_ROUND) { // no more retry times
                        listener.onDownloadFailed("Retry times run out.");
                    } else {
                        subBlockLoop();
                    }
                }
            }
        }

        @Override
        public void onError(int status, String error) {
            synchronized (PictureDLManager.class) {
                ++mRoundProCount;
                ++mFailRoundProCount;

                if (status == ERROR_NOT_NETWORK) {
                    listener.onDownloadFailed(error);
                    mDLManager.closeAllTaskAndThread();
                    return;
                }

                if (mRoundProCount == mRoundTotalCount && mFailRoundProCount != 0) { // new round
                    if (roundNumPlusPlus() > MAX_ROUND) { // no more retry times
                        listener.onDownloadFailed("Retry times run out.");
                    } else {
                        subBlockLoop();
                    }
                }
            }
        }
    }

    //删除之前已下载但当前不需要的无效图片
    private void deleteInvalidPicture() {
        FinancialApplication.getApp().runInBackground(new Runnable() {
            @Override
            public void run() {

                List<String> mPathList = new ArrayList<>();
                List<DownloadJob> fileExistJobList = new ArrayList<>();
                for (DownloadJob tempJob : mTotalJobList) {
                    fileExistJobList.add(tempJob);
                    String path = tempJob.getLocalDir();
                    if (!mPathList.contains(path)) {
                        mPathList.add(path);
                    }
                }

                for (String path : mPathList) {
                    File file = new File(path);
                    File[] fs = file.listFiles();
                    for (File f : fs) {
                        if (f.isDirectory()) {
                            deleteDir(f.getPath());
                        }

                        if (f.isFile()) {
                            boolean bFound = false;
                            for (DownloadJob tempJob : fileExistJobList) {
                                String tempPath = tempJob.getLocalDir() + "/" + tempJob.getImageName();
                                if (f.getPath().equals(tempPath)) {
                                    bFound = true;
                                    break;
                                }
                            }

                            if (!bFound) {
                                f.delete();
                            }
                        }
                    }
                }

                Message message = new Message();
                message.what = START_SUB_DOWNLOAD;
                mHandler.sendMessage(message);
            }
        });
    }

    /**
     * delete directory and files in it
     *
     * @param pPath directory to be delete
     */
    private void deleteDir(final String pPath) {
        File dir = new File(pPath);
        deleteDirWithFile(dir);
    }

    private void deleteDirWithFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }

        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete(); // delete all files
            } else if (file.isDirectory()) {
                deleteDirWithFile(file); // delete directory and files in it
            }
        }

        dir.delete(); // delete directory
    }

    public void closeAllDLRunnable() {
        mDLManager.closeAllTaskAndThread();
    }
}
