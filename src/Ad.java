
public class Ad {
	private int id;
	private String title;
	private String body;
	private int price;
	private String pdate;
	
	public Ad() {}
	
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
}
