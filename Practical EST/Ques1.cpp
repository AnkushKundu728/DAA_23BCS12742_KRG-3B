// Description : Return the number of ways the monkeys can move so that at least one collision happens. Since the answer may be very large, return it modulo 109 + 7.

class Solution {
public:
    int maxi = 1000000007 ;
    long solve(int n) {
        if(n==1) return 2 ;
        if(n%2 == 0) {
            long half = solve(n/2) ;
            return (half*half)%maxi ;
        }
        else{
            long half=solve(n/2);
            return ((half*half)%maxi)*2%maxi;
        }
    }

    int monkeyMove(int n) {
        return (int)(((maxi+solve(n))-2)% maxi);
    }
};