package com.support.oauth2postservice.util.wrapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class RereadableRequestWrapper extends HttpServletRequestWrapper {

  private final byte[] rawData;
  private final Charset encoding;

  public RereadableRequestWrapper(HttpServletRequest request) {
    super(request);

    String characterEncoding = request.getCharacterEncoding();
    if (StringUtils.isBlank(characterEncoding))
      characterEncoding = StandardCharsets.UTF_8.name();

    this.encoding = Charset.forName(characterEncoding);

    // Convert InputStream data to byte array and store it to this wrapper instance.
    try (InputStream inputStream = request.getInputStream()) {
      this.rawData = IOUtils.toByteArray(inputStream);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public ServletInputStream getInputStream() {
    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rawData);
    return new ServletInputStream() {
      @Override
      public boolean isFinished() {
        return false;
      }

      @Override
      public boolean isReady() {
        return false;
      }

      @Override
      public void setReadListener(ReadListener listener) {

      }

      public int read() {
        return byteArrayInputStream.read();
      }
    };
  }

  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(new InputStreamReader(this.getInputStream(), this.encoding));
  }

  @Override
  public ServletRequest getRequest() {
    return super.getRequest();
  }
}
