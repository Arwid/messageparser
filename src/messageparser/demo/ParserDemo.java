package messageparser.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import messageparser.common.message.parser.string.StringMessageParser;

import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import common.message.Publication;
import common.message.parser.UniversalMessageParser;
import common.message.parser.UniversalMessageUtil;
import common.message.parser.UniversalMessageUtil.MessageType;

/**
 * Demo of message parsers
 * 
 * @author Arwid Bancewicz
 */
public class ParserDemo extends JFrame implements ActionListener {
	private static final long serialVersionUID = 5464841717626477390L;

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				new ParserDemo().setVisible(true);
			}
		});
	}

	private JTextArea area1, area2, area3;
	private JTextArea inputArea;
	private JComboBox inputCombo;

	private MessageType inputMessageType;
	private UniversalMessageUtil mutil;

	private JButton parseInputBtn, clearInputBtn;

	public ParserDemo() {
		super("ParserDemo");
		mutil = UniversalMessageUtil.getInstance();
		initComponents();
		setSize(1000, 800);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		if ("parse".equals(e.getActionCommand())) {
			String inputString = inputArea.getText();
			try {
				Object obj = mutil.parse(inputString, inputMessageType);

				String stringText = mutil.getString(obj, MessageType.STRING);
				String jsonText = mutil.getString(obj, MessageType.JSON);
				String xmlText = mutil.getString(obj, MessageType.XML);
				area1.setText(stringText);
				area2.setText(jsonText);
				area3.setText(xmlText);

				try {
					// Format the JSON string
					JSONObject jsonObj = new JSONObject(jsonText);
					jsonText = jsonObj.toString(4);
					area2.setText(jsonText);
				} catch (Exception e2) {
				}

				try {
					// Format the XML string
					XMLReader reader = XMLReaderFactory.createXMLReader();
					XMLFormater formater = new XMLFormater();
					reader.setContentHandler(formater);
					reader.parse(new InputSource(new StringReader(xmlText)));
					xmlText = formater.getOutputString();
					area3.setText(xmlText);
				} catch (Exception e2) {
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				area1.setText(e1.getMessage());
				area2.setText(e1.getMessage());
				area3.setText(e1.getMessage());
			}
			System.out.println(inputString);
		} else if ("clear".equals(e.getActionCommand())) {
			// inputArea.setText("");
			area1.setText("");
			area2.setText("");
			area3.setText("");
		} else if ("inputType".equals(e.getActionCommand())) {
			switch (inputCombo.getSelectedIndex()) {
			case 0: /* auto */
				inputMessageType = null;
				break;
			case 1: /* string */
				inputMessageType = MessageType.STRING;
				break;
			case 2: /* JSON */
				inputMessageType = MessageType.JSON;
				break;
			case 3: /* XML */
				inputMessageType = MessageType.XML;
				break;
			}
		}
	}

	private void initComponents() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		JLabel actionLabel = new JLabel("Choose input format :");
		actionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		String[] inputTypes = { "auto", "String", "JSON", "XML" };
		inputCombo = new JComboBox(inputTypes);
		inputCombo.addActionListener(this);
		inputCombo.setActionCommand("inputType");

		JPanel inputPane = new JPanel();
		inputPane.setLayout(new BoxLayout(inputPane, BoxLayout.Y_AXIS));

		String inputAreaString = "publish [class,'temp'],[value,0];";
		inputArea = new JTextArea(inputAreaString);
		inputArea.setFont(new Font("Serif", Font.ITALIC, 16));
		inputArea.setLineWrap(true);
		inputArea.setWrapStyleWord(true);
		JScrollPane inputAreaScroll = new JScrollPane(inputArea);
		inputAreaScroll
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		inputAreaScroll.setPreferredSize(new Dimension(250, 150));
		inputAreaScroll.setMinimumSize(new Dimension(100, 150));

		JLabel inputAreaLabel = new JLabel("text:");
		inputAreaLabel.setLabelFor(inputAreaScroll);

		parseInputBtn = new JButton("parse");
		clearInputBtn = new JButton("clear");
		JPanel buttonPane = new JPanel();
		buttonPane.add(actionLabel);
		buttonPane.add(inputCombo);
		buttonPane.add(parseInputBtn);
		buttonPane.add(clearInputBtn);

		parseInputBtn.setActionCommand("parse");
		parseInputBtn.addActionListener(this);
		clearInputBtn.setActionCommand("clear");
		clearInputBtn.addActionListener(this);

		inputPane.add(inputAreaScroll);
		inputPane.add(buttonPane);

		inputPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder("Input"), BorderFactory.createEmptyBorder(
				5, 5, 5, 5)));

		JPanel content = new JPanel();
		area1 = new JTextArea();
		area2 = new JTextArea();
		area3 = new JTextArea();

		area1.setEditable(false);
		area2.setEditable(false);
		area3.setEditable(false);

		area1.setFont(new Font("Serif", Font.ITALIC, 16));
		area1.setLineWrap(true);
		area1.setWrapStyleWord(true);

		area2.setFont(new Font("Serif", Font.ITALIC, 16));
		area2.setLineWrap(true);
		area2.setWrapStyleWord(true);

		area3.setFont(new Font("Serif", Font.ITALIC, 16));
		area3.setLineWrap(true);
		area3.setWrapStyleWord(true);

		JScrollPane scroll1 = new JScrollPane(area1);
		JScrollPane scroll2 = new JScrollPane(area2);
		JScrollPane scroll3 = new JScrollPane(area3);

		scroll1.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createCompoundBorder(BorderFactory.createTitledBorder(
						BorderFactory.createEmptyBorder(), "String"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)), scroll1
				.getBorder()));
		scroll2.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createCompoundBorder(BorderFactory.createTitledBorder(
						BorderFactory.createEmptyBorder(), "JSON"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)), scroll2
				.getBorder()));
		scroll3.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createCompoundBorder(BorderFactory.createTitledBorder(
						BorderFactory.createEmptyBorder(), "XML"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)), scroll3
				.getBorder()));

		content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
		content.add(scroll1);
		content.add(scroll2);
		content.add(scroll3);
		content.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder("Output"), BorderFactory.createEmptyBorder(
				5, 5, 5, 5)));

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(inputPane, BorderLayout.PAGE_START);
		mainPanel.add(content, BorderLayout.CENTER);

		add(mainPanel, BorderLayout.CENTER);
	}
}

