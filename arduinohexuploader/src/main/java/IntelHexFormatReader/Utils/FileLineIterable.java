//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package IntelHexFormatReader.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class FileLineIterable implements Iterable<String> {
    private File file;

    public static void test(String[] args) {
        try {
            Iterator var2 = (new FileLineIterable(args[0])).iterator();

            while(var2.hasNext()) {
                String s = (String)var2.next();
                System.out.println(s);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public FileLineIterable(String fileName) throws IOException {
        this(new File(fileName));
    }

    public FileLineIterable(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("File does not exist: " + file.getPath());
        } else if (!file.isFile()) {
            throw new IOException("File is not of type 'file': " + file.getPath());
        } else {
            this.file = file;
        }
    }

    public FileLineIterator iterator() {
        try {
            return new FileLineIterator(this.file);
        } catch (IOException var2) {
            var2.printStackTrace();
            return null;
        }
    }
}
