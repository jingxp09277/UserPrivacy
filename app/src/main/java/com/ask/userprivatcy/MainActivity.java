package com.ask.userprivatcy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ask.userprivatcy.dialog.UserPrivacyProtocolDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isFirstRun();
    }


    void isFirstRun(){
        if(UserPrivacyProtocolDialog.isFirstShow(getApplicationContext())) {
            UserPrivacyProtocolDialog userPrivacyProtocolDialog = UserPrivacyProtocolDialog.newInstance(null);
            userPrivacyProtocolDialog.setListener(new UserPrivacyProtocolDialog.IListener() {
                @Override
                public void agree() {
                    UserPrivacyProtocolDialog.agreeUserPrivacyProtocol(getApplicationContext());
                }

                @Override
                public void disagree() {
                    finish();
                }
            });
            userPrivacyProtocolDialog.show(getSupportFragmentManager(), "userPrivacyProtocolDialog");
        }
    }
}
