//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package IntelHexFormatReader.Utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LineReader implements Iterable<String>, Closeable {
    private BufferedReader reader;

    public LineReader(String file) throws FileNotFoundException {
        this((Reader)(new FileReader(file)));
    }

    public LineReader(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    public LineReader() {
        this((Reader)(new StringReader("")));
    }

    public void close() throws IOException {
        this.reader.close();
    }

    protected void finalize() throws Throwable {
        this.close();
    }

    public Iterator<String> iterator() {
        return new LineReader.LineIterator((LineReader.LineIterator)null);
    }

    public Collection<String> readLines() {
        Collection<String> lines = new ArrayList();
        Iterator var3 = this.iterator();

        while(var3.hasNext()) {
            String line = (String)var3.next();
            lines.add(line);
        }

        return lines;
    }

    private class LineIterator implements Iterator<String> {
        private String nextLine;

        private LineIterator(LineIterator lineIterator) {
        }

        public String bufferNext() {
            try {
                return this.nextLine = LineReader.this.reader.readLine();
            } catch (IOException var2) {
                throw new IllegalStateException("I/O error while reading stream.", var2);
            }
        }

        public boolean hasNext() {
            boolean hasNext = this.nextLine != null || this.bufferNext() != null;
            if (!hasNext) {
                try {
                    LineReader.this.reader.close();
                } catch (IOException var3) {
                    throw new IllegalStateException("I/O error when closing stream.", var3);
                }
            }

            return hasNext;
        }

        public String next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                String result = this.nextLine;
                this.nextLine = null;
                return result;
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
