package cc.techial.knowledge.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author techial
 */
public class Property implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, List<PropertyDTO>> property = new HashMap<>();

    public Map<String, List<PropertyDTO>> getProperty() {
        return property;
    }

    public void setProperty(Map<String, List<PropertyDTO>> property) {
        this.property = property;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Property)) return false;
        Property property1 = (Property) o;
        return Objects.equals(getProperty(), property1.getProperty());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProperty());
    }

    public static class PropertyDTO implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PropertyDTO)) return false;
            PropertyDTO that = (PropertyDTO) o;
            return Objects.equals(getId(), that.getId()) &&
                    Objects.equals(getName(), that.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId(), getName());
        }
    }
}
