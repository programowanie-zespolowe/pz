package sample.WebService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class JSONConverter {

    private static void RegisterTypes(ObjectMapper mapper)
    {
        mapper.registerModule(new JavaTimeModule()); // new module, NOT JSR310Module
    }

    public static<T> T ConvertToObject(String Json, java.lang.Class<T> tClass) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        RegisterTypes(mapper);
        return mapper.readValue(Json, tClass);
    }

    public static String ConvertTOJson(Object object) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        RegisterTypes(mapper);
        String json = mapper.writeValueAsString(object);
        return json;
    }
}
