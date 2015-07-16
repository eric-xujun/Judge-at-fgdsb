package judge;import java.util.*;import java.lang.*;import java.io.*;import datastruct.*; public class Solution{
    public ArrayList<Integer> get(TreeNode root) {
        if (root == null) return null;
        ArrayList<Integer> left = get(root.left);
        ArrayList<Integer> right = get(root.right);
        ArrayList<Integer> list;
        if (left == null && right == null) {
            list = new ArrayList<Integer>();
        }else if(left == null){
            if(root.val > right.get(0)){
                list = new ArrayList<Integer>();
            }else{
                list = right;
            }
        }else if(right == null){
            if(root.val > left.get(0)){
                list = new ArrayList<Integer>();
            }else{
                list = left;
            }
        }else if (left != null && right != null) {
            list = left.get(0) > right.get(0) ? left : right;
        }
        list.add(root.val);
        return list;
    }
    
    public ArrayList<TreeNode> solve(TreeNode root) {
        ArrayList<TreeNode> maxPath = new ArrayList<TreeNode>();
        if(root == null){
            return maxPath;
        }
        findPath(root, new ArrayList<TreeNode>(), maxPath, Integer.MIN_VALUE);
        return maxPath;
    }
    
    public void findPath(TreeNode root, ArrayList<TreeNode> path, ArrayList<TreeNode> maxPath, int max){
        if(root == null){
            return;
        }
        
        path.add(root);
        if(root.val >= max){
            maxPath = new ArrayList<TreeNode>(path);
            max = root.val;
        }
        findPath(root.left, path, maxPath, max);
        path.remove(path.size() - 1);
        findPath(root.right, path, maxPath, max);
        path.remove(path.size() - 1);
    }
    
    public ArrayList path = new ArrayList();;
    
    public Boolean printPath(Node root, Node dest){
        if(root==null) return false;
        if(root==dest||printPath(root.left,dest)||printPath(root.right,dest)){
            //System.out.print("  " + root.data);
            path.add(root.data);
            return true;
        }
        return false;
    }
}