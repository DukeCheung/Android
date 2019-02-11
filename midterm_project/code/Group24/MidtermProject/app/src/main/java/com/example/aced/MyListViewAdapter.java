package com.example.aced;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class MyListViewAdapter extends BaseAdapter {

    Context _context;
    int _layoutId;
    List<Hero> _heros;
    public MyListViewAdapter(){
        super();
    }

    public MyListViewAdapter(Context context,int layoutId,List heros){
        _heros = heros;
        _layoutId = layoutId;
        _context = context;
    }
    @Override
    public int getCount() {

        if(_heros == null)
            return 0;
        if(_heros.size()%3==0){
            return _heros.size()/3;
        }else if(_heros.size()%3 == 1){
            return (_heros.size()/3)+1;
        }else if(_heros.size()%3 ==2){
            return (_heros.size()/3)+1;
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(_heros == null)
            return null;
        return _heros.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view ;
        final ViewHolder holder;
        if(convertView ==null){
            view = LayoutInflater.from(_context).inflate(_layoutId, null);
            holder = new ViewHolder();
            holder.hero_1_name  = (TextView)view.findViewById(R.id.tv_item_1_name);
            holder.hero_1_icon = (ImageView)view.findViewById(R.id.iv_item_1_icon);

            holder.hero_2_name  = (TextView)view.findViewById(R.id.tv_item_2_name);
            holder.hero_2_icon = (ImageView)view.findViewById(R.id.iv_item_2_icon);

            holder.hero_3_name  = (TextView)view.findViewById(R.id.tv_item_3_name);
            holder.hero_3_icon = (ImageView)view.findViewById(R.id.iv_item_3_icon);

            view.setTag(holder);

        }else{
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }



      if(_heros.size() > position*3) {
          //       Bitmap bitmap_1 = BitmapFactory.decodeByteArray(_heros.get(position*3).getIcon(),0,_heros.get(position*3).getIcon().length);

          //       holder.hero_1_icon.setImageBitmap(bitmap_1);

          holder.hero_1_name.setText(_heros.get(position * 3).getName().toString());
          String id = Integer.toString(_heros.get(position * 3).getId());

          byte[] image = _heros.get(position * 3).getIcon();
          Bitmap bmp = null;
          if(image !=null)
          {
              bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
          }
          holder.hero_1_icon.setImageBitmap(bmp);

          holder.hero_1_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _onItemHero1ClickListener.onHero1Click(position);
                }
            });

            //最后一行，第二列，第三列没有
            if (position * 3 + 1 == _heros.size()) {
                holder.hero_2_icon.setVisibility(View.INVISIBLE);
                holder.hero_2_name.setVisibility(View.INVISIBLE);

                holder.hero_3_icon.setVisibility(View.INVISIBLE);
                holder.hero_3_name.setVisibility(View.INVISIBLE);

            } else {
                holder.hero_2_icon.setVisibility(View.VISIBLE);
                holder.hero_2_name.setVisibility(View.VISIBLE);

                //           Bitmap bitmap_2 = BitmapFactory.decodeByteArray(_heros.get(position*3+1).getIcon(),0,_heros.get(position*3+1).getIcon().length);

                //           holder.hero_2_icon.setImageBitmap(bitmap_2);
                holder.hero_2_name.setText(_heros.get(position * 3 + 1).getName().toString());
                byte[] image2 = _heros.get(position * 3 + 1).getIcon();
                Bitmap bmp2 = null;
                if(image2 !=null)
                {
                    bmp2 = BitmapFactory.decodeByteArray(image2, 0, image2.length);
                }
                holder.hero_2_icon.setImageBitmap(bmp2);
                holder.hero_2_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _onItemHero2ClickListener.onHero2Click(position);
                    }
                });
            }

            //最后一行，第三列没有
            if (position * 3 + 2 == _heros.size() || position * 3 + 1 == _heros.size()) {

                holder.hero_3_icon.setVisibility(View.INVISIBLE);
                holder.hero_3_name.setVisibility(View.INVISIBLE);

            } else {
                holder.hero_3_icon.setVisibility(View.VISIBLE);
                holder.hero_3_name.setVisibility(View.VISIBLE);

                //           Bitmap bitmap_3 = BitmapFactory.decodeByteArray(_heros.get(position*3+2).getIcon(),0,_heros.get(position*3+2).getIcon().length);

//            holder.hero_3_icon.setImageBitmap(bitmap_3);
                holder.hero_3_name.setText(_heros.get(position * 3 + 2).getName().toString());
                byte[] image3 = _heros.get(position * 3 + 2).getIcon();
                Bitmap bmp3 = null;
                if(image3 !=null)
                {
                    bmp3 = BitmapFactory.decodeByteArray(image3, 0, image3.length);
                }
                holder.hero_3_icon.setImageBitmap(bmp3);

                holder.hero_3_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _onItemHero3ClickListener.onHero3Click(position);
                    }
                });
            }


        }

        return view;
    }

    public interface onItemHero1ClickListener{
        void onHero1Click(int position);
    }

    private onItemHero1ClickListener _onItemHero1ClickListener;

    public void setOnItemHero1ClickListener(onItemHero1ClickListener monItemHero1ClickListener){
        _onItemHero1ClickListener = monItemHero1ClickListener;
    }

    public interface onItemHero2ClickListener{
        void onHero2Click(int position);
    }

    private onItemHero2ClickListener _onItemHero2ClickListener;

    public void setOnItemHero2ClickListener(onItemHero2ClickListener monItemHero2ClickListener){
        _onItemHero2ClickListener = monItemHero2ClickListener;
    }

    public interface onItemHero3ClickListener{
        void onHero3Click(int position);
    }

    private onItemHero3ClickListener _onItemHero3ClickListener;

    public void setOnItemHero3ClickListener(onItemHero3ClickListener monItemHero3ClickListener){
        _onItemHero3ClickListener = monItemHero3ClickListener;
    }

    public void refresh(List newlist){
        _heros = newlist;
        notifyDataSetChanged();
    }

    private  class ViewHolder{
        public TextView hero_1_name;
        public ImageView hero_1_icon;

        public TextView hero_2_name;
        public ImageView hero_2_icon;

        public TextView hero_3_name;
        public ImageView hero_3_icon;

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
