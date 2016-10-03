/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bhrg;

/**
 *
 * @author Bhargab
 */
public class TR {
    
    
public static void main(String [] args)
{
    CircularBuffer b=new CircularBuffer();
    byte by[]=new byte[1024];
    by="hef".getBytes();
    System.out.println(by.toString());
    b.push(by, by.length);
    b.push(by, by.length);
    
   
    b.push("yobnmlo".getBytes(), "yogjhlo".getBytes().length);
    Byt bh=new Byt();
    b.pop(bh);
    System.out.println(bh.b.toString());
    b.pop(bh);
    
    
}
}
