package top.techial.knowledge.domain;

/**
 * @author techial
 */
public enum OperatorMessageEnum {
    ADD_NODE("添加节点，节点名称为 [%s]。"),

    UPDATE_NODE_NAME("更新节点名称，节点新名称为 [%s]。"),

    UPDATE_NODE_LABELS("更新节点属性，节点属性为：[%s]"),

    ADD_NODE_PROPERTY("添加节点关系，关系为：[%s]"),
    UPDATE_NODE_PROPERTY("更新节点关系，关系为：[%s]"),

    DELETE_NODE_RELATION("删除节点关系，关系为：[%s]"),

    UPDATE_NODE_PROPER("更新节点属性，更新内容为：[%s]");

    private final String message;

    OperatorMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
