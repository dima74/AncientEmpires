package ru.ancientempires;

import android.app.Application;

//@ReportsCrashes(
//		formUri = "https://dima74.cloudant.com/acra-ancientempires/_design/acra-storage/_update/report",
//		reportType = org.acra.sender.HttpSender.Type.JSON,
//		httpMethod = org.acra.sender.HttpSender.Method.PUT,
//		formUriBasicAuthLogin = "therflonlyinkingcheystra",
//		formUriBasicAuthPassword = "5f6f4bade3cb307eac8e23526636416d1655548a",
//		// Your usual ACRA configuration
//
//		mode = ReportingInteractionMode.TOAST,
//		resToastText = R.string.toast_crash)
public class MyApplication extends Application {

	public static MyApplication application;

	@Override
	public void onCreate() {
		super.onCreate();
//		ACRA.init(this);
		application = this;
	}

	public static float getDensity() {
		return application.getResources().getDisplayMetrics().density;
	}
}
