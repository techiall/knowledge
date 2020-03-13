package top.techial.knowledge.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

/**
 * @author techial
 */
public class JsonUtils {

    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private JsonUtils() {

    }

    @NonNull
    public static String writeValueAsString(@NonNull ObjectMapper objectMapper, @NonNull Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            if (log.isErrorEnabled()) {
                log.error("write value as string error.", e);
            }
        }
        return "";
    }

    public static <T> T readValue(ObjectMapper objectMapper, String str, Class<T> aClass) {
        try {
            return objectMapper.readValue(str, aClass);
        } catch (JsonProcessingException e) {
            if (log.isErrorEnabled()) {
                log.error("read value error", e);
            }
        }
        return null;
    }
}
