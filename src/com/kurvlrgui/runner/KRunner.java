package com.kurvlrgui.runner;

import com.kurvlrgui.gui.FormMain;
import com.kurvlrgui.util.KurvlrParser;
import com.kurvlrgui.util.KurvlrResult;
import com.kurvlrgui.util.NumericData;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class KRunner implements Runnable {

    public static final int HALT = 0;
    public static final int WORKING = 1;
    public static final int ERROR = 2;
    public static final int SUCCESS = 3;
    
    private FormMain parentFrame;
    
    private int state = KRunner.HALT;
    private String modelContent = null;
    
    private long tempId = 0;
    
    private NumericData SIS_calc;
    private NumericData SIS_obs;
    private NumericData RDF_calc;
    private NumericData RDF_obs;
    private NumericData RDF_diff;
    
    private boolean deleteFiles = true;
    private boolean deleteTemp = false;
    
    public KRunner(FormMain p, String modelContent, boolean deleteFlag, boolean deleteTemp) {
        this.modelContent = modelContent;
        this.tempId = System.currentTimeMillis();
        this.parentFrame = p;
        this.deleteFiles = deleteFlag;
        this.deleteTemp = deleteTemp;
    }
    
    public void execute() {
        this.state = KRunner.WORKING;
        
        Thread t = new Thread(this);
        t.start();
    }
    
    public void run() {
        if(!(new File("tmp")).exists()) {
            try {
                (new File("tmp")).mkdir();
            } catch(Exception ex) {}
        }
        
        try {
            FileWriter fw = new FileWriter("tmp/"+tempId+".kc");
            fw.write(this.modelContent);
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            this.state = KRunner.ERROR;
            this.parentFrame.runnerFailure("Unable to create temporary file! Check permissions for './tmp' directory.");
            Logger.getLogger(KRunner.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        ProcessBuilder pb = new ProcessBuilder("exe/kurvlr.exe");
        Process p;
        try {
            p = pb.start();
        } catch (IOException ex) {
            this.state = KRunner.ERROR;
            this.parentFrame.runnerFailure("Unable to start kurvlr.exe! Check if executable is present in './exe' folder.");
            Logger.getLogger(KRunner.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        OutputStream pos = p.getOutputStream();
        
        try {
            pos.write(("tmp/"+this.tempId+".kc").getBytes());
            pos.write('\r');
            pos.write('\n');
            pos.close();
        } catch (IOException ex) {
            this.state = KRunner.ERROR;
            this.parentFrame.runnerFailure("Unable to redirect kurvlr.exe's stdin to kurvlrGUI!");
            Logger.getLogger(KRunner.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        long oldt = System.currentTimeMillis();
        long newt = oldt;
        while(newt - oldt < 3000) {
            newt = System.currentTimeMillis();
            if((new File("SIS_obs")).exists() && (new File("SIS_calc")).exists() &&
                    (new File("RDF_obs")).exists() && (new File("RDF_calc")).exists()) {
                if(((this.SIS_calc = KurvlrParser.parse("SIS_calc")) != null)
                   && ((this.SIS_obs = KurvlrParser.parse("SIS_obs")) != null)
                   && ((this.RDF_calc = KurvlrParser.parse("RDF_calc")) != null)
                   && ((this.RDF_obs = KurvlrParser.parse("RDF_obs")) != null)
                   && ((this.RDF_diff = KurvlrParser.parse("RDF_diff")) != null)) {
                    // delete unnecessary files
                    if(this.deleteFiles) {
                        try {
                            Files.delete((new File("RDF_calc").toPath()));
                        } catch (IOException ex) {}

                        try {
                            Files.delete((new File("RDF_obs")).toPath());
                        } catch (IOException ex) {}

                        try {
                            Files.delete((new File("SIS_calc").toPath()));
                        } catch (IOException ex) {}

                        try {
                            Files.delete((new File("SIS_obs")).toPath());
                        } catch (IOException ex) {}

                        try {
                            Files.delete((new File("RDF_diff").toPath()));
                        } catch (IOException ex) {}

                        try {
                            Files.delete((new File("output.list")).toPath());
                        } catch (IOException ex) {}

                        try {
                            Files.delete((new File("dummy").toPath()));
                        } catch (IOException ex) {}

                        try {
                            Files.delete((new File("kurvlr.out")).toPath());
                        } catch (IOException ex) {}
                    }
                    
                    if(this.deleteTemp) {
                        try {
                            Files.delete((new File("tmp/"+this.tempId+".kc")).toPath());
                        } catch (IOException ex) {}
                    }
                    
                    this.state = KRunner.SUCCESS;
                    this.parentFrame.runnerSuccess(new KurvlrResult(this.SIS_obs, this.SIS_calc, this.RDF_obs, this.RDF_calc, this.RDF_diff));
                    
                    return;
                } else {
                    this.state = KRunner.ERROR;
                    this.parentFrame.runnerFailure("Error while parsing: kurvlr.exe's output files are not in suitable format!");
                    return;
                }
            }
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {}
        }
        
        this.state = KRunner.ERROR;
        this.parentFrame.runnerFailure("Output files did not appear after 3 seconds. Please check if your model file is in suitable format.");
        
        try {
            Files.delete((new File("output.list")).toPath());
        } catch (IOException ex) {}

        try {
            Files.delete((new File("dummy").toPath()));
        } catch (IOException ex) {}

        try {
            Files.delete((new File("kurvlr.out")).toPath());
        } catch (IOException ex) {}
    }
}
