package com.example.zhangxing.storage2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.BaseAdapter;

import java.util.Currency;
import java.util.List;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.content.Context;

public class MyListViewAdapter extends BaseAdapter{

    private List<Comment> list;
    private Context context;
    private MemberDB memberDB;

    public MyListViewAdapter(Context context, List<Comment> list, MemberDB memberDB){
        this.context = context;
        this.list = list;
        this.memberDB = memberDB;
    }
    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void refresh(List<Comment> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public Object getItem(int i) {
        if (list == null) {
            return null;
        }
        return list.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // 新声明一个View变量和ViewHoleder变量,ViewHolder类在下面定义。
        View convertView;
        ViewHolder viewHolder;
        // 当view为空时才加载布局，否则，直接修改内容
        Member m = memberDB.getByUsername(list.get(i).getUsername());
        if (view == null) {
            // 通过inflate的方法加载布局，context需要在使用这个Adapter的Activity中传入。
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.portrait = (ImageView)convertView.findViewById(R.id.item_portrait);
            viewHolder.username = (TextView)convertView.findViewById(R.id.item_username);
            viewHolder.time = (TextView)convertView.findViewById(R.id.item_time);
            viewHolder.comment = (TextView)convertView.findViewById(R.id.item_comment);
            viewHolder.count = (TextView)convertView.findViewById(R.id.item_count);
            viewHolder.like = (ImageView)convertView.findViewById(R.id.item_like);
            convertView.setTag(viewHolder);

        } else { // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 从viewHolder中取出对应的对象，然后赋值给他们
        Drawable bd= new BitmapDrawable(m.getPortrait());
        viewHolder.portrait.setImageDrawable(bd);
        if(list.get(i).getLike()==0){
            viewHolder.like.setImageResource(R.drawable.white);
        }
        else{
            viewHolder.like.setImageResource(R.drawable.red);
        }
        viewHolder.username.setText(list.get(i).getUsername());
        viewHolder.time.setText(list.get(i).getTime());
        viewHolder.comment.setText(list.get(i).getComment());
        viewHolder.count.setText(list.get(i).getCount().toString());
        final int position = i;
        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemLikeListener.onLikeClick(position);
            }
        });
        // 将这个处理好的view返回
        return convertView;
    }

    private class ViewHolder {
        public ImageView portrait;
        public TextView username;
        public TextView time;
        public TextView comment;
        public TextView count;
        public ImageView like;
    }
    private Resources getResources() {
        Resources mResources = null;
        mResources = getResources();
        return mResources;
    }

    public interface onItemLikeListener {
        void onLikeClick(int i);
    }

    private onItemLikeListener mOnItemLikeListener;

    public void setOnItemLikeClickListener(onItemLikeListener mOnItemLikeListener) {
        this.mOnItemLikeListener = mOnItemLikeListener;
    }


}