package com.bjsxt.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
//
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

import com.example.hotel.R;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private FragmentContainerView fragmentContainerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentContainerView = findViewById(R.id.fragmentContainerView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        try{
            navController = navHostFragment.getNavController();

        }catch (Exception e){
            showResponse(e.toString());
        }
        //获取motion控件
        MotionLayout messageMotionLayout = findViewById(R.id.messageMotionLayout);
        MotionLayout contactMotionLayout = findViewById(R.id.contactMotionLayout);
//        MotionLayout exploreMotionLayout = findViewById(R.id.exploreMotionLayout);
        MotionLayout accountMotionLayout = findViewById(R.id.accountMotionLayout);
        //设置监听事件
        messageMotionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.messageFragment);
            }
        });

        contactMotionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.contactFragment);
            }
        });

//        exploreMotionLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                navController.navigate(R.id.exploreFragment);
//            }
//        });

        accountMotionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.accountFragment);
            }
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                messageMotionLayout.setProgress(0f);
                contactMotionLayout.setProgress(0f);
//                exploreMotionLayout.setProgress(0f);
                accountMotionLayout.setProgress(0f);

                switch (destination.getId()){
                    case R.id.messageFragment:
                        messageMotionLayout.transitionToEnd();
                        break;
                    case R.id.contactFragment:
                        contactMotionLayout.transitionToEnd();
                        break;
//                    case R.id.exploreFragment:
//                        exploreMotionLayout.transitionToEnd();
//                        break;
                    case R.id.accountFragment:
                        accountMotionLayout.transitionToEnd();
                        break;
                    default:
                        break;
                }
            }
        });

    }
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
            }
        });
    }
}