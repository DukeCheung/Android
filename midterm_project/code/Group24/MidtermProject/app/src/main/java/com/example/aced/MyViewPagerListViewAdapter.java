package com.example.aced;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.text.TextUtils;
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

public class MyViewPagerListViewAdapter extends BaseAdapter {

    private Context _context;
    private int _layoutId;
    private List<Equipment> _equipments;
    private List<Hero.Skill> _skills;
    private List<Inscription> _inscriptions;
    private int _type;


    public MyViewPagerListViewAdapter(Context context,int layoutId,List data,int type){

        _layoutId=layoutId;
        _context=context;
        _type = type;
        if(_type ==0){
            _skills = data ;
        }else if(_type ==1){
            _equipments = data;
        }else if(_type == 2){
            _inscriptions = data;

        }

    }

    @Override
    public int getCount() {
        if(_type ==0){
            if(_skills == null){
                return 0;
            }else {
                return _skills.size();
            }

        }else if(_type ==1){
            if(_equipments == null){
                return 0;
            }else {
                return _equipments.size();
            }
        }else if(_type == 2){
            if(_inscriptions == null){
                return 0;
            }else {
                return _inscriptions.size();
            }

        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(_type ==0){
            if(_skills == null){
                return null;
            }else {
                return _skills.get(position);
            }

        }else if(_type ==1){
            if(_equipments == null){
                return null;
            }else {
                return _equipments.get(position);
            }
        }else if(_type == 2){
            if(_inscriptions == null){
                return null;
            }else {
                return _inscriptions.get(position);
            }

        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view ;
        final ViewHolder holder;
        if(convertView ==null){
            view = LayoutInflater.from(_context).inflate(_layoutId, null);
            holder = new MyViewPagerListViewAdapter.ViewHolder();
            holder.icon = (ImageView)view.findViewById(R.id.vp_lv_icon);
            holder.name = (TextView)view.findViewById(R.id.vp_lv_name);
            holder.level_position_price = (TextView)view.findViewById(R.id.vp_lv_level);
            holder.description = (TextView)view.findViewById(R.id.vp_lv_description);
            view.setTag(holder);

        }else{
            view = convertView;
            holder = (MyViewPagerListViewAdapter.ViewHolder)view.getTag();
        }


        if(_type ==0){
            String skillId =Integer.toString( _skills.get(position).getSkillId());
            String heroId = skillId.substring(0,3);
            byte[] image = _skills.get(position).get_skillimage();
            Bitmap bmp = null;
            if(image !=null)
            {
                bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
            }
            holder.icon.setImageBitmap(bmp);
            holder.name.setText(_skills.get(position).getSkillName().toString());
            holder.level_position_price.setText("消耗"+_skills.get(position).getSkillWaste()+"");
            holder.description.setText(_skills.get(position).getSkillDescription().toString());

        }else if(_type ==1){
            String id =Integer.toString( _equipments.get(position).getEquipmentId());
            byte[] image = _equipments.get(position).getEquipmentImage();
            Bitmap bmp = null;
            if(image !=null)
            {
                bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
            }
            holder.icon.setImageBitmap(bmp);
            holder.name.setText(_equipments.get(position).getEquipmentName().toString());
            holder.level_position_price.setText(_equipments.get(position).getEquipmentPrice()+"");
            holder.description.setText(_equipments.get(position).getEquipmentDescription().toString());
        }else if(_type == 2){
            byte[] image = _inscriptions.get(position).getInscriptionImage();
            Bitmap bmp = null;
            if(image !=null)
            {
                bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
            }
            holder.icon.setImageBitmap(bmp);
            holder.name.setText(_inscriptions.get(position).getInscriptionName().toString());
            holder.level_position_price.setText(_inscriptions.get(position).getInscriptionGrade()+"");
            holder.description.setText(_inscriptions.get(position).getInscriptionDescription().toString());

        }

        holder.description.setOnClickListener(new View.OnClickListener() {
            Boolean flag = true;
            @Override
            public void onClick(View v) {
                if(flag){
                    flag = false;
                    holder.description.setEllipsize(null); // 展开
                    //holder.description.setLines(20);
                    holder.description.setMaxLines(20);
                }else{
                    flag = true;
                    holder.description.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                    holder.description.setMaxLines(4);
                }
            }
        });
        return view;
    }

    private  class ViewHolder{
        public ImageView icon;
        public TextView name;
        public TextView level_position_price;
        public TextView description;

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
