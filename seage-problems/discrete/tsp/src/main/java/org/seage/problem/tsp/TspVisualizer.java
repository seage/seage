/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Jan Zmatlik - Initial implementation Richard Malek - Redesign
 */

package org.seage.problem.tsp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TspVisualizer.
 * 
 * @author Richard Malek
 */
public class TspVisualizer {
  protected static Logger logger = LoggerFactory.getLogger(TspVisualizer.class.getName());

  private static final int pointWidth = 2;
  private static final int pointHeight = 2;

  /** . */
  public static void createTourImage(City[] cities, Integer[] tour, String path, int width,
      int height) {

    boolean tourFromZero = Arrays.asList(tour).stream().anyMatch(t -> t == 0);
    if (!tourFromZero) {
      for (int i = 0; i < tour.length; i++) {
        tour[i] = tour[i] - 1;
      }
    }

    // getting current width ang height
    double minX = Double.MAX_VALUE;
    double minY = Double.MAX_VALUE;
    double maxX = Double.MIN_VALUE;
    double maxY = Double.MIN_VALUE;
    for (int i = 0; i < cities.length; i++) {
      if (cities[tour[i]].X < minX) {
        minX = cities[tour[i]].X;
      }
      if (cities[tour[i]].X > maxX) {
        maxX = cities[tour[i]].X;
      }
      if (cities[tour[i]].Y < minY) {
        minY = cities[tour[i]].Y;
      }
      if (cities[tour[i]].Y > maxY) {
        maxY = cities[tour[i]].Y;
      }
    }

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = (Graphics2D) image.getGraphics();

    graphics.setColor(Color.black);
    graphics.setBackground(Color.white);

    width -= pointWidth;
    height -= pointHeight;

    double currWidth = maxX - minX;
    double currHeight = maxY - minY;
    // TODO: B - Simplify the computation of X,Y coordinates.
    graphics.fillOval((int) (width * (cities[tour[0]].X - minX) / currWidth),
        height - (int) (height * (cities[tour[0]].Y - minY) / currHeight), pointWidth, pointHeight);

    int i = 1;
    for (; i < cities.length; i++) {
      graphics.fillOval((int) (width * (cities[tour[i]].X - minX) / currWidth),
          height - (int) (height * (cities[tour[i]].Y - minY) / currHeight), pointWidth,
          pointHeight);

      graphics.drawLine((int) (width * (cities[tour[i]].X - minX) / currWidth + pointWidth / 2.0),
          height - (int) (height * (cities[tour[i]].Y - minY) / currHeight - pointHeight / 2.0),
          (int) (width * (cities[tour[i - 1]].X - minX) / currWidth + pointWidth / 2.0), height
              - (int) (height * (cities[tour[i - 1]].Y - minY) / currHeight - pointHeight / 2.0));
    }

    // draw line between first and end city
    graphics.drawLine((int) (width * (cities[tour[0]].X - minX) / currWidth + pointWidth / 2.0),
        height - (int) (height * (cities[tour[0]].Y - minY) / currHeight - pointHeight / 2.0),
        (int) (width * (cities[tour[i - 1]].X - minX) / currWidth + pointWidth / 2.0),
        height - (int) (height * (cities[tour[i - 1]].Y - minY) / currHeight - pointHeight / 2.0));

    try {
      File f = new File(path);
      if (path.contains("/")) {
        new File(f.getParent()).mkdirs();
      }
      BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));

      ImageIO.write(image, "png", out);
      out.close();
    } catch (Exception ex) {
      logger.error("TspVisualizer failed", ex);
    }
  }

}
