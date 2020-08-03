package com.qiyi.lens.demo.utils;

import android.content.Context;

import com.lens.hook.utils.HookWrapper;
import com.qiyi.lens.utils.iface.IHookFrameWork;

public class HookImpl implements IHookFrameWork {
    @Override
    public void addCustomHookPluginPath(String path) {
        HookWrapper.addCustomHookPluginPath(path);
    }

    @Override
    public void usePluginMode(boolean pluginMode) {
        HookWrapper.usePluginMode(pluginMode);

    }

    @Override
    public void doHookDefault(String className) {
        HookWrapper.doHookDefault(className);

    }

    @Override
    public void setHookPluginInfo(Context context, String cacheDir, String pluginFile) {
        HookWrapper.setHookPluginInfo(context, cacheDir, pluginFile);
    }

    @Override
    public ClassLoader getHookPluginClassLoader() {
        return HookWrapper.getPluginClassLoader();
    }
}
