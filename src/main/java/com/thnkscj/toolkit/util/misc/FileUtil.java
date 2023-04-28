package com.thnkscj.toolkit.util.misc;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {

    public static List<String> getContents(String path) {
        List<String> list = null;
        try {
            list = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("File not found" + e);
        }

        return list;
    }

    public static void zipSingleFile(Path source, String zipFileName) throws IOException {

        if (!Files.isRegularFile(source)) {
            System.err.println("Please provide a file.");
            return;
        }

        try (
                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileName));
                FileInputStream fis = new FileInputStream(source.toFile())
        ) {

            ZipEntry zipEntry = new ZipEntry(source.getFileName().toString());
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            zos.closeEntry();
        }
    }

    public static void zipDirectory(File baseDirectory, File output) {
        try {
            FileOutputStream fos = new FileOutputStream(output);
            ZipOutputStream zos = new ZipOutputStream(fos);
            zos.setLevel(9);

            Deque<File> directories = new ArrayDeque<File>();
            if (baseDirectory.isDirectory()) {
                directories.add(baseDirectory);

                while (!directories.isEmpty()) {
                    File directory = directories.poll();
                    for (File current : directory.listFiles()) {
                        if (current.isDirectory()) {
                            directories.add(current);

                        } else if (current.isFile()) {
                            ZipEntry ze = new ZipEntry(current.getAbsolutePath()
                                    .substring(baseDirectory.getAbsolutePath().length() + 1));
                            zos.putNextEntry(ze);
                            FileInputStream in = new FileInputStream(current);

                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = in.read(buffer)) > 0) {
                                zos.write(buffer, 0, len);

                            }

                            in.close();
                        }
                    }
                }
            }

            zos.closeEntry();
            zos.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
    }

    public static String readInputStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line).append('\n');

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public String getFileContent(File srcFile) throws Exception {
        if ((srcFile != null) && srcFile.exists() && srcFile.isFile()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile)));
            StringBuffer contentOfFile = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                contentOfFile.append(line);
                return contentOfFile.toString();
            }
        }
        return null;
    }

}