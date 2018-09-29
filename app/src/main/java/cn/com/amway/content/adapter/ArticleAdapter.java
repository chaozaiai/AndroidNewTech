package cn.com.amway.content.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.library.data.entity.ArticleEntity;
import com.techidea.library.base.BaseRecyclerAdapter;
import com.techidea.library.base.BaseRecyclerHolder;
import com.techidea.library.utils.StringUtil;

import java.util.Collection;
import java.util.List;

import cn.com.amway.content.R;

/**
 * Created by sam on 2018/6/21.
 */

public class ArticleAdapter extends BaseRecyclerAdapter<ArticleEntity> {

    public ArticleAdapter(RecyclerView v, Collection<ArticleEntity> datas, int itemLayoutId) {
        super(v, datas, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, ArticleEntity item, int position, boolean isScrolling) {
        holder.setText(R.id.tv_title, item.getTitle());
        holder.setText(R.id.tv_des, null == item.getDescription() ? "" : item.getDescription());
        if (StringUtil.isEmpty(item.getContentTag())) {
            holder.setViewVisibility(R.id.tv_tag, View.GONE);
        } else {
            holder.setViewVisibility(R.id.tv_tag, View.VISIBLE);
            holder.setText(R.id.tv_tag, item.getContentTag());
        }
        holder.setText(R.id.tv_time, item.getModifiedDate());
        if (item.getPromotion() == 1) {
            holder.setViewVisibility(R.id.bt_mingpian, View.VISIBLE);
        } else {
            holder.setViewVisibility(R.id.bt_mingpian, View.GONE);
        }
        String host = item.getHost();
        host = "http://" + host;
        if (!StringUtil.isEmpty(item.getThumbnail())){
            Glide.with(context).load(host + item.getThumbnail())
                    .into((ImageView) holder.getView(R.id.iv_thumbnail));
        }else {
            holder.setImageResource(R.id.iv_thumbnail,R.mipmap.default01);
        }
    }

    @Override
    public void refreshItem(BaseRecyclerHolder holder, ArticleEntity item, int position, List<Object> payloads, boolean isScrolling) {

    }
}
