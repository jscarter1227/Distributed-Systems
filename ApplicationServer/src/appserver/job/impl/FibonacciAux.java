package appserver.job.impl;

public class FibonacciAux {
    Integer number = null;
    
    public FibonacciAux(Integer number) {
        this.number = number;
    }
    
    public int doFib(int num) {
        if(num == 0) {
            return 0;
        }
        else if(num == 1) {
    		return 1;
    	}
        return doFib(num-1) + doFib(num-2);
    }
    
    public Integer getResult() {
    	return (Integer)doFib(number);
    }
}
