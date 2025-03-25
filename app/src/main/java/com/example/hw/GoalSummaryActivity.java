package com.example.hw;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GoalSummaryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_summary);

        TextView tvSummary = findViewById(R.id.tvSummary);
        Button btnBack = findViewById(R.id.btnBackToLearning);

        SharedPreferences preferences = getSharedPreferences("LearningPrefs", MODE_PRIVATE);
        String goal = preferences.getString("goal", "尚未設定目標");
        long startTime = preferences.getLong("startTime", 0);
        String startText = DateFormat.format("HH:mm:ss", new Date(startTime)).toString();

        long now = System.currentTimeMillis();
        long duration = now - startTime;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60;

        String summary = "📘 今日目標：" + goal +
                "\n🕒 開始時間：" + startText +
                "\n⏱️ 已學習：" + minutes + " 分 " + seconds + " 秒";
        tvSummary.setText(summary);

        btnBack.setOnClickListener(v -> finish());
    }
}
