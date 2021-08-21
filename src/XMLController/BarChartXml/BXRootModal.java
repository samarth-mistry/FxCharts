package XMLController.BarChartXml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Cover")
public class BXRootModal {
	private List<BXSeriesModal> SeriesModal;

	public List<BXSeriesModal> getLines() {
		return SeriesModal;
	}
	public void setLines(List<BXSeriesModal> lines) {
		this.SeriesModal = lines;
	}
}
