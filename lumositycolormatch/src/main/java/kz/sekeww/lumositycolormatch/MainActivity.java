package kz.sekeww.lumositycolormatch;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFS_FILE_NAME = "kz.sekeww.lumositycolormatch";
    private static final String SHARED_PREFS_SCORE_KEY = "score";
    private static final String SHARED_PREFS_LEADER_KEY = "leaderboard";
    private static final int REQUEST_CODE_PLAY = 1;
    private static final int REQUEST_CODE_LEADERBOARD = 2;

    private TextView leftTextView;
    private TextView rightTextView;
    private TextView correctNum;
    private TextView incorrectNum;
    private TextView timerText;
    private TextView colur1;
    private TextView colur2;

    private Button yesButton;
    private Button noButton;
    private Button leaderboardButton;

    private ArrayList<Integer> colors;
    private ArrayList<String> colorNames;

    private int leftTextIndex;
    private int rightTextIndex;
    private int leftColorIndex;
    private int rightColorIndex;
    private int numberOfColors = 5;
    private int yesCounter = 0;
    private int noCounter = 0;
    private int score = 0;

    private int[] leaderboard = new int[10];

    private SharedPreferences sharedPreferences;
    
    public static final String GAME_PREFS = "ArithmeticFile";

    private AccelerateDecelerateInterpolator mSmoothInterpolator;
    private CharSequence mActionBarTitle;
    private SpannableString mActionBarTitleSpannableString;
    private HashSet<Object> mSpans = new HashSet<Object>();

    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3641402840999818~8428935085");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mActionBarTitle = "Түрлі Түсті";
        mActionBarTitleSpannableString = new SpannableString(mActionBarTitle);
        mSmoothInterpolator = new AccelerateDecelerateInterpolator();

        leftTextView = (TextView) findViewById(R.id.leftTextView);
        rightTextView = (TextView) findViewById(R.id.rightTextView);
        colur1 = (TextView) findViewById(R.id.colur1);
        colur2 = (TextView) findViewById(R.id.colur2);

        String text1 = colur1.getText().toString();
        String text2 = colur2.getText().toString();

        yesButton = (Button) findViewById(R.id.yesButton);
        noButton = (Button) findViewById(R.id.noButton);

        correctNum = (TextView) findViewById(R.id.correctNum);
        incorrectNum = (TextView) findViewById(R.id.incorrectNum);

        timerText = (TextView) findViewById(R.id.timerText);
        Shader shader = new LinearGradient(
                0, 0, 0, timerText.getTextSize(),
                Color.RED, Color.BLUE,
                Shader.TileMode.CLAMP);
        timerText.getPaint().setShader(shader);

        //leaderboardButton = (Button) findViewById(R.id.leaderboardButton);
        for (int i = 0; i < leaderboard.length; i++)
        {
            leaderboard[i] = 0;
        }
        final MyCount counter = new MyCount(11000, 1000);

        counter.start();

        genData();

        updateViews();

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onYesClick();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNoClick();
            }
        });

        timerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                counter.cancel();

                yesCounter = 0;
                noCounter = 0;

                timerText.setText("0");
                correctNum.setText("0");
                incorrectNum.setText("0");

                noButton.setEnabled(true);
                yesButton.setEnabled(true);

                timerText.setShadowLayer(0, 0, 0, Color.parseColor("#fff700"));

                counter.start();
                animateActionBarFireworks();
            }
        });

