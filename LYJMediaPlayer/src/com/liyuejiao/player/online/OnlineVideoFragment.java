package com.liyuejiao.player.online;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.liyuejiao.player.R;
import com.liyuejiao.player.WebVideoActivity;

public class OnlineVideoFragment extends Fragment {
    private ListView mListView;
    private OnlineAdapter mOnlineAdapter;
    private ArrayList<OnlineVideo> mOnlineList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOnlineList = new ArrayList<OnlineVideo>();
        mOnlineList.add(new OnlineVideo("迅雷看看", "http://m.kankan.com"));
        mOnlineList.add(new OnlineVideo("优酷", "http://3g.youku.com"));
        mOnlineList.add(new OnlineVideo("爱奇艺", "http://m.iqiyi.com"));
        mOnlineList.add(new OnlineVideo("腾讯视频", "http://m.v.qq.com"));
        mOnlineList.add(new OnlineVideo("搜狐视频", "http://m.tv.sohu.com"));
        mOnlineList.add(new OnlineVideo("PPTV聚力", "http://m.pptv.com"));
        mOnlineList.add(new OnlineVideo("乐视视频", "http://m.letv.com"));
        mOnlineList.add(new OnlineVideo("风行网", "http://www.fun.tv"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_online, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mOnlineAdapter = new OnlineAdapter(getActivity());
        mOnlineAdapter.updateList(mOnlineList);

        mListView = (ListView) getView().findViewById(R.id.listView);
        mListView.setAdapter(mOnlineAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            OnlineVideo onlineVideo = mOnlineAdapter.getItem(position);

            Intent intent = new Intent(getActivity(), WebVideoActivity.class);
            intent.putExtra("path", onlineVideo.uri);
            startActivity(intent);
        }
    };
}
