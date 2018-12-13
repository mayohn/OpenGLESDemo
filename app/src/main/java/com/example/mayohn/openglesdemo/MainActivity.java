package com.example.mayohn.openglesdemo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private GLAdapter adapter;
    private ArrayList<String> strList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSource();
        isSupportGL();
        listView = findViewById(R.id.listView);
        adapter = new GLAdapter(strList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                switch (i) {
                    case 0:
                        intent.setClass(MainActivity.this, PointActivity.class);
                        break;
                }
                startActivity(intent);
            }
        });
    }

    public void initSource() {
        strList.add("四个点");
    }

    /**
     * 检查是否支持GL2.0
     */
    public void isSupportGL() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        boolean isSupport = configurationInfo.reqGlEsVersion >= 0x20000;
        if (isSupport){
            Toast.makeText(MainActivity.this,"检测您的手机支持GL2.0",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MainActivity.this,"对不起！手机不支持GL2.0",Toast.LENGTH_SHORT).show();
        }
    }

    public class GLAdapter extends BaseAdapter {
        private ArrayList<String> list = new ArrayList<>();

        public GLAdapter(ArrayList<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_item, null);
                holder = new ViewHolder();
                holder.textView = view.findViewById(R.id.textView);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.textView.setText(list.get(i));
            return view;
        }

        class ViewHolder {
            TextView textView;
        }
    }
}
