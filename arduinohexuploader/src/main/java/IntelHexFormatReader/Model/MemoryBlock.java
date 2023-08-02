package IntelHexFormatReader.Model;

import IntelHexFormatReader.Extensions;
import CSharpStyle.Func2;

public class MemoryBlock {
    private short CS;
    private short IP;
    private int EIP;
    private MemoryCell[] Cells;

    public final short getCS() {
        return this.CS;
    }

    public final void setCS(short value) {
        this.CS = value;
    }

    public final short getIP() {
        return this.IP;
    }

    public final void setIP(short value) {
        this.IP = value;
    }

    public final int getEIP() {
        return this.EIP;
    }

    public final void setEIP(int value) {
        this.EIP = value;
    }

    public final int getHighestModifiedOffset() {
        return Extensions.LastIndexOf(this.getCells(), new Func2<MemoryCell, Boolean>() {
            @Override
            public Boolean invoke(MemoryCell cell) {
                return cell.getModified();
            }
        });
    }

    public final int getMemorySize() {
        return this.getCells().length;
    }

    public final MemoryCell[] getCells() {
        return this.Cells;
    }

    public final void setCells(MemoryCell[] value) {
        this.Cells = value;
    }

    public MemoryBlock(int memorySize) {
        this(memorySize, (byte)-1);
    }

    public MemoryBlock(int memorySize, byte fillValue) {
        this.setCells(new MemoryCell[memorySize]);

        for(int i = 0; i < memorySize; ++i) {
            MemoryCell tempVar = new MemoryCell(i);
            tempVar.setValue(fillValue);
            this.getCells()[i] = tempVar;
        }

    }
}
