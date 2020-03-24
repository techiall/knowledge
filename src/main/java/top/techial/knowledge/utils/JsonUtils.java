package top.techial.knowledge.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author techial
 */
public class JsonUtils {

    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private JsonUtils() {

    }

    @NonNull
    public static String writeValueAsString(@NonNull ObjectMapper objectMapper, @Nullable Object object) {
        if (object == null) {
            return "";
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            if (log.isErrorEnabled()) {
                log.error("write value as string error. object = " + object, e);
            }
        }
        return "";
    }

    @Nullable
    public static <T> T readValue(@NonNull ObjectMapper objectMapper, @Nullable String str, @NonNull Class<T> aClass) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(str, aClass);
        } catch (JsonProcessingException e) {
            if (log.isErrorEnabled()) {
                log.error("read value error, str = " + str, e);
            }
        }
        return null;
    }
}
