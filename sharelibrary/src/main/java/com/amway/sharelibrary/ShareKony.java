package com.amway.sharelibrary;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.konylabs.android.KonyMain;
import com.konylabs.vm.Function;
import com.mob.MobSDK;

import java.util.HashMap;
import java.util.Vector;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * Created by sam on 2018/7/27.
 */

public class ShareKony {

    private HashMap<String, Object> wechatParams;
    private HashMap<String, Object> wechatMomentsParams;
    private static final String TAG = ShareKony.class.getCanonicalName();
    private Context context;

    public ShareKony() {
    }

    public void init() {
        this.context = KonyMain.getAppContext();
        MobSDK.init(this.context);
    }

    public void initContext(Context context) {
        this.context = context;
        MobSDK.init(this.context);
    }


    public void setWechatParams(String Id, String SortId, String AppId, String AppSecret,
                                String BypassApproval, String Enable) {
        wechatParams = new HashMap<>();
        wechatParams.put("Id", Id);
        wechatParams.put("SortId", SortId);
        wechatParams.put("AppId", AppId);
        wechatParams.put("AppSecret", AppSecret);
        wechatParams.put("BypassApproval", BypassApproval);
        wechatParams.put("Enable", Enable);
    }

    public void setWechatMomentsParams(String Id, String SortId, String AppId, String AppSecret,
                                       String BypassApproval, String Enable) {
        wechatMomentsParams = new HashMap<>();
        wechatMomentsParams.put("Id", Id);
        wechatMomentsParams.put("SortId", SortId);
        wechatMomentsParams.put("AppId", AppId);
        wechatMomentsParams.put("AppSecret", AppSecret);
        wechatMomentsParams.put("BypassApproval", BypassApproval);
        wechatMomentsParams.put("Enable", Enable);

    }

    public void shareMomentsImageArray(Vector<String> imageUrls, final Function callBack) {
        if (null == wechatMomentsParams || wechatMomentsParams.size() == 0) {
            try {
                callBack.execute(new Object[]{"wechatMomentsParams error"});
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        } else {
            ShareSDK.setPlatformDevInfo(WechatMoments.NAME, wechatMomentsParams);
            Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
            wechat.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    Log.i(TAG, "err");
                    try {
                        callBack.execute(new Object[]{"complete"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    throwable.printStackTrace();
                    Log.i(TAG, throwable.getMessage());
                    try {
                        callBack.execute(new Object[]{"error"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    Log.i(TAG, "cancel");
                    try {
                        callBack.execute(new Object[]{"cancel"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            String[] AVATARS = new String[imageUrls.size()];
            for (int i = 0; i < imageUrls.size(); i++) {
                AVATARS[i] = imageUrls.get(i);
            }
            Platform.ShareParams params = new Platform.ShareParams();
            params.setShareType(Platform.SHARE_IMAGE);
            params.setImageArray(AVATARS);
            params.setUrl("http://www.sharesdk.cn");
            wechat.share(params);
        }

    }

    public void shareWeChat(Vector<String> imageUrls, final Function callBack) {
        if (null == wechatMomentsParams || wechatMomentsParams.size() == 0) {
            try {
                callBack.execute(new Object[]{"wechatMomentsParams error"});
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        } else {
            ShareSDK.setPlatformDevInfo(WechatMoments.NAME, wechatMomentsParams);
            Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
            wechat.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    Log.i(TAG, "err");
                    try {
                        callBack.execute(new Object[]{"complete"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    throwable.printStackTrace();
                    Log.i(TAG, throwable.getMessage());
                    try {
                        callBack.execute(new Object[]{"error"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    Log.i(TAG, "cancel");
                    try {
                        callBack.execute(new Object[]{"cancel"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            String[] AVATARS = new String[imageUrls.size()];
            for (int i = 0; i < imageUrls.size(); i++) {
                AVATARS[i] = imageUrls.get(i);
            }
            Platform.ShareParams params = new Platform.ShareParams();
            params.setShareType(Platform.SHARE_IMAGE);
            params.setImageArray(AVATARS);
            params.setUrl("http://www.sharesdk.cn");
            wechat.share(params);
        }

    }

    private void showToast(String message) {
        if (null != context) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void showToastMsg(String msg) {
        Toast.makeText(KonyMain.getActContext(), msg, Toast.LENGTH_SHORT).show();
    }


}
