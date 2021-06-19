import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;



// TODO: comment and complete your HashTableADT implementation
// DO ADD UNIMPLEMENTED PUBLIC METHODS FROM HashTableADT and DataStructureADT TO YOUR CLASS
// DO IMPLEMENT THE PUBLIC CONSTRUCTORS STARTED
// DO NOT ADD OTHER PUBLIC MEMBERS (fields or methods) TO YOUR CLASS
//
// TODO: implement all required methods
//
// TODO: describe the collision resolution scheme you have chosen
// identify your scheme as open addressing or bucket
//
// TODO: explain your hashing algorithm here 
// NOTE: you are not required to design your own algorithm for hashing,
//       since you do not know the type for K,
//       you must use the hashCode provided by the <K key> object
//       and one of the techniques presented in lecture
//
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {
	private LinkedList<Node>[] table;
	private double maxLoadFactor;
	private int load;
	private int size;
	/**
	 * inner class that stores a key-value pair
	 * @author cheng
	 *
	 */
	public class Node{
		private K key;
		private V value;
		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}

	// TODO: ADD and comment DATA FIELD MEMBERS needed for your implementation
		
	// TODO: comment and complete a default no-arg constructor
	public HashTable() {
		table = init(11);
		this.maxLoadFactor = 0.8;
	}
	
	// TODO: comment and complete a constructor that accepts 
	// initial capacity and load factor threshold
    // threshold is the load factor that causes a resize and rehash

	public HashTable(int initialCapacity, double loadFactorThreshold) {
		table = init(initialCapacity);
		
		this.maxLoadFactor = loadFactorThreshold;
	}
	/**
	 * initializes an array of empty linked lists of node objects with given size
	 * @param size - the size of the array to be initialized
	 * @return - the empty array of linked lists of nodes
	 */
	public LinkedList<Node>[] init(int size){
		LinkedList<Node>[] result = (LinkedList<HashTable<K, V>.Node>[]) Array.newInstance(new LinkedList<Node>().getClass(), size);
		for (int i = 0 ; i < result.length; i++) {
			result[i] = new LinkedList<Node>();
		}
		return result;
	}
	
	// Add the key,value pair to the data structure and increase the number of keys.
	// If key is null, throw IllegalNullKeyException;
	// If key is already in data structure, throw DuplicateKeyException();
	@Override
	public void insert(K key, V value) throws IllegalNullKeyException{
		// check for null key
		if (key==null) throw new IllegalNullKeyException();
		//add the key value pair into the specified location at the end of the linkedlist
		int hash = key.hashCode();
		int length  = table.length;
		for (Node a : table[Math.abs(hash%length)] ){
			if (a.key.equals(key)) {
				table[(Math.abs(hash%table.length))].remove(a);
				break;
			}
		}
		table[Math.abs(hash%table.length)].add(new Node(key, value));
		size++;
		//check for rehash/resize if loadfactor is reached
		if (table[Math.abs(hash%table.length)].size()==1) load++;
		if ((double)load/(double)table.length >= maxLoadFactor) {
			load = 0;
			LinkedList<Node>[] temp = table;
			table = init(table.length*2+1);
			for(LinkedList<Node> n : temp) {
				for (Node a : n) {
					insert(a.key, a.value);
				}
			}
		}
		
	}
	// If key is found,
	// remove the key,value pair from the data structure
	// decrease number of keys.
	// return true
	// If key is null, throw IllegalNullKeyException
	// If key is not found, return false
	@Override
	public boolean remove(K key) throws IllegalNullKeyException {
		if (key==null) throw new IllegalNullKeyException();
		int index = Math.abs(key.hashCode()%table.length);
		for (Node n : table[index]) {
			if (n.key.equals(key)) {
				table[index].remove(n);
				if (table[index].size()==0) load--;
				return true;
			}
		}
		return false;
		
	}
	// Returns the value associated with the specified key
	// Does not remove key or decrease number of keys
	//
	// If key is null, throw IllegalNullKeyException
	// If key is not found, throw KeyNotFoundException().
	@Override
	public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
		if (key==null) throw new IllegalNullKeyException();
		int index = Math.abs(key.hashCode()%table.length);
		//searches the linked list at the hash alg's output position for the key
		for (Node n : table[index]) {
			if (n.key.equals(key)) {
				return n.value;
				
			}
		}
		throw new KeyNotFoundException();
	}
	// Returns the number of key,value pairs in the data structure
	@Override
	public int numKeys() {
		return size;
	}
	// Returns the load factor threshold that was
	// passed into the constructor when creating
	// the instance of the HashTable.
	// When the current load factor is greater than or
	// equal to the specified load factor threshold,
	// the table is resized and elements are rehashed.	
	@Override
	public double getLoadFactorThreshold() {
		return maxLoadFactor;
	}
	// Returns the current load factor for this hash table
	// load factor = number of items / current table size
	@Override
	public double getLoadFactor() {
		return (double)load/table.length;
	}
	// Return the current Capacity (table size)
	// of the hash table array.
	//
	// The initial capacity must be a positive integer, 1 or greater
	// and is specified in the constructor.
	//
	// REQUIRED: When the load factor threshold is reached,
	// the capacity must increase to: 2 * capacity + 1
	//
	// Once increased, the capacity never decreases
	@Override
	public int getCapacity() {
		// TODO Auto-generated method stub
		return table.length;
	}
	// Returns the collision resolution scheme used for this hash table.
	// Implement with one of the following collision resolution strategies.
	// Define this method to return an integer to indicate which strategy.
	//
	// 1 OPEN ADDRESSING: linear probe
	// 2 OPEN ADDRESSING: quadratic probe
	// 3 OPEN ADDRESSING: double hashing
	// 4 CHAINED BUCKET: array of arrays
	// 5 CHAINED BUCKET: array of linked nodes
	// 6 CHAINED BUCKET: array of search trees
	// 7 CHAINED BUCKET: linked nodes of arrays
	// 8 CHAINED BUCKET: linked nodes of linked node
	// 9 CHAINED BUCKET: linked nodes of search trees
	@Override
	public int getCollisionResolution() {
		// TODO Auto-generated method stub
		return 5;
	}

	// TODO: implement all unimplemented methods so that the class can compile
	/**
	 * prints the linked list at the given index of the hash table
	 * @param index
	 */
	public void printlist(int index) {
		LinkedList<Node> current = table[index];
		for (int i = 0;i < current.size(); i++) {
			System.out.print(current.get(i).key+", ");
		}
		if (current.size() != 0) System.out.println();
		
	}
		
}
