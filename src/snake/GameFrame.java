package snake;

import javax.swing.JFrame;

/**
 *
 * @author Antin Williams
 */
public class GameFrame extends JFrame{

    public final void setGUI(){
        this.add(new GamePanel());        
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    
}
