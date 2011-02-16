package messageparser.test.junit.json;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import messageparser.common.message.Advertisement;
import messageparser.common.message.CompositeSubscription;
import messageparser.common.message.Publication;
import messageparser.common.message.Subscription;
import messageparser.common.message.parser.UniversalMessageParser;
import messageparser.common.message.parser.json.JSONMessageParser;
import messageparser.common.message.parser.json.ParseException;
import messageparser.common.message.parser.json.TokenMgrError;

/**
 * Tests for JSONMessageParser
 * 
 * @author Arwid Bancewicz
 */
public class TestJSONParser {

	UniversalMessageParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new JSONMessageParser();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test1() throws Exception {
		// Publication p =
		// parser.parsePublication("{\"p\":[[\"class\",\"temp\"],[\"city\",\"tor\"],[\"value\",10]]}");
		// assertEquals(p.getClassVal(),"temp");
		parser
				.parsePublication("{\"p\":[[\"class\",\"temp\"],[\"city\",\"tor\"],[\"value\",10]]}");
		parser
				.parsePublication("{\"publish\":[[\"class\",\"temp\"],[\"city\",\"tor\"],[\"value\",10]]}");
		parser.parsePublication("{\"p\":[[\"class\",\"temp\"]]}");
		parser
				.parsePublication("{\"p\":[[\"class\",\"temp\"],[\"value\",10e1]]}");
		parser
				.parsePublication("{\"p\":[[\"class\",\"temp\"],[\"value\",-1]]}");

		Publication p = parser
				.parsePublication("{\"p\":[[\"class\",\"temp\"],[\"city\",\"tor\"],[\"value\",10]]}");
		System.out.println(parser.print(p));
		// assertEquals(p.getClassVal(),"d");
	}

	@Test
	public void test2() throws Exception {
		parser
				.parseAdvertisement("{\"a\":[[\"class\",\"eq\",\"temp\"],[\"city\",\"eq\",\"tor\"],[\"value\",\">\",0]]}");
		parser
				.parseAdvertisement("{\"advertise\":[[\"class\",\"eq\",\"temp\"],[\"city\",\"eq\",\"tor\"],[\"value\",\">\",0]]}");
		parser
				.parseAdvertisement("{\"a\":[[\"class\",\"eq\",\"temp\"],[\"value\",\"isPresent\",30]]}");
		parser
				.parseAdvertisement("{\"a\":[[\"class\",\"eq\",\"temp\"],[\"value\",\"<\",1.23e2]]}");
		Advertisement adv = parser
				.parseAdvertisement("{\"a\":[[\"class\",\"eq\",\"temp\"],[\"city\",\"isPresent\",\"tor\"]]}");
		System.out.println(parser.print(adv));
	}

	@Test
	public void test3() throws Exception {
		parser
				.parseSubscription("{\"s\":[[\"class\",\"eq\",\"temp\"],[\"city\",\"eq\",\"tor\"],[\"value\",\">\",0]]}");
		parser
				.parseSubscription("{\"subscribe\":[[\"class\",\"eq\",\"temp\"],[\"city\",\"eq\",\"tor\"],[\"value\",\">\",0]]}");
		parser
				.parseSubscription("{\"s\":[[\"class\",\"eq\",\"temp\"],[\"value\",\"isPresent\",30]]}");
		parser
				.parseSubscription("{\"s\":[[\"class\",\"eq\",\"temp\"],[\"value\",\"<\",1.23e2]]}");
		Subscription sub = parser
				.parseSubscription("{\"s\":[[\"class\",\"eq\",\"temp\"],[\"city\",\"isPresent\",\"tor\"]]}");
		System.out.println(parser.print(sub));
	}

	@Test
	public void test4() throws Exception {
		CompositeSubscription cs = parser
				.parseCompositeSubscription("{\"cs\":[[[\"class\",\"eq\",\"temp\"],[\"value\",\">\",0]],[\"&\"],[[\"class\",\"eq\",\"temp\"],[\"value\",\"<\",10]]]}");
		System.out.println(cs);
		System.out.println(parser.print(cs));
	}

	@Test(expected = ParseException.class)
	public void testX1() throws Exception {
		// No value
		parser.parsePublication("{\"p\":[[\"class\"]]}");
	}

	@Test(expected = ParseException.class)
	public void testX2() throws Exception {
		// Empty quoted value
		parser.parsePublication("{\"p\":[[\"class\",\"\"]]}");
	}

	@Test(expected = ParseException.class)
	public void testX3() throws Exception {
		// Empty value
		parser.parsePublication("{\"p\":[[\"class\",]]}");
	}

	@Test(expected = ParseException.class)
	public void testX4() throws Exception {
		// Empty attribute name
		parser.parsePublication("{\"p\":[[\"\",\"temp\"]]}");
	}

	@Test(expected = ParseException.class)
	public void testX5() throws Exception {
		// No predicates
		parser.parsePublication("{\"p\":[]}");
	}

	@Test(expected = TokenMgrError.class)
	public void testX6() throws Exception {
		// No quotes around attribute name
		parser.parsePublication("{\"p\":[[class,\"temp\"]]}");
	}

	@Test(expected = TokenMgrError.class)
	public void testX7() throws Exception {
		// No quotes around string value
		parser.parsePublication("{\"p\":[[\"class\",temp]]}");
	}
}
