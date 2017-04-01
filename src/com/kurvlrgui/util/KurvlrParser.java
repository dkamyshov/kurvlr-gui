/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurvlrgui.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Данил
 */
public class KurvlrParser {
    public static NumericData parse(String fileName) {
        NumericData result = new NumericData();
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            
            double x = 0, y = 0;
            String str = "";
            String parts[];
            
            // skip first 3 lines
            br.readLine();
            br.readLine();
            br.readLine();
            
            while((str = br.readLine()) != null) {
                str = str.trim();
                parts = str.split(" ");
                if(parts.length > 1) {
                    try {
                        x = Double.parseDouble(parts[0]);
                        y = Double.parseDouble(parts[parts.length-1]);
                        
                        result.addEntry(new NumericPair(x, y));
                    } catch(NumberFormatException ex) {
                        return null;
                    }
                }
            }
            
            br.close();
            
            return result;
        } catch (FileNotFoundException ex) {} catch (IOException ex) {}
        
        return null;
    }
}
