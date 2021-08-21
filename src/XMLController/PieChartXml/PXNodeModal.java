package XMLController.PieChartXml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PieNode")
public class PXNodeModal {
	private String Name;
    private Double Data;

    public PXNodeModal() {}
    public PXNodeModal(String s, Double d) {
        this.Name = s;
        this.Data = d;        
    }
    public String getPieName() {
        return Name;
    }
    public Double getPieValue() {
        return Data;
    }
    public void setPieName(String s) {
    	this.Name = s;
    }
    public void setPieValue(Double d) {
    	this.Data = d;
    }
}
