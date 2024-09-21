package eu.happycoders.shop.bootstrap;


/**
 * Launcher for the application: starts the Undertow server and deploys the shop application.
 *
 * @author Sven Woltmann
 */
public class Launcher {

  private static final int PORT = 8080;

  private UndertowJaxrsServer server;

  public static void main(String[] args) {
    new Launcher().startOnPort(PORT);
  }

  public void startOnPort(int port) {
    server = new UndertowJaxrsServer();
    startServer();
  }

  private void startServer() {
    server.start();
    server.deploy(RestEasyUndertowShopApplication.class);
  }

  public void stop() {
    server.stop();
  }
}
