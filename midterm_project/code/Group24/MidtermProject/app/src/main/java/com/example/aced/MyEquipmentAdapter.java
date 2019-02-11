package com.example.aced;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.BaseAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class MyEquipmentAdapter extends BaseAdapter{
    private List<Equipment> list;
    private Context context;

    public MyEquipmentAdapter(Context context, List<Equipment> list){
        this.context = context;
        this.list = list;
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

    public void refresh(List<Equipment> list){
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
        final ViewHolder viewHolder;
        // 当view为空时才加载布局，否则，直接修改内容
        if (view == null) {
            // 通过inflate的方法加载布局，context需要在使用这个Adapter的Activity中传入。
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_eqiupment, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.equipmentImage);
            viewHolder.name = (TextView)convertView.findViewById(R.id.equipmentName);
            viewHolder.price = (TextView)convertView.findViewById(R.id.equipmentPrice);
            viewHolder.description = (TextView)convertView.findViewById(R.id.equipmentDescription);
            viewHolder.type = (TextView)convertView.findViewById(R.id.equipmentType);
            viewHolder.skill = (TextView)convertView.findViewById(R.id.equipmentSkill);
            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else { // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 从viewHolder中取出对应的对象，然后赋值给他们
        byte[] image = list.get(i).getEquipmentImage();
        Bitmap bmp = null;
        if(image !=null)
        {
            bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        }
        viewHolder.image.setImageBitmap(bmp);
        viewHolder.name.setText(list.get(i).getEquipmentName());
        String str = String.valueOf(list.get(i).getEquipmentPrice());
        viewHolder.price.setText(str);
        int temp = list.get(i).getEquipmentType();
        switch (temp){
            case 1:
                viewHolder.type.setText("攻击");
                break;
            case 2:
                viewHolder.type.setText("法术");
                break;
            case 3:
                viewHolder.type.setText("防御");
                break;
            case 4:
                viewHolder.type.setText("移动");
                break;
            case 5:
                viewHolder.type.setText("打野");
                break;
            case 7:
                viewHolder.type.setText("辅助");
                break;
            default:
                break;
        }
        viewHolder.description.setText(list.get(i).getEquipmentDescription());
        str = list.get(i).getEquipmentSkill();
        if(str==null){
            viewHolder.skill.setText("");
            viewHolder.skill.setVisibility(View.GONE);
        }else{
            viewHolder.skill.setVisibility(View.VISIBLE);
            viewHolder.skill.setText(str);
        }
        final int pos = i;
        viewHolder.skill.setOnClickListener(new View.OnClickListener() {
            Boolean flag = false;
            @Override
            public void onClick(View v) {
                if(flag){
                    flag = false;
                    viewHolder.skill.setEllipsize(null); // 展开
                    viewHolder.skill.setSingleLine(flag);
                }else{
                    flag = true;
                    viewHolder.skill.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                    viewHolder.skill.setSingleLine(flag);
                }
            }
        });
        // 将这个处理好的view返回
        return convertView;
    }

    private class ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView price;
        public TextView type;
        public TextView description;
        public TextView skill;
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
