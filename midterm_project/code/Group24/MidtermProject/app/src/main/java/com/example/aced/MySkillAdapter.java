package com.example.aced;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MySkillAdapter extends BaseAdapter {
    private List<Hero.Skill> list;
    private Context context;

    public MySkillAdapter(Context _context, List<Hero.Skill> _list){
        this.list = _list;
        this.context = _context;
    }

    public void refresh(List<Hero.Skill> _list){
        list = _list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(list == null){
            return 0;
        }
        return list.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        if(list == null){
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
        if (view == null) {
            // 通过inflate的方法加载布局，context需要在使用这个Adapter的Activity中传入。
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_skill, null);
            viewHolder = new ViewHolder();
            viewHolder.skillName = (TextView) convertView.findViewById(R.id.skillName);
            viewHolder.skillPosition = (TextView) convertView.findViewById(R.id.skillPosition);
            viewHolder.skillDescription = (TextView) convertView.findViewById(R.id.skillDescription);
            viewHolder.skillCool = (TextView) convertView.findViewById(R.id.skillCool);
            viewHolder.skillWaste = (TextView) convertView.findViewById(R.id.skillWaste);
            viewHolder.skillTip = (TextView) convertView.findViewById(R.id.skillTip);

            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else { // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 从viewHolder中取出对应的对象，然后赋值给他们
        viewHolder.skillName.setText("名称"+ list.get(i).getSkillName());
        viewHolder.skillPosition.setText("位置："+ list.get(i).getSkillPosition());
        viewHolder.skillDescription.setText("技能描述："+ list.get(i).getSkillDescription());
        viewHolder.skillCool.setText("冷却时间：" + list.get(i).getSkillCool().toString());
        viewHolder.skillWaste.setText("蓝耗：" + list.get(i).getSkillWaste().toString());
        viewHolder.skillTip.setText("技巧："+ list.get(i).getSkillTips());
        // 将这个处理好的view返回
        return convertView;
    }

    private class ViewHolder {
        public TextView skillName;
        public TextView skillPosition;
        public TextView skillDescription;
        public TextView skillCool;
        public TextView skillWaste;
        public TextView skillTip;
    }
}
