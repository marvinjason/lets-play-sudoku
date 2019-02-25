
package frontend;

import backend.Sudoku;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class GameScreen extends JPanel implements ActionListener
{
    public GameScreen(Main main, JFrame frame, JPanel titleScreen, int difficulty)
    {
        try
        {
            bg = ImageIO.read(this.getClass().getResource("/resources/bg.png"));
            submitImg = ImageIO.read(this.getClass().getResource("/resources/submit_button.png"));
            submitHoverImg = ImageIO.read(this.getClass().getResource("/resources/submit_button_hovered.png"));
            clearboardImg = ImageIO.read(this.getClass().getResource("/resources/clearboard_button.png"));
            clearboardHoverImg = ImageIO.read(this.getClass().getResource("/resources/clearboard_button_hovered.png"));
            giveupImg = ImageIO.read(this.getClass().getResource("/resources/giveup_button.png"));
            giveupHoverImg = ImageIO.read(this.getClass().getResource("/resources/giveup_button_hovered.png"));
        }
        catch (IOException ex)
        {
            System.out.println("Exception Thrown: " + ex.getMessage());
        }
        
        this.main = main;
        this.frame = frame;
        this.titleScreen = titleScreen;
        diff = (difficulty == 0 ? "Easy" : difficulty == 1 ? "Intermediate" : difficulty == 2 ? "Difficult" : "Nightmare");
        
        solvedBoard = new int[9][9];
        unsolvedBoard = new int[9][9];
        
        Sudoku.generate(unsolvedBoard);
        Sudoku.shuffle(unsolvedBoard);
        arrayCopy(unsolvedBoard, solvedBoard);
        Sudoku.generateBoard(unsolvedBoard, setDifficulty(difficulty));
        
        editableCell = new boolean[9][9];
        
        Sudoku.generateEditableCell(editableCell, unsolvedBoard);
        
        grid = new JTable(9, 9){
            
            DefaultTableCellRenderer render = new DefaultTableCellRenderer();
            
            {
                render.setHorizontalAlignment(SwingConstants.CENTER);
            }
            
            @Override
            public TableCellRenderer getCellRenderer(int arg0, int arg1)
            {
                return render;
            }
            
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return editableCell[row][column];
            }
        };
        
        grid.getModel().addTableModelListener(new TableModelListener(){
            
            @Override
            public void tableChanged(TableModelEvent e)
            {
                TableModel model = (TableModel) e.getSource();
                
                if (model.getValueAt(e.getFirstRow(), e.getColumn()) != null)
                {
                    try
                    {
                        int val = Integer.parseInt((model.getValueAt(e.getFirstRow(), e.getColumn()).toString()));

                        if (val < 1 || val > 9)
                        {
                            throw new NumberFormatException();
                        }
                    }
                    catch (NumberFormatException ex)
                    {
                        model.setValueAt(null, e.getFirstRow(), e.getColumn());
                    }
                }
                
                if (Sudoku.isFull(grid))
                {
                    submitButton.setEnabled(true);
                }
                else
                {
                    submitButton.setEnabled(false);
                }
            }
        });
        
        grid.setBounds(26, 26, 450, 450);
        grid.setCellSelectionEnabled(false);
        grid.setRowHeight(50);
        grid.setIntercellSpacing(new Dimension(5, 5));
        grid.setFont(new Font("Segoe UI", Font.BOLD, 24));
        grid.setOpaque(false);
        grid.setShowGrid(false);
        ((DefaultTableCellRenderer) grid.getDefaultRenderer(Object.class)).setOpaque(false);
        
        timeLabel1 = new JLabel("Time");
        timeLabel1.setBounds(513, 115, 150, 30);
        timeLabel1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        timeLabel1.setForeground(java.awt.Color.darkGray);
        timeLabel1.setHorizontalAlignment(JLabel.CENTER);
        
        timeLabel2 = new JLabel();
        timeLabel2.setBounds(513, 145, 150, 30);
        timeLabel2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        timeLabel2.setForeground(java.awt.Color.darkGray);
        timeLabel2.setHorizontalAlignment(JLabel.CENTER);
        
        paused = false;
        timeSec = 0;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            
            @Override
            public void run()
            {
                if (!paused)
                {
                    timeLabel2.setText(Sudoku.formatTime(timeSec));
                    ++timeSec;
                }
            }
            
        }, 0, 1000);
        
        diffLabel1 = new JLabel("Difficulty");
        diffLabel1.setBounds(513, 200, 150, 30);
        diffLabel1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        diffLabel1.setForeground(java.awt.Color.darkGray);
        diffLabel1.setHorizontalAlignment(JLabel.CENTER);
        
        diffLabel2 = new JLabel(diff);
        diffLabel2.setBounds(513, 230, 150, 30);
        diffLabel2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        diffLabel2.setForeground(java.awt.Color.darkGray);
        diffLabel2.setHorizontalAlignment(JLabel.CENTER);
        
        submitButton = new JButton(new ImageIcon(submitImg));
        submitButton.setBorder(BorderFactory.createEmptyBorder());
        submitButton.setContentAreaFilled(false);
        submitButton.setBounds(510, 300, 164, 37);
        submitButton.setRolloverIcon(new ImageIcon(submitHoverImg));
        submitButton.addActionListener(this);
        submitButton.setEnabled(false);
        
        clearboardButton = new JButton(new ImageIcon(clearboardImg));
        clearboardButton.setBorder(BorderFactory.createEmptyBorder());
        clearboardButton.setContentAreaFilled(false);
        clearboardButton.setBounds(510, 347, 164, 37);
        clearboardButton.setRolloverIcon(new ImageIcon(clearboardHoverImg));
        clearboardButton.addActionListener(this);
        
        giveupButton = new JButton(new ImageIcon(giveupImg));
        giveupButton.setBorder(BorderFactory.createEmptyBorder());
        giveupButton.setContentAreaFilled(false);
        giveupButton.setBounds(510, 394, 164, 37);
        giveupButton.setRolloverIcon(new ImageIcon(giveupHoverImg));
        giveupButton.addActionListener(this);
        
        setPreferredSize(new Dimension(700, 500));
        setLayout(null);
        add(grid);
        add(timeLabel1);
        add(timeLabel2);
        add(diffLabel1);
        add(diffLabel2);
        add(submitButton);
        add(clearboardButton);
        add(giveupButton);
        
        Sudoku.populate(grid, unsolvedBoard);
    }
    
    private int setDifficulty(int diff)
    {
        if (diff == 0)
        {
            return 50;
        }
        else if (diff == 1)
        {
            return 37;
        }
        else if (diff == 2)
        {
            return 26;
        }
        else
        {
            return 17;
        }
    }
    
    private void arrayCopy(int[][] src, int[][] dest)
    {
        for (int x = 0; x < 9; x++)
        {
            System.arraycopy(src[x], 0, dest[x], 0, 9);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == submitButton)
        {
            paused = true;
            Sudoku.stopSound();
            submitButton.setEnabled(false);
            clearboardButton.setEnabled(false);
            giveupButton.setEnabled(false);
            Sudoku.validate(grid, this);
        }
        else if (e.getSource() == clearboardButton)
        {
            Sudoku.populate(grid, unsolvedBoard);
        }
        else
        {
            paused = true;
            Sudoku.stopSound();
            submitButton.setEnabled(false);
            clearboardButton.setEnabled(false);
            giveupButton.setEnabled(false);
            popUp(false, timeSec - 1, diff);
        }
    }
    
    public void continueAttempt()
    {
        Sudoku.populate(grid, unsolvedBoard);
        paused = false;
        Sudoku.startSound(false);
        grid.setEnabled(true);
        clearboardButton.setEnabled(true);
        giveupButton.setEnabled(true);
    }
    
    public void popUp(boolean arg0, int arg1, String arg2)
    {
        int[][] emptyArr = new int[9][9];
        
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                emptyArr[i][j] = 0;
            }
        }
        
        Sudoku.populate(grid, emptyArr);
        grid.setEnabled(false);
        Dialog dialog = new Dialog(arg0, arg1, arg2, main, frame, titleScreen, this);
        add(dialog);
        this.setComponentZOrder(dialog, 0);
        this.repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, null);
    }
    
    private Main main;
    private JFrame frame;
    private JPanel titleScreen;
    private JTable grid;
    private final JLabel timeLabel1;
    private JLabel timeLabel2;
    private final JLabel diffLabel1;
    private final JLabel diffLabel2;
    private JButton submitButton;
    private final JButton clearboardButton;
    private final JButton giveupButton;
    private final Timer timer;
    private Image bg;
    private Image submitImg;
    private Image submitHoverImg;
    private Image clearboardImg;
    private Image clearboardHoverImg;
    private Image giveupImg;
    private Image giveupHoverImg;
    public int timeSec;
    public String diff;
    private int[][] solvedBoard;
    private int[][] unsolvedBoard;
    private boolean paused;
    private boolean[][] editableCell;
}