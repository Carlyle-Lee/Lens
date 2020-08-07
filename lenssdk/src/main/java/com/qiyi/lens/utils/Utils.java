/*
 *
 * Copyright (C) 2020 iQIYI (www.iqiyi.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.qiyi.lens.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {
    public static String genRandomString() {
        int len = 4;
        //(int) (Math.random() * 100);
        int p = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while (p < len) {
            int a = (int) (Math.random() * 26);
            char c = (char) ('A' + a);
            stringBuilder.append(c);
            p++;
        }
        return stringBuilder.toString() + System.currentTimeMillis();
    }


    public static String getSimpleClassName(String s) {
        if (s != null && s.length() > 0) {
            int lst = s.lastIndexOf('.');
            return s.substring(lst + 1);
        }
        return "";
    }

    public static boolean isValidIP(String key) {
        if (key == null || key.length() == 0) {
            return false;
        } else {
            return key.matches("((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))");
        }
    }

    public static String array2String(String[] ar) {
        if (ar == null || ar.length == 0) {
            return "";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : ar) {
                stringBuilder.append(s);
                stringBuilder.append(',');
            }
            stringBuilder.setLength(stringBuilder.length() - 1);
            return stringBuilder.toString();
        }

    }

    public static String[] string2Array(String data) {
        if (data != null && data.length() > 0) {
            return data.split(",");
        }
        return new String[0];
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static String getViewVisivility(View view) {
        if (view != null) {
            int vis = view.getVisibility();
            if (vis == View.GONE) {
                return "GONE";
            } else if (vis == View.INVISIBLE) {
                return "INVISIBLE";
            } else {
                return "VISIBLE";
            }
        }
        return "null";
    }


    public static boolean isXiaomiDevice() {
        String manu = Build.MANUFACTURER;
        if ("Xiaomi".equals(manu)) {
            return true;
        }
        return false;
    }


    public static StringBuilder throwable2String(Throwable throwable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(throwable.toString());
        StackTraceElement[] elements = throwable.getStackTrace();
        int p = 0;
        for (StackTraceElement element : elements) {
            stringBuilder.append(element.toString());
            stringBuilder.append("\n");
            p++;
            if (p > 20) break;
        }
        return stringBuilder;
    }


    public static boolean checkPermission(Activity activity, String permission) {
        if (isGranted(activity, permission)) {
            return true;
        } else { // request permission
            ActivityCompat.requestPermissions(activity, new String[]{permission}, 0);
        }
        return false;
    }

    private static boolean isGranted(Context context, final String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || PackageManager.PERMISSION_GRANTED
                == ContextCompat.checkSelfPermission(context, permission);
    }


    public static boolean hasFloatingWindowPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(ApplicationLifecycle.getInstance().getContext());
        }
        return true;
    }


    public static void requestFloatingPermission() {
        Context context = ApplicationLifecycle.getInstance().getCurrentActivity();
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static String parseStorageSize(long memoSizeInBytes, int intShift) {
        float var = memoSizeInBytes;
        float K = 1024;
        int shift = intShift;
        while (var > K && shift < 3) {
            var = var / K;
            shift++;
        }

        String ampunt = String.format("%.2f", var);
        switch (shift) {
            case 0:
                return ampunt + "B";
            case 1:
                return ampunt + "K";
            case 2:
                return ampunt + "M";
            case 3:
                return ampunt + "G";
        }
        return ampunt;
    }

    /**
     * return 0 : if the same, -1: var1 is smaller;
     *
     * @param var1
     * @param var2
     * @return
     */
    public static int storageDataCompare(String var1, String var2) {
        if (isEmpty(var1)) {
            if (isEmpty(var2)) return 0;
            else return -1;
        } else {
            if (isEmpty(var2)) return 1; // var1 is larger than var2
            // compare value;
            if(var1.equals(var2)) return 0;

            long a = parseStorageData(var1);
            long b = parseStorageData(var2);
            return a > b ? 1 : -1;
        }
    }

    private static long parseStorageData(@NonNull String var) {
        int len = var.length();
        int shift = 0;
        if (len > 0) {
            char c = var.charAt(len - 1);
            switch (c) {
                case 'M':
                case 'm':
                    shift = 20;
                    break;
                case 'K':
                case 'k':
                    shift = 10;
                    break;
                case 'G':
                case 'g':
                    shift = 30;
                    break;
                default:
                    //do thing
            }
            if (c < '0' || c > '9') {
                // trim
                var = var.substring(0, len - 1);
            }

            // parse:
            if (var.contains(".")) {
                float value = parseFloatSafely(var);
                return (long) (value * (1 << shift));
            } else {
                long value = parseLongSafely(var);
                return value * (1 << shift);
            }
        }

        return 0;

    }

    private static float parseFloatSafely(String value) {

        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            // dp nothing
        }
        return 0f;
    }

    private static long parseLongSafely(String value) {

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            // dp nothing
        }
        return 0L;
    }


    public static void printCmd(String cmd) {
        BufferedReader bufferedReader = null;
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line = bufferedReader.readLine();
            if (line == null) {
                FileUtils.closeSafely(bufferedReader);
                bufferedReader = new BufferedReader(
                        new InputStreamReader(process.getErrorStream()));
                line = bufferedReader.readLine();
                if (line == null) {
                    Log.d("XXXXXXX", "--empty nothing to read--");
                    return;
                }
            }
            Log.d("XXXXXXX", line);
            while (true) {
                line = bufferedReader.readLine();
                if (line == null) break;
                if (line.contains("cpu")) {
                    Log.d("XXXXXXX", line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                FileUtils.closeSafely(bufferedReader);
            }
        }
    }


}
