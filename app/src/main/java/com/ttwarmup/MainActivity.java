/package com.ttwarmup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(60, 200, 60, 60);

        TextView tv = new TextView(this);
        tv.setText("1. Включи службу в настройках доступности.\n2. Открой TikTok.\n3. Служба начнёт листать, лайкать и писать комменты.\n\nДля остановки — выключи службу в настройках.");
        tv.setTextSize(18);
        ll.addView(tv);

        Button btn = new Button(this);
        btn.setText("Открыть настройки доступности");
        btn.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        ll.addView(btn);

        setContentView(ll);
    }
}
