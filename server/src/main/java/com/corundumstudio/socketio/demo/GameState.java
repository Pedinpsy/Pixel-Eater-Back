package com.corundumstudio.socketio.demo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.Date;
@Data
public class GameState {
    private Random r = new Random();
    private int maxSize = 80;
    private int timer = 0;
    private int maxTime= 60;
    private long  init;
    private Calendar c1;

    private int maxFood = 50;
    public GameState(){
       c1 = Calendar.getInstance();
       init = c1.getTime().getTime();
       timer = 0;
    }
    private HashMap<String, Player> jogadores = new HashMap<String, Player>();

    private ArrayList<Food> foods = new ArrayList<Food>();

    public void addJogador(String cookie) {
        jogadores.put(cookie, new Player());


    }

    public void removeJogador(String cookie) {
        jogadores.remove(cookie);

    }
    public void updateGame(){

        long time1 =init;
        long time2 =Calendar.getInstance().getTime().getTime();
        timer = (int)((time2 - time1)/1000);
        if (timer >=  maxTime){
            timer = 0;
            foods.clear();
            init = Calendar.getInstance().getTime().getTime();
            jogadores.forEach( (k,v)  ->{
                v.points = 0;
            });
        }

    }

    public void updatePlayers() {
        jogadores.forEach((key, value) -> {
            if (value.dir == "E") {
                if (value.x - 1 >= 0)
                    value.x -= 1;
                else
                    value.x = maxSize - 1;
            }
            if (value.dir == "D") {
                if (value.x + 1 <= maxSize)
                    value.x += 1;
                else
                    value.x = 0;
            }
            if (value.dir == "C") {
                if (value.y - 1 >= 0)
                    value.y -= 1;
                else
                    value.y = maxSize - 1;
            }
            if (value.dir == "B") {
                if (value.y + 1 <= maxSize)
                    value.y += 1;
                else
                    value.y = 0;
            }
        });


    }

    public void generateFood() {
        if (foods.size() < maxFood) {
            foods.add(new Food());
        }
    }

    public void checkCollideFood() {

        jogadores.forEach(
                (k, v) -> {
                    Iterator<Food> itrF = foods.iterator();
                    while (itrF.hasNext()) {
                        Food f = itrF.next();
                        if (v.y == f.y && v.x == f.x) {
                            v.points += 1;
                            itrF.remove();
                        }

                    }
                }
        );
    }

    @Data
    @AllArgsConstructor
    public class Food {
        private int x, y;

        public Food() {
            this.x = r.nextInt(80);
            this.y = r.nextInt(80);
        }
    }


}
