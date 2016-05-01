package com.main.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;


public class JoinActivity extends Activity {

    public static final String DEFAULT_IP = "192.168.0.11:4444";

    private static final String TAG = "JoinActivity";

    public static final String IP_EXTRA = "IP";
    public static final String PORT_EXTRA = "PORT";
    public static final String TCP = "TCP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_join);

        final TextView ipAddress = (TextView)findViewById(R.id.ip_address);
        final RadioButton tcpRadio = (RadioButton)findViewById(R.id.tcp);
        ipAddress.setText(DEFAULT_IP);

        Button joinBtn = (Button)findViewById(R.id.join_btn);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, MultiPlayerGame.class);
                intent.putExtra(IP_EXTRA, ipAddress.getText().toString().split(":")[0]);
                intent.putExtra(PORT_EXTRA, Integer.parseInt(ipAddress.getText().toString().split(":")[1]));
                intent.putExtra(TCP, tcpRadio.isChecked());
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        Button singlePlayer = (Button)findViewById(R.id.single_player);
        singlePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, SinglePlayerGame.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

    }
}
