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
