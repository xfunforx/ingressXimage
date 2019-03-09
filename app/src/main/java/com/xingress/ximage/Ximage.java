package com.xingress.ximage;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static com.xingress.ximage.MainActivity.*;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Ximage implements IXposedHookLoadPackage {
	public static final String className = "com.nianticproject.ingress.gameentity.components.portal.SimplePhotoStreamInfo";
	public static final String methodName = "getCoverPhoto";

	@Override
	public void handleLoadPackage(LoadPackageParam loadPackageParam) {
		if (!loadPackageParam.packageName.equals(INGRESSPACKAGENAME) && !loadPackageParam.packageName.equals(SCANNERREDACTEDPACKAGENAME)) {
			return;
		}

		log("Found the Ingress started: " + loadPackageParam.packageName);

		final XSharedPreferences xSharedPreferences = new XSharedPreferences(Ximage.class.getPackage().getName(), XIMAGE);

		findAndHookMethod(className, loadPackageParam.classLoader, methodName, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				xSharedPreferences.reload();
				boolean toggle = xSharedPreferences.getBoolean(XIMAGE, false);

				log("toggle: " + String.valueOf(toggle));

				if (toggle) {
					param.setResult(null);
				}
			}
		});
	}
}
