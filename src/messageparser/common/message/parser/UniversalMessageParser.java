package messageparser.common.message.parser;

import messageparser.common.message.Advertisement;
import messageparser.common.message.CompositeSubscription;
import messageparser.common.message.Publication;
import messageparser.common.message.Subscription;

/**
 * Abstract Message Parser
 * 
 * @author Arwid Bancewicz
 */
public abstract class UniversalMessageParser {
	public UniversalMessageParser() {
	}

	public Object parse(String stringRep) throws Exception {
		try {
			Publication pub = parsePublication(stringRep);
			return pub;
		} catch (Error pe) {
		} catch (Exception e) {
		}
		try {
			Advertisement adv = parseAdvertisement(stringRep);
			return adv;
		} catch (Error pe) {
		} catch (Exception e) {
		}
		try {
			Subscription sub = parseSubscription(stringRep);
			return sub;
		} catch (Error pe) {
		} catch (Exception e) {
		}
		try {
			CompositeSubscription cs = parseCompositeSubscription(stringRep);
			return cs;
		} catch (Error pe) {
		} catch (Exception e) {
		}
		throw new Exception("Unrecognized string.");
	}

	public String print(Object obj) {
		return "Unimplemented";
	}

	public abstract Publication parsePublication(String stringRep)
			throws Exception;

	public abstract Advertisement parseAdvertisement(String stringRep)
			throws Exception;

	public abstract Subscription parseSubscription(String stringRep)
			throws Exception;

	public abstract CompositeSubscription parseCompositeSubscription(
			String stringRep) throws Exception;

	/** unescape a C string */
	protected static String unescape(String s) {
		StringBuilder sb = new StringBuilder(s.length());
		for (int i = 1; i < s.length() - 1; ++i) {
			if (s.charAt(i) == '\\') {
				if (i + 1 < s.length() - 1) {
					++i;
					switch (s.charAt(i)) {
					case '\n':
						sb.append('\n');
						break;
					case '\r':
						sb.append('\r');
						break;
					case '\\':
						sb.append('\\');
						break;
					// case 'b' : sb.append('\b');
					// break;
					// case 't' : sb.append('\t');
					// break;
					// case 'f' : sb.append('\f');
					// break;
					case '\'':
						sb.append('\'');
						break;
					case '\"':
						sb.append('\"');
						break;
					default:
						sb.append(s.charAt(i));
					}
				}
			} else {
				sb.append(s.charAt(i));
			}
		}
		return sb.toString();
	}
}