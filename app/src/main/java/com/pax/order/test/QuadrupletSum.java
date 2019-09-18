package com.pax.order.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuadrupletSum {
	
	public List<List<Integer>> addition(int[] nums, int target){
		Arrays.sort(nums);
        return NSum(nums, 0, 4, target);
	}
	
	private List<List<Integer>> NSum (int[] nums, int front, int k, int target){
		int len = nums.length;
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        if(k == 2) { 
        	int left = front;
        	int right = len - 1;
            while(left < right) {
                if(nums[left] + nums[right] == target) {
                    List<Integer> sublist = new ArrayList<Integer>();
                    sublist.add(nums[left]);
                    sublist.add(nums[right]);
                    result.add(sublist);
                    while(left < right && nums[left] == nums[left + 1]) {
                    	left++;
                    }
                    while(left < right && nums[right] == nums[right - 1]) {
                    	right--;
                    } 
                    left++;
                    right--;
                } 
                else if (nums[left] + nums[right] < target) { 
                    left++;
                } 
                else {
                    right--;
                }
            }
        } 
        else {
            for(int i = front; i < len - (k - 1); i++) {
                if(i > front && nums[i] == nums[i - 1]) 
                	continue;
                List<List<Integer>> list = NSum(nums, i + 1, k - 1, target - nums[i]);
                for(List<Integer> t : list) {
                   t.add(0, nums[i]);
                }                    
                result.addAll(list);
            }
        }
        return result;
	}
	public static void main(String[] args) {
		QuadrupletSum obj = new QuadrupletSum();
		int[] nums = new int[] {1,0,-1,0,-2,2};
		int target = 0;
		List<List<Integer>> result = new ArrayList<>();
		result = obj.addition(nums, target);
		System.out.println(result);
	}
}
