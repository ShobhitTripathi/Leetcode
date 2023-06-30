import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonFlattener {
    public static Map<String, String> flattenJson(JsonNode jsonNode, String prefix) {
        Map<String, String> flattenedJson = new HashMap<>();

        if (jsonNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jsonNode.fields();
            while (fieldsIterator.hasNext()) {
                Map.Entry<String, JsonNode> field = fieldsIterator.next();
                String fieldName = field.getKey();
                JsonNode fieldValue = field.getValue();

                String newPrefix = prefix.isEmpty() ? fieldName : prefix + "." + fieldName;
                flattenedJson.putAll(flattenJson(fieldValue, newPrefix));
            }
        } else if (jsonNode.isArray()) {
            int index = 0;
            for (JsonNode arrayElement : jsonNode) {
                String newPrefix = prefix + "[" + index + "]";
                flattenedJson.putAll(flattenJson(arrayElement, newPrefix));
                index++;
            }
        } else if (jsonNode.isValueNode()) {
            flattenedJson.put(prefix, jsonNode.asText());
        }

        return flattenedJson;
    }

    public static void main(String[] args) throws Exception {
        String jsonString = "{\"name\":\"John\",\"age\":30,\"address\":{\"city\":\"New York\",\"country\":\"USA\"},\"hobbies\":[\"reading\",\"traveling\"]}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        Map<String, String> flattenedJson = flattenJson(jsonNode, "");
        flattenedJson.forEach((key, value) -> System.out.println(key + " : " + value));
    }
