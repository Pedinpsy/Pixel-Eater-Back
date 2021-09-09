package com.corundumstudio.socketio.demo;

import com.corundumstudio.socketio.listener.*;
import com.corundumstudio.socketio.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatLauncher {
    @Data
    @NoArgsConstructor
    public class EventKeyPress{
        String code;
    }
    @Data
    @AllArgsConstructor
    public static class responsegameObject{
        String coockie;
        Player player;


    }
    @Data
    @AllArgsConstructor
    public static class responseUpdateObject{
        HashMap<String,Player> players;
        ArrayList<GameState.Food> foods;
        int timer;

    }
    public final static GameState gameState = new GameState();
    public static void main(String[] args) throws InterruptedException {

        int num  = 0 ;
        Configuration config = new Configuration();
        config.setHostname("0.0.0.0");
        config.setPort(9094);

        final SocketIOServer server = new SocketIOServer(config);

        final int finalNum = num;

        ConnectListener connectListener = new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {

                HandshakeData handshakeData = socketIOClient.getHandshakeData();
                System.out.println(handshakeData.getHeaders());
                gameState.addJogador(handshakeData.getSingleHeader("Cookie"));

                System.out.println("Foi conectado o  cliente de id = "+handshakeData.getSingleHeader("Cookie"));

                socketIOClient.sendEvent("initilize",new responsegameObject(handshakeData.getSingleHeader("Cookie"),gameState.getJogadores().get(handshakeData.getSingleHeader("Cookie")) ));


            }

        };
        DisconnectListener disconnectListener = new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                System.out.println("Disconectaod");
                HandshakeData handshakeData = socketIOClient.getHandshakeData();
                gameState.removeJogador(handshakeData.getSingleHeader("Cookie"));
            }
        };
        server.addConnectListener(connectListener);
        server.addDisconnectListener(disconnectListener);

        server.addEventListener("chatevent", ChatObject.class, new DataListener<ChatObject>() {
            @Override
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) {
                HandshakeData handshakeData = client.getHandshakeData();


                // broadcast messages to all clients

                server.getBroadcastOperations().sendEvent("chatevent", data);
            }




        });


        server.addEventListener("keyPressed", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) {

                HandshakeData handshakeData = client.getHandshakeData();

                gameState.getJogadores().get(handshakeData.getSingleHeader("Cookie")).changeDir(data);



            }
        });





        server.start();
        while(1==1){

//            System.out.println("Loop");
            gameState.updateGame();
            gameState.updatePlayers();
            gameState.generateFood();
            gameState.checkCollideFood();
            server.getBroadcastOperations().sendEvent("update", new responseUpdateObject(gameState.getJogadores(), gameState.getFoods(), gameState.getTimer()));
            Thread.sleep(50);
            if(1==2){
                break;
            }
        }



        server.stop();
    }

}
