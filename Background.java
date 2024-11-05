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

class Background implements Commons {

    private Image scaledImage;
    private ImageIcon iid;
    double speed = 0.3;
    double startY;
    int startX = 0;
    int height = 730;
    int width = 384;
    double y;

    Background (int level) {

        //Use to add new background for each level
        //String src = "images/back"+String.valueOf(level)+".png";

        //I have one back image for now so...
        String src = "images/back.png";
        this.iid = new ImageIcon(src);
        Image image = this.iid.getImage();
        this.scaledImage = image.getScaledInstance(this.width, this.height, Image.SCALE_DEFAULT);
    }

    Image getImage() { return this.scaledImage; }

    void doBackground(LevelMap map) {
        this.startY = map.startY * this.speed;
        //System.out.println(this.startY);

        this.y = this.startY % this.height;
        //System.out.println(this.y);
    }
}
