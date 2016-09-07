/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package android.platform.test.helpers;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.EventCondition;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;
import android.view.KeyEvent;

import java.io.IOException;


public class DPadHelper {

    private static final String TAG = DPadHelper.class.getSimpleName();
    private static final long DPAD_DEFAULT_WAIT_TIME_MS = 1000; // 1 sec
    private static DPadHelper mInstance;
    private UiDevice mDevice;


    private DPadHelper(Instrumentation instrumentation) {
        mDevice = UiDevice.getInstance(instrumentation);
    }

    public static DPadHelper getInstance(Instrumentation instrumentation) {
        if (mInstance == null) {
            mInstance = new DPadHelper(instrumentation);
        }
        return mInstance;
    }

    public boolean pressDPad(Direction direction) {
        return pressDPad(direction, 1, DPAD_DEFAULT_WAIT_TIME_MS);
    }

    public void pressDPad(Direction direction, long repeat) {
        pressDPad(direction, repeat, DPAD_DEFAULT_WAIT_TIME_MS);
    }

    /**
     * Presses DPad button of the same direction for the count times.
     * It sleeps between each press for DPAD_DEFAULT_WAIT_TIME_MS.
     *
     * @param direction the direction of the button to press.
     * @param repeat the number of times to press the button.
     * @param timeout the timeout for the wait.
     * @return true if the last key simulation is successful, else return false
     */
    public boolean pressDPad(Direction direction, long repeat, long timeout) {
        int iteration = 0;
        boolean result = false;
        while (iteration++ < repeat) {
            switch (direction) {
                case LEFT:
                    result = mDevice.pressDPadLeft();
                    break;
                case RIGHT:
                    result = mDevice.pressDPadRight();
                    break;
                case UP:
                    result = mDevice.pressDPadUp();
                    break;
                case DOWN:
                    result = mDevice.pressDPadDown();
                    break;
            }
            SystemClock.sleep(timeout);
        }
        return result;
    }

    public boolean pressDPadLeft() {
        return mDevice.pressDPadLeft();
    }

    public boolean pressDPadRight() {
        return mDevice.pressDPadRight();
    }

    public boolean pressDPadUp() {
        return mDevice.pressDPadUp();
    }

    public boolean pressDPadDown() {
        return mDevice.pressDPadDown();
    }

    public boolean pressHome() {
        return mDevice.pressHome();
    }

    public boolean pressBack() {
        return mDevice.pressBack();
    }

    public boolean pressDPadCenter() {
        return mDevice.pressDPadCenter();
    }

    public boolean pressEnter() {
        return mDevice.pressEnter();
    }

    public boolean pressPipKey() {
        return mDevice.pressKeyCode(KeyEvent.KEYCODE_WINDOW);
    }

    public boolean pressKeyCode(int keyCode) {
        return mDevice.pressKeyCode(keyCode);
    }

    public boolean longPressKeyCode(int keyCode) {
        try {
            mDevice.executeShellCommand(String.format("input keyevent --longpress %d", keyCode));
            return true;
        } catch (IOException e) {
            // Ignore
            Log.w(TAG, String.format("Failed to long press the key code: %d", keyCode));
            return false;
        }
    }

    /**
     * Press the key code, and waits for the given condition to become true.
     * @param condition
     * @param keyCode
     * @param timeout
     * @param <R>
     * @return
     */
    public <R> R pressKeyCodeAndWait(int keyCode, EventCondition<R> condition, long timeout) {
        return mDevice.performActionAndWait(new KeyEventRunnable(keyCode), condition, timeout);
    }

    public <R> R pressDPadCenterAndWait(EventCondition<R> condition, long timeout) {
        return mDevice.performActionAndWait(new KeyEventRunnable(KeyEvent.KEYCODE_DPAD_CENTER),
                condition, timeout);
    }

    public <R> R pressEnterAndWait(EventCondition<R> condition, long timeout) {
        return mDevice.performActionAndWait(new KeyEventRunnable(KeyEvent.KEYCODE_ENTER),
                condition, timeout);
    }

    private class KeyEventRunnable implements Runnable {
        private int mKeyCode;
        public KeyEventRunnable(int keyCode) {
            mKeyCode = keyCode;
        }
        @Override
        public void run() {
            mDevice.pressKeyCode(mKeyCode);
        }
    }
}
