package kz.sekeww.crazymathematician;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS_FILE_NAME="kz.sekeww.crazymathematician.sharedprefs";
    private static final String SHARED_PREFS_SCORE_KEY="score";

    private static final int REQUEST_CODE_PLAY = 1;
    private TextView scoreTextView;

    private Button playButton;
    private Button renewScoreBtn;
    private Button historyBtn;

    private int score = 0;
    private String currentDateTimeString;

    private SharedPreferences sharedreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        playButton = (Button) findViewById(R.id.playButton);
        renewScoreBtn = (Button) findViewById(R.id.renewScoreBtn);
        historyBtn = (Button) findViewById(R.id.historyBtn);

        renewScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteScore();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayButtonClick();                
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHistoryBtnClick();
            }
        });

        sharedreferences = getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
        score = sharedreferences.getInt(SHARED_PREFS_SCORE_KEY,0);

        scoreTextView.setText("" + score);
    }

    private void onHistoryBtnClick() {
        Intent intent = new Intent(this,HistoryActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PLAY);
    }

    private void deleteScore() {
        scoreTextView.setText("0");
        SharedPreferences.Editor editor = sharedreferences.edit();
        editor.putInt(SHARED_PREFS_SCORE_KEY,0);
        editor.commit();
    }

    private void onPlayButtonClick() {
        Intent intent = new Intent(this,PlayActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PLAY);
        score = 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PLAY){
            if (resultCode==RESULT_OK){
                int result = data.getIntExtra("result",0);

                score += result;
                saveScore();

                if(result > 0) {
                    Snackbar.make(scoreTextView,"You won =)",Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(scoreTextView,"You lost =(",Snackbar.LENGTH_SHORT).show();

                }
            }
        }
    }

    private void saveScore() {
        scoreTextView.setText(""+score);
        SharedPreferences.Editor editor = sharedreferences.edit();

        editor.putInt(SHARED_PREFS_SCORE_KEY,score);
        editor.commit();

    }
}
