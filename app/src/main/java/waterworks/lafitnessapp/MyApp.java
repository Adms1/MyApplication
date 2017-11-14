package waterworks.lafitnessapp;

import android.app.Application;

import android.content.Context;

//import android.support.multidex.MultiDex;

import org.acra.ACRA;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;


/**
 * Created by Megha on 3/21/2017.
 */
//@ReportsCrashes(
//        formUri = "shah10846@gmail.com",
//        reportType = HttpSender.Type.JSON,
//        httpMethod = HttpSender.Method.POST,
//        formUriBasicAuthLogin = "GENERATED_USERNAME_WITH_WRITE_PERMISSIONS",
//        formUriBasicAuthPassword = "GENERATED_PASSWORD",
//
//        customReportContent = {
//                ReportField.APP_VERSION_CODE,
//                ReportField.APP_VERSION_NAME,
//                ReportField.ANDROID_VERSION,
//                ReportField.PACKAGE_NAME,
//                ReportField.REPORT_ID,
//                ReportField.BUILD,
//                ReportField.STACK_TRACE
//        },
//        mode = ReportingInteractionMode.TOAST,
//        resToastText = R.string.toast_crash
//)
@ReportsCrashes(mailTo = "shrenik.diwanji@gmail.com,jon@waterworksswim.com,navin@admsystems.net",// my email here
        mode = ReportingInteractionMode.SILENT,
        resToastText = R.string.toast_crash)

public class MyApp extends Application {

    @Override
    public void onCreate() {

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        super.onCreate();
    }
//
//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }


}


