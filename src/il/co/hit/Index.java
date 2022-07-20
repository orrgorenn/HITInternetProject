package il.co.hit;

import java.io.Serializable;
import java.util.Objects;

/**
 * Index class represents a (row, column) location inside matrix
 * @author Orr.G & Or.S
 *
 */
public class Index implements Serializable {
	private static final long serialVersionUID = 1L;
	private int r, c;
	
	public Index(int r, int c) {
		if(r < 0 || c < 0) {
			throw new IllegalArgumentException("Row | Column cannot be negative.");
		}
		this.r = r;
		this.c = c;
	}
	
	/**
	 * @return current index row
	 */
	public int getRow() {
		return this.r;
	}
	
	/**
	 * @return current index column
	 */
	public int getColumn() {
		return this.c;
	}
	
	/**
	 * @param: Object o - represent a potential index object
	 */
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || o.getClass() != this.getClass()) return false;
		Index i = (Index) o;
		return this.r == i.getRow() && this.c == i.getColumn(); 
	}
	
	/**
	 * @return hash of an index object as an integer
	 */
	@Override
    public int hashCode() {
        return Objects.hash(this.r, this.c);
    }
	
	/**
	 * @return return a string representing an index as follows (row, column)
	 */
	@Override
    public String toString(){
        return "(" + this.r + ", " + this.c + ")";
    }
}