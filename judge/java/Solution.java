package judge;import java.util.*;import java.lang.*;import java.io.*;import datastruct.*; public class Solution{
    private int count = 0;
    
    public boolean validUniformTree(TreeNode root){
        if(root == null){
            return true;
        }
        boolean left = validUniformTree(root.left);
        boolean right = validUniformTree(root.right);
        if(left && right){
            if((root.left == null || root.left.val == root.val) && (root.right == null || root.right.val == root.val)){
                count++;
                return true;
            }
        }
        return false;
    }
}