package com.example.aced;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewParent;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private List<View> _views;
    private List<Inscription> _inscriptions;
    private List<Hero.Skill> _skills;
    private List<Equipment> _equipments;
    private List<Integer> _abilites;
    private Hero _hero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


         _hero =  (Hero)getIntent().getSerializableExtra("hero");
        _equipments = _hero.getEquipments();
        _inscriptions =_hero.getInscriptions() ;
        _skills = _hero.getSkills();
        _abilites = _hero.getAbilities();
        initViewPaper();

        TextView tv_hero_name = (TextView)findViewById(R.id.tv_hero_name);
        tv_hero_name.setText("名字 "+_hero.getName());

        TextView tv_hero_position = (TextView)findViewById(R.id.tv_hero_position);
        tv_hero_position.setText("位置 "+_hero.getPositon());

        TextView  tv_hero_occuption =(TextView)findViewById(R.id.tv_hero_occupation);
        tv_hero_occuption.setText("职业 "+_hero.getOccupation());

        ImageView iv_hero_imgv = (ImageView)findViewById(R.id.iv_hero_imgv);

        MyDataBase db = new MyDataBase(this);

        String id =Integer.toString( _hero.getId());
        Bitmap bitmap = null;
        byte[] image = db.queryImage("2"+id,2);
        bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        
        iv_hero_imgv.setImageBitmap(bitmap);

    }

    //初始化viewpager，就是下方的信息，技能，装备，铭文
    public void initViewPaper(){

        _views = new ArrayList<View>();

        initViewData();

        String [] _titles  = {"信息","技能","装备","铭文"} ;
        ViewPager vp_hero_content = (ViewPager)findViewById(R.id.vp_hero_content);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(this,_views,_titles);
        vp_hero_content.setAdapter(myPagerAdapter);

    }

    //初始化信息，技能，装备，铭文的内容
    public void initViewData() {

       View viewpagerA  = initViewPaperInfo();
       View viewpagerB  =initViewPaperSkill();
       View  viewpagerC = initViewPaperEquipment();
       View viewpagerD = initViewPaperInscription();

        _views = new ArrayList<>();
        _views.add(viewpagerA);
        _views.add(viewpagerB);
        _views.add(viewpagerC);
        _views.add(viewpagerD);

    }


    //初始化信息的内容
    public View initViewPaperInfo() {
        View viewpagerA = getLayoutInflater().inflate(R.layout.viewpager_info, null);

        RadarChart radarChart  = (RadarChart)viewpagerA.findViewById(R.id.radarchart);

        setData(radarChart);


        return viewpagerA;
    }
    //初始化技能的内容
    public View initViewPaperSkill() {
        View viewpagerB = getLayoutInflater().inflate(R.layout.viewpager_skill, null);

        ListView lv_vp_skill = (ListView)viewpagerB.findViewById(R.id.lv_vp_skill);

        MyViewPagerListViewAdapter mySkillAdapter = new MyViewPagerListViewAdapter(this,R.layout.viewpager_listview,_skills,0);
        lv_vp_skill.setAdapter(mySkillAdapter);

        return viewpagerB;
    }
    //初始化装备的内容
    public View initViewPaperEquipment() {
        View viewpagerC = getLayoutInflater().inflate(R.layout.viewpager_equipment, null);

        TextView tv_vp_equipmentsTip = (TextView)viewpagerC.findViewById(R.id.tv_vp_equipmentTip);
        tv_vp_equipmentsTip.setText("装备推荐: \n    "+_hero.getEquipmentsTip());

        ListView lv_vp_equipment = (ListView)viewpagerC.findViewById(R.id.lv_vp_equipment);
        MyViewPagerListViewAdapter myEupAdapter = new MyViewPagerListViewAdapter(this,R.layout.viewpager_listview,_equipments,1);
        lv_vp_equipment.setAdapter(myEupAdapter);

        return viewpagerC;
    }
    //初始化铭文的内容
    public View initViewPaperInscription() {
        View viewpagerD = getLayoutInflater().inflate(R.layout.viewpager_inscription, null);
        TextView tv_vp_inscriptionTip = (TextView)viewpagerD.findViewById(R.id.tv_vp_inscriptionTip);
        tv_vp_inscriptionTip.setText("铭文推荐: \n    "+_hero.getInscriptionsTip());
        ListView lv_vp_inscription = (ListView)viewpagerD.findViewById(R.id.lv_vp_inscription);
        MyViewPagerListViewAdapter myInsAdapter = new MyViewPagerListViewAdapter(this,R.layout.viewpager_listview,_inscriptions,2);
        lv_vp_inscription.setAdapter(myInsAdapter);
        return viewpagerD;
    }


    public void setData( RadarChart mChart ) {

            int cnt = 5;

            ArrayList<RadarEntry> entries = new ArrayList<>();

            // NOTE: The order of the entries when being added to the entries array determines their position around the center of
            // the chart.
        if(_abilites.size() != 0) {

            entries.add(new RadarEntry(_abilites.get(0)));
            entries.add(new RadarEntry(_abilites.get(1)));
            entries.add(new RadarEntry(_abilites.get(2)));
            entries.add(new RadarEntry(_abilites.get(3)));
            entries.add(new RadarEntry(_abilites.get(4)));
        }else{
            entries.add(new RadarEntry(50));
            entries.add(new RadarEntry(40));
            entries.add(new RadarEntry(30));
            entries.add(new RadarEntry(20));
            entries.add(new RadarEntry(10));
        }



            RadarDataSet set1 = new RadarDataSet(entries, _hero.getName());
            set1.setColor(Color.rgb(103, 110, 129));
            set1.setFillColor(Color.BLUE);
            set1.setDrawFilled(true);
            set1.setDrawHighlightCircleEnabled(true);
         //   set1.setDrawHighlightIndicators(false);




            ArrayList<IRadarDataSet> sets = new ArrayList<>();
            sets.add(set1);

            RadarData data = new RadarData(sets);
         //   data.setValueTypeface(tfLight);
            data.setValueTextSize(12f);
            data.setDrawValues(true);
            data.setValueTextColor( getResources().getColor(R.color.colorPrimaryDark) );

            mChart.setData(data);
            mChart.getDescription().setEnabled(false);
            mChart.invalidate();

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(18f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            private final String[] mActivities = new String[]{"生存","攻击", "技能", "难度", "喜爱"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        xAxis.setTextColor(Color.BLACK);


        YAxis yAxis = mChart.getYAxis();
        yAxis.setLabelCount(6, true);
        yAxis.setTextSize(10f);
        yAxis.setAxisMinimum(0);
        yAxis.setAxisMaximum(100);
        yAxis.setDrawLabels(false);



        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTextColor(Color.BLUE);
        l.setTextSize(12);

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
