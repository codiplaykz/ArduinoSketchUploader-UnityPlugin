//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package IntelHexFormatReader.Model;

public enum RecordType {
    Data,
    EndOfFile,
    ExtendedSegmentAddress,
    StartSegmentAddress,
    ExtendedLinearAddress,
    StartLinearAddress;

    public static final int SIZE = 32;

    private RecordType() {
    }

    public int getValue() {
        return this.ordinal();
    }

    public static RecordType forValue(int value) {
        return values()[value];
    }

    public static boolean IsDefined(int legIndex) {
        RecordType[] var4;
        int var3 = (var4 = values()).length;

        for(int var2 = 0; var2 < var3; ++var2) {
            RecordType l = var4[var2];
            if (l.getValue() == legIndex) {
                return true;
            }
        }

        return false;
    }
}
