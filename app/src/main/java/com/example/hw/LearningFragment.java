package com.example.hw;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LearningFragment extends Fragment {

    private SharedPreferences preferences;
    private ViewPagerController pagerController;
    private TextView tvMessage, tvTimeResult;
    private Handler handler = new Handler();
    private Runnable updateTimerRunnable;
    private long sessionStartTime;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ViewPagerController) {
            pagerController = (ViewPagerController) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learning, container, false);

        preferences = requireActivity().getSharedPreferences("LearningPrefs", Context.MODE_PRIVATE);
        tvMessage = view.findViewById(R.id.tvMessage);
        tvTimeResult = view.findViewById(R.id.tvTimeResult);
        Button btnDone = view.findViewById(R.id.btnDone);

        btnDone.setOnClickListener(v -> {
            long now = System.currentTimeMillis();
            long sessionDuration = now - sessionStartTime;
            long totalMillis = preferences.getLong("totalStudyMillis", 0);
            preferences.edit().putLong("totalStudyMillis", totalMillis + sessionDuration).apply();

            Toast.makeText(getActivity(), "我完成學習任務囉！", Toast.LENGTH_SHORT).show();
            if (pagerController != null) {
                pagerController.goToPage(0);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        sessionStartTime = System.currentTimeMillis();
        String goal = preferences.getString("goal", "尚未設定目標");
        long startTime = preferences.getLong("startTime", 0);
        String startTimeText = DateFormat.format("HH:mm:ss", new Date(startTime)).toString();
        tvMessage.setText("你的今日目標是：「" + goal + "」\n學習開始時間：" + startTimeText);

        handler.postDelayed(updateTimerRunnable = new Runnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                long totalMillis = preferences.getLong("totalStudyMillis", 0) + (now - sessionStartTime);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(totalMillis);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(totalMillis) % 60;
                tvTimeResult.setText("你已學習了 " + minutes + " 分 " + seconds + " 秒");
                handler.postDelayed(this, 1000);
            }
        }, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(updateTimerRunnable);

        long now = System.currentTimeMillis();
        long sessionDuration = now - sessionStartTime;
        long totalMillis = preferences.getLong("totalStudyMillis", 0);
        preferences.edit().putLong("totalStudyMillis", totalMillis + sessionDuration).apply();
    }
}
