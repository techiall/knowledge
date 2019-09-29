package top.techial.knowledge.utils;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author techial
 */
public class ResourceUtils {

    private ResourceUtils() {
        throw new IllegalStateException("Utility class");
    }

    @SneakyThrows
    public static <T> List<T> read(String resourceLocation, Function<String[], T> mapper, String regex) {
        ClassPathResource classPathResource = new ClassPathResource(resourceLocation);
        byte[] read = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());

        return Arrays.stream(new String(read).split("\n"))
            .map(it -> mapper.apply(it.split(regex))).collect(Collectors.toList());
    }
}
