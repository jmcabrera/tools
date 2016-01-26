/**
 * 
 */
package tools.tcpprint;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public class TcpPrinter {

  static final int DEFAULT_PORT = 12345;

  public static void main(String[] args) throws IOException {
    int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;

    try (ServerSocket seso = new ServerSocket(port)) {
      while (true) {
        try {
          Socket so = seso.accept();
          try (Reader r = new InputStreamReader(so.getInputStream())) {
            char c;
            while (-1 != (c = (char) r.read())) {
              System.out.print(c);
            }
          }
          System.out.println("<end of connection>");
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

}
