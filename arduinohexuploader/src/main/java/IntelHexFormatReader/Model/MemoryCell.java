//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package IntelHexFormatReader.Model;

public class MemoryCell {
    private int Address;
    private boolean Modified;
    private byte Value;

    public final int getAddress() {
        return this.Address;
    }

    private void setAddress(int value) {
        this.Address = value;
    }

    public final boolean getModified() {
        return this.Modified;
    }

    public final void setModified(boolean value) {
        this.Modified = value;
    }

    public final byte getValue() {
        return this.Value;
    }

    public final void setValue(byte value) {
        this.Value = value;
    }

    public MemoryCell(int address) {
        this.setAddress(address);
    }

    public String toString() {
        return String.format("MemoryCell : %1$s Value: %2$s (modified = %3$s)", String.format("%08X", this.getAddress()), String.format("%02X", this.getValue()), this.getModified());
    }
}
