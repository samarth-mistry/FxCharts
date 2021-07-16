package BarChart;

public class BarTableController {
	private String series;
    private String seriesX;
    private Number seriesY;

    public BarTableController(String series, String seriesX, Number value2) {
        this.series = series;
        this.seriesX = seriesX;
        this.seriesY = value2;
    }

    public String getSeries() {    	
		return series;
    }
    public void setSeries(String series) {    	
    	this.series = series;
    }
    public String getSeriesX() {
        return seriesX;
    }
    public Number getSeriesY() {
        return seriesY;
    }
}
