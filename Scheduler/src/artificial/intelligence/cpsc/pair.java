package artificial.intelligence.cpsc;

public class pair<L,R> {
	private final L left;
	private final R right;
	
	public pair(L left, R right) {
	    this.left = left;
	    this.right = right;
	  }

	  public L getLeft() { return left; }
	  public R getRight() { return right; }

	  @Override
	  public int hashCode() { return left.hashCode() ^ right.hashCode(); }

	  public boolean equals(pair<L,R> o) {
	    if (!(o instanceof pair)) return false;
	    pair<L,R> pairo = (pair<L,R>) o;
	    return this.left.equals(pairo.getLeft()) &&
	           this.right.equals(pairo.getRight());
	  }

	  public String toString(){
		  return ("Left Element: "+left.toString()+"Right Element: "+right.toString()+"\n");
	  }
	}

