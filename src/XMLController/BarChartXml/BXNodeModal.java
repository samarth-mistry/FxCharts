package XMLController.BarChartXml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("BarNode")
public class BXNodeModal {
	private String Name;
    private Number Value;

    public BXNodeModal() {}
    public BXNodeModal(String s, Number d) {
        this.Name = s;
        this.Value = d;        
    }
    public String getCordX() {
        return Name;
    }
    public Number getCordY() {
        return Value;
    }
    public void setCordX(String s) {
    	this.Name = s;
    }
    public void setCordY(Number d) {
    	this.Value = d;
    }
}
