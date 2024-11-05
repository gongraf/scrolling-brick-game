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

import java.io.*;
import java.lang.*;
import java.util.Arrays;

class LevelMap implements Commons {

    //This will hold x and y real coo of the brick
    private static int MAX_X = 2;

    //This is the max index of tiles
    private static int MAX_Y = 200;

    //Array memory buffer to read raw map into array
    private static int MAX_FILE_X = 100;
    private static int MAX_FILE_Y = 100;

    //Anything below 4 is considered normal brick
    private static int NORMAL_BRICK = 4;

    private String name;

    //Array to hold raw map read from file
    private int[][] loadedMap = new int [MAX_FILE_Y][MAX_FILE_X];

    //Arrays after filter loadedMap array.
    //These will hold specific tiles with real x,y coo
    int[][] levelMap = new int [MAX_Y][MAX_X];
    int[][] levelSpecialTiles = new int [MAX_Y][MAX_X];
    int MAX_NORMAL_TILES;
    int MAX_SPECIAL_TILES;
    int MAX_MAP_X;
    int MAX_MAP_Y;
    int startX = 0;
    int startY = 0;

    String tileImages[] = new String[MAX_Y];

    LevelMap(int level) {

        this.name = level <= 7
                ? "data/"+String.valueOf(level)+".txt"
                : "data/"+String.valueOf((int) Math.floor(Math.random() * 6)+1)+".txt";
        readFile(name);
        passMapToArray();
        System.out.println("Level: "+level+"\nMax map scrolling x: "+
                this.MAX_MAP_X+"\nMax map scrolling y: "+
                this.MAX_MAP_Y+"\nMap Tiles: "+this.MAX_NORMAL_TILES+
                "\nMap Special Tiles "+this.MAX_SPECIAL_TILES);
    }


    //This method reads the .txt with the level map.
    //It only pass the numbers it finds in the file to loadedMap array.
    //It does not distingue between normal or special bricks, neither
    //calculates real coordinates.
    private void readFile(String level) {

        String delimiters = ",";

        try {
            File file = new File(level);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            int y = 0;
            int x = 0;

            while ((line = bufferedReader.readLine()) != null) {

                String[] tokensVal = line.split(delimiters);

                for (int i = 0 ; i < tokensVal.length; i++) {

                    loadedMap[y][i] = Integer.parseInt(tokensVal[i]);
                    x = i;
                }
                y++;
            }

            fileReader.close();

            //It just takes numer of x elementts counted from file and
            // do them by 16. This will give you max X size of map.
            //Note: In this case as the game does not have side scrolling
            //is always == to SCREEN_WIDTH
            this.MAX_MAP_X = (x + 1) * TILE_SIZE;

            //It just takes numer of y elementts counted from file and
            // do them by 16. This will give you max Y size of map.
            //Note: This is not the same as SCREEN_HEIGHT.
            //The difference is the actual vertical scrolling possible.
            this.MAX_MAP_Y = (y + 1) * TILE_SIZE;
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //This method goes through loadedMap array and filter the bricks
    //Anything below 4 is normal brick, and is passesd to normal bricks array,
    //Note: it also do the tile index by 16 to store the real x,y of the brick in the array.
    //Same logic is applied to specialbricks (aka > 4)
    private void passMapToArray() {

        int pos = 0;
        int pos2 = 0;

        for(int y = 0; y<MAX_FILE_Y;y++) {

            for(int x = 0; x<MAX_FILE_X;x++) {

                if(loadedMap[y][x] == 0) continue;

                int yPos = y * TILE_SIZE;
                int xPos = x * TILE_SIZE;

                if(loadedMap[y][x] <= NORMAL_BRICK) {

                    levelMap[pos][0] = yPos;
                    levelMap[pos][1] = xPos;

                    pos++;
                }

                if(loadedMap[y][x] > NORMAL_BRICK) {

                    levelSpecialTiles[pos2][0] = yPos;
                    levelSpecialTiles[pos2][1] = xPos;
                    tileImages[pos2] = String.valueOf(loadedMap[y][x]);
                    pos2++;
                }
            }
        }
        //System.out.println(Arrays.deepToString(levelMap));

        this.MAX_NORMAL_TILES = pos;
        this.MAX_SPECIAL_TILES = pos2;
    }
}
