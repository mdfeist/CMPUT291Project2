import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser {
	private FileOutputStream fos_terms;
	private OutputStreamWriter out_terms;

	private FileOutputStream fos_dates;
	private OutputStreamWriter out_dates;

	private FileOutputStream fos_prices;
	private OutputStreamWriter out_prices;

	private FileOutputStream fos_ads;
	private OutputStreamWriter out_ads;
	
	private String filename;

	XMLParser() {
		this.filename = "";
	}
	
	XMLParser(String filename) {
		this.filename = filename;
	}

	public void parse() {
		parse(this.filename);
	}
	
	public void parse(String filename) {
		if (filename.equals("")) {
			System.out.println("No File");
			return;
		}
		// Open Streams
		try {
			fos_terms = new FileOutputStream("data/terms.txt");
			out_terms = new OutputStreamWriter(fos_terms, "UTF-8");

			fos_dates = new FileOutputStream("data/pdates.txt");
			out_dates = new OutputStreamWriter(fos_dates, "UTF-8");

			fos_prices = new FileOutputStream("data/prices.txt");
			out_prices = new OutputStreamWriter(fos_prices, "UTF-8");

			fos_ads = new FileOutputStream("data/ads.txt");
			out_ads = new OutputStreamWriter(fos_ads, "UTF-8");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

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

						// Create Terms
						try {
							String title = adObject.getTitle();
							String body = adObject.getBody();

							title = title.replaceAll("&quot;", " ");
							title = title.replaceAll("&apos;", " ");

							title = title.replaceAll("[^A-Za-z0-9_ ]", " ");
							title = title.toLowerCase();

							body = body.replaceAll("&quot;", " ");
							body = body.replaceAll("&apos;", " ");

							body = body.replaceAll("[^A-Za-z0-9_ ]", " ");
							body = body.toLowerCase();

							String[] titles = title.split(" ");
							String[] bodies = body.split(" ");

							for (String t : titles) {
								if (t.length() > 2) {
									out_terms.write("t-" + t + ":"
											+ adObject.getId() + "\n");
								}
							}

							for (String b : bodies) {
								if (b.length() > 2) {
									out_terms.write("b-" + b + ":"
											+ adObject.getId() + "\n");
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}

						// Create Dates
						try {
							out_dates.write(adObject.getDate() + ":"
									+ adObject.getId() + "\n");
						} catch (IOException e) {
							e.printStackTrace();
						}

						// Create Prices
						try {
							int price = adObject.getPrice();

							if (price >= 0) {
								String pr = String.format("%6d", price);
								
								out_prices.write(pr
										+ ":" + adObject.getId() + "\n");
							}
						} catch (IOException e) {
							e.printStackTrace();
						}

						// Create ads
						try {
							int id = adObject.getId();
							String title = adObject.getTitle();
							String body = adObject.getBody();
							int price = adObject.getPrice();
							String date = adObject.getDate();

							out_ads.write(id + ":<ad>" + "<id>" + id + "</id>"
									+ "<title>" + title + "</title>" + "<body>"
									+ body + "</body>");

							out_ads.write("<price>");
							if (price >= 0) {
								out_ads.write(price);
							}
							out_ads.write("</price>");

							out_ads.write("<pdate>" + date + "</pdate>"
									+ "</ad>\n");
						} catch (IOException e) {
							e.printStackTrace();
						}

						adObject = null;
					}

					if (qName.equalsIgnoreCase("body")) {
						body = false;
					}

					if (qName.equalsIgnoreCase("title")) {
						title = false;
					}
				}

				public void characters(char ch[], int start, int length)
						throws SAXException {

					if (ads) {
						ads = false;
					}

					if (id) {
						int i_id = Integer.parseInt(new String(ch, start,
								length));
						adObject.setId(i_id);
						id = false;
					}

					if (title) {
						String s_title = adObject.getTitle();
						s_title += new String(ch, start, length);
						adObject.setTitle(s_title);
					}

					if (body) {
						String s_body = adObject.getBody();
						s_body += new String(ch, start, length);

						adObject.setBody(s_body);
					}

					if (price) {

						String s_price = new String(ch, start, length);
						try {
							Integer i_price = new Integer(s_price);
							adObject.setPrice(i_price.intValue());
						} catch (Exception e) {
							adObject.setPrice(-1);
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

			out_terms.close();
			out_dates.close();
			out_prices.close();
			out_ads.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
