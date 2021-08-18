package PieChart;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PieXmlConvertor {
	private int id;
	private String s;
    private Double d;

    public PieXmlConvertor(String s, Double d) {
        this.s = s;
        this.d = d;        
    }
    @XmlAttribute
    public String getS() {
        return s;
    }
    @XmlElement
    public Double getD() {
        return d;
    }
    public void setS(String s) {
    	this.s = s;
    }
    public void setD(String d) {
    	this.d = Double.parseDouble(d);
    }
}
