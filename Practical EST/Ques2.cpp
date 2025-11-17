// Description : Given arrival arr[] and departure dep[] times of trains on the same day, find the minimum number of platforms needed so that no train waits. A platform cannot serve two trains at the same time; if a train arrives before another departs, an extra platform is needed.

#include<bits/stdc++.h>
using namespace std ;

class Solution {
  public:
    int minPlatform(vector<int>& arr, vector<int>& dep) {
        int n = arr.size();
        int m = dep.size();
        
        sort(arr.begin(), arr.end());
        sort(dep.begin(), dep.end());
        
        int cnt = 0, maxcnt = 0;
        int i = 0, j = 0;
        
        while(i < n && j < m) {
            if(arr[i] <= dep[j]) {
                cnt++;  
                i++;
            } else {
                cnt--;  
                j++;
            }
            maxcnt = max(maxcnt, cnt);
        }
        return maxcnt;
    }
};