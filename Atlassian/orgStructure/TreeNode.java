package atlassian.codeDS;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private String name;
    List<TreeNode> children;


    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    TreeNode(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }
}
