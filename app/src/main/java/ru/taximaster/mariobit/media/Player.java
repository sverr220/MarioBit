package ru.taximaster.mariobit.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;

public class Player {

    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private MediaPlayer player;
    /**
     * Очередь из звуков.
     */
    private int rawId;

    private boolean isRepeate;

    private Context context;

    private Handler playHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (player != null) {
                player.release();
                player = null;
            }

            if (rawId <= 0)
                return;

            // Порядок действия: звук с устройства, после звук с сервера, потом сист. звук.
            player = MediaPlayer.create(context, rawId);
            player.start();
            player.setVolume(100, 100);
            player.setOnCompletionListener(onPlayerComplete);
        }
    };

    private MediaPlayer.OnCompletionListener onPlayerComplete = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) {
            if (isRepeate)
                play();
            else
                stop();
        }
    };

    // ===========================================================
    // Constructors
    // ===========================================================

    public Player(final Context context, final int rawId, final boolean isRepeate) {
        super();
        this.context = context;
        this.rawId = rawId;
        this.isRepeate = isRepeate;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    public void play() {
        if (player == null || !player.isPlaying()) {
            playHandler.removeMessages(0);
            playHandler.sendEmptyMessage(0);
        }
    }

    /**
     * Остановить проигрывание текущей композиции.
     */
    public void stop() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