class XMLFormater extends DefaultHandler {
	private int depth = -1;

	private final String indent = "    ";
	private String outputString = "";

	@Override
	public void characters(char ch[], int start, int length) {
		outputString += "\n";
		outputString += getIndent(depth + 1);
		for (int i = start; i < start + length; i++) {
			outputString += ch[i];
		}
	}

	@Override
	public void endElement(String uri, String name, String qName) {
		if (!"predicate".equals(qName)) {
			outputString += "\n";
			outputString += getIndent(depth);
			outputString += "</" + qName + ">";
		} else {
			outputString += "/>";
		}
		depth--;
	}

	private String getIndent(int amount) {
		String ret = "";
		while (amount-- > 0) {
			ret += indent;
		}
		return ret;
	}

	public String getOutputString() {
		return outputString;
	}

	@Override
	public void startDocument() {
		// System.out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
		outputString = "";
	}

	@Override
	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		depth++;
		if (depth > 0)
			outputString += "\n";
		outputString += getIndent(depth);

		outputString += "<" + qName;
		for (int i = 0; i < atts.getLength(); i++) {
			outputString += " " + atts.getQName(i) + "=\"" + atts.getValue(i)
					+ "\"";
		}
		if (!"predicate".equals(qName)) {
			outputString += ">";
		}
	}
}
