package XMLController.BarChartXml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Series")
public class BXSeriesModal {
	private String SeriesName;
    private List<BXNodeModal> SeriPoints;

    public BXSeriesModal() {}
    public BXSeriesModal(String s, List<BXNodeModal> d) {
        this.SeriPoints = d;
        this.SeriesName = s;
    }
    public String getSeriName() {
        return SeriesName;
    }
    public List<BXNodeModal> getListPoints() {
        return SeriPoints;
    }
    public void setSeriName(String s) {
    	this.SeriesName = s;
    }
    public void setListPoints(List<BXNodeModal> d) {
    	this.SeriPoints = d;
    }
}
