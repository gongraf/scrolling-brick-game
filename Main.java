/***
Copyright (C) 2015 Gonzalo Graf
Email: grafgonzalo@gmail.com

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, 51 Franklin Street, Suite 500, Boston, MA 02110-1335, USA.
*/

import javax.swing.JFrame;
import java.awt.EventQueue;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

class Main implements Commons
{

    public static void main (String [] args) {
        System.out.println("Small scrolling brick game created just for fun and as a research.\n"+
                           "(c) 2015 Gonzalo Graf.\n-----------------------------------------\n");
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().qwe();
            }
        });
    }


    void qwe() {
        JFrame frame = new JFrame("Scrolling Brick Game!");
        Game gameBoard = new Game();
        frame.add(gameBoard);

        frame.pack();
        frame.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        frame.setResizable(false);
        frame.setBackground(Color.BLACK);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	    try {
		    frame.setIconImage(ImageIO.read(new File("images/ic_launcher.png")));	        
		}		
		catch (IOException e){
			e.printStackTrace();
		}
		
		frame.setVisible(true);
    }

}