//        leaderboardButton.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View v)
//            {
//                onLeaderboardButtonClick();
//            }
//        });

        highlight("Түсі",text1,colur1);
        highlight("Аты",text2,colur2);

        animateActionBarFireworks();

        sharedPreferences = getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
        score = sharedPreferences.getInt(SHARED_PREFS_SCORE_KEY, 0);
        String leaders = sharedPreferences.getString(SHARED_PREFS_LEADER_KEY, "0");

        //converting string into int[]
        String[] items = leaders.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "").split(",");

        leaderboard = new int[10];

        for (int i = 0; i < leaderboard.length; i++)
        {
            leaderboard[i] = 0;
        }

        for (int i = 0; i < items.length; i++) {
            // try {

            leaderboard[i] = Integer.parseInt(items[i]);
            //} catch (NumberFormatException nfe) {};
        }


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_PLAY)
        {
            if (resultCode == RESULT_OK)
            {
                int result = data.getIntExtra("result", 0);
                score += result;
                saveScore();


            }
        }
    }

    private void saveScore() {

        //ASK1DUB: VOT ZDES ALGORITHM SORTIROVKI
        //check if its big enough and add if it is
        for (int i = 0; i < leaderboard.length; i++) {
            if (leaderboard[i] < score) {

                //shift
                for (int j = 8; j >= i; j--) {
                    leaderboard[j + 1] = leaderboard[j];
                }

                leaderboard[i] = score;
                break;


            }
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SHARED_PREFS_SCORE_KEY, score);
        editor.putString(SHARED_PREFS_LEADER_KEY, Arrays.toString(leaderboard));
        editor.commit();
    }

    private void onLeaderboardButtonClick() {
        Intent intent = new Intent(this, Results.class);
        intent.putExtra("leader", Arrays.toString(leaderboard));
        startActivityForResult(intent, REQUEST_CODE_LEADERBOARD);
    }

    private void correct() {
        //Snackbar.make(leftTextView, "Correct!", Snackbar.LENGTH_SHORT).show();
        yesCounter++;
        correctNum.setText(String.format("%d", yesCounter));
    }

    private void incorrect() {
        //Snackbar.make(leftTextView, "Incorrect!", Snackbar.LENGTH_SHORT).show();
        noCounter++;
        incorrectNum.setText(String.format("%d", noCounter));
    }

    private void onNoClick() {

        if (leftTextIndex != rightColorIndex) {
            correct();
        } else {
            incorrect();
        }
        updateViews();
    }

    private void onYesClick() {

        if (leftTextIndex == rightColorIndex) {
            correct();
        } else {
            incorrect();
        }
        updateViews();
    }

    private void updateViews() {

        Random random = new Random();

        leftTextIndex = random.nextInt(numberOfColors);
        rightTextIndex = random.nextInt(numberOfColors);

        leftColorIndex = random.nextInt(numberOfColors);
        rightColorIndex = random.nextInt(numberOfColors);

        leftTextView.setText(colorNames.get(leftTextIndex));
        rightTextView.setText(colorNames.get(rightColorIndex));

        leftTextView.setBackgroundResource(colors.get(leftColorIndex));
        rightTextView.setBackgroundResource(colors.get(rightColorIndex));


    }

    private void genData() {
        colors = new ArrayList<>();
        colorNames = new ArrayList<>();

        colors.add(android.R.color.holo_blue_dark);
        colorNames.add("КӨК");
        colors.add(android.R.color.holo_red_dark);
        colorNames.add("ҚЫЗЫЛ");
        colors.add(android.R.color.black);
        colorNames.add("ҚАРА");
        colors.add(android.R.color.holo_green_dark);
        colorNames.add("ЖАСЫЛ");
        colors.add(android.R.color.holo_purple);
        colorNames.add("КҮЛГІН");
        colors.add(Color.parseColor("#fff700"));
        colorNames.add("САРЫ");
        colors.add(Color.parseColor("#976a02"));
        colorNames.add("ҚОҢЫР");

    }

    private class MyCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timerText.setText(String.format("%d", millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            timerText.setText("ОЙНАУ!");
            timerText.setShadowLayer(8, 12, 12, Color.parseColor("#77000000"));

            open();

            noButton.setEnabled(false);
            yesButton.setEnabled(false);
        }
    }

    public void open() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Бұл сіздің нәтижелеріңіз:\n\n" +
                "Дұрыс жауаптар: " + yesCounter + "\n" +
                "Бұрыс жауаптар: " + noCounter + "\n\n" +
                "Қайта бастау?\n");

        alertDialogBuilder.setPositiveButton("Иә", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(MainActivity.this, "You clicked yes button", Toast.LENGTH_LONG).show();
                updateViews();

                yesCounter = 0;
                noCounter = 0;

                correctNum.setText("0");
                incorrectNum.setText("0");

                noButton.setEnabled(true);
                yesButton.setEnabled(true);

                MyCount counter = new MyCount(11000, 1000);
                counter.start();
                timerText.setShadowLayer(0, 0, 0, Color.parseColor("#fff700"));

                animateActionBarFireworks();
            }
        });

        alertDialogBuilder.setNegativeButton("Жоқ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.cancel();
                noButton.setEnabled(false);
                yesButton.setEnabled(false);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void highlight(String query,String text, TextView view) {

        SpannableString spannableString = new SpannableString(text);

        Pattern pattern = Pattern.compile(query.toLowerCase());
        Matcher matcher = pattern.matcher(text.toLowerCase());
        while (matcher.find()) {
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(), matcher.end(), 0);
            spannableString.setSpan(new RainbowSpan(this), matcher.start(), matcher.end(), 0);
        }

        view.setText(spannableString);
    }

    private static class RainbowSpan extends CharacterStyle implements UpdateAppearance {
        private final int[] colors;

        public RainbowSpan(Context context) {
            colors = context.getResources().getIntArray(R.array.rainbow);
        }

        @Override
        public void updateDrawState(TextPaint paint) {
            paint.setStyle(Paint.Style.FILL);
            Shader shader = new LinearGradient(0, 0, 0, paint.getTextSize() * colors.length, colors, null,
                    Shader.TileMode.MIRROR);
            Matrix matrix = new Matrix();
            matrix.setRotate(30);
            shader.setLocalMatrix(matrix);
            paint.setShader(shader);
        }
    }

    private void animateActionBarFireworks() {
        FireworksSpanGroup spanGroup = buildFireworksSpanGroup(0, mActionBarTitle.length() - 1);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(spanGroup, FIREWORKS_GROUP_PROGRESS_PROPERTY, 0.0f, 1.0f);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //refresh
                setTitle(mActionBarTitleSpannableString);
            }
        });
        objectAnimator.setInterpolator(mSmoothInterpolator);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
    }
    private static final class FireworksSpanGroup {

        private static final boolean DEBUG = false;
        private static final String TAG = "FireworksSpanGroup";

        private final float mProgress;
        private final ArrayList<MutableForegroundColorSpan> mSpans;
        private final ArrayList<Integer> mSpanIndexes;

        private FireworksSpanGroup() {
            mProgress = 0;
            mSpans = new ArrayList<MutableForegroundColorSpan>();
            mSpanIndexes = new ArrayList<Integer>();
        }

        public void addSpan(MutableForegroundColorSpan span) {
            span.setAlpha(0);
            mSpanIndexes.add(mSpans.size());
            mSpans.add(span);
        }

        public void init() {
            Collections.shuffle(mSpans);
        }

        public void setProgress(float progress) {
            int size = mSpans.size();
            float total = 1.0f * size * progress;

            if(DEBUG) Log.d(TAG, "progress " + progress + " * 1.0f * size => " + total);

            for(int index = 0 ; index < size; index++) {
                MutableForegroundColorSpan span = mSpans.get(index);

                if(total >= 1.0f) {
                    span.setAlpha(255);
                    total -= 1.0f;
                } else {
                    span.setAlpha((int) (total * 255));
                    total = 0.0f;
                }
            }
        }

        public float getProgress() {
            return mProgress;
        }
    }

    private FireworksSpanGroup buildFireworksSpanGroup(int start, int end) {
        final FireworksSpanGroup group = new FireworksSpanGroup();
        for(int index = start ; index <= end ; index++) {
            MutableForegroundColorSpan span = new MutableForegroundColorSpan(0, Color.WHITE);
            mSpans.add(span);
            group.addSpan(span);
            mActionBarTitleSpannableString.setSpan(span, index, index + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        group.init();
        return group;
    }

    private static final Property<FireworksSpanGroup, Float> FIREWORKS_GROUP_PROGRESS_PROPERTY =
            new Property<FireworksSpanGroup, Float>(Float.class, "FIREWORKS_GROUP_PROGRESS_PROPERTY") {

                @Override
                public void set(FireworksSpanGroup spanGroup, Float value) {
                    spanGroup.setProgress(value);
                }

                @Override
                public Float get(FireworksSpanGroup spanGroup) {
                    return spanGroup.getProgress();
                }
            };


}
