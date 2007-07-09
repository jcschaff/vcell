package org.vcell.util;

import java.io.*;
import java.util.*;

/**
 * Insert the type's description here.
 * Creation date: (10/10/2002 11:57:56 AM)
 * @author: Ion Moraru
 */
public final class FileUtils {
	private static Random rand = new Random(System.currentTimeMillis());
/**
* Convienence method to copy a file from a source to a destination.
* Overwrite is prevented, and the last modified is kept, 4k buffer used.
*
* @throws IOException
*/
public static void copyFile(File sourceFile, File destFile) throws IOException {
	copyFile(sourceFile, destFile, false, true, 4 * 1024);
}
/**
* Method to copy a file from a source to a
* destination specifying if
* source files may overwrite newer destination files and the
* last modified time of <code>destFile</code> file should be made equal
* to the last modified time of <code>sourceFile</code>.
* Local/remote tuning by specifying number of bytes to use for buffering
* (use 2-4k for network, 8-16k for local copying)
*
* @throws IOException
*/
public static void copyFile(File sourceFile, File destFile, boolean overwrite, boolean preserveLastModified, int bufferSize) throws IOException {

    if (overwrite
        || !destFile.exists()
        || destFile.lastModified() < sourceFile.lastModified()) {

        if (destFile.exists() && destFile.isFile()) {
            destFile.delete();
        }

        // ensure that parent dir of dest file exists!
        // not using getParentFile method to stay 1.1 compat
        File parent = new File(destFile.getParent());
        if (!parent.exists()) {
            parent.mkdirs();
        }

        FileInputStream in = new FileInputStream(sourceFile);
        FileOutputStream out = new FileOutputStream(destFile);

        byte[] buffer = new byte[bufferSize];
        int count = 0;
        do {
            out.write(buffer, 0, count);
            count = in.read(buffer, 0, buffer.length);
        } while (count != -1);

        in.close();
        out.close();

        if (preserveLastModified) {
            destFile.setLastModified(sourceFile.lastModified());
        }
    }
}
/**
* Convienence method to copy a file from a source to a destination.
* Overwrite is prevented, and the last modified is kept, 4k buffer used.
*
* @throws IOException
*/
public static void copyFile(String sourceFile, String destFile) throws IOException {
	copyFile(new File(sourceFile), new File(destFile), false, true, 4 * 1024);
}
public static File createTempFile(String prefix, String suffix, File parentDir) {

    File result = null;
    java.text.DecimalFormat fmt = new java.text.DecimalFormat("#####");
    synchronized (rand) {
        do {
            result = new File(parentDir, prefix + fmt.format(rand.nextInt()) + suffix);
        } while (result.exists());
    }
    return result;
}
}
