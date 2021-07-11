package BarChart;

public class BarTableController {
	private String series;
    private String seriesX;
    private String seriesY;

    public BarTableController(String series, String seriesX, String seriesY) {
        this.series = series;
        this.seriesX = seriesX;
        this.seriesY = seriesY;
    }

    public String getSeries() {
        return series;
    }
    public String getSeriesX() {
        return seriesX;
    }
    public String getSeriesY() {
        return seriesY;
    }
}
