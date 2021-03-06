/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurvlrgui.gui;

import com.kurvlrgui.util.NumericData;
import com.kurvlrgui.util.NumericPair;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Данил
 */
public class ChartPanel extends javax.swing.JPanel {

    XYSeries calculated = null;
    XYSeries observed = null;
    org.jfree.chart.ChartPanel panel = null;
    
    /**
     * Creates new form ChartPanel
     */
    public ChartPanel(String title, NumericData s1, NumericData s2) {
        initComponents();
        
        calculated = new XYSeries("Calculated");
        for(NumericPair np : s1.values) {
            calculated.add(np.x, np.y);
        }
        
        observed = null;
        if(s2 != null) {
            observed = new XYSeries("Observed");
            for(NumericPair np : s2.values) {
                observed.add(np.x, np.y);
            }
        }
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(calculated);
        if(s2 != null) dataset.addSeries(observed);
        
        JFreeChart chart = ChartFactory.createXYLineChart(title, "X", "Y", dataset, PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(Color.white);
        
        XYPlot plot = chart.getXYPlot();
        
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesLinesVisible(1, false);
        
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        panel = new org.jfree.chart.ChartPanel(chart);
        
        panel.setHorizontalAxisTrace(true);
        panel.setVerticalAxisTrace(true);
        
        this.add(panel);
    }

    public void resizeChart() {
        Dimension parentSize = this.getSize();
        
        panel.setSize(parentSize);
        panel.setLocation(0, 0);
    }
    
    public void clear() {
        if(calculated != null) calculated.clear();
        if(observed != null) observed.clear();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
