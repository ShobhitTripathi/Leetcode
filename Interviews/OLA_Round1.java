//package StackAndQueue;
//
//import java.util.ArrayList;
//import java.util.List;
//
//class vertex {
//    int val;
//    boolean isVisited;
//    public List<vertex> adjList;
//
//    public vertex(int val, boolean isVisited, List<vertex> adj) {
//        this.val = val;
//        this.isVisited = isVisited;
//        this.adjList = new ArrayList<>(adj);
//    }
//}
//
//
//public class OLA_Round1 {
//    static List<Integer> values = new ArrayList<>();
////    static final List<vertex> adjList;
//    static boolean isCycle (vertex v) {
//        if (v.isVisited) {
//            return true;
//        }
//        if (int i = 0;i < 6;i++) {
//            if (values.get(i) == v.val)
//                return true;
//        }
//        values.add(v.val);
//        v.isVisited = true;
//        for (vertex next : v.adjList) {
//            isCycle(next);
//        }
//        return false;
//    }
//
//}
//
///*
//
//table_name
//emp_no , emp_name, manager_no, salary, hiredate
//
//Select emp_name from  table_name  JOIN emp_name ON (manger_no = emp_no)
//
// */
