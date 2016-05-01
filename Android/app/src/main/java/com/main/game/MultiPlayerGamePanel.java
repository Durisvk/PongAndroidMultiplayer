package com.main.game;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.main.game.network.Client;
import com.main.game.network.OnMessageReceived;
import com.main.game.network.packets.Packet02PositionUpdate;
import com.main.game.network.packets.Packet03ScoreUpdate;
import com.main.game.network.packets.PacketType;
import com.main.game.network.tcp.TCPClient;
import com.main.game.network.udp.UDPClient;
import com.main.game.objects.multiplayer.Ball;
import com.main.game.objects.multiplayer.Enemy;
import com.main.game.objects.multiplayer.Score;
import com.main.game.objects.multiplayer.You;


public class MultiPlayerGamePanel extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener, OnMessageReceived, GamePanel {


    private static final int BACKGROUND_SPEED = -5;
    private static final String TAG = "GamePanel";

    private GameThread thread;

    private Background bg;
    private Ball ball;

    private You you;
    private Enemy enemy;
    private Score score;


    private Client client;
    private boolean TCP = true;
    private String ip = "";
    private int port = -1;



    public MultiPlayerGamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new GameThread(getHolder(), this);

        setFocusable(true);
    }

    public MultiPlayerGamePanel(Context context, String ip, int port, boolean TCP) {
        this(context);
        setNetwork(ip, port, TCP);

    }

    public void setNetwork(String ip, int port, boolean TCP) {
        this.TCP = TCP;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
        bg.setSpeed(BACKGROUND_SPEED);

        ball = new Ball();

        you = new You();
        enemy = new Enemy();

        score = new Score();

        thread.setRunning(true);
        thread.start();

        new ConnectionTask(this).execute();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry = true;
        while(retry) {
            try {

                thread.setRunning(false);
                thread.join();

                retry = false;

            } catch(Exception e) {
                retry = true;
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return super.onTouchEvent(e);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(canvas != null) {
            final int savedState = canvas.save();
            canvas.scale((float)getWidth()/WIDTH, (float)getHeight()/HEIGHT);

            bg.draw(canvas);
            ball.draw(canvas);
            you.draw(canvas);
            enemy.draw(canvas);
            score.draw(canvas);

            canvas.restoreToCount(savedState);
        }
    }

    public void update() {
        bg.update();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double dy = event.values[1];
        if(client != null)
            client.sendMessage(PacketType.ACCELEROMETER_UPDATE, "dx:" + dy + ":");

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        return;
    }

    @Override
    public void messageReceived(String message) {
        String originalMessage = message.substring(2, message.length());
        switch(PacketType.parse(message).toString()) {
            case "02": new Packet02PositionUpdate().process(this, originalMessage);
                        break;
            case "03": new Packet03ScoreUpdate().process(this, originalMessage);
        }

    }

    public Ball getBall() { return ball; }
    public You getYou() { return you; }
    public Enemy getEnemy() { return enemy; }
    public Score getScore() { return score; }

    public void destroy() {
        client.disconnect();
        try {
            thread.join();
        } catch(Exception e) {}
    }

    public class ConnectionTask extends AsyncTask<String, String, TCPClient> {

        private OnMessageReceived callback;


        public ConnectionTask(OnMessageReceived callback) {
            this.callback = callback;
        }

        @Override
        protected TCPClient doInBackground(String... params) {

            if(TCP == true)
                client = new TCPClient(ip, port, callback);
            else
                client = new UDPClient(ip, port, callback);

            client.start();

            return null;
        }

    }
}
