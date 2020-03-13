package top.techial.knowledge.service.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import top.techial.knowledge.domain.Property;
import top.techial.knowledge.utils.JsonUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

/**
 * @author techial
 */
@Convert
public class PropertyConverter implements AttributeConverter<Property, String> {
    private final ObjectMapper objectMapper;

    public PropertyConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convertToDatabaseColumn(Property attribute) {
        return JsonUtils.writeValueAsString(objectMapper, attribute);
    }

    @Override
    public Property convertToEntityAttribute(String dbData) {
        return JsonUtils.readValue(objectMapper, dbData, Property.class);
    }
}
