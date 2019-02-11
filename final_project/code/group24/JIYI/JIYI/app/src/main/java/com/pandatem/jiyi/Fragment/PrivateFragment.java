package com.pandatem.jiyi.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.pandatem.jiyi.ConversationActivity;
import com.pandatem.jiyi.MainActivity;
import com.pandatem.jiyi.Message;
import com.pandatem.jiyi.MyDB.Conversation;
import com.pandatem.jiyi.MyDB.MyDatabase;
import com.pandatem.jiyi.MyDB.Person;
import com.pandatem.jiyi.R;
import com.pandatem.jiyi.RecycleView.HomeRecycleViewAdapter;
import com.pandatem.jiyi.RecycleView.HomeViewHolder;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PrivateFragment extends Fragment {
    private HomeRecycleViewAdapter myAdapter;
    private RecyclerView conversations;
    private List<Conversation> conversationList;
    private String userName;

    public PrivateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_private, container, false);

        //initial database
        final MyDatabase myDB = new MyDatabase(getContext());
        conversationList = new ArrayList<>();

        //initial views
        conversations = (RecyclerView) view.findViewById(R.id.rv_con);
        myAdapter = new HomeRecycleViewAdapter<Conversation>(getContext(), R.layout.item_conversation,conversationList) {
            @Override
            public void convert(HomeViewHolder holder, Conversation m) {
                ImageView avatar = holder.getView(R.id.iv_avatar);
                TextView otherName = holder.getView(R.id.tv_othername);
                TextView lastMess = holder.getView(R.id.tv_lastmess);
                TextView lastCon = holder.getView(R.id.tv_lastcon);

                byte[] cover = m.getOther().getCoverBitmapBytes();

                avatar.setImageBitmap(BitmapFactory.decodeByteArray(cover, 0,cover.length));
                otherName.setText(m.getOther().getName());
                lastMess.setText(m.getLastMessage());

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = simpleDateFormat.format(m.getLastCon());

                lastCon.setText(date);
            }
        };
        myAdapter.setOnItemClickListener(new HomeRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Conversation mCon = (Conversation) myAdapter.getItem(position);
                String sender = ((MainActivity)getActivity()).getGlobalUsername();
                String receiver = mCon.getOther().getName();

                List<String> users = new ArrayList<String>();
                users.add(sender);
                users.add(receiver);

                Intent intent = new Intent(getActivity(), ConversationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("users", (Serializable) users);
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });
        conversations.setAdapter(myAdapter);
        conversations.setLayoutManager(new LinearLayoutManager(getContext()));
        initConversations();
        return view;
    }

    public void initConversations(){
        conversationList.clear();
        final MyDatabase myDB = new MyDatabase(getContext());
        userName = ((MainActivity)getActivity()).getGlobalUsername();
        List<Message> allMessage = myDB.queryAllMessages(userName);
        List<String> other = new ArrayList<>();

        for(int i = 0; i < allMessage.size(); i++){
            String othername = allMessage.get(i).getSender().equals(userName) ? allMessage.get(i).getReceiver() : allMessage.get(i).getSender();
            if(!other.contains(othername)){
                other.add(othername);
            }
        }

        for(int i = 0; i < other.size(); i++){
            List<Message> con = myDB.queryConversation(userName,other.get(i));
            Message lastmessage = con.get(con.size()-1);
            conversationList.add(new Conversation(new Person(other.get(i),"",myDB.getCover(other.get(i))),
                    lastmessage.getTime(),lastmessage.getMessage()));
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null){
            return;
        }
        Bundle bundle = data.getExtras();
        Message lastmessage = (Message) bundle.getSerializable("lastMsg");

        String othername = lastmessage.getReceiver();

        if(lastmessage == null){
            return;
        }

        for(int i = 0; i < conversationList.size(); i++){
            if(conversationList.get(i).getOther().getName().equals(othername)){
                conversationList.get(i).setLastMessage(lastmessage.getMessage());
                conversationList.get(i).setLastCon(lastmessage.getTime());
                myAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if ((isVisibleToUser && isResumed())) {
            onResume();
            //read all cards from database and display them;
            if(userName.equals(((MainActivity)getActivity()).getGlobalUsername())){

            }else{
                userName = ((MainActivity)getActivity()).getGlobalUsername();
                initConversations();
            }
        } else if (!isVisibleToUser) {
            //   Timber.i("On Pause on %s Fragment Invisble", getClass().getSimpleName());
            onPause();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            //  Timber.i("On Resume on %s Fragment Visible", getClass().getSimpleName());
            //TODO give the signal that the fragment is visible
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //TODO give the signal that the fragment is invisible
    }

}
