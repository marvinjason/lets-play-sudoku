
package frontend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class About extends JPanel implements ActionListener
{
    public About(JFrame frame, JPanel titleScreen)
    {
        try
        {
            logoImg = ImageIO.read(this.getClass().getResource("/resources/about_logo.png"));
            bgImg = ImageIO.read(this.getClass().getResource("/resources/about_bg.png"));
        }
        catch (IOException ex)
        {
            System.out.println("Exception Thrown: " + ex.getMessage());
        }
        
        this.frame = frame;
        this.titleScreen = titleScreen;
        
        logo = new JLabel(new ImageIcon(logoImg));
        logo.setBounds(200, 30, 272, 78);
        
        button = new JButton("Back");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        button.setForeground(new Color(90, 90, 90));
        button.setBackground(Color.white);
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        button.setFocusPainted(false);
        button.setBounds(570, 430, 80, 30);
        button.addActionListener(this);
        
        timer = new Timer(40, this);
        
        setPreferredSize(new Dimension(700, 500));
        setLayout(null);
        this.add(logo);
        this.add(button);
        
        swtch = true;
        timer.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == button)
        {
            frame.remove(this);
            titleScreen.setVisible(true);
        }
        else
        {
            if (swtch)
            {
                if (logo.getY() <= 33)
                {
                    logo.setLocation(200, logo.getY() + 1);
                }
                else
                {
                    swtch = false;
                }
            }
            else
            {
                if (logo.getY() >= 27)
                {
                    logo.setLocation(200, logo.getY() - 1);
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
        g.drawImage(bgImg, 0, 0, null);
    }
    
    private Image logoImg;
    private Image bgImg;
    private JLabel logo;
    private JButton button;
    private Timer timer;
    private JFrame frame;
    private JPanel titleScreen;
    private boolean swtch;
}
