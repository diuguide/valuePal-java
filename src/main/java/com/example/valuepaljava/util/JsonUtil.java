package com.example.valuepaljava.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonUtil {

    private final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public void processJson(String jsonString) throws IOException {
        System.out.println("JSON: " + jsonString);
        JsonFactory f = new MappingJsonFactory();
        JsonParser jp = f.createJsonParser(jsonString);
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

                        System.out.println("field2: " + node.get("quoteSummary"));

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
}
