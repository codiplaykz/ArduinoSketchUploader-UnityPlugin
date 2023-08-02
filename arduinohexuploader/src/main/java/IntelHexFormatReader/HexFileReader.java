//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package IntelHexFormatReader;

import IntelHexFormatReader.Model.IntelHexRecord;
import IntelHexFormatReader.Model.MemoryBlock;
import IntelHexFormatReader.Model.MemoryCell;
import IntelHexFormatReader.Utils.FileLineIterable;
import CSharpStyle.Func2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class HexFileReader {
    private Iterable<String> hexRecordLines;
    private int memorySize;

    public HexFileReader(String fileName, int memorySize) {
        try {
            Iterable<String> lines = new FileLineIterable(fileName);
            this.Initialize(lines, memorySize);
        } catch (FileNotFoundException var4) {
            throw new IllegalArgumentException(String.format("File %1$s does not exist!", fileName));
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    public HexFileReader(Iterable<String> hexFileContents, int memorySize) {
        this.Initialize(hexFileContents, memorySize);
    }

    private void Initialize(Iterable<String> lines, int memSize) {
        if (!lines.iterator().hasNext()) {
            throw new IllegalArgumentException("Hex file contents can not be empty!");
        } else if (memSize <= 0) {
            throw new IllegalArgumentException("Memory size must be greater than zero!");
        } else {
            this.hexRecordLines = lines;
            this.memorySize = memSize;
        }
    }

    public final MemoryBlock Parse() throws IOException {
        return ReadHexFile(this.hexRecordLines, this.memorySize);
    }

    private static MemoryBlock ReadHexFile(Iterable<String> hexRecordLines, int memorySize) throws IOException {
        MemoryBlock result = new MemoryBlock(memorySize);
        int baseAddress = 0;
        boolean encounteredEndOfFile = false;
        Iterator var6 = hexRecordLines.iterator();

        while(true) {
            label37:
            while(var6.hasNext()) {
                String hexRecordLine = (String)var6.next();
                IntelHexRecord hexRecord = HexFileLineParser.ParseLine(hexRecordLine);
                switch(hexRecord.getRecordType().ordinal()) {
                    case 0:
                        int nextAddress = hexRecord.getAddress() + baseAddress;
                        int i = 0;

                        while(true) {
                            if (i >= hexRecord.getByteCount()) {
                                continue label37;
                            }

                            if (nextAddress + i > memorySize) {
                                throw new IOException(String.format("Trying to write to position %1$s outside of memory boundaries (%2$s)!", nextAddress + i, memorySize));
                            }

                            MemoryCell cell = result.getCells()[nextAddress + i];
                            cell.setValue(hexRecord.getBytes()[i]);
                            cell.setModified(true);
                            ++i;
                        }
                    case 1:
                        Extensions.Assert(hexRecord, new Func2<IntelHexRecord, Boolean>() {
                            @Override
                            public Boolean invoke(IntelHexRecord rec) {
                                return rec.getAddress() == 0;
                            }
                        }, "Address should equal zero in EOF.");
                        Extensions.Assert(hexRecord, new Func2<IntelHexRecord, Boolean>() {
                            @Override
                            public Boolean invoke(IntelHexRecord rec) {
                                return rec.getByteCount() == 0;
                            }
                        }, "Byte count should be zero in EOF.");
                        Extensions.Assert(hexRecord, new Func2<IntelHexRecord, Boolean>() {
                            @Override
                            public Boolean invoke(IntelHexRecord rec) {
                                return rec.getBytes().length == 0;
                            }
                        }, "Number of bytes should be zero for EOF.");
                        Extensions.Assert(hexRecord, new Func2<IntelHexRecord, Boolean>() {
                            @Override
                            public Boolean invoke(IntelHexRecord rec) {
                                return rec.getCheckSum() == 255;
                            }
                        }, "Checksum should be 0xff for EOF.");
                        encounteredEndOfFile = true;
                        break;
                    case 2:
                        Extensions.Assert(hexRecord, new Func2<IntelHexRecord, Boolean>() {
                            @Override
                            public Boolean invoke(IntelHexRecord rec) {
                                return rec.getByteCount() == 2;
                            }
                        }, "Byte count should be 2.");
                        baseAddress = (hexRecord.getBytes()[0] << 8 | hexRecord.getBytes()[1]) << 4;
                        break;
                    case 3:
                        Extensions.Assert(hexRecord, new Func2<IntelHexRecord, Boolean>() {
                            @Override
                            public Boolean invoke(IntelHexRecord rec) {
                                return rec.getByteCount() == 4;
                            }
                        }, "Byte count should be 4.");
                        Extensions.Assert(hexRecord, new Func2<IntelHexRecord, Boolean>() {
                            @Override
                            public Boolean invoke(IntelHexRecord rec) {
                                return rec.getAddress() == 0;
                            }
                        }, "Address should be zero.");
                        result.setCS((short)(hexRecord.getBytes()[0] << 8 + hexRecord.getBytes()[1]));
                        result.setIP((short)(hexRecord.getBytes()[2] << 8 + hexRecord.getBytes()[3]));
                        break;
                    case 4:
                        Extensions.Assert(hexRecord, new Func2<IntelHexRecord, Boolean>() {
                            @Override
                            public Boolean invoke(IntelHexRecord rec) {
                                return rec.getByteCount() == 2;
                            }
                        }, "Byte count should be 2.");
                        baseAddress = (hexRecord.getBytes()[0] << 8 | hexRecord.getBytes()[1]) << 16;
                        break;
                    case 5:
                        Extensions.Assert(hexRecord, new Func2<IntelHexRecord, Boolean>() {
                            @Override
                            public Boolean invoke(IntelHexRecord rec) {
                                return rec.getByteCount() == 4;
                            }
                        }, "Byte count should be 4.");
                        Extensions.Assert(hexRecord, new Func2<IntelHexRecord, Boolean>() {
                            @Override
                            public Boolean invoke(IntelHexRecord rec) {
                                return rec.getAddress() == 0;
                            }
                        }, "Address should be zero.");
                        result.setEIP((hexRecord.getBytes()[0] << 24) + (hexRecord.getBytes()[1] << 16) + (hexRecord.getBytes()[2] << 8) + hexRecord.getBytes()[3]);
                }
            }

            if (!encounteredEndOfFile) {
                throw new IOException("No EndOfFile marker found!");
            }

            return result;
        }
    }
}
