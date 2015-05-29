package com.liyuejiao.player;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.liyuejiao.player.VideoScanner.OnScanListener;

public class LocalVideoFragment extends Fragment {

    private ListView mListView;
    private LocalVideoAdapter mLocalVideoAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VideoScanner scanner = new VideoScanner(getActivity());
        scanner.setOnScanListener(mOnScanListener);
        scanner.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_local, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLocalVideoAdapter = new LocalVideoAdapter(getActivity());
        mListView = (ListView) getView().findViewById(R.id.listView);
        mListView.setAdapter(mLocalVideoAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
    }

    private OnScanListener mOnScanListener = new OnScanListener() {

        @Override
        public void onStart() {
            
        }

        @Override
        public void onStop(List<LocalVideo> result) {
            mLocalVideoAdapter.updateList(result);
            mLocalVideoAdapter.notifyDataSetChanged();
        }
    };
    
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), PlayerActivity.class);
            startActivity(intent);
        }
    };
}
