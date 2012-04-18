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
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.data.file;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 *
 * @author Richard Malek
 */
public class FileHelper
{

    /**
    * This function is passed a File name and it returns a md5 hash of
    * this file.
    * @param FileToMd5
    * @return The md5 string
    */
    public static String md5fromFile(String path) throws Exception
    {
        return md5fromFile(new File(path));
    }
    
    public static String md5fromFile(File file) throws Exception
    {
        return md5(new FileInputStream(file));
    }

    public static String md5fromString(String stringToMd5) throws Exception
    {        
        return md5(new ByteArrayInputStream(stringToMd5.getBytes()));
    }

    private static String md5(InputStream is) throws Exception
    {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[8192];
        int read = 0;

        while( (read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
        }
        byte[] md5sum = digest.digest();
        BigInteger bigInt = new BigInteger(1, md5sum);
        String output = bigInt.toString(16);

        is.close();

        return output;
    }

    public static void copyfile(String srFile, String dtFile) throws Exception
    {
        File f1 = new File(srFile);
        File f2 = new File(dtFile);
        InputStream in = new FileInputStream(f1);

        //For Overwrite the file.
        OutputStream out = new FileOutputStream(f2);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0){
        out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static boolean deleteDirectory(File path) {
    if( path.exists() ) {
      File[] files = path.listFiles();
      for(int i=0; i<files.length; i++) {
         if(files[i].isDirectory()) {
           deleteDirectory(files[i]);
         }
         else {
           files[i].delete();
         }
      }
    }
    return( path.delete() );
  }

    public static void writeToFile(InputStream is, File file) throws Exception
    {

        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        int c;
        while((c = is.read()) != -1) {
                out.writeByte(c);
        }
        is.close();
        out.close();

    }

}
