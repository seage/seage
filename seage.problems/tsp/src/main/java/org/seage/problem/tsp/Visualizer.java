/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 *     Richard Malek
 *     - Redesign
 */
package org.seage.problem.tsp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;


/**
 *
 * @author Richard Malek
 */
public class Visualizer
{
    // <editor-fold defaultstate="collapsed" desc="Singleton design pattern">
    private static Visualizer _instance;
    private Visualizer()
    {
    }

    public static Visualizer instance()
    {        
        if(_instance == null)
            _instance = new Visualizer();
        return _instance;
    }
    // </editor-fold>

    public void createGraph(City[] cities, Integer[] tour, String path, int width, int height) throws NoninvertibleTransformException
    {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB );

        Graphics2D graphics = (Graphics2D)image.getGraphics();

        // getting current width ang height
        double minX=Double.MAX_VALUE, minY=Double.MAX_VALUE;
        double maxX=Double.MIN_VALUE, maxY=Double.MIN_VALUE;
        for(int i=0;i<cities.length;i++)
        {
            if(cities[tour[i]].X < minX) minX = cities[tour[i]].X;
            if(cities[tour[i]].X > maxX) maxX = cities[tour[i]].X;
            if(cities[tour[i]].Y < minY) minY = cities[tour[i]].Y;
            if(cities[tour[i]].Y > maxY) maxY = cities[tour[i]].Y;
        }

        double currWidth = maxX - minX;
        double currHeight = maxY - minY;

        AffineTransform transform = graphics.getTransform();

        // change the start coordinates to left down corner
        transform.translate(0, height);

        // rotate to fourth quadrant (180 degrees about x-axis)
        // quadrant * Math.PI / 2.0
        transform.quadrantRotate(3);
        graphics.setTransform(transform);

        graphics.setColor(Color.black);
        graphics.setBackground(Color.white);

        // TODO: A - Resolve points over bounds
        // draw first city
        graphics.fillRect(
                    (int)(width*cities[tour[0]].X / currWidth)-1,
                    (int)(height*cities[tour[0]].Y / currHeight)-1, 3 , 3 );

        int i = 1;
        for( ; i < cities.length; i++)
        {
            graphics.fillRect(
                    (int)(width*cities[tour[i]].X / currWidth)-1,
                    (int)(height*cities[tour[i]].Y / currHeight)-1, 3 , 3 );
            
            graphics.drawLine (
                        (int)(width*cities[tour[i]].X / currWidth),
                        (int)(height*cities[tour[i]].Y / currHeight),
                        (int)(width*cities[tour[i - 1]].X / currWidth),
                        (int)(height*cities[tour[i - 1]].Y / currHeight)
                    );
        }

        // draw line between first and end city
        graphics.drawLine (
                        (int)(width*cities[tour[0]].X / currWidth),
                        (int)(height*cities[tour[0]].Y / currHeight),
                        (int)(width*cities[tour[i - 1]].X / currWidth),
                        (int)(height*cities[tour[i - 1]].Y / currHeight)
                    );

        try
        {
            File f = new File(path);
            if(path.contains("/"))
                new File(f.getParent()).mkdirs();
            BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream( f ) );

            ImageIO.write(image, "png", out);
            out.close();
        } catch (Exception e) {
            System.out.println("Chyba");
            e.printStackTrace();
        }
    }
    
}
