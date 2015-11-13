import java.util.Iterator;

public class Test {
	public static void main(String[] args) {
		GMap<String, Integer> myMap = new GMap<String, Integer>();

		System.out.println("Origional HashMap:");
		myMap.put("a", 1);
		myMap.put("b", 2);
		myMap.put("c", 3);
		//myMap.put("d", 4);
		myMap.put("e", 5);
		System.out.println(myMap.toString());

		System.out.println("\n\nHashMap after duplicate key added:");
		myMap.put("a", 25);
		System.out.println(myMap.toString());

		System.out.println("\nIterator object moving through HashMap entries: ");
		Iterator itr = myMap.entries();
		while(itr.hasNext()) {
			System.out.println(itr.next());
		}

		System.out.println("\nIterator object moving through HashMap keys: ");
		Iterator itr2 = myMap.keys();
		while(itr2.hasNext()) {
			System.out.println(itr2.next());
		}

	}
}
