package com.codiplay.arduinohexuploader;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.widget.Toast;

import UsbSerialHelper.SerialPortStreamUart;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import java.util.HashMap;
import java.util.Iterator;

import ArduinoUploader.ArduinoSketchUploader;
import ArduinoUploader.ArduinoSketchUploaderOptions;
import ArduinoUploader.ArduinoUploaderException;
import ArduinoUploader.Hardware.ArduinoModel;
import ArduinoUploader.IArduinoUploaderLogger;
import CSharpStyle.IProgress;

public class ArduinoHexFileUploader extends UnityPlayerActivity {

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private final BroadcastReceiver mUsbHardwareReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(ACTION_USB_PERMISSION)) {
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    // User accepted our USB connection. Try to open the device as a serial port
                    UsbDevice grantedDevice = intent.getExtras().getParcelable(UsbManager.EXTRA_DEVICE);

                    if (grantedDevice != null) {
                        Upload();
                        UnityPlayer.currentActivity.unregisterReceiver(mUsbHardwareReceiver);
                    }
                } else {
                    // User not accepted our USB connection. Send an Intent to the Main Activity
                    UnityCall_Message("error_USB Permission denied");
                    UnityPlayer.currentActivity.unregisterReceiver(mUsbHardwareReceiver);
                }
            }
        }
    };

    public static UsbManager usbManager;
    public static UsbDevice usbDevice;

    private int boardType;
    private String filePath;

    // Represent messages received from Unity as toast messages
    public void UnityAnswer_ShowToast(String message) {
        Toast.makeText(UnityPlayer.currentActivity, message, Toast.LENGTH_SHORT).show();
    }

    // Request Arduino hex file upload from Unity
    public void UnityAnswer_UploadArduinoHex(int type, String path) {
        usbManager = (UsbManager) UnityPlayer.currentActivity.getSystemService(Context.USB_SERVICE);
        usbDevice = null;

        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            usbDevice = deviceIterator.next();
            break;
        }

        if (usbDevice == null) {
            // no connected usbDevice
            UnityCall_Message("error_USB not connected");
        } else {
            // there is a connected usbDevice
            boardType = type;
            filePath = path;

            IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
            UnityPlayer.currentActivity.registerReceiver(mUsbHardwareReceiver, filter);
            PendingIntent permissionIntent = PendingIntent.getBroadcast(UnityPlayer.currentActivity, 0, new Intent(ACTION_USB_PERMISSION), 0);
            usbManager.requestPermission(usbDevice, permissionIntent);
        }
    }

    private IArduinoUploaderLogger logger = new IArduinoUploaderLogger() {

        @Override
        public void Error(String message, Exception exception) {
            UnityCall_Message("error_" + message);
        }

        @Override
        public void Warn(String message) {
//            UnityCall_Message("Warn:" + message);
        }

        @Override
        public void Info(String message) {
//          UnityCall_Message("Info:" + message);
            if (message.startsWith("All done")) {
                UnityCall_Message("success");
            }
        }

        @Override
        public void Debug(String message) {
//          UnityCall_Message("Debug:" + message);
        }

        @Override
        public void Trace(String message) {
//          UnityCall_Message("Trace:" + message);
        }
    };

    private IProgress<Double> progress = new IProgress<Double>() {
        @Override
        public void Report(Double value) {
            UnityCall_Message("uploading progress... " + Math.round(value*100) + "%");
        }
    };

    // Hex file upload start
    private void Upload() {
        ArduinoSketchUploaderOptions options = new ArduinoSketchUploaderOptions();

        switch (boardType) {
            case 0:
            case 2:
                options.setArduinoModel(ArduinoModel.UnoR3);
                options.setFileName(filePath);
                options.setPortName(usbDevice.getDeviceName());
                break;
            case 1:
                options.setArduinoModel(ArduinoModel.NanoR3);
                options.setFileName(filePath);
                options.setPortName(usbDevice.getDeviceName());
                break;
            case 3:
                options.setArduinoModel(ArduinoModel.NanoR2);
                options.setFileName(filePath);
                options.setPortName(usbDevice.getDeviceName());
                break;
            case 4:
                options.setArduinoModel(ArduinoModel.Mega2560);
                options.setFileName(filePath);
                options.setPortName(usbDevice.getDeviceName());
                break;
            case 5:
                options.setArduinoModel(ArduinoModel.Leonardo);
                options.setFileName(filePath);
                options.setPortName(usbDevice.getDeviceName());
                break;
            case 6:
                options.setArduinoModel(ArduinoModel.Micro);
                options.setFileName(filePath);
                options.setPortName(usbDevice.getDeviceName());
                break;
        }

        ArduinoSketchUploader<SerialPortStreamUart> uploader = new ArduinoSketchUploader<SerialPortStreamUart>(SerialPortStreamUart.class, options, logger, progress);

        try {
            UnityCall_Message("start");
            uploader.UploadSketch();
        } catch (ArduinoUploaderException e) {
            UnityCall_Message("error_" + e.toString());
        } catch (Exception ex) {
            UnityCall_Message("error_" + ex.toString());
        }
    }

    // send message to unity
    public void UnityCall_Message(String message) {
        UnityPlayer.UnitySendMessage("ArduinoSketchUploader", "JavaAnswer_Message", message);
    }
}
