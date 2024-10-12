import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Queue;
import java.util.LinkedList;

public class Main {
	final int[][] DELTA = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
	
	private class Block {
		int type;
		
		public Block(int type) {
			this.type = type;
		}
		
		public boolean isBlack() {
			return type == -1;
		}
		
		public boolean isRainbow() {
			return type == 0;
		}
		
		public int getType() {
			return type;
		}
	}
	
	private class GameBoard {
		private int size;
		private Block[][] blocks;
		
		public GameBoard(int size, int[][] rawBlockTypes) {
			this.size = size;
			
			blocks = new Block[size][size];
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					blocks[i][j] = new Block(rawBlockTypes[i][j]);
				}
			}
		}
		
		public Position findBiggestBlockGroup() {
			Queue<Position> queue = new LinkedList<> ();
			boolean[][] checked = new boolean[size][size];
			int maxGroupSize = 0;
			int maxRainbowBlockCount = 0;
			Position biggestBlockGroupPosition = null;
			
			
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					Block b = blocks[i][j];
					if (b == null || b.isBlack() || b.isRainbow()) {
						continue;
					}
					
					
					boolean[][] rainbowChecked = new boolean[size][size];
					queue.add(new Position(i, j));
					
					int groupSize = 0;
					int rainbowBlockCount = 0;
					Position representativeBlockPosition = null;
					int blockType = b.getType();
					
					while (!queue.isEmpty()) {
						Position cur = queue.poll();
						int x = cur.x, y = cur.y;
						
						if (checked[x][y] || rainbowChecked[x][y]) {
							continue;
						}
						
						
						Block block = blocks[x][y];
						if (block.isRainbow()) {
							rainbowChecked[x][y] = true;
							rainbowBlockCount++;
						}
						else {
							checked[x][y] = true;
						}
						groupSize++;
						
						if (!block.isRainbow()) {
							if (
								representativeBlockPosition == null ||
								representativeBlockPosition.x > cur.x ||
								(representativeBlockPosition.x == cur.x && representativeBlockPosition.y > cur.y)
							) {
								representativeBlockPosition = cur;
							}
						}
						
						
						for (int[] d: DELTA) {
							int adjacentX = x + d[0];
							int adjacentY = y + d[1];
							
							if (adjacentX < 0 || adjacentX >= size || adjacentY < 0 || adjacentY >= size) {
								continue;
							}
							
							Block adjacentBlock = blocks[adjacentX][adjacentY];
							if (adjacentBlock == null || adjacentBlock.isBlack()) {
								continue;
							}
							
							
							if (adjacentBlock.isRainbow() || adjacentBlock.getType() == blockType) {
								queue.add(new Position(adjacentX, adjacentY));
							}							
							
						}
					}
					
					
					if (groupSize > 1) {
						if (groupSize > maxGroupSize) {
							biggestBlockGroupPosition = representativeBlockPosition;
							maxGroupSize = groupSize;
							maxRainbowBlockCount = rainbowBlockCount;
						}
						else if (groupSize == maxGroupSize) {
							if (maxRainbowBlockCount < rainbowBlockCount) {
								biggestBlockGroupPosition = representativeBlockPosition;
								maxGroupSize = groupSize;
								maxRainbowBlockCount = rainbowBlockCount;
							}
							else if (maxRainbowBlockCount == rainbowBlockCount) {
								if (representativeBlockPosition.x > biggestBlockGroupPosition.x) {
									biggestBlockGroupPosition = representativeBlockPosition;
									maxGroupSize = groupSize;
									maxRainbowBlockCount = rainbowBlockCount;
								}
								else if (representativeBlockPosition.x == biggestBlockGroupPosition.x) {
									if (representativeBlockPosition.y > biggestBlockGroupPosition.y) {
										biggestBlockGroupPosition = representativeBlockPosition;
										maxGroupSize = groupSize;
										maxRainbowBlockCount = rainbowBlockCount;
									}
								}
							}
							
						}
					}
				}
			}
			
			
			return biggestBlockGroupPosition;
		}
		
		public int deleteBlockGroup(Position p) {
			Queue<Position> queue = new LinkedList<> ();
			int deletedBlockCount = 0;
			
			Block b = blocks[p.x][p.y];
			int blockType = b.getType();
			
			
			queue.add(p);
			while (!queue.isEmpty()) {
				Position cur = queue.poll();
				int x = cur.x, y = cur.y;
				
				if (blocks[x][y] == null) {
					continue;
				}
				
				
				blocks[x][y] = null;
				deletedBlockCount++;
				
				
				for (int[] d: DELTA) {
					int adjacentX = x + d[0];
					int adjacentY = y + d[1];
					
					if (adjacentX < 0 || adjacentX >= size || adjacentY < 0 || adjacentY >= size) {
						continue;
					}
					
					Block adjacentBlock = blocks[adjacentX][adjacentY];
					if (adjacentBlock == null || adjacentBlock.isBlack()) {
						continue;
					}
					
					
					if (adjacentBlock.isRainbow() || adjacentBlock.getType() == blockType) {
						queue.add(new Position(adjacentX, adjacentY));
					}
				}
			}
			
			
			return deletedBlockCount;
		}
		
		public void applyGravity() {
			for (int j = 0; j < size; j++) {
				int floorIdx = size;
				for (int i = size - 1; i >= 0; i--) {
					Block b = blocks[i][j];
					if (b == null) {
						continue;
					}
					
					if (b.isBlack()) {
						floorIdx = i;
						continue;
					}
					
					
					blocks[--floorIdx][j] = blocks[i][j];
					if (floorIdx != i) {
						blocks[i][j] = null;
					}
				}
			}
		}
		
		public void rotate() {
			Block[][] temp = new Block[size][size];
			
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					temp[size - 1 - j][i] = blocks[i][j];
				}
			}
			
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					blocks[i][j] = temp[i][j];
				}
			}
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					Block b = blocks[i][j];
					
					String s= "-";
					if (b != null) {
						if (b.isBlack()) {
							s = "B";
						}
						else if (b.isRainbow()) {
							s = "*";
						}
						else {
							s = Integer.toString(b.getType());
						}
					}
					
					sb.append(s + " ");
				}
				sb.append("\n");
			}
			
			return sb.toString();
		}
	}
	
	private class Position {
		public int x, y;
		
		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	
	private int size;
	private int[][] rawBlockTypes;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		Main main = new Main();
		
		main.getInputs();
		
		System.out.println(main.playGame());
	}

	private void getInputs() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String line = br.readLine();
		String[] tokens = line.split(" ");
		
		size = Integer.parseInt(tokens[0]);
		
		rawBlockTypes = new int[size][size];
		for (int i = 0; i < size; i++) {
			line = br.readLine();
			tokens = line.split(" ");
			
			for (int j = 0; j < size; j++) {
				rawBlockTypes[i][j] = Integer.parseInt(tokens[j]);
			}
		}
	}
	
	private int playGame() {
		int scores = 0;
		GameBoard board = new GameBoard(size, rawBlockTypes);
		
		for (Position p = board.findBiggestBlockGroup(); p != null; p = board.findBiggestBlockGroup()) {
//			System.out.println("-----------");
//			System.out.println(board);
//			System.out.println("biggest p: (" + p.x + ", " + p.y + ")");
			
			int deletedBlockCount = board.deleteBlockGroup(p);
//			System.out.println("deleted count: " + deletedBlockCount);
//			System.out.println(board);
			
			scores += Math.pow(deletedBlockCount, 2);
			
			board.applyGravity();
//			System.out.println("after apply gravity:");
//			System.out.println(board);
			
			board.rotate();
//			System.out.println("after rotate:");
//			System.out.println(board);
			
			board.applyGravity();
//			System.out.println("after apply gravity:");
//			System.out.println(board);
		}
		
		return scores;
	}
}
