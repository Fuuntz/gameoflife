/***************************************************************
* file: GameOfLife.java
* author: Tyler Fuentes
* class: CS 1400 – Intro Programming Prob Solving
*
* assignment: program 6
* date last modified: 12/9/2024
*
* purpose: Implements Conway's Game of Life. Takes input file 
* (first generation) and displays a given number of generations
* based on the rules of Conway's Game of Life
* 
*
****************************************************************/

import java.util.Scanner;
import java.io.*;

/**
 * Implements Conway's Game of Life
 */
public class GameOfLife {
	private char[][] board;
	private int numGenerations;
	
	
	// method: main
	// purpose: creates a new GameOfLife object (starts/runs the program)
	public static void main(String[] args) {
		try {
			new GameOfLife();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	// method: constructor
	// purpose: initializes new game board by prompting the user for file name and loading game board data from file
	public GameOfLife() throws Exception{
		Scanner scnr = new Scanner(System.in);
		
		System.out.print("Please enter a valid file name: ");
		File file = new File(scnr.nextLine());
		Scanner fileScnr = new Scanner(file);
		
		int numRows = fileScnr.nextInt();
		int numColumns = fileScnr.nextInt();
		fileScnr.nextLine();
		board = new char[numRows][numColumns];
		
		for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
			String currRow = fileScnr.nextLine();
			for (int columnIndex = 0, strIndex = 0; columnIndex < numColumns; columnIndex++, strIndex += 2) {
				board[rowIndex][columnIndex] = currRow.charAt(strIndex);
			}
		}
		
		System.out.print("How many generations to compute: ");
		numGenerations = scnr.nextInt();
		this.computeNextGeneration(numGenerations);

		scnr.close();
		fileScnr.close();
	}
	
	
	// method: getColumns
	// purpose: returns the number of columns in the game board
	public int getColumns() {
		return board[0].length;
	}
	
	// method: getRows
	// purpose: returns the number of rows in the game board
	public int getRows() {
		return board.length;
	}
	
	// method: getCell
	// purpose: get the value of the cell at given column and row
	public int getCell(int column, int row) {		
		if (row >= board.length || row < 0) {
			return 0;
		}
		
		if (column >= board[0].length || column < 0) {
			return 0;
		}
		
		return board[row][column] - '0';
	}
	
	// method: setCell
	// purpose: set the value of the cell at given column and row
	public void setCell(int column, int row, int value) {
		if (value == 0) {
			board[row][column] = '0';
		}
		
		if (value == 1) {
			board[row][column] = '1';
		}
	}
	
	// method: computeNextGeneration
	// purpose: compute the next generation, as determined by the Rules of Life
	public void computeNextGeneration(int generation) {
		if (generation == 0) {
			return;
		}
		
		System.out.println();
		System.out.println("Generation " + (1 + numGenerations - generation));
		this.print();
		
		char[][] tempBoard = copyBoard(board);
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				int livingNeighbors = countLivingNeighbors(board, i, j);
				
				// if this cell is alive
				if (board[i][j] == '1') {
					// if less than 2 or more than 3 living neighbors, it dies
					if (livingNeighbors < 2 || livingNeighbors > 3) {
						tempBoard[i][j] = '0';
					}
					
					// else, it has 2 or 3 living neighbors, and stays alive
				}
				
				// if this cell is dead
				if (board[i][j] == '0') {
					// if 3 living neighbors, it comes to life
					if (livingNeighbors == 3) {
						tempBoard[i][j] = '1';
					}
				}
			}
		}
		
		updateBoard(board, tempBoard);
		computeNextGeneration(generation - 1);
	}
	
	// method: checkNeighbor
	// purpose: returns the value at a given cell in the board
	private int checkNeighbor(char[][] arr, int rIndex, int cIndex) {
		try {
			return arr[rIndex][cIndex] - '0';
		}
		catch (IndexOutOfBoundsException e) {
			return 0;
		}
	}
	
	// method: countLivingNeighbors
	// purpose: returns the number of living neighbors of a given cell
	private int countLivingNeighbors(char[][] array, int rowIndex, int columnIndex) {
		
		int livingNeighbors = 
			checkNeighbor(array, rowIndex - 1, columnIndex - 1) +  // top left
			checkNeighbor(array, rowIndex - 1, columnIndex    ) +  // top middle
			checkNeighbor(array, rowIndex - 1, columnIndex + 1) +  // top right
			checkNeighbor(array, rowIndex    , columnIndex - 1) +  // middle left
			checkNeighbor(array, rowIndex    , columnIndex + 1) +  // middle right
			checkNeighbor(array, rowIndex + 1, columnIndex - 1) +  // bottom left
			checkNeighbor(array, rowIndex + 1, columnIndex    ) +  // bottom middle
			checkNeighbor(array, rowIndex + 1, columnIndex + 1) ;  // bottom right
		
		return livingNeighbors;
	}
	
	// method: copyBoard
	// purpose: returns a copy of the board
	private char[][] copyBoard(char[][] original) {
		char[][] copy = new char[original.length][original[0].length];
		for (int i = 0; i < original.length; i++) {
			for (int j = 0; j < original[i].length; j++) {
				copy[i][j] = original[i][j]; 
			}
		}
		
		return copy;
	}
	
	// method: updateBoard
	// purpose: updates the board with new/updated values 
	private void updateBoard(char[][] original, char[][] update) {
		for (int i = 0; i < original.length; i++) {
			for (int j = 0; j < original[i].length; j++) {
				// only consider values that have actually changed
				if (original[i][j] != update[i][j]) {
					original[i][j] = update[i][j]; 
				}
			}
		}
	}
	
	// method: print
	// purpose: prints out the board to the console.
	public void print() {
		System.out.println();
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}
}