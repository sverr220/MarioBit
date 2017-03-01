package ru.taximaster.mariobit.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ru.taximaster.mariobit.R;

public class HintActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);

        setControl();
    }

    private void setControl() {
        int text = getIntent().getExtras().getInt("resId", -1);
        TextView textView = (TextView) findViewById(R.id.tv_hint);
        textView.setTypeface(getTypefaceContent());
        textView.setText(text);

        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public static boolean show(final Context context, final int resId) {
        try {
            Intent intent = new Intent(context, HintActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("resId", resId);
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }
}
