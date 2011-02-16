package messageparser.test.junit.xml;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import messageparser.common.message.parser.UniversalMessageParser;
import messageparser.common.message.parser.xml.ParseException;
import messageparser.common.message.parser.xml.TokenMgrError;
import messageparser.common.message.parser.xml.XMLMessageParser;

/**
 * Tests for XMLMessageParser
 * 
 * @author Arwid Bancewicz
 */
public class TestXMLParser {

	UniversalMessageParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new XMLMessageParser();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test1() throws Exception {
		parser.parsePublication("<publish>" + "<predicates>"
				+ "<predicate name=\"class\" value=\"temp\"/>"
				+ "<predicate name=\"city\" value=\"tor\"/> "
				+ "<predicate name=\"value\" value=\"10\"/>  "
				+ "</predicates>" + "</publish>");
		parser.parsePublication("<publish>" + "<predicates>"
				+ "<predicate name=\"class\" value=\"temp\"/>"
				+ "<predicate name=\"value\" value=\"10e1\"/>"
				+ "</predicates>" + "</publish>");
	}

	@Test
	public void test2() throws Exception {
		parser.parseAdvertisement("<advertise>" + "<predicates>"
				+ "<predicate name=\"class\" op=\"eq\" value=\"temp\"/>"
				+ "<predicate name=\"city\" op=\"eq\" value=\"tor\"/>"
				+ "<predicate name=\"value\" op=\">\" value=\"0\"/>"
				+ "</predicates>" + "</advertise>");
	}

	@Test
	public void test3() throws Exception {
		parser.parseSubscription("<subscription>" + "<predicates>"
				+ "<predicate name=\"class\" op=\"eq\" value=\"temp\"/>"
				+ "<predicate name=\"city\" op=\"eq\" value=\"tor\"/>"
				+ "<predicate name=\"value\" op=\">\" value=\"0\"/>"
				+ "</predicates>" + "</subscription>");
	}

	@Test
	public void test4() throws Exception {

		parser.parseCompositeSubscription("<csubscription>" + "<predicates>"
				+ "<predicate name=\"class\" op=\"eq\" value=\"temp\"/>"
				+ "<predicate name=\"city\" op=\"eq\" value=\"tor\"/>"
				+ "<predicate name=\"value\" op=\">\" value=\"0\"/>"
				+ "</predicates>" + "<csubscription op=\"&\">" + "<predicates>"
				+ "<predicate name=\"class\" op=\"eq\" value=\"temp\"/>"
				+ "<predicate name=\"city\" op=\"eq\" value=\"tor\"/>"
				+ "<predicate name=\"value\" op=\"<\" value=\"10\"/>"
				+ "</predicates>" + "</csubscription>" + "</csubscription>");
	}

	@Test(expected = ParseException.class)
	public void testX1() throws Exception {
		// No value
		parser.parsePublication("<publish>" + "<predicates>"
				+ "<predicate name=\"class\"/>" + "</predicates>"
				+ "</publish>");
	}

	@Test(expected = ParseException.class)
	public void testX2() throws Exception {
		// Empty quoted value
		parser.parsePublication("<publish>" + "<predicates>"
				+ "<predicate name=\"class\" value=\"\"/>" + "</predicates>"
				+ "</publish>");
	}

	@Test(expected = ParseException.class)
	public void testX3() throws Exception {
		// Empty value
		parser.parsePublication("<publish>" + "<predicates>"
				+ "<predicate name=\"class\" value=/>" + "</predicates>"
				+ "</publish>");
	}

	@Test(expected = ParseException.class)
	public void testX4() throws Exception {
		// Empty attribute name
		parser.parsePublication("<publish>" + "<predicates>"
				+ "<predicate name=\"\" value=\"temp\"/>" + "</predicates>"
				+ "</publish>");
	}

	@Test(expected = ParseException.class)
	public void testX5() throws Exception {
		// No predicates
		parser.parsePublication("<publish>" + "<predicates>" + "</predicates>"
				+ "</publish>");
	}

	@Test(expected = TokenMgrError.class)
	public void testX6() throws Exception {
		// No quotes around attribute name
		parser.parsePublication("<publish>" + "<predicates>"
				+ "<predicate name=class value=\"temp\"/>" + "</predicates>"
				+ "</publish>");
	}

	@Test(expected = TokenMgrError.class)
	public void testX7() throws Exception {
		// No quotes around string value
		parser.parsePublication("<publish>" + "<predicates>"
				+ "<predicate name=\"class\" value=temp/>" + "</predicates>"
				+ "</publish>");
	}
}