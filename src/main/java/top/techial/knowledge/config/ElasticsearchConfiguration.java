package top.techial.knowledge.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.vanroy.springdata.jest.JestElasticsearchTemplate;
import com.github.vanroy.springdata.jest.mapper.DefaultJestResultsMapper;
import io.searchbox.client.JestClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import top.techial.knowledge.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author techial
 */
@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticsearchConfiguration {

    @Bean
    public EntityMapper getEntityMapper() {
        return new CustomEntityMapper();
    }

    @Bean
    @Primary
    public ElasticsearchOperations elasticsearchTemplate(final JestClient jestClient,
                                                         final ElasticsearchConverter elasticsearchConverter,
                                                         final SimpleElasticsearchMappingContext simpleElasticsearchMappingContext,
                                                         @Qualifier("getEntityMapper") EntityMapper mapper) {
        return new JestElasticsearchTemplate(jestClient, elasticsearchConverter,
                                             new DefaultJestResultsMapper(simpleElasticsearchMappingContext, mapper));
    }

    public static class CustomEntityMapper implements EntityMapper {

        public CustomEntityMapper() {
            ObjectMapper objectMapper = JsonUtils.DEFAULT_MAPPER;
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
            objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
        }

        @Override
        public String mapToString(Object object) {
            return JsonUtils.writeValueAsString(object);
        }

        @Override
        public <T> T mapToObject(String source, Class<T> clazz) {
            return JsonUtils.readValue(source, clazz);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Map<String, Object> mapObject(Object source) {
            return JsonUtils.readValue(mapToString(source), HashMap.class);
        }

        @Override
        public <T> T readObject(Map<String, Object> source, Class<T> targetType) {
            return mapToObject(mapToString(source), targetType);
        }
    }
}
