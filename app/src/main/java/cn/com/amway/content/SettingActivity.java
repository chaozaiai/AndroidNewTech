package cn.com.amway.content;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.amway.sharelibrary.ShareKony;
import com.konylabs.vm.Function;

import java.util.Arrays;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.amway.content.constant.Constant;

/**
 * Created by sam on 2018/7/6.
 */

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.et_url)
    EditText etUrl;
    @BindView(R.id.et_userinfo)
    EditText etUserInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        ButterKnife.bind(this);
//        etUrl.setText("https://www.baidu.com/");
    }

    @OnClick(R.id.bt_jump)
    void jumpClick() {
        jumpWeb();

//        doPhotoPrint();
    }

    @OnClick(R.id.bt_share)
    void btnWechatShare() {
        String Id = "2";
        String SortId = "2";
        String AppId = "wxdad73b16c39f9654";
        String AppSecret = "59104570b51f28e02429cc5953779c1c";
        String BypassApproval = "true";
        String Enable = "true";
        ShareKony shareKony = new ShareKony();
        shareKony.initContext(getApplicationContext());
        shareKony.setWechatMomentsParams(Id, SortId, AppId, AppSecret, BypassApproval, Enable);
        String[] AVATARS = {
                "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
                "http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
                "http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
                "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
                "http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg"
        };
        Vector<String> avatars = new Vector<>();
        avatars.addAll(Arrays.asList(AVATARS));
        shareKony.shareMomentsImageArray(avatars, null);
    }

    private void jumpWeb() {
        String url = etUrl.getText().toString().trim();
        String userinfo = etUserInfo.getText().toString().trim();
        Intent intent = new Intent();
        intent.putExtra(Constant.PTEXTRA_URL, url);
        intent.putExtra(Constant.PTEXTRA_USERINFO, userinfo);
        intent.setClass(this, WebViewLocalActivity.class);
        startActivity(intent);
    }

    private void doPhotoPrint() {
        PrintHelper photoPrinter = new PrintHelper(this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        photoPrinter.printBitmap("droids.jpg - test print", bitmap);
    }


}
