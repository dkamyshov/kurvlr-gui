/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurvlrgui.util;

import java.util.Vector;

/**
 *
 * @author Данил
 */
public class NumericData {
    public Vector<NumericPair> values = null;
    
    public NumericData() {
        values = new Vector<NumericPair>();
    }
    
    public void addEntry(NumericPair np) {
        values.add(np);
    }
}