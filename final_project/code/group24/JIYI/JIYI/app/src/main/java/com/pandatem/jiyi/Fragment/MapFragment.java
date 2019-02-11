package com.pandatem.jiyi.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.AMapCameraInfo;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import com.pandatem.jiyi.CardDetailActivity;
import com.pandatem.jiyi.DetailActivity;
import com.pandatem.jiyi.MainActivity;
import com.pandatem.jiyi.MyDB.Card;
import com.pandatem.jiyi.MyDB.MyDatabase;
import com.pandatem.jiyi.MyDB.Person;
import com.pandatem.jiyi.MyInfoWindowAdapter;
import com.pandatem.jiyi.R;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class MapFragment extends Fragment implements AMapLocationListener {
    String userName = "";
    Card card;
    MapView mMapView = null;
    List<Card> mCards;
    ArrayList <Marker> PoiMarkers;
    ArrayList <Marker> MapMarkers;
    ArrayList<Marker> CardMarkers;
    private AMap aMap ;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    Boolean hasLogin;

    public MapFragment() {
        // Required empty pu  blic constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCards = new ArrayList<Card>();
        hasLogin = false;
        PoiMarkers = new ArrayList<>();

        MapMarkers = new ArrayList<>();
        CardMarkers = new ArrayList<>();

        mLocationClient  =new AMapLocationClient(getContext());
        mLocationOption = new AMapLocationClientOption();
        card = new Card();

        mLocationClient.setLocationListener(this);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, null);
        mMapView = (MapView)view.findViewById(R.id.mapI);
        mMapView.onCreate(savedInstanceState);

        if(aMap == null){
            aMap = mMapView.getMap();
        }
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setZoomControlsEnabled(false);//设置地图缩放按钮
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。




        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(hasLogin == true) {
                    MarkerOptions markerOption = new MarkerOptions();
                    markerOption.position(latLng);
                    markerOption.title("zhang");
                    markerOption.draggable(false);//设置Marker可拖动
                    markerOption.setFlat(false);//设置marker平贴地图效果
                    markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.marker_bg)));

                    MapMarkers.add(aMap.addMarker(markerOption));
                    double x = latLng.latitude;
                    double y = latLng.longitude;
                    card.setLatLng_x(x);
                    card.setLatLng_y(y);
                }else{
                    Toast.makeText(getContext(),"请登录",Toast.LENGTH_SHORT).show();
                }
            }
        });

        FloatingActionButton flb_add = (FloatingActionButton)view.findViewById(R.id.flb_add);
        flb_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasLogin == true){
                card.setPerson(new Person(((MainActivity)getActivity()).getGlobalUsername(), "",null));
              //  Toast.makeText(getContext(), "flb", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),DetailActivity.class);
                intent.putExtra("card",card);
                startActivityForResult(intent,1);
            }else{
                Toast.makeText(getContext(),"请登录",Toast.LENGTH_SHORT).show();
            }
            }
        });

        FloatingActionButton  flb_location  = (FloatingActionButton)view.findViewById(R.id.flb_location);
        flb_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aMap.setMyLocationEnabled(true);
                mLocationOption.setOnceLocation(true);
                mLocationClient.setLocationOption(mLocationOption);
                //启动定位
                mLocationClient.startLocation();
            }
        });

        ImageView img_search = (ImageView)view.findViewById(R.id.img_search);
        final EditText edit_search = (EditText)view.findViewById(R.id.edit_search);

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PoiSearch.Query query = new PoiSearch.Query(edit_search.getText().toString(),"","");
                query.setPageSize(5);
                PoiSearch poiSearch = new PoiSearch(getContext(),query);
                poiSearch.searchPOIAsyn();
                poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                    @Override
                    public void onPoiSearched(PoiResult poiResult, int i) {
                        if(i == 1000){
                            Toast.makeText(getContext(), "search ok", Toast.LENGTH_SHORT).show();
                            ArrayList <PoiItem> PoiItems =   poiResult.getPois();
                                for(int k =0;k<PoiMarkers.size();k++){
                                    PoiMarkers.get(k).remove();
                                }
                             for(int j = 0; j <PoiItems.size();j++){
                                 PoiItem a =   PoiItems.get(j);

                                 MarkerOptions PoiMarker = new MarkerOptions();
                                 LatLng latLng = new LatLng(a.getLatLonPoint().getLatitude(),a.getLatLonPoint().getLongitude());
                                 PoiMarker.position(latLng);

                                 PoiMarker.title(a.getTitle());
                                 if(a.getSnippet().toString().length()>17) {
                                     PoiMarker.snippet(a.getSnippet().substring(0, 16));
                                 }else {
                                     PoiMarker.snippet(a.getSnippet());

                                 }
                                 PoiMarker.draggable(false);//设置Marker可拖动

                                 PoiMarker.setFlat(false);//设置marker平贴地图效果
                                 PoiMarkers.add( aMap.addMarker(PoiMarker));
                             }

                        }else{
                            //ERROR
                        }
                    }

                    @Override
                    public void onPoiItemSearched(PoiItem poiItem, int i) {

                    }
                });
            }
        });

        MyInfoWindowAdapter info = new MyInfoWindowAdapter(getContext()) {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if(id==R.id.imgv_info) {

                    Integer i = (Integer) v.getTag();
                    if(i != null) {
                 //       Toast.makeText(getContext(), "跳转，cardID = " +i.toString(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(),CardDetailActivity.class);
                        intent.putExtra("cardId",i);
                        startActivity(intent);
                    }

                }
            }
        };


        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.hideInfoWindow();
            }
        });
        aMap.setInfoWindowAdapter(info);

        return view;
    }

    private void displayCard(Card card ){
        LatLng latLng = new LatLng(card.getLatLng_x(),card.getLatLng_y());
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        //  markerOption.title("位置"+latLng.toString()).snippet(card.getContent());

        markerOption.draggable(false);//设置Marker可拖动
        //   markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
        //            .decodeResource(getResources(),R.mipmap.img)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(false);//设置marker平贴地图效果

        if(card.getPerson().getName().equals(userName)) {
            if(card.getPrivate()){
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.marker_bg_p)));
            }else {
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.marker_bg_red)));
            }
        }else{
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.marker_bg_green)));
        }
        markerOption.snippet(card.getContent());

        markerOption.title(card.getId()+","+card.getPerson().getName());

        CardMarkers.add(aMap.addMarker(markerOption));

        for(int j =0 ; j<MapMarkers.size();j++){
            MapMarkers.get(j).remove();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data1){
        if (resultCode == 1){
            card = (Card) data1.getSerializableExtra("card");
            displayCard(card);
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation != null){
                    if(aMapLocation.getErrorCode() == 0){

                        LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

                        MarkerOptions locationMarker = new MarkerOptions();


                        //添加Marker显示定位位置
                        if (locationMarker == null) {
                            //如果是空的添加一个新的,icon方法就是设置定位图标，可以自定义
                            locationMarker.position(latLng);
                            locationMarker.draggable(false);//设置Marker可拖动
                            //   markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            //            .decodeResource(getResources(),R.mipmap.img)));
                            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                            locationMarker.setFlat(false);//设置marker平贴地图效果
                            aMap.addMarker(locationMarker);
                        } else {
                            //已经添加过了，修改位置即可
                            locationMarker.position(latLng);
                        }

                        //然后可以移动到定位点,使用animateCamera就有动画效果
                        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                       mLocationClient.stopLocation();

                       card.setPosition(  aMapLocation.getAddress());
                       card.setLatLng_x(aMapLocation.getLatitude() );
                       card.setLatLng_y(aMapLocation.getLongitude());
                    }else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if ((isVisibleToUser && isResumed())) {
            onResume();
            //read all cards from database and display them;
            if(!userName.equals("")&&userName.equals(((MainActivity)getActivity()).getGlobalUsername())){
                //chenggongdenlu;
                hasLogin = true;
                for(int k =0;k<PoiMarkers.size();k++){
                    PoiMarkers.get(k).remove();
                }
            }else{
                for(int j =0 ; j<CardMarkers.size();j++){
                    CardMarkers.get(j).remove();
                }
                for(int k =0;k<PoiMarkers.size();k++){
                    PoiMarkers.get(k).remove();
                }
                userName = ((MainActivity)getActivity()).getGlobalUsername();
                if(!((MainActivity)getActivity()).getGlobalUsername().equals("")){
                    final MyDatabase myDB = new MyDatabase(getContext());
                    mCards = myDB.queryCardByUser(((MainActivity)getActivity()).getGlobalUsername());
                    for(Card c: mCards){
                        displayCard(c);
                    }
                    hasLogin = true;
                }else{
                    hasLogin =false;
                }
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
