package XMLController.LineChartXml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Point")
public class LXNodeModal {
	private Double X;
    private Double Y;

    public LXNodeModal() {}
    public LXNodeModal(Double s, Double d) {
        this.X = s;
        this.X = d;        
    }
    public Double getCordX() {
        return X;
    }
    public Double getCordY() {
        return Y;
    }
    public void setCordX(Double s) {
    	this.X = s;
    }
    public void setCordY(Double d) {
    	this.Y = d;
    }
}
