//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package IntelHexFormatReader.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FileLineIterator implements Iterator<String> {
    private FileReader reader;
    private BufferedReader in = null;
    private String string = null;

    public FileLineIterator(File file) throws IOException {
        try {
            this.reader = new FileReader(file);
            this.in = new BufferedReader(this.reader);
            this.string = this.in.readLine();
            if (this.string == null) {
                this.in.close();
                this.in = null;
            }

        } catch (IOException var5) {
            this.string = null;
            if (this.in != null) {
                try {
                    this.in.close();
                } catch (IOException var4) {
                }
            }

            this.in = null;
            throw var5;
        }
    }

    public boolean hasNext() {
        return this.string != null;
    }

    public String next() throws NoSuchElementException {
        String returnString = this.string;

        try {
            if (this.string == null) {
                throw new NoSuchElementException("Next line is not available");
            } else {
                this.string = this.in.readLine();
                if (this.string == null && this.in != null) {
                    this.in.close();
                    this.in = null;
                }

                return returnString;
            }
        } catch (Exception var3) {
            throw new NoSuchElementException("Exception caught in FileLineIterator.next() " + var3);
        }
    }

    public void remove() {
        throw new UnsupportedOperationException("FileLineIterator.remove() is not supported");
    }

    protected void finalize() throws Throwable {
        try {
            this.string = null;
            if (this.in != null) {
                try {
                    this.in.close();
                } catch (Exception var5) {
                }
            }

            this.in = null;
        } finally {
            super.finalize();
        }

    }
}
