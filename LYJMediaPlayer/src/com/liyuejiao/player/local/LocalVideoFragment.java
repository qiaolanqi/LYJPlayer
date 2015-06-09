package com.liyuejiao.player.local;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.liyuejiao.player.PlayerActivity;
import com.liyuejiao.player.R;
import com.liyuejiao.player.util.Constant;
import com.liyuejiao.player.util.VideoScanner;
import com.liyuejiao.player.util.VideoScanner.OnScanListener;

public class LocalVideoFragment extends Fragment {

    private ProgressBar mProgressBar;
    private ListView mListView;
    private LocalVideoAdapter mLocalVideoAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lyj", "LocalVideoFragment onCreate");
        VideoScanner scanner = new VideoScanner(getActivity());
        scanner.setOnScanListener(mOnScanListener);
        scanner.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("lyj", "LocalVideoFragment onCreateView");
        return inflater.inflate(R.layout.fragment_local, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("lyj", "LocalVideoFragment onActivityCreated");
        mLocalVideoAdapter = new LocalVideoAdapter(getActivity());
        mListView = (ListView) getView().findViewById(R.id.listView);
        mListView.setAdapter(mLocalVideoAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
        
        mProgressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private OnScanListener mOnScanListener = new OnScanListener() {

        @Override
        public void onStart() {
        }

        @Override
        public void onStop(List<LocalVideo> result) {
            mLocalVideoAdapter.updateList(result);
            mLocalVideoAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.GONE);
        }
    };

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            LocalVideo localVideo = mLocalVideoAdapter.getItem(position);
            Intent intent = new Intent(getActivity(), PlayerActivity.class);
            intent.putExtra(Constant.KEY_TITLE, localVideo.name);
            intent.putExtra(Constant.KEY_PATH, localVideo.path);
            startActivity(intent);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("lyj", "LocalVideoFragment onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("lyj", "LocalVideoFragment onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("lyj", "LocalVideoFragment onDetach");
    }

    
     
}
