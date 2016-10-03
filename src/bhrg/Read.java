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
public class Read implements Runnable{
    HttpURLConnection con;
    
        OutputStream r; 
        private DownloadWorker parent;
        public int progress;
        private int sn;
        public int speed;
        public Read(HttpURLConnection acon,DownloadWorker parent,int sn,OutputStream ar){
            con = acon;
            this.sn=sn;
            this.r=ar;
            progress=0;
            this.parent=parent;
            
        }
  
        @Override
        public void run(){
            try {
                BufferedInputStream in = new BufferedInputStream(con.getInputStream());
                int numRead;
                
                byte data[] = new byte[1024];
                while( (numRead = in.read(data, 0, 1024)) != -1){
                    r.write(data, 0, numRead);
                 //   parent.buffer[sn].push(data, numRead);
                    parent.increaseProgress(numRead);   
                   
                    
                   
                }
                System.out.println("read complete "+sn);
         //        parent.buffer[sn].isReadComplete=true; 
                
            } catch (IOException ex) {
                Logger.getLogger(DownloadWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
              
        }
    }

