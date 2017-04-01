/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurvlrgui.util;

/**
 *
 * @author Данил
 */
public class KurvlrResult {
    private NumericData sis_obs = null, sis_calc = null, rdf_obs = null, rdf_calc = null, rdf_diff = null;
    
    public KurvlrResult(NumericData sis_obs, NumericData sis_calc, NumericData rdf_obs, NumericData rdf_calc, NumericData rdf_diff) {
        this.sis_obs = sis_obs;
        this.sis_calc = sis_calc;
        this.rdf_calc = rdf_calc;
        this.rdf_obs = rdf_obs;
        this.rdf_diff = rdf_diff;
    }
    
    public NumericData getSISObserved() {
        return this.sis_obs;
    }
    
    public NumericData getSISCalculated() {
        return this.sis_calc;
    }
    
    public NumericData getRDFObserved() {
        return this.rdf_obs;
    }
    
    public NumericData getRDFCalculated() {
        return this.rdf_calc;
    }
    
    public NumericData getRDFDiff() {
        return this.rdf_diff;
    }
}
