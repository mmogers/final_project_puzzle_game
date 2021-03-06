package lv.marmog.androidpuzzlegame;


import static lv.marmog.androidpuzzlegame.database.DatabaseHelper.COLUMN_TIMER_RESULT_FOR_12;
import static lv.marmog.androidpuzzlegame.database.DatabaseHelper.COLUMN_TIMER_RESULT_FOR_2;
import static lv.marmog.androidpuzzlegame.database.DatabaseHelper.COLUMN_TIMER_RESULT_FOR_4;
import static lv.marmog.androidpuzzlegame.database.DatabaseHelper.COLUMN_TIMER_RESULT_FOR_9;
import static lv.marmog.androidpuzzlegame.database.DatabaseHelper.COLUMN_TIMER_RESULT_FOR_4;
import static lv.marmog.androidpuzzlegame.database.DatabaseHelper.COLUMN_USER_ID;
import static lv.marmog.androidpuzzlegame.database.DatabaseHelper.TABLE_TIMER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import lv.marmog.androidpuzzlegame.database.DatabaseHelper;
import lv.marmog.androidpuzzlegame.database.User;


public class ScoreActivity extends AppCompatActivity {



    private TextView yourTime;
    private TextView bestTime;
    private TextView kidName;
    private int userId, level, time;
    private String username;
    private long insertResult;
    Cursor cursor;
    //Button to go to the StartActivity
    private FloatingActionButton goHome;

    //Variables for connection to the database
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            DatabaseHelper.COLUMN_USER_ID,
            COLUMN_TIMER_RESULT_FOR_2,
            DatabaseHelper.COLUMN_TIMER_RESULT_FOR_4,
            DatabaseHelper.COLUMN_TIMER_RESULT_FOR_6,
            DatabaseHelper.COLUMN_TIMER_RESULT_FOR_9,
            DatabaseHelper.COLUMN_TIMER_RESULT_FOR_12,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // database
        dbHelper = new DatabaseHelper(ScoreActivity.this);
        database = dbHelper.getWritableDatabase();

        // show timer result from puzzleActivity
        yourTime = (TextView) findViewById(R.id.your_time);
        String timeString = String.valueOf(getTime());
        yourTime.setText(timeString + " seconds");

        kidName = (TextView) findViewById(R.id.kid_name);
        username = getUsername();
        Log.i(ScoreActivity.class.getName(), "Username for textview is " + username);
        kidName.setText(username + "!");

        //show best time
        bestTime = (TextView) findViewById(R.id.best_time);
        bestTime.setText(showBestResult() + " seconds");

        // id, level, timer for db
        userId = getUserId();
        level = getLevel();
        time = getTime();
        //Button to go in StartActivity
        goHome = findViewById(R.id.goHome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });

        // insert in db
        insertResult();

    }

    // --- insert timer result in db
    public boolean insertResult() {
        ContentValues contentValues = new ContentValues();

        // switch for insertion of data in different puzzle complexity/level columns
        switch (level) {
            case 4:
                contentValues.put(DatabaseHelper.COLUMN_TIMER_RESULT_FOR_4, time);
                contentValues.put(DatabaseHelper.COLUMN_USER_ID, userId);
                insertResult = database.insert(TABLE_TIMER, null, contentValues);
                cursor = database.query(TABLE_TIMER, allColumns,
                        DatabaseHelper.COLUMN_TIMER_RESULT_FOR_4 + " = " + insertResult, null, null, null, null);
                cursor.moveToLast();
                cursor.close();
                Log.w(ScoreActivity.class.getName(), "Id: " + userId + " and timer result: " + time + " inserted into column " + level);
                break;

            case 9:
                contentValues.put(DatabaseHelper.COLUMN_TIMER_RESULT_FOR_9, time);
                contentValues.put(DatabaseHelper.COLUMN_USER_ID, userId);
                insertResult = database.insert(TABLE_TIMER, null, contentValues);
                cursor = database.query(TABLE_TIMER, allColumns,
                        DatabaseHelper.COLUMN_TIMER_RESULT_FOR_9 + " = " + insertResult, null, null, null, null);
                cursor.moveToLast();
                cursor.close();
                Log.w(ScoreActivity.class.getName(), "Id: " + userId + " and timer result: " + time + " inserted into column " + level);
                break;

            case 12:
                contentValues.put(DatabaseHelper.COLUMN_TIMER_RESULT_FOR_12, time);
                contentValues.put(DatabaseHelper.COLUMN_USER_ID, userId);
                insertResult = database.insert(TABLE_TIMER, null, contentValues);
                cursor = database.query(TABLE_TIMER, allColumns,
                        DatabaseHelper.COLUMN_TIMER_RESULT_FOR_12 + " = " + insertResult, null, null, null, null);
                cursor.moveToLast();
                cursor.close();
                Log.w(ScoreActivity.class.getName(), "Id: " + userId + " and timer result: " + time + " inserted into column " + level);
                break;
        }

        if (insertResult == -1) {
            Log.e(ScoreActivity.class.getName(), "Results are not saved");
            return false;
        } else {
            Log.i(ScoreActivity.class.getName(), "Results are saved");
            return true;
        }
    }
    // --- /insert timer result in db



    public String showBestResult(){

        Cursor cursor1;
        int result = 0;

        //switch for showing better result for current level( puzzle pieces quantity)
        switch(getLevel()){
            case 2:
                cursor1 = database.rawQuery("SELECT " + COLUMN_TIMER_RESULT_FOR_2 + " from " +
                        TABLE_TIMER + " WHERE user_id " + " = " + getUserId() + " AND " + COLUMN_TIMER_RESULT_FOR_2 + " IS NOT NULL " + " ORDER BY "
                        + COLUMN_TIMER_RESULT_FOR_2 + " LIMIT 1 ", null);
                if( cursor1.moveToFirst()) {
                    result = cursor1.getInt(0);
                }
                cursor1.close();
                break;
            case 4:
                cursor1 = database.rawQuery("SELECT " + COLUMN_TIMER_RESULT_FOR_4 + " from " +
                        TABLE_TIMER + " WHERE user_id " + " = " + getUserId() + " AND " + COLUMN_TIMER_RESULT_FOR_4 + " IS NOT NULL " + " ORDER BY "
                        + COLUMN_TIMER_RESULT_FOR_4 + " LIMIT 1 ", null);
                if( cursor1.moveToFirst()) {
                    result = cursor1.getInt(0);
                }
                cursor1.close();
                break;

            case 9:

                cursor1 = database.rawQuery("SELECT " + COLUMN_TIMER_RESULT_FOR_9 + " from " +
                        TABLE_TIMER + " WHERE user_id " + " = " + getUserId() +
                        " AND " + COLUMN_TIMER_RESULT_FOR_9 + " IS NOT NULL " + " ORDER BY "  + COLUMN_TIMER_RESULT_FOR_9 + " LIMIT 1 ", null);

                if( cursor1.moveToFirst()) {
                    result = cursor1.getInt(0);
                }
                cursor1.close();
                break;
            case 12:

                cursor1 = database.rawQuery("SELECT " + COLUMN_TIMER_RESULT_FOR_12 + " from " +
                        TABLE_TIMER + " WHERE user_id " + " = " + getUserId() +
                        " AND " + COLUMN_TIMER_RESULT_FOR_12 + " IS NOT NULL " + " ORDER BY "  + COLUMN_TIMER_RESULT_FOR_12 + " LIMIT 1 ", null);

                if( cursor1.moveToFirst()) {
                    result = cursor1.getInt(0);
                }
                cursor1.close();
                break;

        }


        return String.valueOf(result);
    }




