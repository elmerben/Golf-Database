package kerho.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author elias
 * @version 27.4.2023
 * Testien ajamista.
 */
@Suite
@SelectClasses({ JasenTest.class, KierrosTest.class })
public class AllTests {
  // Ajetaan kaikki testit
}
