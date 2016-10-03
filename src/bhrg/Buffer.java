package bhrg;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bhargab
 */
public class Buffer {
    private byte data0[] = new byte[1024];
    private byte data1[] = new byte[1024];
    private int flag[]=new int[2];
    private int numRead0,numRead1;
    public boolean isComplete;
    public boolean isReadComplete;
    private boolean read0;
    private boolean write0;
    public boolean isEmpty()
    {
         if(flag[0]==flag[1]&&flag[0]==0)
            return true;
        else
            return false;
    }
    public boolean isFull()
    {
         if(flag[0]==flag[1]&&flag[0]==1)
            return true;
        else
            return false;
    }
    public Buffer()
    {
       
        numRead0=0;
        numRead1=0;
        flag[0]=0;
        flag[1]=0;
        read0=true;
        write0=true;
        isComplete=false;
        isReadComplete=false;
        
    }
    public void push(byte data[],int numRead)
    {
        while((flag[0]==1&&read0)||(flag[1]==1&&!read0)){
       //     System.out.println("Read Waiting");
        }
        
        if(flag[0]==0&&read0)
        {
            data0=data;
            numRead0=numRead;
            flag[0]=1;
            read0=false;
            
        }
        else if(flag[1]==0&&!read0)
        {
            numRead1=numRead;
            data1=data;
            flag[1]=1;
           read0=true;
        }
       
        
           
    }
    
    public void pop(Byt b)
    {
        while((flag[0]==0&&write0)||(flag[1]==0&&!write0))
        {
       //     System.out.println("Write Waiting");
            if(isReadComplete)
            { 
                isComplete=true;
                System.out.println("Breaked");
                break;
            }
        }
        if(flag[0]==1&&write0)
        {
            b.b=data0;
            b.numRead=numRead0;
            flag[0]=0;
            write0=false;
           
          
        }
        else if(flag[1]==1&&!write0)
        {
            b.b=data1;
            b.numRead=numRead1;
            flag[1]=0;
           write0=true;
           
        }
       
            
    }
    
}
