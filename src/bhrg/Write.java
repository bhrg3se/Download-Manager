package bhrg;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bhargab
 */
public class Write implements Runnable{
    HttpURLConnection con;
    
        OutputStream r; 
        private DownloadWorker parent;
        public int progress;
        private int sn;
        public Write( OutputStream ar,DownloadWorker parent,int sn){
            
            r = ar;
            this.sn=sn;
            progress=0;
            this.parent=parent;
            
        }
  
        @Override
        public void run(){
            try {
              
               Byt b = new Byt();
             
                while(parent.buffer[sn].isComplete==false){
                    
                    parent.buffer[sn].pop(b);
                    if(parent.buffer[sn].isComplete)
                        break;
                    r.write(b.b, 0, b.numRead);
                    
                    r.flush();
                    
                    parent.increaseProgress(b.numRead);   
                   
                    
                 //  System.out.println(parent.getProgress());
                }
                 
                r.close();
            } catch (IOException ex) {
                Logger.getLogger(DownloadWorker.class.getName()).log(Level.SEVERE, null, ex);
            }
              
        }
    }

