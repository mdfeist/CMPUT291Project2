import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;

public class CreateDataFiles {

	public static void createTerms(Collection<Ad> ads) {
		try {
			FileOutputStream fos = new FileOutputStream("data/terms.txt");
			OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
			
			for (Ad ad : ads) {
				String title = ad.getTitle();
				String body = ad.getBody();
				
				title = title.replaceAll("&quot;", " ");
				title = title.replaceAll("&apos;", " ");
				
				title = title.replaceAll("[^A-Za-z0-9 ]", " ");
				title = title.toLowerCase();
				
				body = body.replaceAll("&quot;", " ");
				body = body.replaceAll("&apos;", " ");
				
				body = body.replaceAll("[^A-Za-z0-9 ]", " ");
				body = body.toLowerCase();
				
				String[] titles = title.split(" ");
				String[] bodies = body.split(" ");
				
				for (String t : titles) {
					if (t.length() > 2) {
						out.write("t-" + t + ":" + ad.getId() + "\0\n");
					}
				}
				
				for (String b : bodies) {
					if (b.length() > 2) {
						out.write("b-" + b + ":" + ad.getId() + "\0\n");
					}
				}
			}
			out.close();
		} catch (IOException e) {
			System.out.print("Exception");
		}
	}
	
	public static void createDates(Collection<Ad> ads) {
		try {
			FileOutputStream fos = new FileOutputStream("data/pdates.txt");
			OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
			
			for (Ad ad : ads) {
				out.write(ad.getDate() + ":" + ad.getId() + "\0\n");
			}
			out.close();
		} catch (IOException e) {
			System.out.print("Exception");
		}
	}
	
	public static void createPrices(Collection<Ad> ads) {
		try {
			FileOutputStream fos = new FileOutputStream("data/prices.txt");
			OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
			
			for (Ad ad : ads) {
				int price = ad.getPrice();
				
				if (price >= 0) {
					out.write(price + ":" + ad.getId() + "\0\n");
				}
			}
			out.close();
		} catch (IOException e) {
			System.out.print("Exception");
		}
	}
	
	public static void createAds(Collection<Ad> ads) {
		try {
			FileOutputStream fos = new FileOutputStream("data/ads.txt");
			OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
			
			for (Ad ad : ads) {
				int id = ad.getId();
				String title = ad.getTitle();
				String body = ad.getBody();
				int price = ad.getPrice();
				String date = ad.getDate();
				
				out.write(id + ":<ad>" +
							"<id>" + id + "</id>" +
							"<title>" + title + "</title>" +
							"<body>" + body + "</body>");
				
				out.write("<price>");
				if (price >= 0) {
					out.write(price);
				}
				out.write("</price>");
	
				out.write("<pdate>" + date + "</pdate>" +
							"</ad>\0\n");
			}
			out.close();
		} catch (IOException e) {
			System.out.print("Exception");
		}
	}
}
