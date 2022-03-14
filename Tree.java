
public class Tree {
	private String data;
	private Tree parent;
	private Tree down;
	private Tree right;
	private Tree left;
	
	
	public Tree getParent() {
		return parent;
	}


	public void setParent(Tree parent) {
		this.parent = parent;
	}


	
	

	public Tree(String dataToAdd) {
		this.data= dataToAdd;
		parent=null;
		left = null;
		right = null;
		down = null;
		
	}

	
	
	public Tree getDown() {
		return down;
	}


	
	public void setDown(Tree down) {
		this.down = down;
	}


	
	public Tree getRight() {
		return right;
	}


	
	public void setRight(Tree right) {
		this.right = right;
	}


	
	public Tree getLeft() {
		return left;
	}


	
	public void setLeft(Tree left) {
		this.left = left;
	}


	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
	}
   
	
	
}
