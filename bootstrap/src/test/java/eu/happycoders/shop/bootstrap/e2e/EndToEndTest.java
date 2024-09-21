package eu.happycoders.shop.bootstrap.e2e;

import static eu.happycoders.shop.adapter.in.rest.Http

import eu.happycoders.shop.bootstrap.Launcher;
import org.junit.jupiter.api.BeforeAll;

abstract class EndToEndTest {

  private static Launcher launcher;

  @BeforeAll
  static void init() {
    launcher = new Launcher();
    launcher.startOnServer()
  }
}