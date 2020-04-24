package top.techial.knowledge.domain;

import java.io.Serializable;
import java.util.Set;

/**
 * @author techial
 */
public class Labels implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<String> labels;

    public Set<String> getLabels() {
        return labels;
    }

    public void setLabels(Set<String> labels) {
        this.labels = labels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Labels)) return false;

        Labels labels1 = (Labels) o;

        return getLabels() != null ? getLabels().equals(labels1.getLabels()) : labels1.getLabels() == null;
    }

    @Override
    public int hashCode() {
        return getLabels() != null ? getLabels().hashCode() : 0;
    }
}
