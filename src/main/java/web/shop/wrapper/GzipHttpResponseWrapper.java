package web.shop.wrapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Objects;

public class GzipHttpResponseWrapper extends HttpServletResponseWrapper implements AutoCloseable {

    private GZipServletOutputStream gzipOutputStream;
    private PrintWriter printWriter;

    public GzipHttpResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void close() throws IOException {
        if (Objects.nonNull(this.printWriter)) {
            this.printWriter.close();
        }
        if (Objects.nonNull(gzipOutputStream)) {
            this.gzipOutputStream.close();
        }
    }


    @Override
    public void flushBuffer() throws IOException {
        if (Objects.nonNull(this.printWriter)) {
            this.printWriter.close();
        }
        if (Objects.nonNull(gzipOutputStream)) {
            this.gzipOutputStream.close();
        }
        super.flushBuffer();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (Objects.nonNull(printWriter)) {
            throw new IllegalStateException("PrintWriter obtained already - cannot get OutputStream");
        }
        if (Objects.isNull(gzipOutputStream)) {
            this.gzipOutputStream = new GZipServletOutputStream(getResponse().getOutputStream());
        }
        return this.gzipOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (Objects.isNull(printWriter) && Objects.nonNull(gzipOutputStream)) {
            throw new IllegalStateException("OutputStream obtained already - cannot get PrintWriter");
        }
        if (Objects.isNull(printWriter)) {
            this.gzipOutputStream = new GZipServletOutputStream(getResponse().getOutputStream());
            this.printWriter = new PrintWriter(new OutputStreamWriter(this.gzipOutputStream, getResponse().getCharacterEncoding()));
        }
        return this.printWriter;
    }


    @Override
    public void setContentLength(int len) {
    }
}

