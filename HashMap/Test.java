
public class Test {
	public static void main(String[] args) {
		GMap<String, Integer> myMap = new GMap<String, Integer>();
		
		myMap.put("a", 1);
		myMap.put("b", 2);
		myMap.put("c", 3);
		//myMap.put("d", 4);
		myMap.put("e", 5);
		myMap.put("a", 25);
		System.out.print(myMap.toString());
		//System.out.println(myMap.get("a"));
		//System.out.println(myMap.get("b"));
		//System.out.println(myMap.get("c"));
		//System.out.println(myMap.get("d"));
		//System.out.println(myMap.get("e"));
	}
}
