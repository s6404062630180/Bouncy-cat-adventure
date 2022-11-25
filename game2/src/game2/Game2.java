
package game2;

import javax.swing.*;



public class Game2 {

    public static void main(String[] args){
        maingame mg =new maingame();
        JFrame gamescreen = new JFrame("Bouncy cat adventure");
        JLabel kids = new JLabel("Timmy");
        gamescreen.setBounds(10,10,961,637);
        gamescreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gamescreen.setVisible(true);
        gamescreen.setLocationRelativeTo(null);
        gamescreen.setResizable(false);
        gamescreen.add(kids);
        gamescreen.add(mg);
    }
    
}
