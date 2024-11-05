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

class Brick {

    private int w, h, x, y;
    private boolean active;
    private Image image;

    private static final String[] options = {"images/1.png","images/2.png","images/3.png","images/4.png"};

    Brick () { 
        this.active = false;
        ImageIcon iid = new ImageIcon(options[(int) Math.floor(Math.random() *4)]);
        this.w = iid.getIconWidth();
        this.h = iid.getIconHeight();
        this.image = iid.getImage();
    }

	//Overload for special bricks
    Brick (String resource) {
        String dir = "images/";
        this.active = false;
        ImageIcon iid = new ImageIcon(dir+resource);
        this.w = iid.getIconWidth();
        this.h = iid.getIconHeight();
        this.image = iid.getImage();
    }

    void setX (int x) { this.x = x; }

    void setY (int y) { this.y = y; }

    void setActive (boolean active) { this.active = active; }

    Image getImage() { return this.image; }
    
    boolean isActive () { return this.active; }
    
    int getX () { return this.x; }

    int getY () { return this.y; }

    int getW () { return this.w; }

    int getH() { return this.h; }

    void doEntity () { /*Do nothing... you know... just being bricks */ }
}
