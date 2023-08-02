package UsbSerialHelper;

import android.content.Context;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import CSharpStyle.StringHelper;

public class SerialStreamHelper {
	public static final <E extends ISerialPortStream> E newInstance(E responseType, String portName, int baudRate) {
		E tempVar = null;
		if (StringHelper.isNullOrEmpty(portName)) {
			return null;
		}
		try {
			Class clazz = responseType.getClass();
			tempVar = (E) clazz.getConstructor(String.class, int.class).newInstance(portName, baudRate);

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			return tempVar;
		}

	}

	public static final <E extends ISerialPortStream> E newInstance(Class<E> clazz, String portName, int baudRate) {
		E tempVar = null;
		if (StringHelper.isNullOrEmpty(portName)) {
			return null;
		}
		try {
			tempVar = (E) clazz.getConstructor(String.class, int.class).newInstance(portName, baudRate);


		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			return tempVar;
		}

	}

//https://stackoverflow.com/questions/3437897/how-to-get-a-class-instance-of-generics-type-t/5684761#5684761

}
