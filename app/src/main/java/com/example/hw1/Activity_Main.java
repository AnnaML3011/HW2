package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donkey.dolly.Dolly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
/*
Anna Meleshko -319346045
 */
public class Activity_Main extends AppCompatActivity {
    public static final String NAME = "NAME";
    private final int NUM_OF_PLAYERS = 2;
    private final int NUM_OF_CARDS = 52;
    private List<Card> cards;
    private List<Player> players;
    private TextView score_LBL_1;
    private TextView score_LBL_2;
    private ImageButton start_game_BTN;
    private ImageView first_IMG_card;
    private ImageView second_IMG_card;
    private ProgressBar progress_Bar;
    private int progressStatus = 0;
    private Timer timer;
    private TimerTask timerTask;
    final private Handler handler = new Handler();
    final private int delay = 200;
    private boolean isKilled = false;
    private MediaPlayer mp;
    private boolean firstGame = true;


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(players.get(0).getCardDeck().deckIsEmpty() == 0 || players.get(1).getCardDeck().deckIsEmpty() == 0){
                isKilled = true;
                stopTimer();
                checkWinner();
            }
            if(!isKilled){
                start_game();
            }
        }
    };

    private void setImage(int id, ImageView imageView){
        Glide
                .with(this)
                .load(id)
                .into(imageView);
    }


    private void changeCardImg(){
//        setImage(R.drawable.backward_card, first_IMG_card);
//        setImage(R.drawable.backward_card, second_IMG_card);
        first_IMG_card.setImageResource(R.drawable.backward_card);
        second_IMG_card.setImageResource(R.drawable.backward_card);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        findViews();
        createPlayers();
        initViews();
        createCards();
        shuffleAndCreateDeck();

    }

    /*
press the button once, and than start a timer to until the deck is empty
 */
    private void initViews() {
        timer = new Timer();
        score_LBL_1.setText("Score: " + players.get(0).getScore());
        score_LBL_2.setText("Score: " + players.get(1).getScore());
        start_game_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstGame = false;
                playSound(R.raw.click);
                start_game_BTN.setImageResource(R.drawable.stopwatch);
                if(!isKilled) {
                    startTimer(timer);
                }
            }
        });
    }


    @Override
    protected void onStart() {
        Log.d("aaa","onStart");
        super.onStart();
        if(!firstGame) {
            timer = new Timer();
            startTimer(timer);
        }
    }

    @Override
    protected void onResume() {
        Log.d("aaa","onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("aaa","onPause");
        super.onPause();
        stopTimer();
        handler.removeCallbacks(runnable);

    }

    @Override
    protected void onStop() {
        Log.d("aaa","onStop");
        super.onStop();
        stopTimer();
        handler.removeCallbacks(runnable);

    }

    @Override
    protected void onDestroy() {
        Log.d("aaa","onDestroy");
        super.onDestroy();
        stopTimer();
        handler.removeCallbacks(runnable);

    }

    private void findViews() {
        score_LBL_1 = findViewById(R.id.score_LBL_1);
        score_LBL_2 = findViewById(R.id.score_LBL_2);
        start_game_BTN = findViewById(R.id.start_game_BTN);
        first_IMG_card = findViewById(R.id.first_IMG_card);
        second_IMG_card =findViewById(R.id.seconed_IMG_card);
        progress_Bar = findViewById(R.id.progress_Bar);

    }

    protected void playSound(int rawName) {

        mp = MediaPlayer.create(this, rawName);
        mp.setOnCompletionListener(mp -> {
            mp.reset();
            mp.release();
        });
        mp.start();
    }

    //To start timer
    private void startTimer(Timer timer){
//        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                changeCardImg();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(runnable);

            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }


    /*to stop timer*/
    private void stopTimer(){
        if(timer != null){
            timer.cancel();
            timer.purge();
        }
    }


    /*
    Create Cards from Enums (4 types*14 values = 52 cards).
     */
    private void createCards(){
        cards = new ArrayList<>();
        for (CardDeck.CardType type: CardDeck.CardType.values()) {
            for(CardDeck.CardValue value : CardDeck.CardValue.values()){
                cards.add(new Card(value,type));
            }
        }
    }

    /*
       Create players as in the enum(player1,player2)
     */
    private void createPlayers(){
        players = new ArrayList<>();
        for(Player.Players player: Player.Players.values()){
            players.add(new Player(player));
        }
    }

    /*
       shuffle the cards and for each player put half of the deck.
     */
    private void shuffleAndCreateDeck(){
        Collections.shuffle(cards);
        int sizeOfDeckEachPlayer = NUM_OF_CARDS/NUM_OF_PLAYERS;
        List<Card> cardsEachPlayer = new ArrayList<>();
        for (int i = 0; i < NUM_OF_PLAYERS; i++) {
            cardsEachPlayer.addAll(cards.subList(i*sizeOfDeckEachPlayer,(i+1)*sizeOfDeckEachPlayer));
            players.get(i).addCardsToPlayerDeck(cardsEachPlayer);
            cardsEachPlayer.clear();
        }
    }

    /*
       pop cards for each player for their deck(26 cards),
        find image of that specific card, and than go check and caclcute the score.
     */
    private void start_game() {
        playSound(R.raw.flipcard);
        if(progressStatus <NUM_OF_CARDS/NUM_OF_PLAYERS) {
            progressStatus += 1;
            progress_Bar.setProgress(progressStatus);
        }
        Card card1 = players.get(0).getCardDeck().getCard();
        Card card2 = players.get(1).getCardDeck().getCard();
        String nameCard1 = card1.getType() + "_" + card1.getValue();
        String nameCard2 = card2.getType() + "_" + card2.getValue();
        int id1 = getResources().getIdentifier(nameCard1,"drawable", getPackageName());
        int id2 = getResources().getIdentifier(nameCard2,"drawable", getPackageName());
//        setImage(id1, first_IMG_card);
//        setImage(id2, second_IMG_card);
        first_IMG_card.setImageResource(id1);
        second_IMG_card.setImageResource(id2);
        calculateScore(card1, card2);
    }

    /*
       check which player got higher card, add points to the player with the higher card value,
       if both have the same value - add point to both of them.
     */
     private void calculateScore(Card c1, Card c2){
         int cardValue1 = c1.getValue();
         int cardValue2= c2.getValue();
         if(cardValue1 > cardValue2){
             players.get(0).addPoint();
             score_LBL_1.setText("Score: " + players.get(0).getScore());
         }else if(cardValue1 < cardValue2){
             players.get(1).addPoint();
             score_LBL_2.setText("Score: " + players.get(1).getScore());
         }else if(cardValue1 == cardValue2){
             players.get(0).addPoint();
             players.get(1).addPoint();
             score_LBL_1.setText("Score: " + players.get(0).getScore());
             score_LBL_2.setText("Score: " + players.get(1).getScore());
         }
     }

        /*
           check which player have higher final score, and go for next activity to show the winner.
         */
     public void checkWinner(){
        int score = 0;
        int player = 0;// 1-player1 , 2-player2 ,3- teco
        if(players.get(0).getScore() > players.get(1).getScore()){
            score = players.get(0).getScore();
            player = 1;
        }else if(players.get(0).getScore() < players.get(1).getScore()){
            score = players.get(1).getScore();
            player = 2;
            //go to winner activity with player 2 name
        }else {
            score = players.get(1).getScore();
            player = 3;
        }
        Intent myIntent = new Intent(Activity_Main.this, Activity_Winner.class);
        myIntent.putExtra(Activity_Winner.PLAYER,player);
        myIntent.putExtra(Activity_Winner.SCORE,score);
        startActivity(myIntent);
        finish();

     }

}