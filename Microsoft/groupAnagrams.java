/* 
Given an array of strings strs, group the anagrams together. You can return the answer in any order.

An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.

 

Example 1:

Input: strs = ["eat","tea","tan","ate","nat","bat"]
Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
Example 2:

Input: strs = [""]
Output: [[""]]
Example 3:

Input: strs = ["a"]
Output: [["a"]]

*/
class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> result = new ArrayList<>();
        Map<String, List<String>> map = new HashMap<>();
        
        for (int i = 0;i < strs.length;i++) {
            List<String> list;
            char[] arr = strs[i].toCharArray();
            Arrays.sort(arr);
            String keyStr = String.valueOf(arr);
            if (!map.containsKey(keyStr)) {
                list = new ArrayList<>();
                list.add(strs[i]);
                map.put(keyStr, list);
            } else {
                list = map.get(keyStr);
                list.add(strs[i]);
                map.put(keyStr, list);
            }
        }
        
        for (String s : map.keySet()) {
            result.add(map.get(s));
        }
        
    return result;
        
    }
}
