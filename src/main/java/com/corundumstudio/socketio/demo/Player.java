package com.corundumstudio.socketio.demo;

import lombok.Data;

import java.util.Random;

@Data
public class Player {
    String[] dirs = {"E","C","B","D"};


    int x;
    int y;
    int points;
    String dir;
    public Player(){
        Random r=new Random();

        this.x = (int)Math.floor(Math.random()*(80-0+1)+0);

        this.y = (int)Math.floor(Math.random()*(80-0+1)+0);
        this.dir = dirs[r.nextInt(dirs.length)];
        System.out.printf(this.y +" " +this.x);
    }
    public void changeDir(String code){
        if(code.equals("KeyA")  || code.equals("ArrowLeft") ){
            this.dir = "E";
        }
        if(code.equals("KeyD")  || code.equals("ArrowRight") ){
            this.dir = "D";
        }
        if(code.equals("KeyS")  || code.equals("ArrowDown") ){
            this.dir = "B";
        }
        if(code.equals("KeyW")  || code.equals("ArrowUp") ){
            this.dir = "C";
        }

    }
}
