/**
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

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;

class Player implements Commons 
{

    private static final String IMG_SRC = "images/paddle.png";  
                
    private int w,h,lives;
    private int x,y,dirX;
    private boolean right,left;

    private Image image;
    private ImageIcon iid = new ImageIcon(IMG_SRC);

    Player () {
        this.h = 16;
        this.w = 52;
        this.image = this.iid.getImage();
    }

    void setX (int x) { this.x = x; }
        
    void setY (int y) { this.y = y; }
        
    int getDirX () { return this.dirX;}
        
    int getX () { return this.x; }
        
    int getY () { return this.y; }
        
    int getW () { return this.w; }
        
    int getH () { return this.h; }

    Image getImage() { return this.image; }

    void setRight (boolean value) { this.right = value; }

    void setLeft (boolean value) { this.left = value; }

    boolean moveRight () { return this.right; }    

    boolean moveLeft () { return this.left; }

    void doPlayer () {
        
        this.dirX = 0;
                
        if (this.right)this.dirX += 6;
                
        else if (this.left)this.dirX -= 6;
                
        this.x += this.dirX; 

        if (this.x+this.w >= SCREEN_WIDTH) this.x = SCREEN_WIDTH - this.w;
                                               
        else if (this.x <= 0) this.x = 0;
    }

}

