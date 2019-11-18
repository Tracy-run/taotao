package com.demo.taotao.leetcode;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
public class TwoAdd {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        ListNode rev = new ListNode(0);//创建一个新的single link
        ListNode cursor = rev; //根元素
        int carry = 0; //判断是否要进位
        while(l1 != null || l2 != null || carry != 0){
            int l1val = l1 != null ? l1.val : 0;
            int l2val = l2 != null ? l2.val : 0;

            int sumval = l1val + l2val + carry;
            carry = sumval / 10;//进位？

            ListNode sumnode = new ListNode(sumval % 10);//下一个元素
            cursor.next = sumnode;
            cursor = sumnode;

            if(l1 != null) l1 = l1.next;
            if(l2 != null) l2 = l2.next;
        }
        return rev.next;

    }

    class ListNode{
        int val;
        ListNode next;
        ListNode(int num){
            val = num;
        }
    }
}