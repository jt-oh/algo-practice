import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Main{
	private enum Direction {
		UP, DOWN, RIGHT, LEFT;
	}
	
	private class Shark {
		private int speed;
		private Direction dir;
		private int size;
		
		public Shark(int speed, Direction dir, int size) {
			this.speed = speed;
			this.dir = dir;
			this.size = size;
		}
		
		public int getSpeed() {
			return speed;
		}
		
		public int getSize() {
			return size;
		}
		
		public Direction getDirection() {
			return dir;
		}
		
		public void changeDirection() {
			switch (dir) {
			case UP:
				dir = Direction.DOWN;
				break;
			case DOWN:
				dir = Direction.UP;
				break;
			case RIGHT:
				dir = Direction.LEFT;
				break;
			case LEFT:
				dir = Direction.RIGHT;
				break;
			}
		}
	}
	
	private class FishTank {
		private int r, c;
		private Shark[][] positions;
		
		public FishTank(int r, int c, int[][] sharkInfos) {
			this.r = r;
			this.c = c;
			
			positions = new Shark[r][c];
			for (int[] shark: sharkInfos) {
				Direction dir;
				switch (shark[3]) {
				case 1:
					dir = Direction.UP;
					break;
				case 2:
					dir = Direction.DOWN;
					break;
				case 3:
					dir = Direction.RIGHT;
					break;
				case 4:
					dir = Direction.LEFT;
					break;
				default:
					throw new RuntimeException("invalid dir");	
				}
				
				positions[shark[0] - 1][shark[1] - 1] = new Shark(shark[2], dir, shark[4]);
			}
		}
		
		public Shark catchNearestShark(int colIdx) {
			for (int i = 0; i < r; i++) {
				Shark shark = positions[i][colIdx];
				if (shark != null) {
					positions[i][colIdx] = null;
					return shark;
				}
				
			}
			
			return null;
		}
		
		public void moveSharks() {
			Shark[][] tempPositions = new Shark[r][c];
			
			for (int i = 0; i < r; i++) {
				for (int j = 0; j < c; j++) {
					Shark shark = positions[i][j];
					if (shark == null) {
						continue;
					}
					
					
//					System.out.println("shark size: " + shark.getSize());
					
					int distanceFromRecentWall;
					int distanceBetweenWall;
					switch (shark.getDirection()) {
					case UP:
						distanceFromRecentWall = r - 1 - i;
						distanceBetweenWall = r - 1;
						break;
					case DOWN:
						distanceFromRecentWall = i;
						distanceBetweenWall = r - 1;
						break;
					case RIGHT:
						distanceFromRecentWall = j;
						distanceBetweenWall = c - 1;
						break;
					case LEFT:
						distanceFromRecentWall = c - 1 -j;
						distanceBetweenWall = c - 1;
						break;
					default:
						throw new RuntimeException("invalid dir");
					}
					
					int movingDistance = distanceFromRecentWall + shark.getSpeed();
//					System.out.println("remained moving distance: " + movingDistance + ", dir: " + shark.getDirection());
					while (movingDistance > distanceBetweenWall) {
						movingDistance -= distanceBetweenWall;
						shark.changeDirection();
//						System.out.println("remained moving distance: " + movingDistance + ", dir: " + shark.getDirection());
					}
				
						
					int newX;
					int newY;
					switch (shark.getDirection()) {
					case UP:
						newX = r - 1 - movingDistance;
						newY = j;
						break;
					case DOWN:
						newX = movingDistance;
						newY = j;
						break;
					case RIGHT:
						newX = i;
						newY = movingDistance;
						break;
					case LEFT:
						newX = i;
						newY = c - 1 - movingDistance;
						break;
					default:
						throw new RuntimeException("invalid dir");
					}
					
//					System.out.println("new: (" + newX + ", " + newY + ")");
					
					
					Shark overlappedShark = tempPositions[newX][newY];
					if (overlappedShark == null) {
						tempPositions[newX][newY] = shark;
					}
					else {
						if (overlappedShark.getSize() < shark.getSize()) {
							tempPositions[newX][newY] = shark;
						}
					}
				}
			}
			
			for (int i = 0; i < r; i++) {
				for (int j = 0; j < c; j++) {
					positions[i][j] = tempPositions[i][j];
				}
			}
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < r; i++) {
				for (int j = 0; j < c; j++) {
					Shark s = positions[i][j];
					
					if (s != null) {
						sb.append(s.getSize() + " ");
					}
					else {
						sb.append("X ");
					}
				}
				
				sb.append("\n");
			}
			
			return sb.toString();
		}
	}
	
	
	private int r, c;
	private int[][] rawSharks;
	
	public static void main(String[] argv) throws IOException {
		Main main = new Main();
		
		main.getInputs();
		
		System.out.println(main.fish());
	}
	
	private void getInputs() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String line = br.readLine();
		String[] tokens = line.split(" ");
		
		r = Integer.parseInt(tokens[0]);
		c = Integer.parseInt(tokens[1]);
		int sharkCount = Integer.parseInt(tokens[2]);
		
		rawSharks = new int[sharkCount][5];
		for (int i = 0; i < sharkCount; i++) {
			line = br.readLine();
			tokens = line.split(" ");
			
			for (int j = 0; j < 5; j++) {
				rawSharks[i][j] = Integer.parseInt(tokens[j]);
			}
		}
		
		
		br.close();
	}
	
	private int fish() {
		int totalSharkSizes = 0;
		FishTank tank = new FishTank(r, c, rawSharks);
		
		for (int fisherIdx = 0; fisherIdx < c; fisherIdx++) {
//			System.out.println("--------- " + fisherIdx + " ---------");
//			System.out.println(tank);
			Shark shark = tank.catchNearestShark(fisherIdx);
			if (shark != null) {
				totalSharkSizes += shark.getSize();
			}
//			System.out.println("sharkSize: " + (shark != null ? shark.getSize() : "X"));
//			System.out.println(tank);
			
			tank.moveSharks();
//			System.out.println("after moving sharks");
//			System.out.println(tank);
		}
		
		
		return totalSharkSizes;
	}
}