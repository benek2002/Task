package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BasketSplitter {
    private Map<String, List<String>> mapFromJson;

    public BasketSplitter(String absolutePathToConfigFile) throws IOException, ParseException {
        try {
            this.mapFromJson = readMapFromFile(absolutePathToConfigFile);
        } catch (IOException | ParseException e) {
            System.err.println("An error occurred while loading the configuration file: " + e.getMessage());
            throw e;
        }
    }

    public Map<String, List<String>> readMapFromFile(String absolutePathToConfigFile) throws IOException, ParseException {

        Map<String, List<String>> mapFromFile = new HashMap<>();
        JSONParser jsonParser = new JSONParser();
        FileReader fileReader = new FileReader(absolutePathToConfigFile);
        Object object = jsonParser.parse(fileReader);
        JSONObject jsonObject = (JSONObject) object;
        for (Object key : jsonObject.keySet()) {
            String product = (String) key;
            JSONArray deliveryOptionsForProduct = (JSONArray) jsonObject.get(key);
            List<String> deliveryOptions = new ArrayList<>();
            for (Object deliveryOption : deliveryOptionsForProduct) {
                deliveryOptions.add((String) deliveryOption);
            }
            mapFromFile.put(product, deliveryOptions);
        }
        return mapFromFile;


    }

    public Map<String, List<String>> split(List<String> items) {

        Map<String, List<String>> reversedMap = makeReverseMap(items);
        return minimazeDeliveryOptions(sortReversedMapByItemsCountDesc(reversedMap));
    }

    public Map<String, List<String>> sortReversedMapByItemsCountDesc(Map<String, List<String>> reversedMap) {
        Map<String, List<String>> sortedReversedMap = new LinkedHashMap<>();
        List<Map.Entry<String, List<String>>> sortedEntries = new ArrayList<>(reversedMap.entrySet());
        Comparator<Map.Entry<String, List<String>>> comparator = Comparator.comparingInt(entry -> entry.getValue().size());
        comparator = comparator.thenComparing(Map.Entry::getKey);
        sortedEntries.sort(comparator.reversed());
        for (Map.Entry<String, List<String>> entry : sortedEntries) {
            sortedReversedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedReversedMap;
    }

    public Map<String, List<String>> makeReverseMap(List<String> items) {
        Map<String, List<String>> reversedMap = new HashMap<>();
        Map<String, List<String>> itemAndDeliveryOptionsMap = new HashMap<>();
        for (String item : items) {
            List<String> deliveryOptions = mapFromJson.get(item);
            itemAndDeliveryOptionsMap.put(item, deliveryOptions);
        }
        for (Map.Entry<String, List<String>> entry : itemAndDeliveryOptionsMap.entrySet()) {
            String product = entry.getKey();
            List<String> deliveryOptions = entry.getValue();

            for (String deliveryOption : deliveryOptions) {
                reversedMap.computeIfAbsent(deliveryOption, k -> new ArrayList<>()).add(product);
            }

        }
        return reversedMap;
    }

    public Map<String, List<String>> minimazeDeliveryOptions(Map<String, List<String>> sortedMap) {
        Map<String, List<String>> result = new HashMap<>();
        Set<String> usedProducts = new HashSet<>();
        for (Map.Entry<String, List<String>> entry : sortedMap.entrySet()) {
            String deliveryOption = entry.getKey();
            List<String> products = entry.getValue();

            for (String product : products) {
                if (!usedProducts.contains(product)) {
                    result.computeIfAbsent(deliveryOption, k -> new ArrayList<>()).add(product);
                    usedProducts.add(product);
                }
            }
        }
        return result;
    }
}
