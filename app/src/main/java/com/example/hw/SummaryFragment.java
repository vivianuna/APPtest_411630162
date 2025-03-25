package com.example.hw;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SummaryFragment extends Fragment {

    private ViewPagerController pagerController;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ViewPagerController) {
            pagerController = (ViewPagerController) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        TextView tvSummary = view.findViewById(R.id.tvSummary);
        Button btnBack = view.findViewById(R.id.btnBackToLearning);

        SharedPreferences preferences = requireActivity().getSharedPreferences("LearningPrefs", Context.MODE_PRIVATE);
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

        btnBack.setOnClickListener(v -> {
            if (pagerController != null) {
                pagerController.goToPage(1); // 回到學習頁
            }
        });

        return view;
    }
}