//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package IntelHexFormatReader.Model;

public class IntelHexRecord {
    private RecordType RecordType;
    private int ByteCount;
    private int Address;
    private byte[] Bytes;
    private int CheckSum;

    public IntelHexRecord() {
    }

    public final RecordType getRecordType() {
        return this.RecordType;
    }

    public final void setRecordType(RecordType value) {
        this.RecordType = value;
    }

    public final int getByteCount() {
        return this.ByteCount;
    }

    public final void setByteCount(int value) {
        this.ByteCount = value;
    }

    public final int getAddress() {
        return this.Address;
    }

    public final void setAddress(int value) {
        this.Address = value;
    }

    public final byte[] getBytes() {
        return this.Bytes;
    }

    public final void setBytes(byte[] value) {
        this.Bytes = value;
    }

    public final int getCheckSum() {
        return this.CheckSum;
    }

    public final void setCheckSum(int value) {
        this.CheckSum = value;
    }
}
