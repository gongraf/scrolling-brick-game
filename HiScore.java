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

class HiScore implements Commons
{

    private static final String file = "data/hiscores.dat"; 

    private static final int MAX_HISCORES = 10;

    String[][] table = new String[MAX_HISCORES][2];

	HiScore(){ 
        readHiscores(); 
        formatTable();
        //System.out.println(Arrays.deepToString(this.table));
    }

    private void readHiscores() {
    
    	String delimiters = ",";

        try {
            //Not a pointer, but for the good old times :P 
			File fp = new File(this.file);
			FileReader fileReader = new FileReader(fp);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
            
            int y = 0;
                   
			while ((line = bufferedReader.readLine()) != null) {
				            
                String[] tokensVal = line.split(delimiters);
                        
                table[y][NAME]  = tokensVal[NAME];
                table[y][SCORE] = tokensVal[SCORE];
                             
                if(++y==MAX_HISCORES)break;
			}
			
			fileReader.close();
			
		} 
        
        catch (IOException e) {
			e.printStackTrace();
		}
    
    
    }
    
    private void writeTableToFile (){
        try{
    	    BufferedWriter outputWriter = null;
            outputWriter = new BufferedWriter(new FileWriter(file));
        
            for (int i = 0; i < this.table.length; i++) {
            
                outputWriter.write(table[i][NAME]+",");
                outputWriter.write(table[i][SCORE]);        
                outputWriter.newLine();
            }
            outputWriter.flush();  
            outputWriter.close();
        }
       
        catch (IOException e) {
			e.printStackTrace();
		}
    }


    private void formatTable(){
    	
    	int index = this.table.length-1;
    	
    	while(0!=index){
            for(int i=0; i<index; i++){
                if(table[i][SCORE]==null||table[index][SCORE]==null)continue;        
                if(Integer.parseInt(table[i][SCORE])<
                   Integer.parseInt(table[index][SCORE])){
                    
                    String temp = table[i][SCORE];
                    String name = table[i][NAME];           

                    table[i][SCORE]  = table[index][SCORE];
                    table[i][NAME]   = table[index][NAME];     

                    table[index][SCORE]  = temp;
                    table[index][NAME]   = name;
                }
            
            }
    		
            index--;		
        }
    	
    }     

    void addNewRecord(int value, String name){
        
        for(int i =0; i<this.table.length-1; i++){
            
            if(table[i][SCORE]== null || value >= Integer.parseInt(table[i][SCORE])){
                table[MAX_HISCORES-1][SCORE] = String.valueOf(value);
                table[MAX_HISCORES-1][NAME]  = name;
                break;
            }
        }
        formatTable();
        writeTableToFile();
    }

    boolean isHiscore(int value){
        for(int i = 0; i<this.table.length; i++){
            if(table[i][SCORE]== null) continue;  
            if(value >= Integer.parseInt(table[i][SCORE])) return true;
        }
        return false;
    }

}
