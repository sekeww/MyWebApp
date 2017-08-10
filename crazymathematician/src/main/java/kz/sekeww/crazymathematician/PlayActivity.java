package kz.sekeww.crazymathematician;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class PlayActivity extends AppCompatActivity {

    private TextView equationTextView;
    private EditText answerEditText;
    private Button submitButton;
    private TextView plusOneTxt;

    private static final String SHARED_PREFS_FILE_NAME="kz.sekeww.crazymathematician.sharedprefs";
    private static final String SHARED_PREFS_SCORE_KEY="score";


    private int a;
    private int b;

    private SharedPreferences sharedreferences;
    int score;
    int i=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        sharedreferences = getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
        score = sharedreferences.getInt(SHARED_PREFS_SCORE_KEY,0);

        equationTextView = (TextView) findViewById(R.id.equationTextView);
        answerEditText = (EditText) findViewById(R.id.answerEditText);
        submitButton = (Button) findViewById(R.id.submitButton);
        plusOneTxt = (TextView) findViewById(R.id.plusOneTxt);

        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(3000);

        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(3000);

        in.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                plusOneTxt.setText("");
                plusOneTxt.startAnimation(out);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitClick();
                plusOneTxt.startAnimation(in);
                plusOneTxt.setText("+1");


            }
        });


        Random r = new Random();

        a = r.nextInt(9) + 1;
        b = r.nextInt(9) + 1;

        equationTextView.setText(a + "X" + b);
    }

    private void onSubmitClick() {
        int answer = Integer.parseInt(answerEditText.getText().toString());

        //+1 if ans is correct
        //-1 if ans is incorrect

        if(answer == a*b){
            score++;
            i++;
            if (i<6){
                renew();
            }
            else if (i%5==0 && i>6){
                newlevel(i);
            }else{
                newlevel((i/5)*5);
                System.out.println((i/5)*5);

            }

        }else {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", --score);
            setResult(RESULT_OK, returnIntent);
            finish();
        }

    }


    private void newlevel(int i) {
        Random r = new Random();

        a = r.nextInt(9+i) + i;
        b = r.nextInt(9+i) + i;

        equationTextView.setText(a + "X" + b);

        answerEditText.setText(null);
    }

    private void renew() {
        Random r = new Random();

        a = r.nextInt(9) + 1;
        b = r.nextInt(9) + 1;

        equationTextView.setText(a + "X" + b);

        answerEditText.setText(null);
    }
}
