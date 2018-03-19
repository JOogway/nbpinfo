package Controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.*;

class DataFetcher {

    private String input;

    private double averageBid;
    private double standardAskDeviation;

    DataFetcher(String query) {
        try {
            URL url = new URL(query);

            StringBuilder stringBuilder = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());
            stringBuilder.append(scanner.nextLine() + "\n");
            scanner.close();

            input = stringBuilder.toString();
            System.out.println(input);

            //TODO second JSON to data
            JSONArray dataArray = getSubJSONfromJSON(input, "rates");

            String[] subJSONs = new String[dataArray.size()];
            for (int i = 0; i < dataArray.size(); i++) {
                subJSONs[i] = dataArray.get(i).toString();
            }

            String askArray[] = new String[dataArray.size()];
            String bidArray[] = new String[subJSONs.length];
            for (int i = 0; i < subJSONs.length; i++) {
                bidArray[i] = getStringFromJSON(subJSONs[i], "bid");
                askArray[i] = getStringFromJSON(subJSONs[i], "ask");
            }
            averageBid = countAverage(bidArray);
            standardAskDeviation = countStandardAskDeviation(askArray);
        } catch (IOException | ParseException e) {
            System.out.println("Error occured while attempting on data fetch: " + e);
            e.printStackTrace();
        }


    }

    private String getStringFromJSON(String json, String searchedKey) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
        jsonParser.reset();
        return jsonObject.get(searchedKey).toString();
    }

    private JSONArray getSubJSONfromJSON(String json, String key) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
        jsonParser.reset();
        return (JSONArray) jsonObject.get(key);
    }

    private double countAverage(String[] arrayOfNumbers) {
        double sum = 0;
        for (String s : arrayOfNumbers) {
            sum += Double.parseDouble(s);
        }
        return sum / arrayOfNumbers.length;
    }

    private double countStandardAskDeviation(String[] askArray) {
        return Math.sqrt(getVariance(askArray));
    }

    private double getMean(String[] askArray) {
        double sum = 0.0;
        for (String a : askArray)
            sum += Double.parseDouble(a);
        return sum / askArray.length;
    }

    private double getVariance(String[] askArray) {
        double mean = getMean(askArray);
        double temp = 0;
        for (String a : askArray)
            temp += (Double.parseDouble(a) - mean) * (Double.parseDouble(a) - mean);
        return temp / askArray.length;
    }

    public double getAverageBid() {
        return averageBid;
    }

    public double getStandardAskDeviation() {
        return standardAskDeviation;
    }
}