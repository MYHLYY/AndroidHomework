package com.bjsxt.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.bjsxt.entity.VertificationCode;
import com.example.hotel.R;
import com.bjsxt.util.MyCallBack;
import com.bjsxt.util.Tools;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VertificationCode code = new VertificationCode("13207502290");
        Tools.PostDataWithOkHttp("", code, new MyCallBack() {
            @Override
            public void onSuccess(byte[] bytes) {
                String str = null;
                try {
                    str=(String) Tools.bytesToObject(bytes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                showResponse(str);
            }
        });
    }
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();
            }
        });
    }
}