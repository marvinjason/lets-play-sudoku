
package frontend;

import backend.HighscoreEntry;
import backend.Sudoku;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Dialog extends JPanel implements ActionListener
{
    public Dialog(boolean arg0, int arg1, String arg2, Main main, JFrame frame, JPanel titleScreen, GameScreen gameScreen)
    {
        timeSec = arg1;
        difficulty = arg2;
        this.main = main;
        this.frame = frame;
        this.titleScreen = titleScreen;
        this.gameScreen = gameScreen;
        
        try
        {
            background = ImageIO.read(this.getClass().getResource("/resources/dialog_bg.png"));
        }
        catch (IOException ex)
        {
            System.out.println("Exception Thrown: " + ex.getMessage());
        }
        
        this.setBounds(120, 135, 263, 232);
        this.setLayout(null);
        
        if (arg0)
        {
            win();
        }
        else
        {
            lose();
        }
    }
    
    private void win()
    {
        winLabel1 = new JLabel("CONGRATULATIONS!");
        winLabel1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        winLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        winLabel1.setBounds(38, 20, 185, 20);
        
        winLabel2 = new JLabel("You solved the puzzle!");
        winLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        winLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        winLabel2.setBounds(50, 45, 160, 20);
        
        winLabel3 = new JLabel("Time:");
        winLabel3.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        winLabel3.setBounds(30, 90, 70, 20);
        
        winLabel4 = new JLabel("Difficulty:");
        winLabel4.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        winLabel4.setBounds(30, 120, 70, 20);
        
        winLabel5 = new JLabel(Sudoku.formatTime(timeSec));
        winLabel5.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        winLabel5.setHorizontalAlignment(SwingConstants.CENTER);
        winLabel5.setBounds(170, 90, 70, 20);
        
        winLabel6 = new JLabel(difficulty);
        winLabel6.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        winLabel6.setHorizontalAlignment(SwingConstants.CENTER);
        winLabel6.setBounds(155, 118, 100, 24);
        
        winButton = new JButton("OK");
        winButton.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        winButton.setBackground(new Color(220, 220, 220));
        winButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        winButton.setFocusPainted(false);
        winButton.setBounds(105, 180, 50, 25);
        winButton.addActionListener(this);
        
        this.add(winLabel1);
        this.add(winLabel2);
        this.add(winLabel3);
        this.add(winLabel4);
        this.add(winLabel5);
        this.add(winLabel6);
        this.add(winButton);
    }
    
    private void lose()
    {
        loseLabel1 = new JLabel("GAME OVER");
        loseLabel1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loseLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        loseLabel1.setBounds(38, 20, 185, 20);
        
        loseLabel2 = new JLabel("Better luck next time!");
        loseLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        loseLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        loseLabel2.setBounds(50, 47, 160, 20);
        
        loseButton1 = new JButton("Continue Attempt");
        loseButton1.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        loseButton1.setForeground(new Color(90, 90, 90));
        loseButton1.setBackground(new Color(230, 230, 230));
        loseButton1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        loseButton1.setFocusPainted(false);
        loseButton1.setBounds(68, 90, 125, 30);
        loseButton1.addActionListener(this);
        
        loseButton2 = new JButton("New Puzzle");
        loseButton2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        loseButton2.setForeground(new Color(90, 90, 90));
        loseButton2.setBackground(new Color(230, 230, 230));
        loseButton2.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        loseButton2.setFocusPainted(false);
        loseButton2.setBounds(68, 132, 125, 30);
        loseButton2.addActionListener(this);
        
        loseButton3 = new JButton("Back to Title");
        loseButton3.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        loseButton3.setForeground(new Color(90, 90, 90));
        loseButton3.setBackground(new Color(230, 230, 230));
        loseButton3.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        loseButton3.setFocusPainted(false);
        loseButton3.setBounds(68, 174, 125, 30);
        loseButton3.addActionListener(this);
        
        this.add(loseLabel1);
        this.add(loseLabel2);
        this.add(loseButton1);
        this.add(loseButton2);
        this.add(loseButton3);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("Exit"))
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("highscores.txt", true)));
                writer.write(highscoreField.getText());
                writer.newLine();
                writer.write(difficulty);
                writer.newLine();
                writer.write(Integer.toString(timeSec));
                writer.newLine();
                writer.close();
            }
            catch (Exception ex)
            {
                System.out.println("Exception Thrown: " + ex.getMessage());
            }
            
            highscores = new HighScores(main, frame, titleScreen);
            frame.add(highscores);
            frame.remove(gameScreen);
            frame.revalidate();
            Sudoku.startSound(true);
        }
        else if (e.getSource() == winButton)
        {
            ArrayList<HighscoreEntry> list = new ArrayList();
            Sudoku.fetchData(list);
            Sudoku.sortDataOne(list);
            Sudoku.sortDataTwo(list);
            
            int tempPriorityLevel = (difficulty.equalsIgnoreCase("Easy")) ? 1 : (difficulty.equalsIgnoreCase("Intermediate")) ? 2 : (difficulty.equalsIgnoreCase("Difficult")) ? 3 : 4;
            boolean check = false;
            
            for (int i = 0; i < 5 && i < list.size(); i++)
            {
                if (list.get(i).priorityLevel <= tempPriorityLevel)
                {
                    if (list.get(i).time >= timeSec)
                    {
                        check = true;
                    }
                }
            }
            
            if (list.size() < 5)
            {
                check = true;
            }
            
            if (check)
            {
                highscoreLabel1 = new JLabel("New High Score!");
                highscoreLabel1.setFont(new Font("Segoe UI", Font.BOLD, 18));
                highscoreLabel1.setBounds(39, 30, 185, 25);
                highscoreLabel1.setHorizontalAlignment(SwingConstants.CENTER);
                
                highscoreLabel2 = new JLabel("Name:");
                highscoreLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                highscoreLabel2.setBounds(30, 104, 70, 20);
                
                highscoreField = new JTextField();
                highscoreField.setBounds(110, 105, 110, 25);
                
                this.remove(winLabel1);
                this.remove(winLabel2);
                this.remove(winLabel3);
                this.remove(winLabel4);
                this.remove(winLabel5);
                this.remove(winLabel6);
                this.add(highscoreLabel1);
                this.add(highscoreLabel2);
                this.add(highscoreField);
                this.repaint();
                
                winButton.setActionCommand("Exit");
            }
            else
            {
                frame.remove(gameScreen);
                titleScreen.setVisible(true);
                main.initializeButtonContainer(false);
                Sudoku.startSound(true);
            }
        }
        else if (e.getSource() == loseButton1)
        {
            gameScreen.continueAttempt();
            gameScreen.remove(this);
        }
        else if (e.getSource() == loseButton2)
        {
            int diff = (difficulty.equalsIgnoreCase("Easy") ? 0 : difficulty.equalsIgnoreCase("Intermediate") ? 1 : difficulty.equalsIgnoreCase("Difficult") ? 2 : 3);
            frame.remove(gameScreen);
            frame.add(new GameScreen(main, frame, titleScreen, diff));
            Sudoku.startSound(false);
        }
        else
        {
            frame.remove(gameScreen);
            titleScreen.setVisible(true);
            main.initializeButtonContainer(false);
            Sudoku.startSound(true);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
    }
    
    private Main main;
    private JFrame frame;
    private JPanel titleScreen;
    private HighScores highscores;
    private GameScreen gameScreen;
    private Image background;
    private JLabel winLabel1;
    private JLabel winLabel2;
    private JLabel winLabel3;
    private JLabel winLabel4;
    private JLabel winLabel5;
    private JLabel winLabel6;
    private JLabel loseLabel1;
    private JLabel loseLabel2;
    private JLabel highscoreLabel1;
    private JLabel highscoreLabel2;
    private JButton winButton;
    private JButton loseButton1;
    private JButton loseButton2;
    private JButton loseButton3;
    private JTextField highscoreField;
    private int timeSec;
    private String difficulty;
}
