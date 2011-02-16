package messageparser.test.junit.string;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RMISecurityManager;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import messageparser.common.message.Advertisement;
import messageparser.common.message.CompositeSubscription;
import messageparser.common.message.Publication;
import messageparser.common.message.Subscription;
import messageparser.common.message.parser.UniversalMessageParser;
import messageparser.common.message.parser.string.StringMessageParser;
import messageparser.common.message.parser.string.ParseException;
import messageparser.common.message.parser.string.TokenMgrError;

/**
 * Tests for StringMessageParser
 * 
 * @author Arwid Bancewicz
 */
public class TestStringParser {

	UniversalMessageParser parser;

	@Before
	public void setUp1() throws Exception {
		parser = new StringMessageParser();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test1() throws Exception {
		// Publication
		parser.parsePublication("p [class,\"temp\"],[city,'tor'],[value,10];");
		parser
				.parsePublication("publish [class,\"temp\"],[city,'tor'],[value,10];");
		parser.parsePublication("p [class,\"temp\"];");
		parser
				.parsePublication("publish [class,\"temp\"],[city,'tor'],[value,-10];");
		// Publication p =
		// parser.parsePublication("p [class,\"temp\"],[value,10e1];");

		UniversalMessageUtil mutil = UniversalMessageUtil.getInstance();
		String pubString = "p [class,\"temp\"],[value,10e1];";
		Publication pub = (Publication) mutil.parse(pubString,
				MessageType.STRING);
		System.out.println(mutil.getString(pub, MessageType.STRING));
		System.out.println(mutil.getString(pub, MessageType.JSON));

	}

	@Test
	public void test2() throws Exception {
		parser
				.parseAdvertisement("a [class,eq,\"temp\"],[city,eq,'tor'],[value,>,10];");
		parser
				.parseAdvertisement("advertise [class,eq,\"temp\"],[city,eq,'tor'],[value,>,10];");
		parser.parseAdvertisement("a [class,eq,\"temp\"],[value,<,1.23e2];");
		parser
				.parseAdvertisement("a [class,eq,\"temp\"],[value,isPresent,10];");
		parser
				.parseAdvertisement("a [class,eq,\"temp\"],[city,isPresent,'tor'];");
		parser.parseAdvertisement("a [class,isPresent,\"\"];");
	}

	@Test
	public void test3() throws Exception {
		// Subscription
		parser
				.parseSubscription("s [class,eq,\"temp\"],[city,eq,'tor'],[value,>,10];");
		parser
				.parseSubscription("subscribe [class,eq,\"temp\"],[city,eq,'tor'],[value,>,10];");
		parser.parseSubscription("s [class,eq,\"temp\"],[value,<,1.23e2];");
		parser.parseSubscription("s [class,eq,\"temp\"],[value,isPresent,10];");
		parser
				.parseSubscription("s [class,eq,\"temp\"],[city,isPresent,'tor'];");
	}

	@Test
	public void test4() throws Exception {
		// Composite Subscription
		// cs
		// {{[class,eq,\"temp\"],[value,>,0]}&{[class,eq,\"temp\"],[value,<,10]}}}
		CompositeSubscription cs = parser
				.parseCompositeSubscription("cs {{[class,eq,\"temp\"],[value,>,0]}&{[class,eq,\"temp\"],[value,<,10]}}}");
		UniversalMessageUtil m = UniversalMessageUtil.getInstance();
		System.out.println(m.getString(cs, MessageType.STRING));
		System.out.println(m.getString(cs, MessageType.JSON));
	}

	@Test(expected = ParseException.class)
	public void testX1() throws Exception {
		// No value
		parser.parsePublication("p [class];");
	}

	@Test(expected = ParseException.class)
	public void testX2() throws Exception {
		// Empty quoted value
		parser.parsePublication("p [class,\"\"];");
	}

	@Test(expected = ParseException.class)
	public void testX3() throws Exception {
		// Empty value
		parser.parsePublication("p [class,];");
	}

	@Test(expected = ParseException.class)
	public void testX4() throws Exception {
		// Empty attribute name
		parser.parsePublication("p [,\"temp\"];");
	}

	@Test(expected = ParseException.class)
	public void testX5() throws Exception {
		// No predicates
		parser.parsePublication("p;");
	}

	/**
	 * Help method that is meant to compare two Maps
	 * 
	 * @param m1
	 *            m2
	 * @return true if each of their predicate pairs are equal false if not
	 */
	@SuppressWarnings("unchecked")
	public boolean equals(Map m1, Map m2) {
		if (m1.size() != m2.size()) {
			return false;
		} else {
			for (Iterator i = (m1.keySet()).iterator(); i.hasNext();) {
				String attribute = (String) i.next();
				if (!m2.containsKey(attribute)) {
					return false;
				} else {
					if (m1.get(attribute).getClass().equals(Predicate.class)) {
						// deal with predicateMap for adv and sub
						Predicate p1 = (Predicate) m1.get(attribute);
						Predicate p2 = (Predicate) m2.get(attribute);
						if (!equals(p1, p2)) {
							return false;
						}
					} else {
						// deal with pairMap for pub
						Object v1 = (Object) m1.get(attribute);
						Object v2 = (Object) m2.get(attribute);
						if (!v1.equals(v2)) {
							return false;
						}
					}
				}
			}
			return true;
		}
	}

	/**
	 * Help method that is meant to compare two Predicates
	 * 
	 * @param p1
	 *            p2
	 * @return true if their op and value are equal seperately false if not
	 */
	public boolean equals(Predicate p1, Predicate p2) {
		if (p1.getOp().equals(p2.getOp())
				&& (p1.getValue().equals(p2.getValue()))) {
			return true;
		} else {
			return false;
		}
	}

}
