package SpringAPIStudy.bookstore.app.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomObjectMapper<T> {

    public static ObjectMapper objectMapper = new ObjectMapper();

    public CustomObjectMapper() {
        objectMapper
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false)
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static<F,T> T to(F from, Class<T> to){
        if(from == null) return null;
        return objectMapper.convertValue(from,to);
    }


    // List를 변환해준다.
    public static<F,T> List<T> toList(List<F> froms, Class<T> to) {
        List<T> ts = new ArrayList<>();
        if (froms.size() == 0) return ts;
        froms.stream().forEach(f -> ts.add(objectMapper.convertValue(f,to)));
        return ts;
    }
}