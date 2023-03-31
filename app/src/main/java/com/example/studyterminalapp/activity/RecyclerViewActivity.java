package com.example.studyterminalapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.studyterminalapp.R;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zpn
 * @date 2020-09-24
 */
public class RecyclerViewActivity extends AppCompatActivity implements OnRefreshListener {

    private RecyclerView rv_recyclerview;
    private SmartRefreshLayout sr_layout;
    private LinearLayoutManager mLayoutManager;

    private List<String> list;
    private MyAdapter adapter;

    private int lastVisibleItem = 0;
    private final int PAGE_COUNT = 10;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        sr_layout = findViewById(R.id.sr_layout);
        rv_recyclerview = findViewById(R.id.rv_recyclerview);
        initData();
        initRefreshLayout();
        initRecyclerView();
    }

    private void initData() {
        list = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            list.add("条目" + i);
        }
    }

    private void initRefreshLayout() {
        sr_layout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        adapter = new MyAdapter(getDatas(0, PAGE_COUNT), this, getDatas(0, PAGE_COUNT).size() > 0 ? true : false);
        mLayoutManager = new LinearLayoutManager(this);
        rv_recyclerview.setLayoutManager(mLayoutManager);
        rv_recyclerview.setAdapter(adapter);
        rv_recyclerview.setItemAnimator(new DefaultItemAnimator());

        rv_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (adapter.isFadeTips() == false && lastVisibleItem + 1 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }

                    if (adapter.isFadeTips() == true && lastVisibleItem + 2 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private List<String> getDatas(final int firstIndex, final int lastIndex) {
        List<String> resList = new ArrayList<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            if (i < list.size()) {
                resList.add(list.get(i));
            }
        }
        return resList;
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        List<String> newDatas = getDatas(fromIndex, toIndex);
        if (newDatas.size() > 0) {
            adapter.updateList(newDatas, true);
        } else {
            adapter.updateList(null, false);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        //sr_layout.setRefreshing(true);
        adapter.resetDatas();
        updateRecyclerView(0, PAGE_COUNT);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //sr_layout.setRefreshing(false);
            }
        }, 1000);
    }



    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> datas; // 数据源
        private Context context;    // 上下文Context

        private int normalType = 0;     // 第一种ViewType，正常的item
        private int footType = 1;       // 第二种ViewType，底部的提示View

        private boolean hasMore = true;   // 变量，是否有更多数据
        private boolean fadeTips = false; // 变量，是否隐藏了底部的提示

        public MyAdapter(List<String> datas, Context context, boolean hasMore) {
            // 初始化变量
            this.datas = datas;
            this.context = context;
            this.hasMore = hasMore;
        }

        // 获取条目数量，之所以要加1是因为增加了一条footView
        @Override
        public int getItemCount() {
            return datas.size() + 1;
        }

        // 自定义方法，获取列表中数据源的最后一个位置，比getItemCount少1，因为不计上footView
        public int getRealLastPosition() {
            return datas.size();
        }


        // 根据条目位置返回ViewType，以供onCreateViewHolder方法内获取不同的Holder
        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return footType;
            } else {
                return normalType;
            }
        }

        // 正常item的ViewHolder，用以缓存findView操作
        class NormalHolder extends RecyclerView.ViewHolder {
            private TextView textView;

            public NormalHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.tv_recycler);
            }
        }

        // // 底部footView的ViewHolder，用以缓存findView操作
        class FootHolder extends RecyclerView.ViewHolder {
            private TextView tips;
            private ProgressBar pb_footer_progressBar;

            public FootHolder(View itemView) {
                super(itemView);
                tips = (TextView) itemView.findViewById(R.id.tv_footer);
                pb_footer_progressBar = (ProgressBar)itemView.findViewById(R.id.pb_footer_progressBar);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 根据返回的ViewType，绑定不同的布局文件，这里只有两种
            if (viewType == normalType) {
                return new NormalHolder(LayoutInflater.from(context).inflate(R.layout.rv_recyclerview_item, parent,false));
            } else {
                return new FootHolder(LayoutInflater.from(context).inflate(R.layout.rv_recyclerview_item_footer, parent,false));
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            // 如果是正常的imte，直接设置TextView的值
            if (holder instanceof NormalHolder) {
                ((NormalHolder) holder).textView.setText(datas.get(position));
            } else {
                if(getItemCount() < PAGE_COUNT){
                    ((FootHolder) holder).tips.setVisibility(View.GONE);
                    ((FootHolder) holder).pb_footer_progressBar.setVisibility(View.GONE);
                }else {
                    // 之所以要设置可见，是因为我在没有更多数据时会隐藏了这个footView
                    ((FootHolder) holder).tips.setVisibility(View.VISIBLE);
                    ((FootHolder) holder).pb_footer_progressBar.setVisibility(View.VISIBLE);
                    // 只有获取数据为空时，hasMore为false，所以当我们拉到底部时基本都会首先显示“正在加载更多...”
                    if (hasMore == true) {
                        // 不隐藏footView提示
                        fadeTips = false;
                        if (datas.size() > 0) {
                            // 如果查询数据发现增加之后，就显示正在加载更多
                            ((FootHolder) holder).tips.setText("正在加载更多...");
                        }
                    } else {
                        if (datas.size() > 0) {
                            // 如果查询数据发现并没有增加时，就显示没有更多数据了
                            ((FootHolder) holder).tips.setText("没有更多数据了");

                            // 然后通过延时加载模拟网络请求的时间，在500ms后执行
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // 隐藏提示条
                                    ((FootHolder) holder).tips.setVisibility(View.GONE);
                                    ((FootHolder) holder).pb_footer_progressBar.setVisibility(View.GONE);
                                    // 将fadeTips设置true
                                    fadeTips = true;
                                    // hasMore设为true是为了让再次拉到底时，会先显示正在加载更多
                                    hasMore = true;
                                }
                            }, 500);
                        }
                    }
                }
            }
        }

        // 暴露接口，改变fadeTips的方法
        public boolean isFadeTips() {
            return fadeTips;
        }

        // 暴露接口，下拉刷新时，通过暴露方法将数据源置为空
        public void resetDatas() {
            datas = new ArrayList<>();
        }

        // 暴露接口，更新数据源，并修改hasMore的值，如果有增加数据，hasMore为true，否则为false
        public void updateList(List<String> newDatas, boolean hasMore) {
            // 在原有的数据之上增加新数据
            if (newDatas != null) {
                datas.addAll(newDatas);
            }
            this.hasMore = hasMore;
            notifyDataSetChanged();
        }

    }



}