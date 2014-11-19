import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.*;

public class Parser {
    public List<String> getData(String city) {
        Document doc;
        Map<String,String> citiesMap = getCitiesLinks();
        String cityName;

        cityName = city;
        cityName = Character.toUpperCase(cityName.charAt(0)) + cityName.substring(1);

        String cityURL = citiesMap.get(cityName);

        try {
            doc = Jsoup.connect(cityURL).get();

        }   catch(IOException | IllegalArgumentException e) {
            doc = null;
        }

        if(doc != null) {
            return getDataFromDocument(doc);
        }
        return null;
    }

    private Map<String, String> getCitiesLinks() {
        Document country;
        Map<String, String> linksMap = new HashMap<String,String>();
        try {
            country = Jsoup.connect("http://www.gismeteo.com/catalog/armenia/").get();
        }   catch(IOException e) {
            country = null;
        }

        if(country != null) {
            Elements links = country.select("li.catalog_item a[href]");
            for(Element link : links) {
                linksMap.put(link.text(), "http://www.gismeteo.com" + link.attr("href"));
            }
            return linksMap;

        }   else {
            System.out.println("Error!");
            return null;
        }

    }

    private  List<String> getDataFromDocument(Document doc) {
        if(doc != null) {
            List<String> props = new ArrayList<String>();
            String c = doc.select("div.temp > dd.c").first().text();
            String wind = doc.select("dl.wicon").attr("title") + ": " + doc.select("dd.ms").text();
            String pres =  doc.select("div.barp").attr("title") + ": " + doc.select("dd.torr").text();
            String moisture = doc.select("div.hum").attr("title") + ": " + doc.select("div.hum").text();
            String water = doc.select("div.water").attr("title") + ": " + doc.select("div.water dd.c").text();

            String [] arr = new String[] {
                    "Temperature: " + c, wind, pres, moisture.substring(0, moisture.length()),
                    water.substring(0, water.length()-5)
            };

            props.addAll(Arrays.asList(arr));
            return props;

        }   else {
                System.out.println("ERROR: Can't connect. Do you have an Internet connection? ");
                return null;
        }
    }
}
