
package frontend;

import backend.Sudoku;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

public class Main implements ActionListener
{
    public static void main(String[] args)
    {
        Main obj = new Main();
    }
    
    public Main()
    {
        try
        {
            logoSudoku = ImageIO.read(this.getClass().getResource("/resources/logo_sudoku.png"));
            logoLetsPlay = ImageIO.read(this.getClass().getResource("/resources/logo_letsplay.png"));
            logoGrid = ImageIO.read(this.getClass().getResource("/resources/logo_grid.png"));
            titleScreenBg = ImageIO.read(this.getClass().getResource("/resources/plain_bg.png"));
            playImg = ImageIO.read(this.getClass().getResource("/resources/play_button.png"));
            playHoverImg = ImageIO.read(this.getClass().getResource("/resources/play_button_hovered.png"));
            highscoresImg = ImageIO.read(this.getClass().getResource("/resources/highscores_button.png"));
            highscoresHoverImg = ImageIO.read(this.getClass().getResource("/resources/highscores_button_hovered.png"));
            aboutImg = ImageIO.read(this.getClass().getResource("/resources/about_button.png"));
            aboutHoverImg = ImageIO.read(this.getClass().getResource("/resources/about_button_hovered.png"));
            easyImg = ImageIO.read(this.getClass().getResource("/resources/easy_button.png"));
            easyHoverImg = ImageIO.read(this.getClass().getResource("/resources/easy_button_hovered.png"));
            intermediateImg = ImageIO.read(this.getClass().getResource("/resources/intermediate_button.png"));
            intermediateHoverImg = ImageIO.read(this.getClass().getResource("/resources/intermediate_button_hovered.png"));
            difficultImg = ImageIO.read(this.getClass().getResource("/resources/difficult_button.png"));
            difficultHoverImg = ImageIO.read(this.getClass().getResource("/resources/difficult_button_hovered.png"));
            nightmareImg = ImageIO.read(this.getClass().getResource("/resources/nightmare_button.png"));
            nightmareHoverImg = ImageIO.read(this.getClass().getResource("/resources/nightmare_button_hovered.png"));
            
            new File("highscores.txt").createNewFile();
        }
        catch (Exception e)
        {
            System.out.println("Exception Thrown: " + e.getMessage());
        }

        labelSudoku = new JLabel();
        labelSudoku.setIcon(new ImageIcon(logoSudoku));
        labelSudoku.setBounds(0, 0, 323, 75);
        
        labelLetsPlay = new JLabel();
        labelLetsPlay.setIcon(new ImageIcon(logoLetsPlay));
        labelLetsPlay.setBounds(100, 35, 0, 0);

        labelGrid = new JLabel();
        labelGrid.setIcon(new ImageIcon(logoGrid));
        labelGrid.setBounds(700, 0, 150, 310);

        playButton = new JButton(new ImageIcon(playImg));
        playButton.setBorder(BorderFactory.createEmptyBorder());
        playButton.setContentAreaFilled(false);
        playButton.setBounds(13, 13, 131, 41);
        playButton.setRolloverIcon(new ImageIcon(playHoverImg));
        playButton.addActionListener(this);

        highscoresButton = new JButton(new ImageIcon(highscoresImg));
        highscoresButton.setBorder(BorderFactory.createEmptyBorder());
        highscoresButton.setContentAreaFilled(false);
        highscoresButton.setBounds(13, 67, 131, 41);
        highscoresButton.setRolloverIcon(new ImageIcon(highscoresHoverImg));
        highscoresButton.addActionListener(this);

        aboutButton = new JButton(new ImageIcon(aboutImg));
        aboutButton.setBorder(BorderFactory.createEmptyBorder());
        aboutButton.setContentAreaFilled(false);
        aboutButton.setBounds(13, 121, 131, 41);
        aboutButton.setRolloverIcon(new ImageIcon(aboutHoverImg));
        aboutButton.addActionListener(this);
        
        buttonContainer1 = new JPanel();
        buttonContainer1.setLayout(null);
        buttonContainer1.setBounds(271, 300, 157, 175);
        buttonContainer1.setOpaque(false);
        buttonContainer1.add(playButton);
        buttonContainer1.add(highscoresButton);
        buttonContainer1.add(aboutButton);
        
        easyButton = new JButton(new ImageIcon(easyImg));
        easyButton.setBorder(BorderFactory.createEmptyBorder());
        easyButton.setContentAreaFilled(false);
        easyButton.setBounds(13, 13, 131, 33);
        easyButton.setRolloverIcon(new ImageIcon(easyHoverImg));
        easyButton.addActionListener(this);
        
        intermediateButton = new JButton(new ImageIcon(intermediateImg));
        intermediateButton.setBorder(BorderFactory.createEmptyBorder());
        intermediateButton.setContentAreaFilled(false);
        intermediateButton.setBounds(13, 59, 131, 33);
        intermediateButton.setRolloverIcon(new ImageIcon(intermediateHoverImg));
        intermediateButton.addActionListener(this);
        
        difficultButton = new JButton(new ImageIcon(difficultImg));
        difficultButton.setBorder(BorderFactory.createEmptyBorder());
        difficultButton.setContentAreaFilled(false);
        difficultButton.setBounds(13, 105, 131, 33);
        difficultButton.setRolloverIcon(new ImageIcon(difficultHoverImg));
        difficultButton.addActionListener(this);
        
        nightmareButton = new JButton(new ImageIcon(nightmareImg));
        nightmareButton.setBorder(BorderFactory.createEmptyBorder());
        nightmareButton.setContentAreaFilled(false);
        nightmareButton.setBounds(13, 151, 131, 33);
        nightmareButton.setRolloverIcon(new ImageIcon(nightmareHoverImg));
        nightmareButton.addActionListener(this);
        
        buttonContainer2 = new JPanel();
        buttonContainer2.setLayout(null);
        buttonContainer2.setBounds(271, 300, 0, 0);
        buttonContainer2.setOpaque(false);
        buttonContainer2.add(easyButton);
        buttonContainer2.add(intermediateButton);
        buttonContainer2.add(difficultButton);
        buttonContainer2.add(nightmareButton);
        
        titleScreen = new JPanel(){
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.drawImage(titleScreenBg, 0, 0, null);
            }
        };

