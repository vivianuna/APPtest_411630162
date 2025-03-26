package com.example.hw;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;

public class HomeFragment extends Fragment {

    private SharedPreferences preferences;
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        preferences = requireActivity().getSharedPreferences("LearningPrefs", Context.MODE_PRIVATE);

        Button btnToast = view.findViewById(R.id.btnToast);
        Button btnSnackbar = view.findViewById(R.id.btnSnackbar);
        Button btnDialog = view.findViewById(R.id.btnDialog);
        Button btnSetGoal = view.findViewById(R.id.btnToSecond);

        btnSetGoal.setOnClickListener(v -> {
            long startTime = System.currentTimeMillis();
            preferences.edit()
                    .putLong("startTime", startTime)
                    .putLong("totalStudyMillis", 0)
                    .apply();
            Toast.makeText(getActivity(), "已開始學習", Toast.LENGTH_SHORT).show();
            if (pagerController != null) {
                pagerController.goToPage(1); // 直接跳轉到學習頁
            }
        });

        btnToast.setOnClickListener(v ->
                Toast.makeText(getActivity(), "記得每天複習！", Toast.LENGTH_SHORT).show()
        );

        btnSnackbar.setOnClickListener(v -> {
            String goal = preferences.getString("goal", "尚未設定今日目標");
            Snackbar.make(v, "今日目標是：" + goal, Snackbar.LENGTH_SHORT).show();
        });

        btnDialog.setOnClickListener(v -> {
            String[] goals = {"閱讀10頁", "寫作一篇日記", "背5個單字", "➕ 自訂目標"};
            new AlertDialog.Builder(getActivity())
                    .setTitle("今天的目標")
                    .setItems(goals, (dialog, which) -> {
                        if (which == 3) {
                            showCustomGoalDialog();
                        } else {
                            preferences.edit()
                                    .putString("goal", goals[which])
                                    .putLong("startTime", System.currentTimeMillis())
                                    .putLong("totalStudyMillis", 0)
                                    .apply();
                            Toast.makeText(getActivity(), "你選擇了：" + goals[which], Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        });

        return view;
    }

    private void showCustomGoalDialog() {
        EditText input = new EditText(getActivity());
        input.setHint("請輸入你的學習目標");

        new AlertDialog.Builder(getActivity())
                .setTitle("自訂學習目標")
                .setView(input)
                .setPositiveButton("儲存", (dialog, which) -> {
                    String customGoal = input.getText().toString().trim();
                    if (!customGoal.isEmpty()) {
                        preferences.edit()
                                .putString("goal", customGoal)
                                .putLong("startTime", System.currentTimeMillis())
                                .putLong("totalStudyMillis", 0)
                                .apply();
                        Toast.makeText(getActivity(), "已儲存自訂目標：" + customGoal, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "請輸入目標內容！", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
