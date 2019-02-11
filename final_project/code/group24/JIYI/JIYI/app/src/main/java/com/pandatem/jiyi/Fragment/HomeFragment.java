package com.pandatem.jiyi.Fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.pandatem.jiyi.CircleImageView.CircleImageView;
import com.pandatem.jiyi.ConversationActivity;
import com.pandatem.jiyi.MainActivity;
import com.pandatem.jiyi.MyDB.Card;
import com.pandatem.jiyi.MyDB.MyDatabase;
import com.pandatem.jiyi.R;
import com.pandatem.jiyi.RecycleView.HomeRecycleViewAdapter;
import com.pandatem.jiyi.RecycleView.HomeViewHolder;
import com.rey.material.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private  View mView;
    private List mData;
    private  HomeRecycleViewAdapter homeAdapter;
    private MyDatabase myDB;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        mView = view;
        mData = new ArrayList<Card>();
       for(int i=0;i<5;i++){
        Card card = new Card();
        mData.add(card);
       }
        initRecycleView();
        initRefreshLayout();
        return view;
    }


    public void initRecycleView(){
       RecyclerView recyclerview =(RecyclerView) mView.findViewById(R.id.recyclerview);


        homeAdapter = new  HomeRecycleViewAdapter<Card>(getContext(),R.layout.item,mData) {

            @Override
            public void convert(HomeViewHolder holder, final Card card) {
                ImageView img_cover  = (ImageView)holder.getView(R.id.img_cover);
                CircleImageView img_user_cover=(CircleImageView)holder.getView(R.id.img_person_cover);
                final TextView tv_content = (TextView)holder.getView(R.id.tv_content);
                TextView tv_position = (TextView)holder.getView(R.id.tv_position);
                TextView tv_user_name=(TextView)holder.getView(R.id.tv_person_name);

                tv_content.setText(card.getContent());
                tv_position.setText(card.getPosition());
                tv_user_name.setText(card.getPerson().getName());


                byte bytes1[] = card.getPerson().getCoverBitmapBytes();
                if(bytes1 != null) {
                    Bitmap bmp1 = BitmapFactory.decodeByteArray(bytes1, 0, bytes1.length);
                    if(bmp1 !=null ){
                        img_user_cover.setImageBitmap(bmp1);
                    }else{
                        img_user_cover.setImageResource(R.mipmap.user);
                    }
                }else{
                    img_user_cover.setImageResource(R.mipmap.user);
                }

                byte bytes2[] = card.getCoverBitmapBytes();
                if(bytes2 != null) {
                    Bitmap bmp2 = BitmapFactory.decodeByteArray(bytes2, 0, bytes2.length);
                    if(bmp2 != null){
                        img_cover.setImageBitmap(bmp2);

                    }else {
                        img_cover.setImageResource(R.mipmap.demo);
                    }
                }else {
                    img_cover.setImageResource(R.mipmap.demo);
                }




                //display only part of the text
                tv_content.setLines(4);
                tv_content.setOnClickListener(new View.OnClickListener() {
                    Boolean flag = true;
                    @Override
                    public void onClick(View v) {
                        if(flag){
                            flag = false;
                            tv_content.setEllipsize(null);
                            tv_content.setMaxLines(100);
                        }else{
                            flag = true;
                            tv_content.setEllipsize(TextUtils.TruncateAt.END);
                            tv_content.setLines(4);
                        }
                    }
                });

                //click the user's cover to start a conversation
                img_user_cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ConversationActivity.class);
                        Bundle bundle = new Bundle();
                        String sender = ((MainActivity)getActivity()).getGlobalUsername();
                        String receiver = card.getPerson().getName();
                        if(sender!=null){
                            List<String> users = new ArrayList<String>();
                            users.add(sender);
                            users.add(receiver);
                            bundle.putSerializable("users", (Serializable) users);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getContext(),"Please login before sending messages.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };

        homeAdapter.setOnItemClickListener(new HomeRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        });

        recyclerview.setLayoutManager(new GridLayoutManager(getContext(),1));


        recyclerview.setAdapter(homeAdapter);




    }

    public void initRefreshLayout(){
        final SwipeRefreshLayout  mRefreshLayout = (SwipeRefreshLayout)mView.findViewById(R.id.layout_swipe_refresh);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            public void onRefresh() {
                //我在List最前面加入一条数据

                mData.clear();
                myDB = new MyDatabase(getContext());
                mData.addAll( myDB.queryAllCard());

                //数据重新加载完成后，提示数据发生改变，并且设置现在不在刷新
                homeAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

}
