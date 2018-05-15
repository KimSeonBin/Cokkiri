package utill_network;

import java.util.LinkedList;

public class PeerQueue<T> {
  
	private LinkedList<T> queue;
	
	
	public PeerQueue() {
		queue = new LinkedList<T>();
	}
	
	public T peek() {return queue.peek();}
	public T pool() {return queue.poll();}
	
	public void add(T t){
		queue.add(t);
	}
	
	
}
