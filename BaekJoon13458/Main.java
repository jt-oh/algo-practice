import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
// import java.util.Scanner;

public class Main {
    BufferedReader br;
    // private BufferedReader scanner;
    private int roomCount;
    private int[] roomCapacities;
    private int primaryViewerAbility, assistantViewerAbility;


    public static void main(String... args) {
        Main mainObject = new Main();

        mainObject.initializeResources();

        try {
            mainObject.getInputs();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(mainObject.getMinViewerCount());

        try {
            mainObject.releaseResources();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initializeResources() {
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    private void releaseResources() throws IOException {
        br.close();
    }

    private void getInputs() throws IOException {
        String line;
        String[] tokens;
        
        line = br.readLine();
        roomCount = Integer.parseInt(line);
        roomCapacities = new int[roomCount];

        line = br.readLine();
        tokens = line.split(" ");
        for (int i = 0; i < roomCount; i++) {
            roomCapacities[i] = Integer.parseInt(tokens[i]);
        }

        line = br.readLine();
        tokens = line.split(" ");
        primaryViewerAbility = Integer.parseInt(tokens[0]);
        assistantViewerAbility = Integer.parseInt(tokens[1]);
    }

    private long getMinViewerCount() {
        long minResult = 0;

        for (int i = 0; i < roomCount; i++) {
            int stuCount = roomCapacities[i];

            minResult++;
            stuCount -= primaryViewerAbility;

            if (stuCount > 0) {
                minResult += stuCount / assistantViewerAbility;
                if (stuCount % assistantViewerAbility != 0) {
                    minResult++;
                }
            }
        }


        return minResult;
    }
}