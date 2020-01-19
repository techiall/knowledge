package top.techial.knowledge.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import top.techial.knowledge.domain.Property;

import javax.persistence.AttributeConverter;

/**
 * @author techial
 */
@Component
public class PropertyConverter implements AttributeConverter<Property, String> {
    private final ObjectMapper objectMapper;

    public PropertyConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(Property attribute) {
        return objectMapper.writeValueAsString(attribute);
    }

    @Override
    @SneakyThrows
    public Property convertToEntityAttribute(String dbData) {
        return objectMapper.readValue(dbData, Property.class);
    }
}
