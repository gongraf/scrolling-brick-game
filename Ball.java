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

class Ball implements Commons {

    private static final String IMG_SRC = "images/ball.png";
    private int w = TILE_SIZE; 
    private int h = TILE_SIZE;
    private double x,y;
    private int speed = 5;
    private double dirX; 
    private double dirY;        
    private double dir;
    private int mappingSpeed = 1;
    private int thinkTime = MAX_RELOAD;
    private Image image;
    private ImageIcon iid = new ImageIcon(IMG_SRC);
    
    private boolean active,launched;
        
    int resets, hits;

    Ball() {
        this.image = this.iid.getImage();
        this.resets = 3;
        this.active = true;
        this.launched = false;
    }

    void setX (double x) { this.x = x; }

    void setY (double y) { this.y = y; }

    void setDir (double dir) { this.dir = dir; }
        
    void setDirX (double x) { this.dirX = x; }

    void setDirY (double y) { this.dirY = y; }
        
    void setActive (boolean active) { this.active = active; }
        
    void setLaunched (boolean launched) { this.launched = launched; }
        
    Image getImage() { return this.image; }
       
    int getW () { return this.w; }

    int getH() { return this.h; }
        
    double getX () { return this.x; }

    double getDir () { return this.dir; }

    double getY () { return this.y; }

    double getDirX () { return this.dirX; }

    double getDirY () { return this.dirY; }

    boolean isActive () { return this.active; }

    boolean isLaunched () { return this.launched; }
        
    boolean isAlive() { return (this.resets > 0); }
        
        
    void doEntity (Player player, LevelMap map) {
             
        if(this.launched) {
        		  
            if(this.x + this.w >= SCREEN_WIDTH ) {
        	this.x = SCREEN_WIDTH - this.w;
        	this.dirX *=  -1;
            }
        		        
            if(this.x < 0 ) {
        	this.x = 0;
        	this.dirX *=  -1;
            }
        		        
            if(this.y <= 0) this.dirY = -this.dirY;
        		       
            if(this.y + this.h > map.MAX_MAP_Y) {
        		        
        	this.active = false;
        		    
        	if(--this.thinkTime == 0) {
        		                
        	    this.dirX = 0;
        	    this.dirY = 0;
        	    this.launched = false;
        	    this.resets -= 1;
        	    this.thinkTime = MAX_RELOAD;
                                      		                
        	}
            }
        		       
            this.y += this.dirY;
            this.x += this.dirX;
        		        
            centerEntityOnMap(map);
        }
        else if(!this.launched) {
            showTable(map);
            this.x = (int)player.getX()+player.getW()/2 - this.w/2;
            this.y = (int)player.getY()-this.h;
        }
    }

    void showTable(LevelMap map) {
		
        if (map.startY < 0){
            map.startY = 0;
            this.mappingSpeed *= -1;
        }
        else if ( map.startY + SCREEN_HEIGHT >= map.MAX_MAP_Y ) {
            map.startY = map.MAX_MAP_Y - SCREEN_HEIGHT;
            this.mappingSpeed *= -1;
        }

        map.startY += this.mappingSpeed;
    }

    void centerEntityOnMap(LevelMap map) {

	map.startX = (int)this.x - (SCREEN_WIDTH / 2);

        if (map.startX < 0){ 
            map.startX = 0;
        }
        else if (map.startX + SCREEN_WIDTH >= map.MAX_MAP_X){
            map.startX = map.MAX_MAP_X - SCREEN_WIDTH;
        }

        map.startY = (int)this.y - (SCREEN_HEIGHT / 2);
        
        if (map.startY < 0){ 
            map.startY = 0;
        }
        else if ( map.startY + SCREEN_HEIGHT >= map.MAX_MAP_Y ) { 
            map.startY = map.MAX_MAP_Y - SCREEN_HEIGHT;
        }
    }

    void setLaunchDir(){
        this.dir =  1; 
        this.dirX = Math.cos(dir) * this.speed;
        this.dirY = Math.sin(dir) * this.speed;
        this.hits = 0;
    }
}
