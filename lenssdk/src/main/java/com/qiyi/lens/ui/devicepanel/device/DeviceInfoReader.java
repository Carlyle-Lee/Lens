package com.qiyi.lens.ui.devicepanel.device;

import android.util.Log;

import com.qiyi.lens.utils.FileUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 仅仅提供CPU 数据读取的封装
 * /proc/stat : 需要权限
 */
public class DeviceInfoReader {

    public CpuInfo read() {
        BufferedReader bufferedReader = null;
        try {
            Process process = Runtime.getRuntime().exec("top -n 1");
            bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line = "";
            while (true) {
                line = bufferedReader.readLine();
                if (line == null) break;
                if (line.contains("cpu")) {
                    Log.d("XXXXXXX", line);
                    CpuInfo cpuInfo = new CpuInfo(line);
                    return cpuInfo;
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
        return null;
    }



}
