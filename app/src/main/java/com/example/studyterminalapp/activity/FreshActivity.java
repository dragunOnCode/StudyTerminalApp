package com.example.studyterminalapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.adapter.RecycleAdapterDome;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class FreshActivity extends AppCompatActivity {
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private RecycleAdapterDome adapterDome;//声明适配器
    private Context context;
    private List<String> list;
    private List<String> result;
    private int pageCount = 1;
    private int prePage = 0;
    private int lastVisibleItem = 0;
    private final int PAGE_SIZE = 10;
    private Button btnChange;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresh);
        context = this;

        initView();
        initData();
    }

    private void initData() {
        refreshLayout.autoRefresh();
    }

    private void initView() {

        //添加数据
        list = new ArrayList<>();
        result = new ArrayList<>();
        for (int i = 0;i < 30;i++){
            list.add("这是第"+i+"个测试");
        }

        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Log.i("On Refresh", "正在刷新");
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                prePage = pageCount;
                refreshlayout.setEnableLoadMore(true);
                request(pageCount, PAGE_SIZE);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                pageCount++;
                request(pageCount, PAGE_SIZE);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //
        adapterDome = new RecycleAdapterDome(context,result);
        /*
        与ListView效果对应的可以通过LinearLayoutManager来设置
        与GridView效果对应的可以通过GridLayoutManager来设置
        与瀑布流对应的可以通过StaggeredGridLayoutManager来设置
        */
        //LinearLayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        //创建线性布局
        LinearLayoutManager manager = new LinearLayoutManager(context);
        //垂直方向
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        //RecyclerView.LayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        //GridLayoutManager manager1 = new GridLayoutManager(context,2);
        //manager1.setOrientation(GridLayoutManager.VERTICAL);
        //StaggeredGridLayoutManager manager2 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        //给RecyclerView设置布局管理器
        recyclerView.setLayoutManager(manager);
        //创建适配器，并且设置
        recyclerView.setAdapter(adapterDome);

        btnChange = (Button) findViewById(R.id.btn_change);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result != null) {
                    result.clear();
                    for (int i = 0; i < 10; i++) {
                        result.add("我的奋斗" + i);
                    }
                    pageCount = 1;
                    adapterDome.notifyDataSetChanged();
                }
            }
        });
    }

    private void request(int pageCount, int pageSize) {
        int start = (pageCount - 1) * pageSize;
        int end = start + pageSize;
        for (int i = start; i < end; i++) {
            if (i < list.size()){
                result.add(list.get(i));
            }
        }
        adapterDome.notifyDataSetChanged();
    }


}