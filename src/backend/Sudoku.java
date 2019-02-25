
package backend;

import frontend.GameScreen;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JTable;

public class Sudoku
{
    // determine the validity of a move
    public static boolean isValidMove(int[][] arr, int num, int gridIndex, int cellIndex)
    {
        // inner grid check
        for (int x = 0; x < 9; x++)
        {   
            if (arr[gridIndex][x] == 0 || x == cellIndex)
            {
                continue;
            }
            
            if (arr[gridIndex][x] == num)
            {
                return false;
            }
        }
        
        // horizontal check
        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 3; y++)
            {
                if ((gridIndex / 3 * 3 + x == gridIndex &&
                        cellIndex / 3 * 3 + y == cellIndex) ||
                        arr[gridIndex / 3 * 3 + x][cellIndex / 3 * 3 + y] == 0)
                {
                    continue;
                }
                
                if (arr[gridIndex / 3 * 3 + x][cellIndex / 3 * 3 + y] == num)
                {
                    return false;
                }
            }
        }
        
        // vertical check
        for (int x = gridIndex % 3; x < 9; x += 3)
        {
            for (int y = cellIndex % 3; y < 9; y += 3)
            {
                if ((x == gridIndex && y == cellIndex) || arr[x][y] == 0)
                {
                    continue;
                }
                
                if (arr[x][y] == num)
                {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    // fill a 9x9 array with valid sequence of integers
    public static boolean generate(int[][] arr)
    {
        for (int x = 0; x < 9; x++)
        {
            for (int y = 0; y < 9; y++)
            {
                // skip cell if not empty
                if (arr[x][y] != 0)
                {
                    continue;
                }
                
                for (int z = 1; z <= 9; z++)
                {
                    if (isValidMove(arr, z, x, y))
                    {
                        arr[x][y] = z;
                        
                        if (generate(arr))
                        {
                            return true; // if no cell is empty, return true
                        }
                        else
                        {
                            arr[x][y] = 0; // clear cell if no valid integer found
                        }
                    }
                }
                
                return false;
            }
        }
        
        return true;
    }
    
    // shuffle the board for a new problem
    public static void shuffle(int[][] arr)
    {
        // cell row shuffling
        for (int x = 0; x < 9; x += 3)
        {
            for (int y = 0; y < rand.nextInt(5); y++)
            {
                for (int z = 0; z < 3; z++)
                {
                    for (int w = 0; w < 3; w++)
                    {
                        int temp = arr[z + x][w];
                        arr[z + x][w] = arr[z + x][w + 3];
                        arr[z + x][w + 3] = arr[z + x][w + 6];
                        arr[z + x][w + 6] = temp;
                    }
                }
            }
        }
        
        // cell column shuffling
        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < rand.nextInt(5); y++)
            {
                for (int z = 0; z < 9; z += 3)
                {
                    for (int w = 0; w < 9; w += 3)
                    {
                        int temp = arr[z + x][w];
                        arr[z + x][w] = arr[z + x][w + 1];
                        arr[z + x][w + 1] = arr[z + x][w + 2];
                        arr[z + x][w + 2] = temp;
                    }
                }
            }
        }

        // grid shuffling
        for (int x = 0; x < 3; x++)
        {
            int[] temp;
            
            // row shift
            int rn1 = rand.nextInt(3) * 3;
            int rn2 = rand.nextInt(3) * 3;
            
            for (int y = 0; y < 3; y++)
            {
                temp = arr[rn1 + y];
                arr[rn1 + y] = arr[rn2 + y];
                arr[rn2 + y] = temp;
            }
            
            // column shift
            rn1 = rand.nextInt(3);
            rn2 = rand.nextInt(3);
            
            for (int y = 0; y < 9; y += 3)
            {
                temp = arr[rn1 + y];
                arr[rn1 + y] = arr[rn2 + y];
                arr[rn2 + y] = temp;
            }
        }
    }
    
    // generate problem from a filled board
    public static void generateBoard(int[][] arr, int given)
    {
        for (int x = 0; x < (81 - given); x++)
        {
            int rn1, rn2;
            
            do
            {
                rn1 = rand.nextInt(9);
                rn2 = rand.nextInt(9);
            }
            while (arr[rn1][rn2] == 0);
            
            arr[rn1][rn2] = 0;
        }
    }
    
    public static void generateEditableCell(boolean[][] arr1, int[][] arr2)
    {
        for (int x = 0; x < 9; x++)
        {
            for (int y = 0; y < 9; y++)
            {
                int val = arr2[((x / 3) * 3) + (y / 3)][((x % 3) * 3) + (y % 3)];
                
                arr1[x][y] = (val == 0);
            }
        }
    }
    
    // display a 9x9 array as a board in the output window
    public static void display(int[][] arr)
    {
        for (int x = 0; x < 9; x += 3)
        {
            for (int y = 0; y < 9; y += 3)
            {
                for (int z = 0; z < 3; z++)
                {
                    for (int w = 0; w < 3; w++)
                    {
                        System.out.print(arr[x + z][y + w] + " ");
                    }
                    
                    System.out.print("| ");
                }
                
                System.out.println();
            }
            
            System.out.println("-----------------------");
        }
    }
    
    public static void populate(JTable table, int[][] arr)
    {   
        for (int x = 0; x < 9; x++)
        {
            for (int y = 0; y < 9; y++)
            {
                int val = arr[((x / 3) * 3) + (y / 3)][((x % 3) * 3) + (y % 3)];
                
                if (val != 0)
                {
                    table.getModel().setValueAt(val, x, y);
                }
                else
                {
                    table.getModel().setValueAt("", x, y);
                }
            }
        }
    }
    
    public static void validate(JTable table, GameScreen object)
    {
        Timer timer = new Timer();
        int[][] arr = new int[9][9];
        
        for (int x = 0; x < 9; x++)
        {
            for (int y = 0; y < 9; y++)
            {
                arr[((x / 3) * 3) + (y / 3)][((x % 3) * 3) + (y % 3)] = Integer.parseInt(table.getModel().getValueAt(x, y).toString());
            }
        }
        
        table.requestFocus();
        
        timer.scheduleAtFixedRate(new TimerTask(){
            
            private int row = 0;
            private int column = 0;
            private boolean valid = true;
            
            @Override
            public void run()
            {
                table.changeSelection(row, column, false, false);
                
                if (!isValidMove(arr, arr[row][column], row, column))
                {
                    valid = false;
                    
                    // TO-DO: highlight cell with red border
                }
                
                if (row > 7 && column > 7)
                {
                    timer.cancel();
                    
                    if (valid)
                    {
                        object.popUp(true, object.timeSec - 1, object.diff);
                    }
                    else
                    {
                        object.popUp(false, object.timeSec - 1, object.diff);
                    }
                }
                else if (column > 7)
                {
                    ++row;
                    column = 0;
                }
                else
                {
                    ++column;
                }
            }
        }, 0, 50);
    }
    
    public static boolean isFull(JTable table)
    {
        boolean valid = true;
        
        for (int x = 0; x < 9; x++)
        {
            for (int y = 0; y < 9; y++)
            {
                if (table.getModel().getValueAt(x, y) == null)
                {
                    valid = false;
                }
            }
        }
        
        return valid;
    }
    
    public static String formatTime(int sec)
    {
        int mm = 0;
        int ss = sec;
        
        while (ss >= 60)
        {
            ++mm;
            ss -= 60;
        }
        
        return String.format("%02d:%02d", mm, ss);
    }
    
    public static void fetchData(ArrayList<HighscoreEntry> list)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("highscores.txt"));
            String str;
         
            while ((str = reader.readLine()) != null)
            {
                HighscoreEntry obj = new HighscoreEntry();
                obj.name = str;
                obj.difficulty = reader.readLine();
                obj.time = Integer.parseInt(reader.readLine());
                obj.priorityLevel = (obj.difficulty.equalsIgnoreCase("Easy")) ? 1 : (obj.difficulty.equalsIgnoreCase("Intermediate")) ? 2 : (obj.difficulty.equalsIgnoreCase("Difficult")) ? 3 : 4;
                list.add(obj);
            }
            
            reader.close();
        }
        catch (IOException ex)
        {
            System.out.println("Exception Thrown: " + ex.getMessage());
        }
    }
    
    public static void sortDataOne(ArrayList<HighscoreEntry> list)
    {
        boolean check = false;
        
        for (int i = 0; i < list.size() - 1; i++)
        {
            if (list.get(i).priorityLevel < list.get(i + 1).priorityLevel)
            {
                list.add(i, list.get(i + 1));
                list.remove(i + 2);
                
                check = true;
            }
        }
        
        if (check)
        {
            sortDataOne(list);
        }
    }
    
    public static void sortDataTwo(ArrayList<HighscoreEntry> list)
    {
        boolean check = false;
        
        for (int i = 0; i < list.size() - 1; i++)
        {
            if ((list.get(i).difficulty.equalsIgnoreCase(list.get(i + 1).difficulty)) && (list.get(i).time > list.get(i + 1).time))
            {
                list.add(i, list.get(i + 1));
                list.remove(i + 2);
                
                check = true;
            }
        }
        
        if (check)
        {
            sortDataTwo(list);
        }
    }
    
    public static void startSound(boolean swtch)
    {
        try
        {
            clip = AudioSystem.getClip();
            
            if (swtch)
            {
                clip.open(AudioSystem.getAudioInputStream(Sudoku.class.getResource("/resources/theme.wav")));
            }
            else
            {
                clip.open(AudioSystem.getAudioInputStream(Sudoku.class.getResource("/resources/clock.wav")));
            }
            
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch (Exception ex)
        {
            System.out.println("Exception Thrown: " + ex.getMessage());
        }
    }
    
    public static void stopSound()
    {
        clip.stop();
    }
    
    private static final Random rand = new Random();
    private static Clip clip;
}
