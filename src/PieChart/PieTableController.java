package PieChart;

public class PieTableController {
	private String s;
    private Double d;

    public PieTableController(String s, Double d) {
        this.s = s;
        this.d = d;        
    }
    public String getS() {
        return s;
    }
    public Double getD() {
        return d;
    }
    public void setS(String s) {
    	System.out.println("Setting S");
    	this.s = s;
    }
    public void setD(String d) {
    	System.out.println("Setting D");
    	this.d = Double.parseDouble(d);
    }
}
