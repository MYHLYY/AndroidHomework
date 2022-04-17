package com.bjsxt.ui;

import static com.bjsxt.util.Tools.bytesToObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bjsxt.entity.VertificationCode;

import com.example.hotel.R;
import com.bjsxt.util.MyCallBack;
import com.bjsxt.util.Tools;

import java.util.Date;

public class RegisterActivity extends AppCompatActivity {



    private static final String TAG = "RegisterActivity";
    private Button btn_register_submit;
    private TextView tv_get_code;
    private EditText register_et_telephone;
    private EditText register_et_code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_register_submit = findViewById(R.id.btn_register);
        tv_get_code = (TextView) findViewById(R.id.regiater_tv_get_code);
        register_et_telephone = (EditText) findViewById(R.id.register_et_telephone);
        register_et_code = (EditText) findViewById(R.id.register_et_code);

        setViewListener();
    }

    private void setViewListener(){
        tv_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取参数
                String phone=register_et_telephone.getText()==null?"":register_et_telephone.getText().toString();
                if (!Tools.isMobile(phone)){
                    register_et_telephone.setError("请输入正确的手机号");
                    return;
                }

                VertificationCode code = new VertificationCode(phone);
                Tools.PostDataWithOkHttp("sendVertificationCode",code,new MyCallBack() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        VertificationCode code = null;
                        try {
                            code = (VertificationCode) bytesToObject(bytes);
                        } catch (Exception e) {
                            showResponse(e.toString());
                            e.printStackTrace();
                        }
                        if(code!=null){
                            showResponse(code.getVertification_code());
                        }else{
                            showResponse("获取验证码失败!");
                        }
                    }
                });
            }
        });
        btn_register_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取参数
                String phone = register_et_telephone.getText() == null ? "" : register_et_telephone.getText().toString();
                String etcode = register_et_code.getText() == null ? "" : register_et_code.getText().toString();
                if (!Tools.isMobile(phone)) {
                    register_et_telephone.setError("请输入正确的手机号");
                    return;
                }
                if (etcode.length() != 6) {
                    register_et_code.setError("请输入六位验证码");
                    return;
                }

                VertificationCode code = new VertificationCode(phone, etcode, new Date());
                Tools.PostDataWithOkHttp("RegisterCheck", code, new MyCallBack() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        String newcode = null;
                        try {
                            newcode = (String) bytesToObject(bytes);
                        } catch (Exception e) {
                            showResponse(e.toString());
                            e.printStackTrace();
                        }
                        if ("Existed".equals(newcode)) {
                            showResponse("该手机号已注册");
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else if (newcode != null && "Registered".equals(newcode)) {
                            showResponse("注册成功");
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            showResponse("else");
                            showResponse(newcode);
                        }
                    }
                });


            }
        });
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this,response,Toast.LENGTH_SHORT).show();
            }
        });
    }
}


