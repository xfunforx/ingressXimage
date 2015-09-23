package com.xingress.ximage;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Ximage implements IXposedHookLoadPackage {
    boolean toggle;

    @Override
    public void handleLoadPackage(LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals("com.nianticproject.ingress")) {
            return;
        }
        final XSharedPreferences xSharedPreferences = new XSharedPreferences("com.xingress.ximage", "ximage");
        toggle = xSharedPreferences.getBoolean("ximage", false);
        XposedBridge.log("toggle:" + String.valueOf(toggle));
        XposedBridge.log("found the ingress started: " + loadPackageParam.packageName);
        findAndHookMethod("com.nianticproject.ingress.gameentity.components.portal.SimplePhotoStreamInfo", loadPackageParam.classLoader, "getCoverPhoto", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (toggle) {
                    param.setResult(null);
                }
            }
        });
    }
}

