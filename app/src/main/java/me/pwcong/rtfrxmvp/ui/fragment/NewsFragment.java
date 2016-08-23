package me.pwcong.rtfrxmvp.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;

import java.util.List;

import butterknife.BindView;
import me.pwcong.rtfrxmvp.R;
import me.pwcong.rtfrxmvp.adapter.NewsFragmentAdapter;
import me.pwcong.rtfrxmvp.conf.Constants;
import me.pwcong.rtfrxmvp.mvp.bean.News;
import me.pwcong.rtfrxmvp.mvp.presenter.NewsFragmentPresenter;
import me.pwcong.rtfrxmvp.mvp.view.BaseView;
import me.pwcong.rtfrxmvp.view.RecyclerViewDivider;
import rx.functions.Action1;

/**
 * Created by pwcong on 2016/8/20.
 */
public class NewsFragment extends BaseFragment implements BaseView.NewsFragmentView,NewsFragmentAdapter.NewsItemListener {

    private final String TAG=getClass().getSimpleName();

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    String type;
    NewsFragmentPresenter presenter;

    public static NewsFragment newInstant(String type){

        Bundle bundle=new Bundle();
        bundle.putString(Constants.TOUTIAO_TYPE,type);

        NewsFragment newsFragment=new NewsFragment();
        newsFragment.setArguments(bundle);

        return newsFragment;
    }



    @Override
    protected void initVariable() {

        type=getArguments().getString(Constants.TOUTIAO_TYPE);
        presenter=new NewsFragmentPresenter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new RecyclerViewDivider(getContext(),RecyclerViewDivider.VERTICAL_LIST));

        RxSwipeRefreshLayout.refreshes(refreshLayout).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                presenter.initNewsFragmentData(type);
                refreshLayout.setRefreshing(false);
                Log.d(TAG, "call: 刷新数据");
            }
        });

    }

    @Override
    protected void doAction() {
        presenter.initNewsFragmentData(type);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_news;
    }


    @Override
    public void showError() {

    }

    @Override
    public void setData(List<News> data) {

        recyclerView.setAdapter(new NewsFragmentAdapter(getContext(),data,this));
        Log.d(TAG, "setData: 设置数据");
    }

    @Override
    public void onNewsItemInteraction(News news) {
        Log.d(TAG, "onNewsItemInteraction: "+news.getTitle());
    }
}
