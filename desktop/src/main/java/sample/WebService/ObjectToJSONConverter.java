package sample.WebService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectToJSONConverter {

    public static String Convert(Object object) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(object);
        System.out.println("ResultingJSONstring = " + json);
        return json;
    }
}
