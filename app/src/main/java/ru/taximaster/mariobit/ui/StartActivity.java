package ru.taximaster.mariobit.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.taximaster.mariobit.R;

public class StartActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeMain);
        setContentView(R.layout.activity_start);
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView hint = (TextView) findViewById(R.id.tv_hint);
        hint.setTypeface(getTypefaceContent());

        Button start = (Button) findViewById(R.id.bt_start);
        start.setTypeface(getTypefaceBtn());
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.show(StartActivity.this);
                StartActivity.this.finish();
            }
        });
    }
}
