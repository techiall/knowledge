package top.techial.knowledge.service.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import top.techial.knowledge.domain.Labels;
import top.techial.knowledge.utils.JsonUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

/**
 * @author techial
 */
@Convert
public class LabelsConverter implements AttributeConverter<Labels, String> {
    private final ObjectMapper objectMapper;

    public LabelsConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convertToDatabaseColumn(Labels attribute) {
        return JsonUtils.writeValueAsString(objectMapper, attribute);
    }

    @Override
    public Labels convertToEntityAttribute(String dbData) {
        return JsonUtils.readValue(objectMapper, dbData, Labels.class);
    }
}
