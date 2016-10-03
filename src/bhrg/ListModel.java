/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bhrg;

import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 *
 * @author Bhargab
 */
public class ListModel extends AbstractListModel{

    public ListModel()
    {
        values=new ArrayList<String>();
    }
    @Override
    public int getSize() {
      return values.size(); }

    @Override
    public Object getElementAt(int index) {
    return values.get(index);
    }
    public void add(String nmae)
    {
        values.add(nmae);
    }
    public void remove(int index)
    {
        values.remove(index);
    }
    
    private ArrayList<String> values;
}
