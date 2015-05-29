package com.liyuejiao.player.online;

import java.util.ArrayList;
import java.util.List;

import com.liyuejiao.player.R;
import com.liyuejiao.player.R.id;
import com.liyuejiao.player.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OnlineAdapter extends BaseAdapter {

    private Context mContext;
    private List<OnlineVideo> mVideoList;

    public OnlineAdapter(Context context) {
        mContext = context;
        mVideoList = new ArrayList<OnlineVideo>();
    }

    public void updateList(List<OnlineVideo> videoList) {
        if (videoList != null) {
            mVideoList.clear();
            mVideoList.addAll(videoList);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mVideoList == null ? 0 : mVideoList.size();
    }

    @Override
    public OnlineVideo getItem(int position) {
        return mVideoList == null ? null : mVideoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.local_video_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OnlineVideo video = getItem(position);
        if (video != null) {
            holder.populateViews(video);
        }
        return convertView;
    }

    class ViewHolder {
        TextView mVideoName;
        TextView mVideoSize;

        public ViewHolder(View root) {
            mVideoName = (TextView) root.findViewById(R.id.local_video_title);
            mVideoSize = (TextView) root.findViewById(R.id.local_video_size);
        }

        public void populateViews(OnlineVideo video) {
            mVideoName.setText(video.name);
            mVideoSize.setText(video.uri);
        }
    }
}
