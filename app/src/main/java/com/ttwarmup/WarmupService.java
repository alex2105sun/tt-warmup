/package com.ttwarmup;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.List;
import java.util.Random;

public class WarmupService extends AccessibilityService {

    private Handler handler;
    private Random random = new Random();
    private boolean running = false;

    private final String[] COMMENTS = {
        "это топ", "ору с этого", "сохранил себе", "ахахах", "ну это база",
        "в точку", "не ожидал", "лучшее что видел сегодня", "почему так мало лайков",
        "пересматриваю", "это я", "жесть", "огонь", "подписался", "полезно",
        "спасибо за это", "ору", "не могу перестать смотреть", "кто ещё в 2026",
        "это гениально", "база", "имба", "согласен на все сто"
    };

    // Координаты для 720x1612
    private final int ICON_LIKE_X = 650, ICON_LIKE_Y = 910;
    private final int ICON_COMMENT_X = 650, ICON_COMMENT_Y = 1015;
    private final int ICON_SHARE_X = 650, ICON_SHARE_Y = 1250;
    private final int INPUT_FIELD_X = 360, INPUT_FIELD_Y = 1550;
    private final int SEND_BTN_X = 680, SEND_BTN_Y = 1550;

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        handler = new Handler(Looper.getMainLooper());
        running = true;
        handler.postDelayed(this::mainLoop, 5000);
    }

    private void mainLoop() {
        if (!running) return;
        handler.postDelayed(() -> {
            try { doCycle(); } catch (Exception e) { e.printStackTrace(); }
            mainLoop();
        }, 1000);
    }

    private void doCycle() {
        double r = random.nextDouble();
        if (r < 0.40) {
            swipeUp();
            sleep(randomInt(300, 800));
        } else if (r < 0.65) {
            tap(ICON_LIKE_X, ICON_LIKE_Y);
            sleep(randomInt(500, 1200));
            swipeUp();
        } else if (r < 0.75) {
            tap(ICON_COMMENT_X, ICON_COMMENT_Y);
            sleep(randomInt(1500, 2500));
            tap(INPUT_FIELD_X, INPUT_FIELD_Y);
            sleep(randomInt(1000, 1500));
            typeComment();
            sleep(randomInt(1000, 1500));
            tap(SEND_BTN_X, SEND_BTN_Y);
            sleep(randomInt(1000, 2000));
            pressBack();
            sleep(randomInt(500, 1000));
            swipeUp();
        } else if (r < 0.85) {
            longPress(ICON_SHARE_X, ICON_SHARE_Y);
            sleep(randomInt(1500, 2500));
            tap(360, 1300);
            sleep(randomInt(1000, 1500));
            swipeUp();
        } else if (r < 0.95) {
            sleep(randomInt(4000, 15000));
            swipeUp();
        } else {
            sleep(randomInt(15000, 45000));
        }
    }

    private void typeComment() {
        String text = COMMENTS[random.nextInt(COMMENTS.length)];
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) return;
        List<AccessibilityNodeInfo> editTexts = root.findAccessibilityNodeInfosByViewId("com.zhiliaoapp.musically:id/edit_text");
        if (editTexts == null || editTexts.isEmpty()) {
            editTexts = root.findAccessibilityNodeInfosByClassName("android.widget.EditText");
        }
        if (editTexts != null && !editTexts.isEmpty()) {
            AccessibilityNodeInfo edit = editTexts.get(0);
            Bundle args = new Bundle();
            args.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            edit.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args);
        }
    }

    private void pressBack() {
        performGlobalAction(GLOBAL_ACTION_BACK);
    }

    private void swipeUp() {
        int x = getScreenWidth() / 2;
        int y1 = (int) (getScreenHeight() * 0.8);
        int y2 = (int) (getScreenHeight() * 0.2);
        swipe(x, y1, x, y2, 280);
    }

    private void tap(int x, int y) {
        Path p = new Path(); p.moveTo(x, y);
        GestureDescription.StrokeDescription s = new GestureDescription.StrokeDescription(p, 0, 50);
        GestureDescription.Builder b = new GestureDescription.Builder();
        b.addStroke(s);
        dispatchGesture(b.build(), null, null);
    }

    private void longPress(int x, int y) {
        Path p = new Path(); p.moveTo(x, y);
        GestureDescription.StrokeDescription s = new GestureDescription.StrokeDescription(p, 0, 600);
        GestureDescription.Builder b = new GestureDescription.Builder();
        b.addStroke(s);
        dispatchGesture(b.build(), null, null);
    }

    private void swipe(int x1, int y1, int x2, int y2, int dur) {
        Path p = new Path(); p.moveTo(x1, y1); p.lineTo(x2, y2);
        GestureDescription.StrokeDescription s = new GestureDescription.StrokeDescription(p, 0, dur);
        GestureDescription.Builder b = new GestureDescription.Builder();
        b.addStroke(s);
        dispatchGesture(b.build(), null, null);
    }

    private void sleep(long ms) { try { Thread.sleep(ms); } catch (InterruptedException ignored) {} }
    private int randomInt(int min, int max) { return min + random.nextInt(max - min); }
    private int getScreenWidth() { return getResources().getDisplayMetrics().widthPixels; }
    private int getScreenHeight() { return getResources().getDisplayMetrics().heightPixels; }

    @Override public void onAccessibilityEvent(AccessibilityEvent event) {}
    @Override public void onInterrupt() { running = false; }
    @Override public void onDestroy() { running = false; super.onDestroy(); }
                        }