//
//                List<Integer> arrayListOfScore = new ArrayList<>();
//
//                try {
//
//                    Cursor cursor1 = database.rawQuery("SELECT * FROM " +
//                            TABLE_TIMER + " WHERE user_id " + " = " + getUserId(),  null);
//
//                    if (cursor1.moveToFirst()) {
//                       int id = cursor1.getInt(5);
//                       int score2 = cursor1.getInt(0);
//                       int score4 = cursor1.getInt(1);
//                       int score6 = cursor1.getInt(2);
//                       int score9 = cursor1.getInt(3);
//                       int score12 = cursor1.getInt(4);
//
//
//
//                       Log.i(ScoreActivity.class.getName(), "Cursor id from db is " + id);
//                       Log.i(ScoreActivity.class.getName(), "Cursor score for 2 is " + score2);
//                       Log.i(ScoreActivity.class.getName(), "Cursor score for 4 is " + score4);
//                       Log.i(ScoreActivity.class.getName(), "Cursor score for 6 is " + score6);
//                       Log.i(ScoreActivity.class.getName(), "Cursor score for 9 is " + score9);
//                       Log.i(ScoreActivity.class.getName(), "Cursor score for 12 is " + score12);
//
//
//                       while (!cursor.isAfterLast()) {
//                           arrayListOfScore.add(score4);
//                           cursor.moveToNext();
//                       }
//                   }
//               } catch (NullPointerException e) {
//                   Log.i(ScoreActivity.class.getName(),"NullPointerException thrown!");
//               }
//








    public void startNewGame(View view) {
        Intent intent = new Intent(this, ComplexityActivity.class);// redirect from this page to MainActivity page- list of images
        userId = getUserId();
        username = getUsername();
        intent.putExtra("userIdFromScoreActivity", userId);
        intent.putExtra("usernameFromScoreActivity", username);
        Log.i(ScoreActivity.class.getName(), "User id " + userId + " was sent to complexity activity");
        startActivity(intent);
    }

    public void goToMenu(View view) {
        // redirect from this activity to the first activity, for now it redirects to MianActivy!!!!!!!!!!!!!!!!
        Intent intent = new Intent(this, ComplexityActivity.class);
        startActivity(intent);
    }

    // --- methods to get userId, level and time from intent extras
    public int getUserId() {
        Intent getUserIntent = getIntent();
        userId = getUserIntent.getIntExtra("userId", 0);
        Log.i(ScoreActivity.class.getName(), "Id is " + userId);
        return userId;
    }

    public String getUsername() {
        Intent getUserIntent = getIntent();
        username = getUserIntent.getStringExtra("username");
        Log.i(ScoreActivity.class.getName(), "Username is " + username);
        return username;
    }

    public int getLevel() {
        Intent getLevelIntent = getIntent();
        level = getLevelIntent.getIntExtra("level", 0);
        Log.i(ScoreActivity.class.getName(), "Level is " + level);
        return level;
    }

    public int getTime() {
        Intent getTimeIntent = getIntent();
        int time = getTimeIntent.getIntExtra("time", 0);
        return time;
    }
    // --- /methods to get userId, level and time

    public Cursor getData() {
        Cursor cursor = database.rawQuery("Select * from " + TABLE_TIMER, null);
        return cursor;
    }

    //Method to go to the StartActivity
    public void goHome() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}