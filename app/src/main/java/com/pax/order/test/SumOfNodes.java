package com.pax.order.test;

import java.util.ArrayList;
import java.util.List;

class Node {
	     int val;
	     Node left;
	     Node right;
	    
	     Node(int x) { 
	    	 val = x; 
	     }
}

public class SumOfNodes {
	public List<List<Integer>> Sum(Node root, int sum) {
        List<List<Integer>> result = new ArrayList<>();
        pathSum(root,sum,new ArrayList<Integer>(),result);
        return result;
    }
	
	  public void pathSum(Node root, int sum, List<Integer> list, List<List<Integer>> res){
	        if(root==null)
	            return;
	        list.add(root.val);
	        if(root.left==null && root.right==null && root.val==sum){
	            res.add(new ArrayList<Integer>(list));
	        }
	        else{
	            pathSum(root.left, sum-root.val, list, res);
	            pathSum(root.right, sum-root.val, list, res);
	        }
	        list.remove(list.size()-1);
	    }
    
	
	public static void main(String[] args) {
		SumOfNodes obj = new SumOfNodes();
		int sum =22;
		Node root = new Node(5);
		root.left = new Node(4);
		root.right = new Node(8);
		root.left.left = new Node(11);
		root.right.left = new Node(13);
		root.right.right = new Node(4);
		root.left.left.left = new Node(7);
		root.left.left.right = new Node(2);
		root.right.right.left = new Node(5);
		root.right.right.right = new Node(1);
		List<List<Integer>> res = obj.Sum(root, sum);
		System.out.println(res);
		 
	}
}
