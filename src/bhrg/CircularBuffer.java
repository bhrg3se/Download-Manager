/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bhrg;

import static java.lang.Math.abs;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Bhargab
 */
public class CircularBuffer {
    private byte[][] data = new byte[10][1024];
    private int[] numRead = new int[10];
    private int rp,num,wp;
    public boolean isReadComplete;
    public boolean isComplete;
    private final Lock lock = new ReentrantLock();
    public CircularBuffer()
    {
        rp=0;
        num=0;
        wp=0;
        isReadComplete=false;;
        isComplete=false;
    }
    
    public boolean isEmpty()
    {
        if(num==0)
            return true;
        else
            return false;
    }
    
    public boolean isFull()
    {
        if(num==10)
            return true;
        else
            return false;
    }
    
    
    
    public  void push(byte d[],int n)
    {
        while(num>9){
            System.out.print("");
        }
        data[rp]=d;
        numRead[rp]=n;
        System.out.println("read "+rp);
        
        try{
            
            lock.lock();
            num++;
            System.out.println("num++"+num);
        }
        finally
        {
            lock.unlock();
        }
        if(rp>=9)
            rp=0;
        else
            rp++;
    }
    public  boolean pop(Byt b)
    {
        while(num<1){
            if(isReadComplete)
            {
                isComplete=true;
                System.out.println("Done writing");
                return true;
            
            }
            System.out.print("");
            
        }
        b.b=data[wp];
        b.numRead=numRead[wp];
        System.out.println("write "+wp);
        try{
            lock.lock();
            num--;
            System.out.println("num--"+num);
        }
        finally
        {
            lock.unlock();
        }
        if(wp>=9)
            wp=0;
        else
            wp++;
         
         return false;
    }
    
    
}
