package messageparser.common.message.parser;

import java.util.HashMap;
import java.util.Map;

import messageparser.common.message.parser.string.StringMessageParser;
import messageparser.common.message.parser.xml.XMLMessageParser;
import messageparser.common.message.parser.json.JSONMessageParser;

/**
 * Singleton Utility for Message Parsers
 * 
 * @author Arwid Bancewicz
 */
public class UniversalMessageUtil {
	public static enum MessageType {
		STRING, JSON, XML
	};

	private Map<MessageType, UniversalMessageParser> parsers;
	private static UniversalMessageUtil instance;

	private UniversalMessageUtil() {
		parsers = new HashMap<MessageType, UniversalMessageParser>();
		parsers.put(MessageType.STRING, new StringMessageParser());
		parsers.put(MessageType.JSON, new JSONMessageParser());
		parsers.put(MessageType.XML, new XMLMessageParser());
	}

	public Object parse(String stringRep) throws Exception {
		for (MessageType type : parsers.keySet()) {
			try {
				Object obj = parse(stringRep, type);
				return obj;
			} catch (Exception e) {
			}
		}
		throw new Exception("Could not parse.");
	}

	public Object parse(String stringRep, MessageType type) throws Exception {
		if (type == null)
			return parse(stringRep);
		if (!parsers.containsKey(type))
			return null;
		UniversalMessageParser parser = parsers.get(type);
		Object obj = parser.parse(stringRep);
		return obj;
	}

	public String getString(Object obj, MessageType type) {
		if (!parsers.containsKey(type))
			return null;
		UniversalMessageParser parser = parsers.get(type);
		String ret = parser.print(obj);
		return ret;
	}

	public static synchronized UniversalMessageUtil getInstance() {
		if (instance == null)
			instance = new UniversalMessageUtil();
		return instance;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
