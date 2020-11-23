package top.techial.knowledge.service.converter;

import top.techial.knowledge.domain.Labels;
import top.techial.knowledge.utils.JsonUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

/**
 * @author techial
 */
@Convert
public class LabelsConverter implements AttributeConverter<Labels, String> {
    @Override
    public String convertToDatabaseColumn(Labels attribute) {
        return JsonUtils.writeValueAsString(attribute);
    }

    @Override
    public Labels convertToEntityAttribute(String dbData) {
        return JsonUtils.readValue(dbData, Labels.class);
    }
}
