package UsbSerialHelper;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.codiplay.arduinohexuploader.ArduinoHexFileUploader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.felhr.usbserial.SerialBuffer.DEFAULT_READ_BUFFER_SIZE;

public class SerialPortStreamUart implements ISerialPortStream {

    protected int baudRate;
    protected UsbSerialDevice serialPort;

    private int readTimeout = 0;
    private int writeTimeout = 0;
    private UsbDeviceConnection connection;
    protected String portName;

    protected ArrayBlockingQueue<Integer> data = new ArrayBlockingQueue<Integer>(DEFAULT_READ_BUFFER_SIZE);
    protected volatile boolean is_open;

    private UsbSerialInterface.UsbReadCallback mReadCallback = new UsbSerialInterface.UsbReadCallback() {
        @Override
        public void onReceivedData(byte[] new_data) {
            for (byte b : new_data) {
                try {
                    data.put(((int) b) & 0xff);
                } catch (InterruptedException e) {
                    // ignore, possibly losing bytes when buffer is full
                }
            }
        }
    };

    public SerialPortStreamUart(String portKey, int baud) {
        UsbManager tempManager = ArduinoHexFileUploader.usbManager;
        UsbDevice tempDevice = ArduinoHexFileUploader.usbDevice;
        this.baudRate = baud;
        this.portName = portKey;
        connection = tempManager.openDevice(tempDevice);
        serialPort = UsbSerialDevice.createUsbSerialDevice(tempDevice, connection);

        serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
        serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
        serialPort.setParity(UsbSerialInterface.PARITY_NONE);
    }

    @Override
    public String getPortName() {
        return this.portName;
    }

    @Override
    public String[] getPortNames() {
        List<String> portNames = new ArrayList<>();
        UsbDevice tempDevice = ArduinoHexFileUploader.usbDevice;
        int deviceVID = tempDevice.getVendorId();
        int devicePID = tempDevice.getProductId();
        String deviceKey = tempDevice.getDeviceName();
        if (deviceVID != 0x1d6b && (devicePID != 0x0001 && devicePID != 0x0002 && devicePID != 0x0003)) {
            // There is a device connected to our Android device. Try to open it as a Serial Port.
            portNames.add(deviceKey);
        }
        return portNames.toArray(new String[portNames.size()]);
    }

    @Override
    public void setBaudRate​(int newBaudRate) {
        synchronized (this) {
            serialPort.setBaudRate(newBaudRate);
        }
    }

    @Override
    public void setComPortTimeouts​(int newReadTimeout, int newWriteTimeout) {

    }

    @Override
    public void setReadTimeout(int miliseconds) {
        synchronized (this) {
            readTimeout = miliseconds;
        }
    }

    @Override
    public void setWriteTimeout(int miliseconds) {
        synchronized (this) {
            writeTimeout = miliseconds;
        }
    }

    @Override
    public void open() {
        synchronized (this) {
            serialPort.open();
            serialPort.setBaudRate(this.baudRate);
            serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
            is_open = true;
            data.clear();
            serialPort.read(mReadCallback);
        }
    }

    @Override
    public void close() {
        synchronized (this) {
            serialPort.close();
            is_open = false;
        }
    }

    @Override
    public void setDtrEnable(boolean enable) {
        synchronized (this) {
            serialPort.setDTR(enable);
        }
    }

    @Override
    public void setRtsEnable(boolean enable) {
        synchronized (this) {
            serialPort.setRTS(enable);
        }
    }

    @Override
    public void setNumDataBits​(int newDataBits) {
        synchronized (this) {
            serialPort.setDataBits(newDataBits);
        }
    }

    @Override
    public void setNumStopBits​(int newStopBits) {
        synchronized (this) {
            serialPort.setStopBits(newStopBits);
        }
    }

    @Override
    public void setParity​(int newParity) {
        synchronized (this) {
            serialPort.setParity(newParity);
        }
    }

    @Override
    public int readBytes​(byte[] buffer, int bytesToRead) {
        synchronized (this) {
            int index = 0;
            int count = 0;
            while (is_open && index < bytesToRead) {
                try {
                    int readByte = data.poll(readTimeout, TimeUnit.MILLISECONDS);
                    if (readByte != -1) {
                        buffer[index] = (byte) readByte;
                        count++;
                        index++;
                    } else return -1;
                } catch (InterruptedException e) {
                    // ignore, will be retried by while loop
                } catch (NullPointerException e) {
                    return -1;
                }
            }
            return count;
        }
    }

    @Override
    public int readBytes​(byte[] buffer, int bytesToRead, int offset) {
        synchronized (this) {
            int index = offset;
            int numRead = 0;
            while (is_open && index < bytesToRead) {
                try {
                    int readByte = data.poll(readTimeout, TimeUnit.MILLISECONDS);
                    if (readByte != -1) {

                        buffer[index] = (byte) readByte;
                        index++;
                        numRead++;
                    } else return -1;
                } catch (InterruptedException e) {
                    // ignore, will be retried by while loop
                } catch (NullPointerException e) {
                    return -1;
                }
            }
            return numRead;
        }
    }

    @Override
    public int writeBytes​(byte[] buffer, int bytesToWrite) {
        synchronized (this) {
            byte[] mWriteBuffer = new byte[bytesToWrite];
            System.arraycopy(buffer, 0, mWriteBuffer, 0, bytesToWrite);
            serialPort.write(mWriteBuffer);
            return bytesToWrite;
        }
    }

    @Override
    public int writeBytes​(byte[] buffer, int bytesToWrite, int offset) {
        synchronized (this) {
            byte[] mWriteBuffer = new byte[bytesToWrite];
            System.arraycopy(buffer, offset, mWriteBuffer, 0, bytesToWrite);
            serialPort.write(mWriteBuffer);
            return bytesToWrite;
        }
    }

    @Override
    public void DiscardInBuffer() {
        data.clear();
    }
}
