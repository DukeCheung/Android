package com.pandatem.jiyi.RecycleView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> views;
    private View view;
    public HomeViewHolder( Context context,@NonNull View itemView,ViewGroup viewGroup) {
        super(itemView);
         view= itemView;
         views = new SparseArray<View>();
    }

    public static   HomeViewHolder get(Context _context, ViewGroup _viewGroup, int _layoutId){
        View _view = LayoutInflater.from(_context).inflate(_layoutId,_viewGroup,false);
        HomeViewHolder holder = new  HomeViewHolder(_context,_view,_viewGroup);
        return holder;
    }

    public <T extends View> T getView(int _viewId){
        View _view = views.get(_viewId);
        if(_view == null){
            _view  = view.findViewById(_viewId);
            views.put(_viewId,_view);
        }
        return (T)_view;
    }

}
