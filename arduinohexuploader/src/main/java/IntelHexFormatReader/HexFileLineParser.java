package IntelHexFormatReader;

import IntelHexFormatReader.Model.IntelHexRecord;
import IntelHexFormatReader.Model.RecordType;
import CSharpStyle.StringHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class HexFileLineParser {
    private static final String COLON = ":";

    public HexFileLineParser() {
    }

    public static IntelHexRecord ParseLine(String line) throws IOException {
        if (line == null) {
            throw new IOException("Line to parse can not be null");
        } else if (line.length() < 11) {
            throw new IOException(String.format("Line '%1$s' is too short!", line));
        } else if (!line.startsWith(":")) {
            throw new IOException(String.format("Illegal line start character ('%1$s'!", line));
        } else {
            int byteCount = TryParseByteCount(line.substring(1, 3));
            int requiredRecordLength = 9 + 2 * byteCount + 2;
            if (line.length() != requiredRecordLength) {
                throw new IOException(String.format("Line '%1$s' does not have required record length of %2$s!", line, requiredRecordLength));
            } else {
                int address = TryParseAddress(line.substring(3, 7));
                int recTypeVal = TryParseRecordType(line.substring(7, 9));
                if (!RecordType.IsDefined(recTypeVal)) {
                    throw new IOException(String.format("Invalid record type value: '%1$s'!", recTypeVal));
                } else {
                    RecordType recType = RecordType.forValue(recTypeVal);
                    byte[] bytes = TryParseBytes(line.substring(9, 9 + 2 * byteCount));
                    int checkSum = TryParseCheckSum(StringHelper.substring(line, 9 + 2 * byteCount, 2));
                    if (!VerifyChecksum(line, byteCount, checkSum)) {
                        throw new IOException(String.format("Checksum for line %1$s is incorrect!", line));
                    } else {
                        IntelHexRecord tempVar = new IntelHexRecord();
                        tempVar.setByteCount(byteCount);
                        tempVar.setAddress(address);
                        tempVar.setRecordType(recType);
                        tempVar.setBytes(bytes);
                        tempVar.setCheckSum(checkSum);
                        return tempVar;
                    }
                }
            }
        }
    }

    private static int TryParseByteCount(String hexByteCount) throws IOException {
        try {
            return Integer.parseInt(hexByteCount, 16);
        } catch (RuntimeException var2) {
            throw new IOException(String.format("Unable to extract byte count for '%1$s'.", hexByteCount), var2);
        }
    }

    private static int TryParseAddress(String hexAddress) throws IOException {
        try {
            return Integer.parseInt(hexAddress, 16);
        } catch (RuntimeException var2) {
            throw new IOException(String.format("Unable to extract address for '%1$s'.", hexAddress), var2);
        }
    }

    private static int TryParseRecordType(String hexRecType) throws IOException {
        try {
            return Integer.parseInt(hexRecType, 16);
        } catch (RuntimeException var2) {
            throw new IOException(String.format("Unable to extract record type for '%1$s'.", hexRecType), var2);
        }
    }

    private static byte[] TryParseBytes(String hexData) throws IOException {
        try {
            byte[] bytes = new byte[hexData.length() / 2];
            int counter = 0;

            String hexByte;
            for(Iterator var4 = Split(hexData, 2).iterator(); var4.hasNext(); bytes[counter++] = (byte)Integer.parseInt(hexByte, 16)) {
                hexByte = (String)var4.next();
            }

            return bytes;
        } catch (RuntimeException var5) {
            throw new IOException(String.format("Unable to extract bytes for '%1$s'.", hexData), var5);
        }
    }

    private static int TryParseCheckSum(String hexCheckSum) throws IOException {
        try {
            return Integer.parseInt(hexCheckSum, 16);
        } catch (RuntimeException var2) {
            throw new IOException(String.format("Unable to extract checksum for '%1$s'.", hexCheckSum), var2);
        }
    }

    private static boolean VerifyChecksum(String line, int byteCount, int checkSum) {
        byte[] allbytes = new byte[5 + byteCount];
        int counter = 0;

        String hexByte;
        for(Iterator var6 = Split(line.substring(1, 1 + (4 + byteCount) * 2), 2).iterator(); var6.hasNext(); allbytes[counter++] = (byte)Integer.parseInt(hexByte, 16)) {
            hexByte = (String)var6.next();
        }

        int maskedSumBytes = 0;

        int checkSumCalculated;
        for(checkSumCalculated = 0; checkSumCalculated < allbytes.length; ++checkSumCalculated) {
            maskedSumBytes += (short)allbytes[checkSumCalculated];
        }

        maskedSumBytes &= 255;
        checkSumCalculated = (byte)(256 - maskedSumBytes) & 255;
        return checkSumCalculated == checkSum;
    }

    private static Iterable<String> Split(String str, int chunkSize) {
        List<String> strings = new ArrayList();

        for(int i = 0; i < str.length() / chunkSize; ++i) {
            strings.add(str.substring(i * chunkSize, i * chunkSize + chunkSize));
        }

        return strings;
    }

    private static Iterable<String> splitEqually(String str, int chunkSize) {
        List<String> strings = new ArrayList();

        for(int index = 0; index < str.length(); index += chunkSize) {
            strings.add(str.substring(index, Math.min(index + chunkSize, str.length())));
        }

        return strings;
    }
}
