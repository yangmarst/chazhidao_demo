package com.yang.mychabaike.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.yang.mychabaike.app.ConstantKey;
import com.yang.mychabaike.app.utils.Pref_Utils;

import yang.mychabaike.R;

public class LoadingActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent();
            intent.setClass(LoadingActivity.this, WelcomeActivity.class);
            if (!getFirstOpenFlag()) {
                intent.setClass(LoadingActivity.this, HomeActivity.class);
            }
            startActivity(intent);
            finish();
        }
    }, 3000);
        System.out.println("onCreate===========LoadingActivity");

    }


    public boolean getFirstOpenFlag(){

       return Pref_Utils.getBoolean(this, ConstantKey.PRE_KEY_FIRST_OPEN,true);
    }

}
