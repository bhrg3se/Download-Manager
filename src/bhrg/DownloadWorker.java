/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bhrg;

import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author Bhargab
 */
public class DownloadWorker extends SwingWorker<String,Integer>{

    public DownloadWorker(DownloadWindow parent)
    {
        this.parent=parent;
        parent.prog=0;
        parent.percent=0;
        parent.name=getFileName(parent.url);
        
    }
    
    @Override
    protected String doInBackground() {
    //download
    
        
       
       
        try{
        setProgress(0);
        System.out.println("Starting worker thread");
        parent.r = new OutputStream[parent.parts];
        parent.tempFile = new File[parent.parts];
        for (int i = 0 ; i < parent.parts ; i++){
            parent.tempFile[i] = new File("e:\\meta\\"+parent.name+i);
            parent.r[i] = new BufferedOutputStream(new FileOutputStream(parent.tempFile[i]));
        }
        System.out.println(" worker ");   
        
         
        parallel(parent.url);
        for(int i=0;i<parent.parts;i++)
            {
                parent.r[i].flush();
                parent.r[i].close();
                parent.tempFile[i].deleteOnExit();
                System.out.println(parent.tempFile[i].delete());
            }

        System.out.println(" worker thread Completed");
        
    }
    catch(Exception e)
    {
        
        System.out.println(e.getMessage());System.out.println(e.getMessage());
    
        e.printStackTrace();
    }
        return "Done";
    }
    
     @Override
        protected void done() {
            Toolkit.getDefaultToolkit().beep();
            
        try {
            System.out.println(get());
        } catch (InterruptedException ex) {
            Logger.getLogger(DownloadWorker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(DownloadWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    

    private void normal(String link) throws MalformedURLException, IOException{
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
  
        // connect to the server
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.connect();
        parent.size=conn.getContentLength();
        // prepare for read from server
        in = new BufferedInputStream(conn.getInputStream());
          
        // prepare for write to local disk       
        String fileName = getFileName(link);
        out = new BufferedOutputStream(new FileOutputStream(fileName));
        int n;
          
        // read from the server and write to local storage
        byte data[] = new byte[1024];
        while( (n = in.read(data)) != -1){
            out.write(data, 0, n);
            increaseProgress(n);
            
        }
        
        in.close();
        out.close();
    }
      private void parallel(String link){
        try {
            // open up a connection to get total size of the file
            URL url = new URL(link); 
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            int total = conn.getContentLength();
            parent.size=conn.getContentLength();  
            // connection array
           
         
            HttpURLConnection[] con = new HttpURLConnection[parent.parts];
         
            Read[] m = new Read[parent.parts];
            Write[] n=new Write[parent.parts];
            buffer=new CircularBuffer[parent.parts];
            // calculate bytes each stream has to download
            int eachDownload = total/parent.parts;                        
            String byteRange = "";
            ExecutorService tE = Executors.newCachedThreadPool();
           
            for (int i = 0 ; i < parent.parts ; i++){
                // open the connection streams with byte range
                byteRange = (eachDownload*i)+"-"+((i!=(parent.parts)-1)?(eachDownload*(i+1)-1):total);
                con[i] = (HttpURLConnection) url.openConnection();
                con[i].setRequestProperty("Range", "bytes="+byteRange);System.out.println("Connecting");
                con[i].connect();
                System.out.println("Connected");  
                // if server doesn't support partial content
                if (con[i].getResponseCode() != 206){
                    System.out.println("Parallel Stream is not supported ");
                    parent.canResume=false;
                    this.normal(link);
                    break;
                }
                  parent.canResume=true; 
                  
                  
                // Fire the threads to do the job.
               
            //    buffer[i] = new CircularBuffer();
                m[i] = new Read(con[i],this,i,parent.r[i]);
            //    n[i] = new Write(parent.r[i],this,i);
                
                 tE.execute(m[i]);
             //   tE.execute(n[i]);
                  
            }
            
           
            
            tE.shutdown();                                  // no new threads are to be created
            tE.awaitTermination(10000, TimeUnit.DAYS);      // wait untill all the threads terminates
                          
            File dest = new File("e:\\meta\\"+getFileName(link));
            String filePath = mergeFiles(dest, parent.tempFile);
            System.out.println("Download File Completed.");
            System.out.println("Saved to " + filePath);
            
        } catch (InterruptedException ex) {
            Logger.getLogger(DownloadWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DownloadWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private String mergeFiles(File dest, File[] files){
        try {
            byte[] reader = new byte[1024*5];
            int n;
            InputStream is;
            OutputStream os;
              
            os = new BufferedOutputStream(new FileOutputStream(dest));
            for (File iter : files){
                is = new BufferedInputStream(new FileInputStream(iter));
                while((n = is.read(reader)) != -1){
                    os.write(reader, 0, n);
                }
            }
            os.flush();
            os.close();
                        
        } catch (IOException ex) {
            Logger.getLogger(DownloadWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dest.getAbsolutePath();
    }
      
    // get filename out of the link unable to find one returns 'DownloadedFile' as default
    private String getFileName(String link){
        String fileName;
        
        if (!link.contains("/")) 
            fileName = "DownloadedFile";
        else
            fileName = link.substring(link.lastIndexOf("/"), link.length());
        if(fileName.isEmpty())
             fileName = "DownloadedFile";
        fileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
        return fileName;
    }
    public String getName()
    {
        if(parent.name== "DownloadedFile")
            return parent.url;
        else
            return parent.name;
    }
    protected synchronized void increaseProgress(int change)
    {
        parent.prog+=change;
        setProgress((int)((float)parent.prog)*100/parent.size);
    }
    
    public DownloadWindow parent;
    CircularBuffer[] buffer;
}
