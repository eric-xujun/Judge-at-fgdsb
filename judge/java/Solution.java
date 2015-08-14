package judge;import java.util.*;import java.lang.*;import java.io.*;import datastruct.*; // This is the text editor interface
// Anything you type or change here will be seen by the other person in real time
// An "expression" is either a number, or a list (AKA vector/array) with the first element one of '+', '-', '*', '/' ,
// and then other expressions.
// Write a function "evaluate" that gets an expression and returns its value.
// ['+', 1, 2] --> 3
// ['*', 3, ['+', 1, 10]] --> 33
// ['+', 1000, 1200] --> 2200
// ['/', 6, ['+', 1, ['*', 1, 1]]] --> 3
// ['+', 1, 2, 3] --> 63
// ['+', ['*', 1, 2], 3] --> 5
// ['-', ['*', 5, 5], ['*', 2, 2]] --> 21
// Assume - / -- only take two operands
// + * take any number of operands
// ['*', 0, 5] --> 0
// ['-', 2, 1] --> 1
public class EvaluationExpression {
    public int valueOfExpression(ENode root) {
        if(root == null)        
            return 0;
        switch(root.op){
            case '+': {
                int val = 0;
                for(ENode nd : root.child) {
                        val += valueOfExpression(nd);
                }
                return val;
            }
            case '-': {
                int val = (root.child.size() == 0) ? 0 : valueOfExpression(root.child.get(0));                                ) f. {- _# u- F% q
                for(int i = 1; i < root.child.size(); ++i) {
                        val -= valueOfExpression(root.child.get(i));
                }
                return val;
            }
            case '*': {
                int val = 1;
                for(ENode nd : root.child) {
                        val *= valueOfExpression(nd);
                }
                return val;
            }
            case '/': {
                int val = (root.child.size() == 0) ? 0 : valueOfExpression(root.child.get(0));
                for(int i = 1; i < root.child.size(); ++i) {
                        val /= valueOfExpression(root.child.get(i));
                }
                return val;
            }
            default: return root.val;
        }                 
    }
} 

class ENode {
    char op;
    int val;
    List<ENode> child = new ArrayList<ENode>();
    public ENode(int val) {
        op = ' ';
        this.val = val;
    }
    public ENode(char op) {
        this.op = op;
        val = 0;
    }
}