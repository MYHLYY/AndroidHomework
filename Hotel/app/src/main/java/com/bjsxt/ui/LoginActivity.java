package com.bjsxt.ui;

import static com.bjsxt.util.Tools.bytesToObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bjsxt.entity.VertificationCode;
import com.example.hotel.R;
import com.bjsxt.util.MyCallBack;
import com.bjsxt.util.Tools;


import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    boolean isLogin=false;
    //控件
    private Button btnSubmit;
    private LinearLayout llSms;
    private LinearLayout llPassword;
    private TextView tvSms;
    private TextView tvPassword;
    private TextView tvRegister;
    private TextView tvForgetPassword;
    private EditText etPhone;
    private EditText etCode;
    private TextView tv_get_code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置布局
        setContentView(R.layout.activity_login);

        //获取控件  ctrl+alt+f快速变成属性
        btnSubmit = (Button) findViewById(R.id.btn_login_submit);
        //登录界面短信验证码布局
        llSms = findViewById(R.id.ll_login_sms);
        //登录界面密码登录布局
        llPassword = findViewById(R.id.ll_login_password);
        //登录界面最下面选项--使用验证码登录
        tvSms = findViewById(R.id.tv_login_sms);
        //登录界面最下面选项--使用密码登录
        tvPassword = findViewById(R.id.tv_login_password);
        //登录界面最下面选项--注册
        tvRegister = findViewById(R.id.tv_login_register);
        //登录界面最下面选项--重置密码
        tvForgetPassword = findViewById(R.id.tv_login_forget_password);
        //手机号输入框
        etPhone = findViewById(R.id.etPhone);
        //验证码输入框
        etCode = findViewById(R.id.etCode);
        //获取验证码
        tv_get_code = findViewById(R.id.tv_get_code);


        setViewListener();


//        //    第二种事件的写法---内部实现类
//        ClickListener clickListener = new ClickListener();
//        btnSubmit.setOnClickListener(clickListener);
//        //第三种方式--Activity实现OnclickListenser
//        btnSubmit.setOnClickListener(this);

//        //第四种方式 匿名内部实现类
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int id = view.getId();
//           3     switch (id) {
//                    case R.id.btn_login_submit:
//                        Toast.makeText(LoginActivity.this, "提示:你点击了按钮,匿名内部实现类", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        });
    }

    /**
     * 设置控件事件
     */
    private void setViewListener() {
        //获取验证码按钮
        tv_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取参数
                String phone=etPhone.getText()==null?"":etPhone.getText().toString();
                if (!Tools.isMobile(phone)){
                    etPhone.setError("请输入正确的手机号");
                    return;
                }

                VertificationCode code = new VertificationCode(phone);//上传服务器信息
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

        //文本--按钮    注册新会员 点击事件
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //意图    参数:context， 要打开页面的类.class
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                //打开Activity
                startActivity(intent);
            }
        });

        //文本--按钮    重置密码 点击事件
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //意图    参数:context， 要打开页面的类.class
                Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                //打开Activity
                startActivity(intent);
            }
        });



        //文本--按钮  密码登录 点击事件
        tvPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示密码登录输入框
                llPassword.setVisibility(View.VISIBLE);
                //显示登录界面最下面选项用验证码登录
                tvSms.setVisibility(View.VISIBLE);
                //隐藏验证码输入框
                llSms.setVisibility(View.GONE);
                //隐藏登陆界面最下面选项密码登录
                tvPassword.setVisibility(View.GONE);
            }
        });

        //文本--按钮 验证码登录 点击事件
        tvSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示验证码输入框
                llSms.setVisibility(View.VISIBLE);
                //显示登陆界面最下面选项密码登录
                tvPassword.setVisibility(View.VISIBLE);
                //隐藏密码输入框
                llPassword.setVisibility(View.GONE);
                //隐藏登陆界面最下面选项验证码登录
                tvSms.setVisibility(View.GONE);
            }
        });

        //登录按钮
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取参数
                String phone=etPhone.getText()==null?"":etPhone.getText().toString();
                String etcode = etCode.getText()==null?"":etCode.getText().toString();
                if (!Tools.isMobile(phone)){
                    etPhone.setError("请输入正确的手机号");
                    return;
                }
                if(etcode.length()!=6){
                    etCode.setError("请输入六位验证码");
                    return;
                }

                VertificationCode code = new VertificationCode(phone,etcode,new Date());
                Tools.PostDataWithOkHttp("LoginCheck",code,new MyCallBack() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        String newcode = null;
                        try {
                            newcode = (String) bytesToObject(bytes);
                        } catch (Exception e) {
                            showResponse(e.toString());
                            e.printStackTrace();
                        }
                        if("NoUser".equals(newcode)){
                            showResponse("该手机号未注册");
                        }
                        else if(newcode!=null && "login".equals(newcode)){
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else{
                            showResponse(newcode);
                        }
                    }
                });

            }

        });
    }

//    private void sendRequestWithOkHttp(){
//
//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(1000, TimeUnit.MILLISECONDS)
//                .build();
//// 2. 创建一个请求
//        Request request = new Request.Builder()
//                .url("http://3995r916i2.qicp.vip/")
//                .get()//默认get请求，可以省略
//                .build();
//// 3. 客户端发起请求实例化Call对象，执行请求任务
//
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                showResponse("===============");
//                Log.d(TAG, "onFailure...");
//            }
//
//            //response.body().string() 将请求体的内容以字符创的形式输出
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String str = "hkjfshdaf";
////                int code = response.code();
//                byte[] bytes = getResponseBytes(response);
//                showResponse(Arrays.toString(bytes));
//                hello tex = null;
//                try {
//                    tex = (hello) Tools.bytesToObject(bytes);
//                } catch (Exception e) {
//                    showResponse(e.toString());
//                    e.printStackTrace();
//                }
//                try {
//                    showResponse(tex.getPro());
//
//                }catch (Exception e){
//                    showResponse("连接失败");
//                }
//
//                Log.d(TAG, "response code --> " + response.code());
//                Log.d(TAG, "response body-->" + response.body().string());
//            }
//        });
//
//    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this,response,Toast.LENGTH_SHORT).show();
            }
        });
    }

////    第三种方式
//    @Override
//    public void onClick(View view) {
//        int id = view.getId();
//        switch (id) {
//            case R.id.btn_login_submit:
//                Toast.makeText(LoginActivity.this, "提示:你点击了按钮,Activity实现OnClickListener", Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }

////    第二种事件的写法---内部实现类
//    class ClickListener implements View.OnClickListener{
//
//    @Override
//    public void onClick(View view) {
//        int id = view.getId();
//        switch (id){
//            case R.id.btn_login_submit:
//                Toast.makeText(LoginActivity.this,"提示:你点击了按钮,内部实现类",Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }
//}


//    //第一种事件的写法：不推荐使用---结合layout文件声明点击事件的方法
//    public void btnClick(View view){
//        //如果和layout文件关联，不能用private，否则xml文件中的Android:onClick="btnClick"无法调用该btnClick方法
//        //参数 Context,text,duration
//        //上下文，要提示的信息，提示显示的时长
//        Toast.makeText(LoginActivity.this,"提示:你点击了按钮",Toast.LENGTH_SHORT).show();
//    }
}
