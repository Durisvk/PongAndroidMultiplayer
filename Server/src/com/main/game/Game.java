package com.main.game;


import com.main.game.network.OnAuthorizedMessageReceived;
import com.main.game.network.Server;
import com.main.game.network.packets.PacketType;
import com.main.game.network.tcp.TCPServer;
import com.main.game.network.udp.UDPServer;
import com.main.game.objects.Ball;
import com.main.game.objects.Player;
import com.main.game.objects.Score;

import java.util.Scanner;

public class Game extends Thread implements OnAuthorizedMessageReceived {

    public static final int FPS = 30;

    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;

    private static final int MOVEMENT_SPEED = 4;

    private static final int LOG_FPS_AFTER_FRAMES = 10000;

    private static final int WAIT_TIME_AFTER_SCORE = 100;

    private Server server;
    private boolean running = false;

    private long averageFPS;

    private Player player1;
    private Player player2;
    private Ball ball;
    private Score score;

    private int waitingTime = 0;

    public Game() {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Which port should I use? [4444]: ");
        int port;
        try {
            port = Integer.parseInt(scanner.nextLine());
        } catch(Exception e) { port = 4444; }

        System.out.print("Which protocol use TCP/UDP? [TCP]: ");

        if(scanner.nextLine().equalsIgnoreCase("UDP"))
            server = new UDPServer(this, port);
        else server = new TCPServer(this, port);

        scanner.close();


        player1 = new Player();
        player2 = new Player();
        ball = new Ball();
        score = new Score();

    }

    @Override
    public void onAuthorizedMessageReceived(int playernum, String message) {
        if(PacketType.parse(message) == PacketType.ACCELEROMETER_UPDATE) {
            String dx = message.split(":")[1];
            switch (playernum) {
                case 1:
                    player1.setDx(Float.parseFloat(dx) * MOVEMENT_SPEED);
                    break;
                case 2:
                    player2.setDx(Float.parseFloat(dx) * MOVEMENT_SPEED);
                    break;
            }
        }
    }

    public void stopGame() {
        running = false;
    }

    @Override
    public void run() {
        super.run();

        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000/FPS;

        running = true;

        try {
            if (!((Thread) server).isAlive())
                server.start();
        } catch(Exception e) {}

        while(running) {
            startTime = System.nanoTime();

            this.update();
            this.sendToPlayers();

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                this.sleep(waitTime);
            } catch(Exception e) {}

            totalTime += System.nanoTime() - startTime;
            frameCount++;

            if(frameCount == LOG_FPS_AFTER_FRAMES) {
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println("FPS: " + Double.toString(averageFPS));
            }
        }



    }


    public void update() {

        if(waitingTime > 0) {
            waitingTime--;
            return;
        }

        if(server.getNumberOfConnections() > 1 && !ball.isRolling())
            ball.roll();

        ball.update();
        player1.update();
        player2.update();

        ball.handleCollisionWithPlayer(player1, false);
        ball.handleCollisionWithPlayer(player2, true);

        if(!ball.isInVerticalBounds()) {
            handleScoreChange();
        }

    }


    public void handleScoreChange() {
        if(ball.getY() <= 0) {
            score.setScore(score.getScoreForPlayer1(), score.getScoreForPlayer2() + 1);
        } else if(ball.getY() >= HEIGHT) {
            score.setScore(score.getScoreForPlayer1() + 1, score.getScoreForPlayer2());
        }

        server.sendAuthorizedMessage(1, PacketType.SCORE_UPDATE, score.getScoreForPlayer1() + ":" + score.getScoreForPlayer2() + ":");
        server.sendAuthorizedMessage(2, PacketType.SCORE_UPDATE, score.getScoreForPlayer2() + ":" + score.getScoreForPlayer1() + ":");

        ball.setPosition(WIDTH/2, HEIGHT/2);
        player1.setPosition(WIDTH/2);
        player2.setPosition(WIDTH/2);
        setWaitingTimeTo(WAIT_TIME_AFTER_SCORE);
    }

    public void setWaitingTimeTo(int gameCycles) {
        waitingTime = gameCycles;
    }

    public void sendToPlayers() {

        if(server.getNumberOfConnections() > 0) {

            if (server.getNumberOfConnections() >= 1) {
                String ballString = "" + ball.getX() + ":" + ball.getY() + ";";

                server.sendAuthorizedMessage(1, PacketType.POSITION_UPDATE, ballString + player2.getTransformedX() + ";" + player1.getX());
            }
            if (server.getNumberOfConnections() >= 2) {
                String ballString = "" + ball.getTransformedX() + ":" + ball.getTransformedY() + ";";
                server.sendAuthorizedMessage(2, PacketType.POSITION_UPDATE, ballString + player1.getTransformedX() + ";" + player2.getX());
            }
        }
    }


    public static void main(String [] args) {
        Game game = new Game();
        game.start();
        game.run();
    }

}
