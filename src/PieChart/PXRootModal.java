package PieChart;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Cover")
public class PXRootModal {
	private List<PXNodeModal> PieNodes;

	public List<PXNodeModal> getPies() {
		return PieNodes;
	}
	public void setPies(List<PXNodeModal> pies) {
		this.PieNodes = pies;
	}
}
