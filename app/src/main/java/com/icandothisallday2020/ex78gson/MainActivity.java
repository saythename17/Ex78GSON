package com.icandothisallday2020.ex78gson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    ListView listView;
    ArrayAdapter adapter;
    List<String> items=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=findViewById(R.id.tv);
    }

    public void clickBtn(View view) {
        //GSON Library 를 이용-> json 문자열을 Person 객체로 바로 parsing

        String jsonStr="{'name':'robin','age':25}";

        Gson gson=new Gson();
        Person p=gson.fromJson(jsonStr,Person.class);//json 문자열->Person 객체
        tv.setText(p.name+","+p.age);
    }

    public void clickBtn2(View view) {
        //객체를 json 으로
        Person p=new Person("hoshi",25);
        Gson gson=new Gson();
        String jsonStr=gson.toJson(p);
        tv.setText(jsonStr);
    }

    public void clickBtn3(View view) {
        //json 배열을 객체로
        String str="[{'name':'jisu','age':26},{'name':'bsg','age':23}]";

        Gson gson=new Gson();
        Person[] people=gson.fromJson(str,Person[].class);

        //배열->리스트
        for(Person p :people){
            items.add(p.name+","+p.age);
        }
        listView=findViewById(R.id.listview);
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,items);
        listView.setAdapter(adapter);
    }

    public void clickBtn4(View view) {
        final String serverUrl="http://soon0.dothome.co.kr/test.json";

        new Thread(){
            @Override
            public void run() {
                try {
                    URL url=new URL(serverUrl);
                    HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setUseCaches(false);

                    InputStream is=connection.getInputStream();
                    InputStreamReader isr=new InputStreamReader(is);
                    //Reader 까지만 있으면 GSON이 알아서 읽어와 객체로 parsing(분석)
                    Gson gson=new Gson();
                    final Person p=gson.fromJson(isr,Person.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(p.name+","+p.age);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
