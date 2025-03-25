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
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LearningFragment extends Fragment {

    private SharedPreferences preferences;
    private TextView tvMessage, tvTimeResult;
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
        View view = inflater.inflate(R.layout.fragment_learning, container, false);

        preferences = requireActivity().getSharedPreferences("LearningPrefs", Context.MODE_PRIVATE);
        tvMessage = view.findViewById(R.id.tvMessage);
        tvTimeResult = view.findViewById(R.id.tvTimeResult);
        Button btnCheckTime = view.findViewById(R.id.btnCheckTime);
        Button btnSummary = view.findViewById(R.id.btnSummary);
        Button btnDone = view.findViewById(R.id.btnDone);

        String goal = preferences.getString("goal", "尚未設定目標");
        long startTime = preferences.getLong("startTime", 0);
        String startTimeText = DateFormat.format("HH:mm:ss", new Date(startTime)).toString();
        tvMessage.setText("你的今日目標是：「" + goal + "」\n學習開始時間：" + startTimeText);

        btnCheckTime.setOnClickListener(v -> {
            long now = System.currentTimeMillis();
            long duration = now - startTime;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60;
            tvTimeResult.setText("你已學習了 " + minutes + " 分 " + seconds + " 秒");
        });

        btnSummary.setOnClickListener(v -> {
            if (pagerController != null) {
                pagerController.goToPage(2); // 切換到 Summary 頁
            }
        });

        btnDone.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "我完成學習任務囉！", Toast.LENGTH_SHORT).show();
            if (pagerController != null) {
                pagerController.goToPage(0); // 回首頁
            }
        });

        return view;
    }
}