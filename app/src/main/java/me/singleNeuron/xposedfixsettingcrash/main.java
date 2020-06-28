package me.singleNeuron.xposedfixsettingcrash;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class main implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        //if (lpparam.packageName.equals("com.android.settings")||lpparam.packageName.equals("android")) {
            try {
                Class clazz = XposedHelpers.findClass("com.android.settingslib.drawer.Tile",lpparam.classLoader);
                XposedBridge.hookAllMethods(clazz,"getTitle", new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        try {
                            return XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args);
                        } catch (Exception e) {
                            try {
                                Log.d("xposedfixsettingcrash",(String)XposedHelpers.callMethod(param.thisObject,"getDescription"));
                            } catch (Exception ee) {
                                //ignored
                            }
                            e.printStackTrace();
                        }
                        return "";
                    }
                });
                XposedBridge.hookAllMethods(clazz,"hasOrder", new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        try {
                            return XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                });
                Log.d("xposedfixsettingcrash ",lpparam.packageName);
            } catch (XposedHelpers.ClassNotFoundError e) {
                //ignored
            }
        //}
    }

}
