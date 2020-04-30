package top.techial.knowledge.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author techial
 */
public class Property implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, List<PropertyDTO>> property = new HashMap<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Property)) return false;

        Property property1 = (Property) o;

        return getProperty() != null ? getProperty().equals(property1.getProperty()) : property1.getProperty() == null;
    }

    @Override
    public int hashCode() {
        return getProperty() != null ? getProperty().hashCode() : 0;
    }

    public Map<String, List<PropertyDTO>> getProperty() {
        return property;
    }

    public void setProperty(Map<String, List<PropertyDTO>> property) {
        this.property = property;
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

            if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
            return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
        }

        @Override
        public int hashCode() {
            int result = getId() != null ? getId().hashCode() : 0;
            result = 31 * result + (getName() != null ? getName().hashCode() : 0);
            return result;
        }
    }
}
