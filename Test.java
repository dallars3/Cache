import cache.*;

public class Test {

	public static void main(String[] args) {
		LFUCache<Integer, String> cache;
		cache = new LFUCache<Integer, String>(100000, 10000, 100);
		for(int i = 0; i < 100000; i ++){ 
			cache.put(i, "aa" + i);
		}
		cache.startDecrease(100, 100);
		long a = System.currentTimeMillis();
		for(int i = 0; i < 10000000; i ++){  
			cache.get(i % 1024);
		}
		long b = System.currentTimeMillis();
		System.out.print(b - a);
	}

}
