package IntelHexFormatReader;

import CSharpStyle.Func2;
import IntelHexFormatReader.Model.IntelHexRecord;
import IntelHexFormatReader.Model.MemoryCell;
import CSharpStyle.Func2;
import java.io.IOException;

public final class Extensions {
    public Extensions() {
    }

    public static void Assert(IntelHexRecord record, Func2<IntelHexRecord, Boolean> predicate, String message) throws IOException {
        if (!(Boolean)predicate.invoke(record)) {
            throw new IOException(String.format("%1$s -- record %2$s!", message, record));
        }
    }

    public static int LastIndexOf(MemoryCell[] source, Func2<MemoryCell, Boolean> predicate) {
        MemoryCell[] reversedTemp = new MemoryCell[source.length];

        int index;
        for(index = 0; index < source.length; ++index) {
            reversedTemp[index] = source[source.length - index - 1];
        }

        index = reversedTemp.length - 1;
        MemoryCell[] var7 = reversedTemp;
        int var6 = reversedTemp.length;

        for(int var5 = 0; var5 < var6; ++var5) {
            MemoryCell item = var7[var5];
            if ((Boolean)predicate.invoke(item)) {
                return index;
            }

            --index;
        }

        return -1;
    }
}
