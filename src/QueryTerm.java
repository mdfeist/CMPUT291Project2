public class QueryTerm {

	public static enum TermField {TITLE, BODY, BOTH};
	
	private String term;
	private TermField field;
	private boolean wildCard;
	
	public QueryTerm() {
		this.term = "";
		this.field = TermField.BOTH;
		this.wildCard = false;
	}
	
	public QueryTerm(String term) {
		if (term.contains("t-")) {
			term = term.replace("t-", "");
			this.field = TermField.TITLE;
		} else if (term.contains("b-")) {
			term = term.replace("b-", "");
			this.field = TermField.BODY;
		} else {
			this.field = TermField.BOTH;
		}
		
		if (term.contains("%")) {
			this.term = term.replace("%", "");
			this.wildCard = true;
		} else {
			this.term = term;
			this.wildCard = false;
		}
	}
	
	public String getTerm() {
		return this.term;
	}
	
	public TermField getField() {
		return this.field;
	}
	
	public boolean hasWildCard() {
		return this.wildCard;
	}
}
