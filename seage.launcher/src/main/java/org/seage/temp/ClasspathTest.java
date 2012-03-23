/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.temp;

/**
 *
 * @author rick
 */
public class ClasspathTest {
    public static void main(String[] args)
    {
        String cp = System.getProperty("java.class.path");
        String delim = System.getProperty("path.separator");
        
        String[] paths = cp.split(delim);
        
        for(String s : paths)
            System.out.println(s);
    }
}
