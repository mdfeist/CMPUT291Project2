import java.util.Collection;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser {
	private HashMap<Integer, Ad> adArray;
	
	XMLParser(String filename)
	{
		adArray = new HashMap<Integer, Ad>(1000);

		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				boolean ads = false;
				boolean id = false;
				boolean title = false;
				boolean body = false;
				boolean price = false;
				boolean pdate = false;

				Ad adObject = null;

				public void startElement(String uri, String localName,
						String qName, Attributes attributes)
						throws SAXException {

					if (qName.equalsIgnoreCase("ads")) {
						ads = true;
					} else if (qName.equalsIgnoreCase("ad")) {
						adObject = new Ad();
					} else if (qName.equalsIgnoreCase("id")) {
						id = true;
					} else if (qName.equalsIgnoreCase("title")) {
						title = true;
					} else if (qName.equalsIgnoreCase("body")) {
						body = true;
					} else if (qName.equalsIgnoreCase("price")) {
						price = true;
					} else if (qName.equalsIgnoreCase("pdate")) {
						pdate = true;
					}

				}

				public void endElement(String uri, String localName,
						String qName) throws SAXException {
					
					if (qName.equalsIgnoreCase("ad")) {
						adArray.put(new Integer(adObject.getId()), adObject);
						adObject = null;
					}
				}

				public void characters(char ch[], int start, int length)
						throws SAXException {

					if (ads) {
						ads = false;
					}
					
					if (id) {
						int i_id = Integer.parseInt(new String(ch, start, length));
						adObject.setId(i_id);
						id = false;
					}
					
					if (title) {
						adObject.setTitle(new String(ch, start, length));
						title = false;
					}
					
					if (body) {
						adObject.setBody(new String(ch, start, length));
						body = false;
					}
					
					if (price) {
						
						String s_price = new String(ch, start, length);
						try {
							Integer i_price = new Integer(s_price);
							adObject.setPrice(i_price.intValue());
						} catch (Exception e) {
							adObject.setPrice(0);
						}
						
						price = false;
					}
					
					if (pdate) {
						adObject.setDate(new String(ch, start, length));
						pdate = false;
					}

				}

			};

			saxParser.parse(filename, handler);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Ad getAdWithId(int id)
	{
		Ad ad = null;
		
		try {
			Integer i_id = new Integer(id);
			ad = adArray.get(i_id);
		} catch (Exception e) {
			System.out.println("Error: invalid id");
		}
		
		return ad;
	}
	
	public Collection<Ad> getAds()
	{
		return adArray.values();
	}
	
	public void printFile()
	{
		for (Ad ad : adArray.values())
		{
			System.out.println("Id: " + ad.getId());
			System.out.println("Title: " + ad.getTitle());
			System.out.println("Body: " + ad.getBody());
			System.out.println("Price: " + ad.getPrice());
			System.out.println("Date: " + ad.getDate());
			System.out.println();
		}
	}
}
