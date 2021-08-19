package PieChart;
public class PieXmlConvertor {
	private String pieName;
    private Double pieData;

    public PieXmlConvertor() {}
    public PieXmlConvertor(String s, Double d) {
        this.pieName = s;
        this.pieData = d;        
    }
    public String getPieName() {
        return pieName;
    }
    public Double getPieValue() {
        return pieData;
    }
    public void setPieName(String s) {
    	this.pieName = s;
    }
    public void setPieValue(Double d) {
    	this.pieData = d;
    }
}
