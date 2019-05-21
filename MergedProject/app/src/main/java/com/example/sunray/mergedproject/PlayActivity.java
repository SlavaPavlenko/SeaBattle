package com.example.sunray.mergedproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends AppCompatActivity {

    GridView playerGV;
    GridView enemyGV;
    Button start_btn;
    Button clear_btn;
    ImageView ship_1_iv;
    ImageView ship_2_iv;
    ImageView ship_3_iv;
    ImageView ship_4_iv;
    TextView ship_1_tv;
    TextView ship_2_tv;
    TextView ship_3_tv;
    TextView ship_4_tv;
    GridAdapter enemyGA;
    GridAdapter playerGA;
    List<Cell> playerList;
    List<Cell> enemyList;
    ImageView chosenShip;
    AdapterView<?> parent_tmp;
    Intent bgMusic;
    boolean changingActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        playerGV = findViewById(R.id.playerGridView);
        enemyGV = findViewById(R.id.enemyGridView);
        start_btn = findViewById(R.id.start_btn);
        clear_btn = findViewById(R.id.clear_btn);
        ship_1_iv = findViewById(R.id.ship_1_iv);
        ship_1_tv = findViewById(R.id.ship_1_tv);

        ship_2_iv = findViewById(R.id.ship_2_iv);
        ship_2_iv.setTag("horizontal");
        ship_2_tv = findViewById(R.id.ship_2_tv);

        ship_3_iv = findViewById(R.id.ship_3_iv);
        ship_3_iv.setTag("horizontal");
        ship_3_tv = findViewById(R.id.ship_3_tv);

        ship_4_iv = findViewById(R.id.ship_4_iv);
        ship_4_iv.setTag("horizontal");
        ship_4_tv = findViewById(R.id.ship_4_tv);

        ship_1_iv.setOnClickListener(ship_iv_clickListener);
        ship_2_iv.setOnClickListener(ship_iv_clickListener);
        ship_3_iv.setOnClickListener(ship_iv_clickListener);
        ship_4_iv.setOnClickListener(ship_iv_clickListener);
        playerGV.setOnItemClickListener(player_grid_clickListener);
        enemyGV.setOnItemClickListener(enemy_grid_clickListener);

        bgMusic = new Intent(this, bgMusicService.class);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_btn.setEnabled(false);
                start_btn.setVisibility(View.INVISIBLE);
                clear_btn.setEnabled(false);
                clear_btn.setVisibility(View.INVISIBLE);
                enemyGV.setVisibility(View.VISIBLE);
                ship_1_iv.setEnabled(false);
                ship_2_iv.setEnabled(false);
                ship_3_iv.setEnabled(false);
                ship_4_iv.setEnabled(false);
                ship_1_tv.setEnabled(false);
                ship_2_tv.setEnabled(false);
                ship_3_tv.setEnabled(false);
                ship_4_tv.setEnabled(false);
                ship_1_iv.setVisibility(View.INVISIBLE);
                ship_2_iv.setVisibility(View.INVISIBLE);
                ship_3_iv.setVisibility(View.INVISIBLE);
                ship_4_iv.setVisibility(View.INVISIBLE);
                ship_1_tv.setVisibility(View.INVISIBLE);
                ship_2_tv.setVisibility(View.INVISIBLE);
                ship_3_tv.setVisibility(View.INVISIBLE);
                ship_4_tv.setVisibility(View.INVISIBLE);
                chosenShip = null;
                Cell cell1 = (Cell) playerGA.getItem(0);
                Cell cell2 = (Cell) enemyGA.getItem(0);
                if (cell1.hashCode() == cell2.hashCode())
                    Toast.makeText(PlayActivity.this, "same hash", Toast.LENGTH_SHORT).show();
                else Toast.makeText(PlayActivity.this, "different hash", Toast.LENGTH_SHORT).show();

            }
        });
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ship_1_tv.setText("4");
                ship_2_tv.setText("3");
                ship_3_tv.setText("2");
                ship_4_tv.setText("1");
                chosenShip = null;
                Cell cell;
                for (int i = 0; i <= 99; i++) {
                    cell = (Cell) playerGV.getItemAtPosition(i);
                    cell.setImageName("empty");
                    cell.setShip(false);
                    View view = playerGV.getChildAt(i);
                    playerGA.getView(i, view, parent_tmp);
                }
            }
        });

        playerList = getListData();
        enemyList = getListData();
        playerGA = new GridAdapter(playerGV.getContext(), playerList);      //ошибка из-за одинакового контекста?
        enemyGA = new GridAdapter(enemyGV.getContext(), enemyList);         //PlayActivity.this?
        if (playerGV.getContext() == enemyGV.getContext())
            Toast.makeText(PlayActivity.this, "same context", Toast.LENGTH_SHORT).show();
        else Toast.makeText(PlayActivity.this, "different context", Toast.LENGTH_SHORT).show();
        playerGV.setAdapter(playerGA);  //эти адаптеры как-то связаны?
        enemyGV.setAdapter(enemyGA);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!changingActivity) stopService(bgMusic);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (changingActivity)
            changingActivity = false;
        else startService(bgMusic);
    }

    @Override
    public void onBackPressed() {
        changingActivity = true;
        finish();
    }

    View.OnClickListener ship_iv_clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (chosenShip != v) {
                chosenShip = (ImageView) v;
            }
            else chosenShip = null;
        }
    };

    GridView.OnItemClickListener enemy_grid_clickListener = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cell cell = (Cell) enemyGA.getItem(position);
            //Toast.makeText(PlayActivity.this, cell.getImageName(), Toast.LENGTH_SHORT).show();
            if (cell.getImageName() == "empty")
                if (cell.isShip()) {
                    //cell.setImageName("killed");
                    enemyGA.setItem(position, new Cell("killed", false));
                }
                else {
                    //cell.setImageName("miss");
                    enemyGA.setItem(position, new Cell("miss", false));
                }
            enemyGA.getView(position, view, parent);
        }
    };

    GridView.OnItemClickListener player_grid_clickListener = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            parent_tmp = parent;
            if (chosenShip == ship_1_iv && checkPosition(position, ship_1_iv)) {
                Cell cell = (Cell) playerGA.getItem(position);
                int n = Integer.parseInt((String) ship_1_tv.getText());
                if (!cell.isShip() && n  > 0) {
                    n--;
                    ship_1_tv.setText(String.valueOf(n));

                    Cell c = (Cell) enemyGA.getItem(position);
                    Toast.makeText(PlayActivity.this, c.getImageName(), Toast.LENGTH_SHORT).show();

                    playerGA.setItem(position, new Cell("ship1", true));            //баг здесь!!!
                    //cell.setImageName("ship1");

                    c = (Cell) enemyGA.getItem(position);
                    Toast.makeText(PlayActivity.this, c.getImageName(), Toast.LENGTH_SHORT).show();

                    cell.setShip(true);
                    playerGA.getView(position, view, parent);
                }
            }
            else if (chosenShip == ship_2_iv && checkPosition(position, ship_2_iv))
            {
                Cell cell = (Cell) playerGV.getItemAtPosition(position);
                int n = Integer.parseInt((String) ship_2_tv.getText());
                if (!cell.isShip() && ship_2_iv.getTag() == "horizontal")
                {
                    if (!((Cell) playerGV.getItemAtPosition(position + 1)).isShip() && n  > 0) {
                        n--;
                        ship_2_tv.setText(String.valueOf(n));
                        cell.setImageName("ship1");
                        cell.setShip(true);
                        playerGA.getView(position, view, parent);
                        cell = (Cell) playerGV.getItemAtPosition(position + 1);
                        cell.setImageName("ship1");
                        cell.setShip(true);
                        View v = playerGV.getChildAt(position + 1);
                        playerGA.getView(position + 1, v, parent);
                    }
                }
                else if (!cell.isShip() && ship_2_iv.getTag() == "vertical")
                {
                    if (!((Cell) playerGV.getItemAtPosition(position + 10)).isShip() && n  > 0) {
                        n--;
                        ship_2_tv.setText(String.valueOf(n));
                        cell.setImageName("ship1");
                        cell.setShip(true);
                        playerGA.getView(position, view, parent);
                        cell = (Cell) playerGV.getItemAtPosition(position + 10);
                        cell.setImageName("ship1");
                        cell.setShip(true);
                        View v = playerGV.getChildAt(position + 10);
                        playerGA.getView(position + 10, v, parent);
                    }
                }
            }
            else if (chosenShip == ship_3_iv && checkPosition(position, ship_3_iv))
            {
                Cell cell = (Cell) playerGV.getItemAtPosition(position);
                int n = Integer.parseInt((String) ship_3_tv.getText());
                if (!cell.isShip() && ship_3_iv.getTag() == "horizontal")
                {
                    if (!((Cell) playerGV.getItemAtPosition(position + 1)).isShip()
                            && !((Cell) playerGV.getItemAtPosition(position + 2)).isShip() && n  > 0) {
                        n--;
                        ship_3_tv.setText(String.valueOf(n));
                        cell.setImageName("ship1");
                        cell.setShip(true);
                        playerGA.getView(position, view, parent);
                        cell = (Cell) playerGV.getItemAtPosition(position + 1);
                        cell.setImageName("ship1");
                        cell.setShip(true);
                        View v = playerGV.getChildAt(position + 1);
                        playerGA.getView(position + 1, v, parent);
                        cell = (Cell) playerGV.getItemAtPosition(position + 2);
                        cell.setImageName("ship1");
                        cell.setShip(true);
                        v = playerGV.getChildAt(position + 2);
                        playerGA.getView(position + 2, v, parent);
                    }
                }
                else if (!cell.isShip() && ship_3_iv.getTag() == "vertical")
                {
                    if (!((Cell) playerGV.getItemAtPosition(position + 10)).isShip()
                            && !((Cell) playerGV.getItemAtPosition(position + 20)).isShip() && n  > 0) {
                        n--;
                        ship_3_tv.setText(String.valueOf(n));
                        cell.setImageName("ship1");
                        cell.setShip(true);
                        playerGA.getView(position, view, parent);
                        cell = (Cell) playerGV.getItemAtPosition(position + 10);
                        cell.setImageName("ship1");
                        cell.setShip(true);
                        View v = playerGV.getChildAt(position + 10);
                        playerGA.getView(position + 10, v, parent);
                        cell = (Cell) playerGV.getItemAtPosition(position + 20);
                        cell.setImageName("ship1");
                        cell.setShip(true);
                        v = playerGV.getChildAt(position + 20);
                        playerGA.getView(position + 20, v, parent);
                    }
                }

            }
            else if (chosenShip == ship_4_iv && checkPosition(position, ship_4_iv))
            {
                Cell cell = (Cell) playerGV.getItemAtPosition(position);
                int n = Integer.parseInt((String) ship_4_tv.getText());
                if (!cell.isShip() && ship_4_iv.getTag() == "horizontal")
                {
                    if (!((Cell) playerGV.getItemAtPosition(position + 1)).isShip()
                            && !((Cell) playerGV.getItemAtPosition(position + 2)).isShip()
                            && !((Cell) playerGV.getItemAtPosition(position + 3)).isShip() && n  > 0) {
                        n--;
                        ship_4_tv.setText(String.valueOf(n));
                        cell.setImageName("ship1");
                        cell.setShip(true);
                        playerGA.getView(position, view, parent);
                        cell = (Cell) playerGV.getItemAtPosition(position + 1);
                        cell.setImageName("ship1");
                        cell.setShip(true);
                        View v = playerGV.getChildAt(position + 1);
                        playerGA.getView(position + 1, v, parent);
                        cell = (Cell) playerGV.getItemAtPosition(position + 2);
                        cell.setImageName("ship1");
                        cell.setShip(true);
                        v = playerGV.getChildAt(position + 2);
                        playerGA.getView(position + 2, v, parent);
                        cell = (Cell) playerGV.getItemAtPosition(position + 3);
                        cell.setImageName("ship1");
                        cell.setShip(true);
                        v = playerGV.getChildAt(position + 3);
                        playerGA.getView(position + 3, v, parent);
                    }
                }
                else if (!cell.isShip() && ship_3_iv.getTag() == "vertical")
                {
                    if (!((Cell) playerGV.getItemAtPosition(position + 10)).isShip()
                            && !((Cell) playerGV.getItemAtPosition(position + 20)).isShip()
                            && ((Cell) playerGV.getItemAtPosition(position + 30)).isShip() && n  > 0) {
                        n--;
                        ship_4_tv.setText(String.valueOf(n));
                        cell.setImageName("ship1");
                        cell.setShip(true);
                        playerGA.getView(position, view, parent);
                        cell = (Cell) playerGV.getItemAtPosition(position + 10);
                        cell.setImageName("ship1");
                        cell.setShip(true);
                        View v = playerGV.getChildAt(position + 10);
                        playerGA.getView(position + 10, v, parent);
                        cell = (Cell) playerGV.getItemAtPosition(position + 20);
                        cell.setImageName("ship1");
                        cell.setShip(true);
                        v = playerGV.getChildAt(position + 20);
                        playerGA.getView(position + 20, v, parent);
                        cell = (Cell) playerGV.getItemAtPosition(position + 30);
                        cell.setImageName("ship1");
                        cell.setShip(true);
                        v = playerGV.getChildAt(position + 30);
                        playerGA.getView(position + 30, v, parent);
                    }
                }
            }
            /*  тут лежит одна и та же клекта
            if (playerGA.getItem(position) == enemyGA.getItem(position))
                Toast.makeText(PlayActivity.this, "!!!", Toast.LENGTH_SHORT).show();
                */
        }
    };

    private boolean checkPosition(int position, ImageView iv) {
        boolean forbidden = false;
        Cell cell;
        if (iv == ship_1_iv) {
            if (position - 11 >= 0 && position / 10 != 0 && position % 10 != 0) {       //лево верх
                cell = (Cell) playerGV.getItemAtPosition(position - 11);
                if (cell.isShip())
                    forbidden = true;
            }
            if (position - 9 >= 0 && position / 10 != 0 && position % 10 != 9) {        //право верх
                cell = (Cell) playerGV.getItemAtPosition(position - 9);
                if (cell.isShip())
                    forbidden = true;
            }
            if (position + 9 <= 99 && position / 10 != 9 && position % 10 != 0) {       //лево низ
                cell = (Cell) playerGV.getItemAtPosition(position + 9);
                if (cell.isShip())
                    forbidden = true;
            }
            if (position + 11 <= 99 && position / 10 != 9 && position % 10 != 9) {      //право низ
                cell = (Cell) playerGV.getItemAtPosition(position + 11);
                if (cell.isShip())
                    forbidden = true;
            }
            if (position - 1 >= 0 && position % 10 != 0) {                              //лево
                cell = (Cell) playerGV.getItemAtPosition(position - 1);
                if (cell.isShip())
                    forbidden = true;
            }
            if (position + 1 <= 99 && position % 10 != 9) {                             //право
                cell = (Cell) playerGV.getItemAtPosition(position + 1);
                if (cell.isShip())
                    forbidden = true;
            }
            if (position - 10 >= 0 && position / 10 != 0) {                              //верх
                cell = (Cell) playerGV.getItemAtPosition(position - 10);
                if (cell.isShip())
                    forbidden = true;
            }
            if (position + 10 <= 99 && position / 10 != 9) {                              //низ
                cell = (Cell) playerGV.getItemAtPosition(position + 10);
                if (cell.isShip())
                    forbidden = true;
            }
        }
        if (iv == ship_2_iv) {
            if (ship_2_iv.getTag() == "horizontal") {
                if (position % 10 < 9) {
                    if (position - 11 >= 0 && position / 10 != 0 && position % 10 != 0) {       //лево верх
                        cell = (Cell) playerGV.getItemAtPosition(position - 11);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position - 8 >= 0 && position / 10 != 0 && position % 10 != 8) {        //право верх
                        cell = (Cell) playerGV.getItemAtPosition(position - 8);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position + 9 <= 99 && position / 10 != 9 && position % 10 != 0) {       //лево низ
                        cell = (Cell) playerGV.getItemAtPosition(position + 9);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position + 12 <= 99 && position / 10 != 9 && position % 10 != 8) {      //право низ
                        cell = (Cell) playerGV.getItemAtPosition(position + 12);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position - 1 >= 0 && position % 10 != 0) {                              //лево
                        cell = (Cell) playerGV.getItemAtPosition(position - 1);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position + 2 <= 99 && position % 10 != 8) {                             //право
                        for (int i = 1; i <= 2; i++) {
                            cell = (Cell) playerGV.getItemAtPosition(position + i);
                            if (cell.isShip())
                                forbidden = true;
                        }
                    }
                    if (position - 10 >= 0 && position / 10 != 0) {                              //верх
                        for (int i = 9; i <= 10; i++) {
                            cell = (Cell) playerGV.getItemAtPosition(position - i);
                            if (cell.isShip())
                                forbidden = true;
                        }
                    }
                    if (position + 11 <= 99 && position / 10 != 9) {                              //низ
                        for (int i = 10; i <= 11; i++) {
                            cell = (Cell) playerGV.getItemAtPosition(position + i);
                            if (cell.isShip())
                                forbidden = true;
                        }
                    }
                } else forbidden = true;
            }
            if (ship_2_iv.getTag() == "vertical") {
                if (position / 10 < 9) {
                    if (position - 11 >= 0 && position / 10 != 0 && position % 10 != 0) {       //лево верх
                        cell = (Cell) playerGV.getItemAtPosition(position - 11);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position - 9 >= 0 && position / 10 != 0 && position % 10 != 9) {        //право верх
                        cell = (Cell) playerGV.getItemAtPosition(position - 9);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position + 19 <= 99 && position / 10 != 8 && position % 10 != 0) {      //лево низ
                        cell = (Cell) playerGV.getItemAtPosition(position + 19);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position + 21 <= 99 && position / 10 != 8 && position % 10 != 9) {      //право низ
                        cell = (Cell) playerGV.getItemAtPosition(position + 21);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position - 1 >= 0 && position + 9 <= 99 && position % 10 != 0) {        //лево
                        for (int i = -1; i <= 9; i += 10) {
                            cell = (Cell) playerGV.getItemAtPosition(position + i);
                            if (cell.isShip())
                                forbidden = true;
                        }
                    }
                    if (position + 11 <= 99 && position % 10 != 9) {                             //право
                        for (int i = 1; i <= 11; i += 10) {
                            cell = (Cell) playerGV.getItemAtPosition(position - i);
                            if (cell.isShip())
                                forbidden = true;
                        }
                    }
                    if (position - 10 >= 0 && position / 10 != 0) {                              //верх
                        cell = (Cell) playerGV.getItemAtPosition(position - 10);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position + 20 <= 99 && position / 10 != 8) {                              //низ
                        cell = (Cell) playerGV.getItemAtPosition(position + 20);
                        if (cell.isShip())
                            forbidden = true;
                    }
                } else forbidden = true;
            }
        }
        if (iv == ship_3_iv) {
            if (ship_3_iv.getTag() == "horizontal") {
                if (position % 10 < 8) {
                    if (position - 11 >= 0 && position / 10 != 0 && position % 10 != 0) {       //лево верх
                        cell = (Cell) playerGV.getItemAtPosition(position - 11);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position - 7 >= 0 && position / 10 != 0 && position % 10 != 7) {        //право верх
                        cell = (Cell) playerGV.getItemAtPosition(position - 7);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position + 9 <= 99 && position / 10 != 9 && position % 10 != 0) {       //лево низ
                        cell = (Cell) playerGV.getItemAtPosition(position + 9);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position + 13 <= 99 && position / 10 != 9 && position % 10 != 7) {      //право низ
                        cell = (Cell) playerGV.getItemAtPosition(position + 13);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position - 1 >= 0 && position % 10 != 0) {                              //лево
                        cell = (Cell) playerGV.getItemAtPosition(position - 1);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position + 3 <= 99 && position % 10 != 7) {                             //право
                        for (int i = 1; i <= 3; i++) {
                            cell = (Cell) playerGV.getItemAtPosition(position + i);
                            if (cell.isShip())
                                forbidden = true;
                        }
                    }
                    if (position - 10 >= 0 && position / 10 != 0) {                              //верх
                        for (int i = 8; i <= 10; i++) {
                            cell = (Cell) playerGV.getItemAtPosition(position - i);
                            if (cell.isShip())
                                forbidden = true;
                        }
                    }
                    if (position + 12 <= 99 && position / 10 != 9) {                              //низ
                        for (int i = 10; i <= 12; i++) {
                            cell = (Cell) playerGV.getItemAtPosition(position + i);
                            if (cell.isShip())
                                forbidden = true;
                        }
                    }
                } else forbidden = true;
            }
            if (ship_3_iv.getTag() == "vertical") {
                if (position / 10 < 8) {
                    if (position - 11 >= 0 && position / 10 != 0 && position % 10 != 0) {       //лево верх
                        cell = (Cell) playerGV.getItemAtPosition(position - 11);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position - 9 >= 0 && position / 10 != 0 && position % 10 != 9) {        //право верх
                        cell = (Cell) playerGV.getItemAtPosition(position - 9);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position + 29 <= 99 && position / 10 != 8 && position % 10 != 0) {      //лево низ
                        cell = (Cell) playerGV.getItemAtPosition(position + 29);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position + 31 <= 99 && position / 10 != 8 && position % 10 != 9) {      //право низ
                        cell = (Cell) playerGV.getItemAtPosition(position + 31);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position - 1 >= 0 && position + 9 <= 99 && position % 10 != 0) {        //лево
                        for (int i = -1; i <= 9; i += 10) {
                            cell = (Cell) playerGV.getItemAtPosition(position + i);
                            if (cell.isShip())
                                forbidden = true;
                        }
                    }
                    if (position + 11 <= 99 && position % 10 != 9) {                             //право
                        for (int i = 1; i <= 11; i += 10) {
                            cell = (Cell) playerGV.getItemAtPosition(position + i);
                            if (cell.isShip())
                                forbidden = true;
                        }
                    }
                    if (position - 10 >= 0 && position / 10 != 0) {                              //верх
                        cell = (Cell) playerGV.getItemAtPosition(position - 10);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position + 20 <= 99 && position / 10 != 8) {                              //низ
                        cell = (Cell) playerGV.getItemAtPosition(position + 20);
                        if (cell.isShip())
                            forbidden = true;
                    }
                } else forbidden = true;
            }
        }
        if (iv == ship_4_iv) {
            if (ship_4_iv.getTag() == "horizontal") {
                if (position % 10 < 7) {
                    if (position - 11 >= 0 && position / 10 != 0 && position % 10 != 0) {       //лево верх
                        cell = (Cell) playerGV.getItemAtPosition(position - 11);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position - 6 >= 0 && position / 10 != 0 && position % 10 != 6) {        //право верх
                        cell = (Cell) playerGV.getItemAtPosition(position - 6);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position + 9 <= 99 && position / 10 != 9 && position % 10 != 0) {       //лево низ
                        cell = (Cell) playerGV.getItemAtPosition(position + 9);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position + 14 <= 99 && position / 10 != 9 && position % 10 != 6) {      //право низ
                        cell = (Cell) playerGV.getItemAtPosition(position + 14);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position - 1 >= 0 && position % 10 != 0) {                              //лево
                        cell = (Cell) playerGV.getItemAtPosition(position - 1);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position + 4 <= 99 && position % 10 != 6) {                             //право
                        for (int i = 1; i <= 4; i++) {
                            cell = (Cell) playerGV.getItemAtPosition(position + i);
                            if (cell.isShip())
                                forbidden = true;
                        }
                    }
                    if (position - 10 >= 0 && position / 10 != 0) {                              //верх
                        for (int i = 7; i <= 10; i++) {
                            cell = (Cell) playerGV.getItemAtPosition(position - i);
                            if (cell.isShip())
                                forbidden = true;
                        }
                    }
                    if (position + 13 <= 99 && position / 10 != 9) {                              //низ
                        for (int i = 10; i <= 13; i++) {
                            cell = (Cell) playerGV.getItemAtPosition(position + i);
                            if (cell.isShip())
                                forbidden = true;
                        }
                    }
                } else forbidden = true;
            }
            if (ship_4_iv.getTag() == "vertical") {
                if (position / 10 < 7) {
                    if (position - 11 >= 0 && position / 10 != 0 && position % 10 != 0) {       //лево верх
                        cell = (Cell) playerGV.getItemAtPosition(position - 11);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position - 9 >= 0 && position / 10 != 0 && position % 10 != 9) {        //право верх
                        cell = (Cell) playerGV.getItemAtPosition(position - 9);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position + 39 <= 99 && position / 10 != 6 && position % 10 != 0) {      //лево низ
                        cell = (Cell) playerGV.getItemAtPosition(position + 39);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position + 41 <= 99 && position / 10 != 6 && position % 10 != 9) {      //право низ
                        cell = (Cell) playerGV.getItemAtPosition(position + 41);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position - 1 >= 0 && position + 29 <= 99 && position % 10 != 0) {        //лево
                        cell = (Cell) playerGV.getItemAtPosition(position - 1);
                        if (cell.isShip())
                            forbidden = true;
                        for (int i = 9; i <= 29; i += 10) {
                            cell = (Cell) playerGV.getItemAtPosition(position + i);
                            if (cell.isShip())
                                forbidden = true;
                        }
                    }
                    if (position + 31 <= 99 && position % 10 != 9) {                             //право
                        for (int i = 1; i <= 31; i += 10) {
                            cell = (Cell) playerGV.getItemAtPosition(position + i);
                            if (cell.isShip())
                                forbidden = true;
                        }
                    }
                    if (position - 10 >= 0 && position / 10 != 0) {                              //верх
                        cell = (Cell) playerGV.getItemAtPosition(position - 10);
                        if (cell.isShip())
                            forbidden = true;
                    }
                    if (position + 40 <= 99 && position / 10 != 8) {                              //низ
                        cell = (Cell) playerGV.getItemAtPosition(position + 40);
                        if (cell.isShip())
                            forbidden = true;
                    }
                } else forbidden = true;
            }
        }
        return !forbidden;
    }
    private List<Cell> getListData() {
        List<Cell> list = new ArrayList();
        for (int i = 0; i <= 99; i++)
        {
            Cell cell = new Cell("empty", false);
            list.add(cell);
        }
        return list;
    }
}
