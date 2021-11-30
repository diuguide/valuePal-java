package com.example.valuepaljava.util;

import com.example.valuepaljava.models.Quote;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonUtil {

    private final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public void processJson(String jsonString) throws IOException {
        System.out.println("JSON: " + jsonString);
        JsonFactory f = new MappingJsonFactory();
        JsonParser jp = f.createParser(jsonString);
        JsonToken current;
        current = jp.nextToken();
        if (current != JsonToken.START_OBJECT) {
            System.out.println("Error: root should be object: quiting.");
            return;
        }
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jp.getCurrentName();
            System.out.println("Current Field: " + fieldName);
            // move from field name to field value
            current = jp.nextToken();
            System.out.println("next token: " + jp.getCurrentName());
            if (fieldName.equals("quoteResponse")) {
//                if (current == JsonToken.START_ARRAY) {
                    // For each of the records in the array

                        // read the record into a tree model,
                        // this moves the parsing position to the end of it
                        JsonNode node = jp.readValueAsTree();
                        // And now we have random access to everything in the object
                        System.out.println("field1: " + node.get("result"));


                        System.out.println("field2: " + node.get("language"));

//                } else {
//                    System.out.println("Error: records should be an array: skipping.");
//                    jp.skipChildren();
//                }
            } else {
                System.out.println("Unprocessed property: " + fieldName);
                jp.skipChildren();
            }
        }
    }

    public void jsonParser(String jsonString) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
        JSONObject quoteResponse = (JSONObject) jsonObject.get("quoteResponse");
        JSONArray result = (JSONArray) quoteResponse.get("result");
        for (Object o : result) {
            System.out.println(o);
            JSONObject current = (JSONObject) o;
            Quote currentQuote = new Quote(
                    (String) current.get("symbol"),
                    (String) current.get("longName"),
                    (double) current.get("regularMarketPrice"),
                    (double) current.get("regularMarketChange"),
                    (double) current.get("regularMarketChangePercent"),
                    (long) current.get("regularMarketTime"));
            System.out.println("currentQuote: " + currentQuote);
        }
    }

}
