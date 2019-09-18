package com.pax.order.orderserver.Impl.retrofit;

import android.util.Log;

import com.pax.order.orderserver.Impl.retrofit.inf.ProgressCancelListener;
import com.pax.order.orderserver.entity.baseModel.BaseRspModel;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

import static com.pax.order.orderserver.constant.spRespCode.SP_RESPONSE_ORDERDATA_ERROR;
import static com.pax.order.orderserver.constant.spRespCode.SP_RESPONSE_SUCCESS;
import static com.pax.order.orderserver.constant.spRespCode.TM_ON_FAIL_THROW;

public abstract class OnSuccessAndFaultSub<T extends BaseRspModel> extends DisposableObserver<T> implements ProgressCancelListener {
    @Override
    public void onStart() {
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onError(Throwable e) {
        try {
            if (e instanceof SocketTimeoutException) {//请求超时
                onFault(TM_ON_FAIL_THROW, "Network request timeout");
            } else if (e instanceof ConnectException) {//网络连接超时
                onFault(TM_ON_FAIL_THROW, "Network connection timeout");
            } else if (e instanceof SSLHandshakeException) {//安全证书异常
                onFault(TM_ON_FAIL_THROW, "Security certificate exception");
            } else if (e instanceof HttpException) {//请求的地址不存在
                int code = ((HttpException) e).code();
                if (code == 504) {
                    onFault(TM_ON_FAIL_THROW, "Network abnormal, please check your network status");
                } else if (code == 404) {
                    onFault(TM_ON_FAIL_THROW, "The requested address does not exist");
                } else {
                    onFault(TM_ON_FAIL_THROW, "The request failed");
                }
            } else if (e instanceof UnknownHostException) {//域名解析失败
                onFault(TM_ON_FAIL_THROW, "Domain name resolution failed");
            } else {
                onFault(TM_ON_FAIL_THROW, "error:" + e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            Log.e("OnSuccessAndFaultSub", "error:" + e.getMessage());
        }
    }

    @Override
    public void onNext(T body) {
        String ResultCode = body.getResultCode();
        if (ResultCode.equals(SP_RESPONSE_SUCCESS) || ResultCode.equals(SP_RESPONSE_ORDERDATA_ERROR)) {
            onSuccess(body);
        } else {
            String errorMsg = body.getResultMessage();
            if (null != errorMsg && !errorMsg.isEmpty()) {
            } else {
                errorMsg = "Unknown Error";
            }
            onFault(ResultCode, errorMsg);
            Log.e("OnSuccessAndFaultSub", "ResultCode: " + ResultCode + ", errorMsg: " + errorMsg);
        }
    }

    /**
     * 取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isDisposed()) {
            this.dispose();
        }
    }

    public abstract void onSuccess(T rsp);

    public abstract void onFault(String code, String msg);
}
