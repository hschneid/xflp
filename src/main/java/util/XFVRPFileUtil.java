package util;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 **/
public class XFVRPFileUtil {
	private final static CompressorStreamFactory FACTORY = new CompressorStreamFactory();

	public static void writeGZip(String content, String name){
		try {
			FileOutputStream fos = new FileOutputStream(new File(name+".txt.gz"));
			CompressorOutputStream out = 
					FACTORY.createCompressorOutputStream(CompressorStreamFactory.GZIP, fos);

			PrintWriter pw = new PrintWriter(out);
			pw.write(content);
			pw.flush();
			pw.close();
			fos.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CompressorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public static String readCompressedFile(String file){
		String out = null;
		File f = new File(file);
		try(
				FileInputStream fis = new FileInputStream(new File(file));
				BufferedInputStream bis = new BufferedInputStream(fis);
				InputStream in = (f.getName().endsWith(".gz")) ? FACTORY.createCompressorInputStream(bis) : bis;
				) {
			out = getStreamContents(in);
		} catch (IOException | CompressorException e) {
			e.printStackTrace();
		}
		return out;
	}

	public static String getStreamContents(InputStream stream) throws IOException {
		StringBuilder content = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

		String lineSeparator = System.getProperty("line.separator");

		try {
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line + lineSeparator);
			}
			return content.toString();

		} finally {
			reader.close();
		}
	}

	public static String getStreamContentsJava7_ISO(Path p) throws IOException{
		return new String(
				Files.readAllBytes(p),
				StandardCharsets.ISO_8859_1
				);
	}

	public static void main(String[] args) {
		writeGZip("This is a test", "test.txt");
		String out = readCompressedFile("test.txt");
		System.out.println(out);
	}
}
