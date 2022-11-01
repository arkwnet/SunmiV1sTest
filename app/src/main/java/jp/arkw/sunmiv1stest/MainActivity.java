package jp.arkw.sunmiv1stest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;

import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;

public class MainActivity extends AppCompatActivity {

    private SunmiPrinterService sunmiPrinterService;
    public static int NoSunmiPrinter = 0x00000000;
    public static int CheckSunmiPrinter = 0x00000001;
    public static int FoundSunmiPrinter = 0x00000002;
    public static int LostSunmiPrinter = 0x00000003;
    public int sunmiPrinter = CheckSunmiPrinter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSunmiPrinterService(this);

        // Resources
        String text = "https://arkw.net/";
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_192x256);

        // Print Text
        findViewById(R.id.button_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printText(text);
                feedPaper();
            }
        });

        // Print Image
        findViewById(R.id.button_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printImage(bitmap);
                feedPaper();
            }
        });
    }

    private void printText(String text) {
        try {
            sunmiPrinterService.printText(text, new InnerResultCallback() {
                @Override
                public void onRunResult(boolean isSuccess) throws RemoteException {
                }

                @Override
                public void onReturnString(String result) throws RemoteException {
                }

                @Override
                public void onRaiseException(int code, String msg) throws RemoteException {
                }

                @Override
                public void onPrintResult(int code, String msg) throws RemoteException {
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        };
    }

    private void printImage(Bitmap bitmap) {
        try {
            sunmiPrinterService.printBitmap(bitmap, new InnerResultCallback() {
                @Override
                public void onRunResult(boolean isSuccess) throws RemoteException {
                }

                @Override
                public void onReturnString(String result) throws RemoteException {
                }

                @Override
                public void onRaiseException(int code, String msg) throws RemoteException {
                }

                @Override
                public void onPrintResult(int code, String msg) throws RemoteException {
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        };
    }

    private void feedPaper() {
        try {
            sunmiPrinterService.lineWrap(5, new InnerResultCallback() {
                @Override
                public void onRunResult(boolean isSuccess) throws RemoteException {
                }

                @Override
                public void onReturnString(String result) throws RemoteException {
                }

                @Override
                public void onRaiseException(int code, String msg) throws RemoteException {
                }

                @Override
                public void onPrintResult(int code, String msg) throws RemoteException {
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        };
    }

    private InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
        @Override
        protected void onConnected(SunmiPrinterService service) {
            sunmiPrinterService = service;
            checkSunmiPrinterService(service);
        }

        @Override
        protected void onDisconnected() {
            sunmiPrinterService = null;
            sunmiPrinter = LostSunmiPrinter;
        }
    };

    private void checkSunmiPrinterService(SunmiPrinterService service){
        boolean ret = false;
        try {
            ret = InnerPrinterManager.getInstance().hasPrinter(service);
        } catch (InnerPrinterException e) {
            e.printStackTrace();
        }
        sunmiPrinter = ret?FoundSunmiPrinter:NoSunmiPrinter;
    }

    public void initSunmiPrinterService(Context context){
        try {
            boolean ret =  InnerPrinterManager.getInstance().bindService(context,
                    innerPrinterCallback);
            if(!ret){
                sunmiPrinter = NoSunmiPrinter;
            }
        } catch (InnerPrinterException e) {
            e.printStackTrace();
        }
    }
}