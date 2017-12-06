package artificial.intelligence.cpsc;

import java.util.ArrayList;

public class Tree<T> {
	private Node<T> root;
	
	public Tree(T rootData){
		root = new Node<T>();
		root.data = rootData;
		root.children = new ArrayList<Tree<T>>();
	}
	
	private static class Node<T>{
		private T data;
		private Node<T> parent;
		private ArrayList<Tree<T>> children;
	}
	
	public void setData(T givenData){
		root.data = givenData;
	}
	public T getData(){
		return root.data;
	}
	
	public void addChild(Tree<T> child){
		root.children.add(child);
	}
	public boolean removeChild(Tree<T> child){
		return root.children.remove(child);
	}
	public void addChildren(ArrayList<Tree<T>> newChildren){
		for(Tree<T> t: newChildren){
			root.children.add(t);
		}
	}
	public ArrayList<Tree<T>> getChildren(){
		return root.children;
	}
	
	
	public void setParent(Node<T> parent){
		root.parent = parent;
	}
	public Node<T> getParent(){
		return root.parent;
	}
}
