package com.novillex.appium;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

import static com.novillex.appium.AutomatorConstants.ANDROID;

public class NovillexMobileDriverFactory {

    private ThreadLocal<AndroidDriver> driverThread = new ThreadLocal<>();

    private String getApkFile(String apkFileName) {
        String dirPath = System.getProperty("user.dir") + File.separator + "apps";
        File folder = new File(dirPath);

        if (!folder.exists() || !folder.isDirectory()) {
            throw new RuntimeException("Apps directory not found: " + dirPath);
        }

        Optional<File> apkFile = Arrays.stream(folder.listFiles())
                .filter(file -> file.getName().toLowerCase().endsWith(".apk"))
                .filter(file -> file.getName().toLowerCase().contains(apkFileName.toLowerCase()))
                .findFirst();

        return apkFile
                .map(File::getAbsolutePath)
                .orElseThrow(() -> new RuntimeException(
                        "APK not found with name: " + apkFileName
                ));
    }

    public AndroidDriver createAndroidDriver(String automatorName, String apkFileName) {
        UiAutomator2Options uiAutomator2Options = new UiAutomator2Options();
        uiAutomator2Options.setPlatformName(ANDROID);
        uiAutomator2Options.setAutomationName(automatorName);
        uiAutomator2Options.setApp(getApkFile(apkFileName));
        uiAutomator2Options.setNoReset(false);
        uiAutomator2Options.setDeviceName("Android Emulator");
        uiAutomator2Options.setAutoGrantPermissions(true);

        try {
            driverThread.set(new AndroidDriver(
                    new URL("http://127.0.0.1:4723"),
                    uiAutomator2Options
            ));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Driver initialization failed", e);
        }

        return driverThread.get();
    }
}
