package org.seage.data;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class HashHelper {

  /**
   * This function is passed a File name and it returns a md5 hash of this file.
   * 
   * @param path File path to hash
   * @return The md5 string
   */
  public static String hashFromFile(String path) throws Exception {
    return hashFromFile(new File(path));
  }

  public static String hashFromFile(File fileToHash) throws Exception {
    return Files.asByteSource(fileToHash).hash(Hashing.goodFastHash(128)).toString();
  }

  public static String hashFromString(String stringToHash) throws Exception {
    return Hashing.goodFastHash(128)
      .hashString(stringToHash, StandardCharsets.UTF_8)
      .toString();
  }

  public static String sha256FromString(String stringToHash) throws Exception {
    return Hashing.sha256()
      .hashString(stringToHash, StandardCharsets.UTF_8)
      .toString();
  }
}
