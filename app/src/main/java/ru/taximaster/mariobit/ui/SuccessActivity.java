package ru.taximaster.mariobit.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ru.taximaster.mariobit.R;
import ru.taximaster.mariobit.ScanningUnit;
import ru.taximaster.mariobit.media.Player;

public class SuccessActivity extends CommonActivity {

    private ScanningUnit unit;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String resourceName = getIntent().getExtras().getString("file_name");
        unit = new ScanningUnit(this, resourceName);

        setControl();
    }

    @Override
    public void finish() {
        super.finish();
        player.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        player.play();
    }

    private void setControl() {
        findViewById(R.id.iv_play).setVisibility(unit.isRepeate() ? View.INVISIBLE : View.VISIBLE);
        findViewById(R.id.iv_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.stop();
                player.play();
            }
        });

        findViewById(R.id.iv_hint).setVisibility(unit.isExistHint() ? View.VISIBLE : View.GONE);
        findViewById(R.id.iv_hint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HintActivity.show(SuccessActivity.this, unit.getHint());
            }
        });

        Button complete = (Button) findViewById(R.id.btn_complete);
        complete.setTypeface(getTypefaceBtn());
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageDrawable(unit.getDrawable());
        if (unit.isExistHint())
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HintActivity.show(SuccessActivity.this, unit.getHint());
                }
            });

        TextView textView = (TextView) findViewById(R.id.tv_name);
        textView.setTypeface(getTypefaceContent());
        textView.setText(unit.getName());

        player = new Player(this, unit.getRaw(), unit.isRepeate());
    }

    public static boolean show(final Context context, final String fileName) {
        try {
            Intent intent = new Intent(context, SuccessActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("file_name", fileName);
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

}