        titleScreen.setPreferredSize(new Dimension(700, 500));
        titleScreen.setLayout(null);
        
        titleScreen.add(labelSudoku);
        titleScreen.add(labelLetsPlay);
        titleScreen.add(labelGrid);

        frame = new JFrame("Let's Play Sudoku!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocation(350, 300);
        frame.setAlwaysOnTop(true);
        frame.add(titleScreen);
        frame.pack();
        frame.setVisible(true);

        timer = new Timer(5, this);
        timer.start();
        Sudoku.startSound(true);
    }
    
    public void initializeButtonContainer(boolean arg)
    {
        if (arg)
        {
            buttonContainer1.setSize(0, 0);
            buttonContainer2.setSize(157, 197);
        }
        else
        {
            buttonContainer1.setSize(157, 175);
            buttonContainer2.setSize(0, 0);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == timer)
        {
            ++ctr;

            if (ctr > 150)
            {
                timer.stop();
                titleScreen.add(buttonContainer1);
                titleScreen.add(buttonContainer2);
                titleScreen.repaint();
            }

            labelGrid.setLocation(gridXAxis, 0);
            gridXAxis -= 1;

            if (!(letsPlayH > 57))
            {
                labelLetsPlay.setSize(letsPlayW, letsPlayH);
                letsPlayW += 8;
                letsPlayH += 2;
            }

            if (!(sudokuYAxis < 75))
            {
                labelSudoku.setLocation(180, sudokuYAxis);
                sudokuYAxis -= 3;
            }
        }
        else
        {
            if (e.getSource() == playButton)
            {
                initializeButtonContainer(true);
            }
            else if (e.getSource() == highscoresButton)
            {
                highscores = new HighScores(this, frame, titleScreen);
                titleScreen.setVisible(false);
                frame.add(highscores);
            }
            else if (e.getSource() == aboutButton)
            {
                about = new About(frame, titleScreen);
                titleScreen.setVisible(false);
                frame.add(about);
            }
            else
            {
                int diff;

                if (e.getSource() == easyButton)
                {
                    diff = 0;
                }
                else if (e.getSource() == intermediateButton)
                {
                    diff = 1;
                }
                else if (e.getSource() == difficultButton)
                {
                    diff = 2;
                }
                else
                {
                    diff = 3;
                }
                
                Sudoku.stopSound();
                Sudoku.startSound(false);
                gameScreen = new GameScreen(this, frame, titleScreen, diff);
                titleScreen.setVisible(false);
                frame.add(gameScreen);
            }
        }
    }
    
    private final JFrame frame;
    private final JPanel titleScreen;
    private final JPanel buttonContainer1;
    private final JPanel buttonContainer2;
    private final JButton playButton;
    private final JButton highscoresButton;
    private final JButton aboutButton;
    private final JButton easyButton;
    private final JButton intermediateButton;
    private final JButton difficultButton;
    private final JButton nightmareButton;
    private final JLabel labelSudoku;
    private final JLabel labelLetsPlay;
    private final JLabel labelGrid;
    private final Timer timer;
    private Image logoSudoku;
    private Image logoLetsPlay;
    private Image logoGrid;
    private Image titleScreenBg;
    private Image playImg;
    private Image playHoverImg;
    private Image highscoresImg;
    private Image highscoresHoverImg;
    private Image aboutImg;
    private Image aboutHoverImg;
    private Image easyImg;
    private Image easyHoverImg;
    private Image intermediateImg;
    private Image intermediateHoverImg;
    private Image difficultImg;
    private Image difficultHoverImg;
    private Image nightmareImg;
    private Image nightmareHoverImg;
    private GameScreen gameScreen;
    private HighScores highscores;
    private About about;
    private Clip clip;
    private int sudokuYAxis = 500;
    private int gridXAxis = 700;
    private int letsPlayW = 0;
    private int letsPlayH = 0;
    private int ctr = 0;
}