package messageparser.test.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ca.utoronto.msrg.padres.test.junit.message.string.TestStringParser;
import ca.utoronto.msrg.padres.test.junit.message.json.TestJSONParser;
import ca.utoronto.msrg.padres.test.junit.message.xml.TestXMLParser;

/**
 * Run all Message Parser tests
 * 
 * @author Arwid Bancewicz
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( { TestStringParser.class, TestJSONParser.class,
		TestXMLParser.class })
public class AllTests {
}
