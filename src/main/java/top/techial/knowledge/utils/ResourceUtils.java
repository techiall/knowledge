package top.techial.knowledge.utils;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;

/**
 * @author techial
 */
public class ResourceUtils {

    private ResourceUtils() {
        throw new IllegalStateException("Utility class");
    }

    @SneakyThrows
    public static <T> Iterator<T> read(String resourceLocation, Function<String, T> mapper) {
        ClassPathResource classPathResource = new ClassPathResource(resourceLocation);
        byte[] read = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());

        return Arrays.stream(new String(read).split("\n")).map(mapper).iterator();
    }
}
