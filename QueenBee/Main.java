import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class Main {
	private int size;
	private int[][] cmds;
	private int[] larvaSizes;
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Main main = new Main();
		
		main.getInputs();
		
		main.initializeLarvaSizes();
		main.growLarvae();
		
		main.printLarvaSizes();
	}

	
	private void getInputs() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String line = br.readLine();
		String[] tokens = line.split(" ");
		
		size = Integer.parseInt(tokens[0]);
		int cmdCount = Integer.parseInt(tokens[1]);
		
		
		cmds = new int[cmdCount][3];
		for (int i = 0; i < cmdCount; i++) {
			line = br.readLine();
			tokens = line.split(" ");
			
			for (int j = 0; j < 3; j++) {
				cmds[i][j] = Integer.parseInt(tokens[j]);
			}
		}
		
		br.close();
	}
	
	private void initializeLarvaSizes() {
		int larvaSizeCount = getLarvaSizeCount();
		larvaSizes = new int[larvaSizeCount];
		
		for (int i = 0; i < larvaSizeCount; i++) {
			larvaSizes[i] = 1;
		}
	}
	
	private int getLarvaSizeCount() {
		return 2 * size - 1;
	}
	
	private void growLarvae() {
		int larvaSizeCount = getLarvaSizeCount();
		
		int cmdIdx = 0;
		for (int[] cmd: cmds) {
			for (int i = 0; i < size; i++) {
				int growSize = getNextGrowSizeFromCmd(cmdIdx);
				larvaSizes[i] += growSize;
			}
			for (int i = size; i < larvaSizeCount; i++) {
				int growSize = getNextGrowSizeFromCmd(cmdIdx);
				larvaSizes[i] += growSize;
			}
			
			
//			System.out.println("--------");
//			System.out.println("2: (" + twoXIdx + ", " + twoYIdx + ")");
//			System.out.println("1: (" + oneXIdx + ", " + oneYIdx + ")");
			
			cmdIdx++;
		}
	}
	
	private int getNextGrowSizeFromCmd(int cmdIdx) {
		if (cmds[cmdIdx][0] != 0) {
			cmds[cmdIdx][0]--;
			return 0;
		}
		
		if (cmds[cmdIdx][1] != 0) {
			cmds[cmdIdx][1]--;
			return 1;
		}
		
		cmds[cmdIdx][2]--;
		return 2;
	}
	
	private void printLarvaSizes() throws IOException {
		BufferedWriter br = new BufferedWriter(new OutputStreamWriter(System.out));
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (j == 0) {
					sb.append(larvaSizes[size - i - 1]);
				}
				else {
					sb.append(larvaSizes[size + j - 1]);
				}
				
				
				if (j != size - 1) {
					sb.append(" ");
				}
			}
			
			if (i != size - 1) {
				sb.append("\n");
			}
		}
		
		br.write(sb.toString() + "\n");
		br.flush();
		
		br.close();
	}
}
