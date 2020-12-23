package cc.techial.knowledge.domain;

import java.io.Serializable;
import java.util.Objects;
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
        return Objects.equals(getLabels(), labels1.getLabels());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLabels());
    }
}
