package com.bjsxt.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel.R;

public class ForgetPasswordActivity extends AppCompatActivity {
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //设置页面布局
        setContentView(R.layout.activity_forgetpassword);
    }
}
