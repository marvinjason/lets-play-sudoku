
package frontend;

import backend.HighscoreEntry;
import backend.Sudoku;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class HighScores extends JPanel implements ActionListener
{
    public HighScores(Main main, JFrame frame, JPanel titleScreen)
    {
        try
        {
            bg = ImageIO.read(this.getClass().getResource("/resources/plain_bg.png"));
            logoGrid = ImageIO.read(this.getClass().getResource("/resources/logo_grid.png"));
            logoHighscore = ImageIO.read(this.getClass().getResource("/resources/logo_highscores.png"));
        }
        catch (IOException ex)
        {
            System.out.println("Exception Thrown: " + ex.getMessage());
        }
        
        this.main = main;
        this.frame = frame;
        this.titleScreen = titleScreen;
        
        labelGrid = new JLabel();
        labelGrid.setIcon(new ImageIcon(logoGrid));
        labelGrid.setBounds(550, 0, 150, 310);
        
        labelHighscore = new JLabel();
        labelHighscore.setIcon(new ImageIcon(logoHighscore));
        labelHighscore.setBounds(190, 50, 320, 50);
        
        button = new JButton("Back");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        button.setForeground(new Color(90, 90, 90));
        button.setBackground(new Color(230, 230, 230));
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        button.setFocusPainted(false);
        button.setBounds(500, 430, 80, 30);
        button.addActionListener(this);
        
        table = new JTable(5, 4)
        {
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
                return false;
            }
        };
        
        table.setBounds(100, 150, 500, 400);
        table.setRowHeight(50);
        table.setShowGrid(false);
        table.setOpaque(false);
        table.setCellSelectionEnabled(false);
        table.setIntercellSpacing(new Dimension(5, 5));
        table.setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setForeground(new Color(100, 100, 100));
        table.setFocusable(false);
        ((DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(false);
        
        list = new ArrayList();
        
        this.setLayout(null);
        this.setPreferredSize(new Dimension(700, 500));
        this.add(labelGrid);
        this.add(labelHighscore);
        this.add(button);
        this.add(table);
        
        Sudoku.fetchData(list);
        Sudoku.sortDataOne(list);
        Sudoku.sortDataTwo(list);
        viewData();
        
        swtch = true;
        timer = new Timer(40, this);
        timer.start();
    }
    
    public void viewData()
    {
        for (int i = 0; i < 5 && i < list.size(); i++)
        {
            table.getModel().setValueAt(i + 1, i, 0);
            table.getModel().setValueAt(list.get(i).name, i, 1);
            table.getModel().setValueAt(list.get(i).difficulty, i, 2);
            table.getModel().setValueAt(Sudoku.formatTime(list.get(i).time), i, 3);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == button)
        {
            frame.remove(this);
            titleScreen.setVisible(true);
            main.initializeButtonContainer(false);
        }
        else
        {
            if (swtch)
            {
                if (labelHighscore.getY() <= 53)
                {
                    labelHighscore.setLocation(190, labelHighscore.getY() + 1);
                }
                else
                {
                    swtch = false;
                }
            }
            else
            {
                if (labelHighscore.getY() >= 47)
                {
                    labelHighscore.setLocation(190, labelHighscore.getY() - 1);
                }
                else
                {
                    swtch = true;
                }
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, null);
    }
    
    private JFrame frame;
    private JPanel titleScreen;
    private Main main;
    private Image bg;
    private Image logoGrid;
    private Image logoHighscore;
    private JLabel labelGrid;
    private JLabel labelHighscore;
    private JButton button;
    private JTable table;
    private Timer timer;
    private ArrayList<HighscoreEntry> list;
    private boolean swtch;
}
