package top.techial.knowledge.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import top.techial.knowledge.domain.Labels;

import javax.persistence.AttributeConverter;

/**
 * @author techial
 */
@Component
public class LabelsConverter implements AttributeConverter<Labels, String> {
    private final ObjectMapper objectMapper;

    public LabelsConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(Labels attribute) {
        return objectMapper.writeValueAsString(attribute);
    }

    @Override
    @SneakyThrows
    public Labels convertToEntityAttribute(String dbData) {
        return objectMapper.readValue(dbData, Labels.class);
    }
}
