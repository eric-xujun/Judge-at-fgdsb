package judge;import java.util.*;import java.lang.*;import java.io.*;import datastruct.*; /*
public class TreeNode {
    public int val;
    public TreeNode left, right;
    public TreeNode(int x) { val = x; }
}
*/
public class Solution {
    public TreeNode bst_to_list(TreeNode root) {
        if (root == null) return null;
        TreeNode[] head = new TreeNode[1]; // tricky
        TreeNode[] prev = new TreeNode[1];
        convert(root, head, prev);
        return head[0];
    }
    
    private void convert(TreeNode node, TreeNode[] head, TreeNode[] prev) {
        if (node == null) return;
        convert(node.left, head, prev);
        if (prev[0] == null) head[0] = node;
        else {
            node.left = prev[0];
            prev[0].right = node;
        }
        prev[0] = node; //
        convert(node.right, head, prev);
    }
}