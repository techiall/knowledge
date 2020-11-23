package top.techial.knowledge.service.converter;

import top.techial.knowledge.domain.Property;
import top.techial.knowledge.utils.JsonUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

/**
 * @author techial
 */
@Convert
public class PropertyConverter implements AttributeConverter<Property, String> {

    @Override
    public String convertToDatabaseColumn(Property attribute) {
        return JsonUtils.writeValueAsString(attribute);
    }

    @Override
    public Property convertToEntityAttribute(String dbData) {
        return JsonUtils.readValue(dbData, Property.class);
    }
}
