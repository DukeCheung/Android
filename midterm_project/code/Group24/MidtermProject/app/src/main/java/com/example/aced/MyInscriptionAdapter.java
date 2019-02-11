package com.example.aced;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MyInscriptionAdapter extends BaseAdapter {
    private Context context;
    private List<Inscription> list;

    public MyInscriptionAdapter(Context context, List<Inscription> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount(){
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list == null) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        // 新声明一个View变量和ViewHoleder变量,ViewHolder类在下面定义。
        View convertView;
        final MyInscriptionAdapter.ViewHolder viewHolder;
        // 当view为空时才加载布局，否则，直接修改内容
        if (view == null) {
            // 通过inflate的方法加载布局，context需要在使用这个Adapter的Activity中传入。
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_inscription, null);
            viewHolder = new MyInscriptionAdapter.ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.iv_ins_img);
            viewHolder.name = (TextView)convertView.findViewById(R.id.tv_ins_name);
            viewHolder.description = (TextView)convertView.findViewById(R.id.iv_ins_des);
            viewHolder.grade = (TextView)convertView.findViewById(R.id.tv_ins_grade);
            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else { // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (MyInscriptionAdapter.ViewHolder) convertView.getTag();
        }

        byte[] image = list.get(i).getInscriptionImage();
        Bitmap bmp = null;
        if(image !=null)
        {
            bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        }
        viewHolder.image.setImageBitmap(bmp);
        viewHolder.name.setText(list.get(i).getInscriptionName());
        viewHolder.description.setText(list.get(i).getInscriptionDescription());
        viewHolder.grade.setText("等级"+list.get(i).getInscriptionGrade());
        return convertView;
    }

    private class ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView grade;
        public TextView description;
    }

    public void refresh(List<Inscription> newlist){
        this.list = newlist;
        notifyDataSetChanged();
    }

    public Bitmap getBitmap(String path) throws IOException {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
