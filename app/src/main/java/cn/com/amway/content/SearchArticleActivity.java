package cn.com.amway.content;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.library.data.entity.AEMResult;
import com.library.data.entity.ArticleEntity;
import com.library.data.net.HttpMethods;
import com.techidea.library.base.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.amway.content.adapter.ArticleAdapter;
import rx.Observer;

/**
 * Created by sam on 2018/6/21.
 */

public class SearchArticleActivity extends AppCompatActivity {

    @BindView(R.id.et_keywords)
    EditText etKeywords;
    @BindView(R.id.rl_result)
    RecyclerView rlResult;

    String q = "*:*";
    String fq = "-id:*.pdf AND -id:*.doc AND -id:*.docx -id:*.ppt AND -id:*.pptx " +
            "AND -id:*.mp3 AND -id:*.mp4 AND -(id:*/product/* AND productImageUrl:*) " +
            "AND ((metatag.keywords: %s OR metatag.aemtags: %s)) " +
            "AND -aboReadOnly:1 AND -allowShare:0 AND " +
            "-id:*/content/china/accl/aaworkshop/mobile/clientpages/productdetail*";
    String start = "0";
    String rows = "10";
    String wt = "json";
    String indent = "true";
    String sort = "if(exists(query({!v='id:*/productdetail.*'})),0,1) asc," +
            "if(exists(query({!v='(metatag.keywords: %s " +
            "OR metatag.aemtags: %s)'})),0,1) " +
            "asc,if(exists(query({!v='id:http?//www.amwayfoundation.org*'})),0,1) desc,score desc," +
            "metatag.publishdate desc";
    String tag = "";

    private ArticleAdapter articleAdapter;
    private List<ArticleEntity> articleEntities = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_search)
    void searchClick() {
        String keywords = etKeywords.getText().toString().trim();
        initData(keywords);
    }

    private void initData(String keyWords) {
        String tag = keyWords;
        String qQ = String.format(q, tag, tag, tag, tag, tag);
        String fqQ = String.format(fq, tag, tag);
        String sortQ = String.format(sort, tag, tag);
        HttpMethods.getInstance().searchAEM(qQ, fqQ, start, rows, wt, indent, sortQ)
                .subscribe(new Observer<AEMResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(AEMResult aemResult) {
                        initView(aemResult.getResponse().getDocs());
                    }
                });
    }

    private void initView(List<ArticleEntity> datas) {
        this.articleEntities = datas;
        articleAdapter = new ArticleAdapter(rlResult, articleEntities, R.layout.article_item);
        rlResult.setLayoutManager(new LinearLayoutManager(this));
        rlResult.setAdapter(articleAdapter);
        articleAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                ArticleEntity articleEntity = (ArticleEntity) data;
                Intent intent = new Intent();
                intent.setClass(SearchArticleActivity.this, WebViewActivity.class);
                intent.putExtra("url", articleEntity.getId());
                startActivity(intent);
            }
        });
    }
}
