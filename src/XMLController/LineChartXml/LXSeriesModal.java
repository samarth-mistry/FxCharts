package XMLController.LineChartXml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Series")
public class LXSeriesModal {
	private String SeriesName;
    private List<LXNodeModal> SeriPoints;

    public LXSeriesModal() {}
    public LXSeriesModal(Double s, List<LXNodeModal> d) {
        this.SeriPoints = d;
        //this.X = d;        
    }
    public String getSeriName() {
        return SeriesName;
    }
    public List<LXNodeModal> getListPoints() {
        return SeriPoints;
    }
    public void setSeriName(String s) {
    	this.SeriesName = s;
    }
    public void setListPoints(List<LXNodeModal> d) {
    	this.SeriPoints = d;
    }
}
