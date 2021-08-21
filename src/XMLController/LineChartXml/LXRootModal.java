package XMLController.LineChartXml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Cover")
public class LXRootModal {
	private List<LXSeriesModal> SeriesModal;

	public List<LXSeriesModal> getLines() {
		return SeriesModal;
	}
	public void setLines(List<LXSeriesModal> lines) {
		this.SeriesModal = lines;
	}
}
