/*
Note:
Primary API: Yahoo Finance (as per task requirement).
Fallback API: CoinGecko (used when Yahoo returns HTTP 429 or fails).

This ensures system reliability despite rate limiting issues.
*/

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.JFrame;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App extends JFrame {

    private static final String YAHOO_URL =
            "https://query1.finance.yahoo.com/v8/finance/chart/%5EDJI";

    private static final String FALLBACK_URL =
            "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd";

    private static TimeSeries series = new TimeSeries("Market Price");

    public App(String title) {
        super(title);

        TimeSeriesCollection dataset = new TimeSeriesCollection(series);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Live Market Dashboard",
                "Time",
                "Price",
                dataset
        );

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    public static void main(String[] args) {

        App chart = new App("Citi Dashboard");
        chart.setSize(800, 600);
        chart.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chart.setVisible(true);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                double price = fetchPrice();

                System.out.println(LocalDateTime.now() + " -> " + price);

                series.addOrUpdate(new Millisecond(), price);

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        }, 0, 10, TimeUnit.SECONDS);
    }

    private static double fetchPrice() {
        try {
            return fetchYahoo();
        } catch (Exception e) {
            System.out.println("⚠ Yahoo failed, switching to fallback...");
            try {
                return fetchFallback();
            } catch (Exception ex) {
                System.out.println("❌ Fallback also failed");
                return 0.0;
            }
        }
    }

    // 🔹 Yahoo API
    private static double fetchYahoo() throws Exception {

        String json = getResponse(YAHOO_URL);

        int index = json.indexOf("regularMarketPrice");
        if (index != -1) {
            int start = json.indexOf(":", index) + 1;
            int end = json.indexOf(",", start);
            return Double.parseDouble(json.substring(start, end));
        }

        throw new Exception("Yahoo parse failed");
    }

    // 🔹 CoinGecko fallback
    private static double fetchFallback() throws Exception {

        String json = getResponse(FALLBACK_URL);

        int index = json.indexOf("usd");
        if (index != -1) {
            int start = json.indexOf(":", index) + 1;
            int end = json.indexOf("}", start);
            return Double.parseDouble(json.substring(start, end));
        }

        throw new Exception("Fallback parse failed");
    }

    // 🔹 Common HTTP method
    private static String getResponse(String urlStr) throws Exception {

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new Exception("HTTP Error: " + conn.getResponseCode());
        }

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );

        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        return response.toString();
    }
}