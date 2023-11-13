package extentReport;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

public class ExtentReporter {

    public static ExtentReports generateExtentReport() {

        ExtentReports extentReport = new ExtentReports();
        File extentReportFile = new File(System.getProperty("user.dir") + "/testOUtput/extentReports/extentReport.html");

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(extentReportFile);

        //set configuration of extentReprt looks and theme using sparkReporter
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setReportName("API Playwright Hybrid Automation Framework");
        sparkReporter.config().setDocumentTitle("API Automation Report");
        //sparkReporter.config().setTimeStampFormat("MM/dd/YYYY hh:mm:ss");

        //To bind all sparkReporter request above use following command
        extentReport.attachReporter(sparkReporter);

        extentReport.setSystemInfo("Operating System", System.getProperty("os.name"));
        extentReport.setSystemInfo("User Name", System.getProperty("user.name"));
        extentReport.setSystemInfo("Java Version", System.getProperty("java.version"));


        //Use Following Line to get OS, UserName and JavaVersion Information which implemented in above code
        //System.getProperties().list(System.out);

        return extentReport;
    }
}
