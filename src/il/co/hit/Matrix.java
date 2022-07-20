package il.co.hit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Matrix implements Serializable {
	private static final long serialVersionUID = 1L;
	int[][] matrix;

	/**
	 * Populates the 2D array with 0's and 1's
	 * 
	 * @param numOfRows
	 * @param numOfColumns
	 */
	public Matrix(int numOfRows, int numOfColumns) {
		System.out.println("Calling class= " + getClass().getName());
		this.matrix = new int[numOfRows][numOfColumns];
		Random generator = new Random();
		for (int row = 0; row < numOfRows; row++) {
			for (int column = 0; column < numOfColumns; column++) {
				this.matrix[row][column] = generator.nextInt(2);
			}
		}
	}

	public Matrix(int[][] anArray) {
		System.out.println("Calling class= " + getClass().getName());
		List<int[]> rowList = new ArrayList<>();
		for (int[] row : anArray) {
			int[] clone = row.clone();
			rowList.add(clone);
		}
		this.matrix = rowList.toArray(new int[0][]);
	}

	public int getValue(Index index) {
		return this.matrix[index.getRow()][index.getColumn()];
	}

	public String toString() {
		StringBuilder matrixBuilder = new StringBuilder();
		for (int[] row : this.matrix) {
			matrixBuilder.append(Arrays.toString(row));
			matrixBuilder.append("\n");
		}
		return matrixBuilder.toString();
	}

	public Collection<Index> getNeighbors(final Index index) {
		Collection<Index> list = new ArrayList<>();
		try { // S
			list.add(new Index(index.getRow() + 1, index.getColumn()));
		} catch (ArrayIndexOutOfBoundsException ignored) {}
		try { // E
			list.add(new Index(index.getRow(), index.getColumn() + 1));
		} catch (ArrayIndexOutOfBoundsException ignored) {}
		try { // N
			list.add(new Index(index.getRow() - 1, index.getColumn()));
		} catch (ArrayIndexOutOfBoundsException ignored) {}
		try { // W
			list.add(new Index(index.getRow(), index.getColumn() - 1));
		} catch (ArrayIndexOutOfBoundsException ignored) {}

		try {
			// NE
			list.add(new Index(index.getRow() + 1, index.getColumn() + 1));
		} catch (ArrayIndexOutOfBoundsException ignored) {}
		try {
			// NW
			list.add(new Index(index.getRow() - 1, index.getColumn() - 1));
		} catch (ArrayIndexOutOfBoundsException ignored) {}
		try {
			// SW
			list.add(new Index(index.getRow() + 1, index.getColumn() - 1));
		} catch (ArrayIndexOutOfBoundsException ignored) {}
		try {
			// SE
			list.add(new Index(index.getRow() - 1, index.getColumn() + 1));
		} catch (ArrayIndexOutOfBoundsException ignored) {}
		return list;
	}
	
	public void printMatrix(){
        for (int[] row : this.matrix) {
            String s = Arrays.toString(row);
            System.out.println(s);
        }
    }

    public final int[][] getMatrix() {
        return this.matrix;
    }
    
    public List<Index> findOnes() {
        List<Index> ones = new ArrayList<>();
        Index index;
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[i].length; j++) {
                if (this.matrix[i][j] == 1) {
                    index = new Index(i, j);
                    ones.add(index);
                }
            }
        }
        return ones;
    }
}