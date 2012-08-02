/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 *     Richard Malek
 *     - Redesign
 */
package org.seage.problem.tsp;

import java.awt.Color;
import java.awt.Graphics2D;
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
    private static final int _pWidth = 4;
    private static final int _pHeight = 4;

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

        graphics.setColor(Color.black);
        graphics.setBackground(Color.white);

        width -=_pWidth;
        height -=_pHeight;

        //TODO: B - Simplify the computation of X,Y coordinates.
        graphics.fillOval(
                    (int)(width*(cities[tour[0]].X-minX) / currWidth),
                    height-(int)(height*(cities[tour[0]].Y-minY) / currHeight), _pWidth , _pHeight );

        int i = 1;
        for( ; i < cities.length; i++)
        {
            graphics.fillOval(
                    (int)(width*(cities[tour[i]].X-minX) / currWidth),
                    height-(int)(height*(cities[tour[i]].Y-minY) / currHeight), _pWidth , _pHeight );
            
            graphics.drawLine (
                        (int)(width*(cities[tour[i]].X-minX) / currWidth+_pWidth/2),
                        height-(int)(height*(cities[tour[i]].Y-minY) / currHeight-_pHeight/2),
                        (int)(width*(cities[tour[i - 1]].X-minX) / currWidth+_pWidth/2),
                        height-(int)(height*(cities[tour[i - 1]].Y-minY) / currHeight-_pHeight/2)
                    );
        }

        // draw line between first and end city
        graphics.drawLine (
                        (int)(width*(cities[tour[0]].X-minX) / currWidth+_pWidth/2),
                        height-(int)(height*(cities[tour[0]].Y-minY) / currHeight-_pHeight/2),
                        (int)(width*(cities[tour[i - 1]].X-minX) / currWidth+_pWidth/2),
                        height-(int)(height*(cities[tour[i - 1]].Y-minY) / currHeight-_pHeight/2)
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
