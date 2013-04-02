/**
 * Holds information about an ad.
 * 
 * @author Michael Feist and George Coomber
 *
 */

public class Ad {
	private int id;
	private String title;
	private String body;
	private int price;
	private String pdate;
	
	public Ad() 
	{
		this.id = -1;
		this.title = "";
		this.body = "";
		this.price = -1;
		this.pdate = "";
	}
	
	public Ad(int id,
			String title,
			String body,
			int price,
			String pdate)
	{
		this.id = id;
		this.title = title;
		this.body = body;
		this.price = price;
		this.pdate = pdate;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getDate() {
		return pdate;
	}

	public void setDate(String pdate) {
		this.pdate = pdate;
	}
	
	public void print() {
		String price = String.format("%d", this.price);
		
		if (this.price < 0) {
			price = "null";
		}
		
		String title = this.title;
		if (this.title.length() > 20)
			title = this.title.substring(0, 17) + "...";
		
		String body = this.body;
		if (this.body.length() > 22)
			body = this.body.substring(0, 22) + "...";
		
		String out =  String.format("%7d | %20s | %25s | %8s | %s", 
									this.id, 
									title,
									body,
									price,
									this.pdate);
		
		System.out.println(out);
	}
	
	public String toString() {
		String price = String.format("%d", this.price);

		if (this.price < 0) {
			price = "null";
		}

		return String.format("%d\t%s\t%s\t%s\t%s\n", this.id,
				this.title.replaceAll("\t", " "), this.body.replaceAll("\t", " "), price, this.pdate);
	}
}
