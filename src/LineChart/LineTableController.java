package LineChart;

public class LineTableController {
	private String series;
    private Double seriesX;
    private Double seriesY;

    public LineTableController(String series, Double seriesX, Double seriesY) {
        this.series = series;
        this.seriesX = seriesX;
        this.seriesY = seriesY;
    }

    public String getSeries() {
        return series;
    }
    public Double getSeriesX() {
        return seriesX;
    }
    public Double getSeriesY() {
        return seriesY;
    }
}
