package com.xuwen.demo.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;

public class IoUtil {
	/**
	 * 将输入流写入一个文件中
	 * 
	 * @param is
	 *            输入流
	 * @param file
	 *            文件File对象
	 * @throws IOException
	 */
	public static boolean inputStreamToFile(InputStream is, File file, boolean isClosedIs) throws IOException {
		if (file == null || file.isDirectory()) {
			return false;
		}
		BufferedInputStream bis = new BufferedInputStream(is);
		OutputStream os = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(os);
		byte[] buffer = new byte[1024];
		int i = 0;
		while ((i = bis.read(buffer)) != -1) {
			bos.write(buffer, 0, i);
		}
		bos.flush();
		bos.close();
		os.close();
		bis.close();
		if (isClosedIs) {
			is.close();
		}
		return true;
	}

	/**
	 * 将输入流写一个文件文件中
	 * 
	 * @param is
	 *            输入流
	 * @param fileFullName
	 *            文件全路径
	 * @param overWrite
	 *            是否重写这个文件
	 * @throws IOException
	 */
	public static boolean inputStreamToFile(InputStream is, String fileFullName, boolean isCreate, boolean overWrite,
			boolean isClosedIs) throws IOException {
		File file = FileUtil.getFileIsHisAndCreate(fileFullName, isCreate, overWrite);
		return inputStreamToFile(is, file, isClosedIs);
	}

	/**
	 * 将文件写入一个输出流
	 * 
	 * @param file
	 *            需要写出的文件
	 * @param os
	 *            输出流
	 * @throws IOException
	 * @注意：你需要自己关闭这个输出流
	 */
	public static boolean fileToOutputStream(File file, OutputStream os) throws IOException {
		if (file == null || file.isDirectory()) {
			return false;
		}
		InputStream is = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(is);
		BufferedOutputStream bos = new BufferedOutputStream(os);
		byte[] buffer = new byte[1024];
		int i = 0;
		// bos.write(buffer);
		while ((i = bis.read(buffer)) != -1) {
			bos.write(buffer, 0, i);
		}
		is.close();
		bis.close();
		bos.flush();
		os.flush();
		bos.close();
		bis.close();
		return false;
	}

	/**
	 * 将文件写入一个输出流
	 * 
	 * @param fileFullName
	 *            需要写出的文件的全路径
	 * @param os
	 *            输出流
	 * @throws IOException
	 * @注意：你需要自己关闭这个输出流
	 */
	public static boolean fileToOutputStream(String fileFullName, OutputStream os) throws IOException {
		File file = new File(fileFullName);
		return fileToOutputStream(file, os);
	}

	/**
	 * 读取文件中的字符串
	 * 
	 * @param filePath
	 *            文件路径
	 * @param fileName
	 *            文件名
	 * @param charset
	 *            编码格式
	 * @return
	 * @throws IOException
	 */
	public static String getStr2File(String filePath, String fileName, String charset) throws IOException {
		return getStr2File(new File(filePath + "/" + fileName), charset);
	}

	/**
	 * 读取文件中的字符串
	 * 
	 * @param fileFullPath
	 *            文件全路径
	 * @param charset
	 *            编码形式
	 * @return
	 * @throws IOException
	 */
	public static String getStr2File(String fileFullPath, String charset) throws IOException {
		return getStr2File(new File(fileFullPath), charset);
	}

	/**
	 * 读取文件中的字符串
	 * 
	 * @param file
	 *            文件
	 * @param charset
	 *            编码形式
	 * @return
	 * @throws IOException
	 */
	public static String getStr2File(File file, String charset) throws IOException {
		if (file == null || file.isDirectory()) {
			return null;
		}
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		InputStreamReader isr = new InputStreamReader(bis, Charset.forName(charset));
		BufferedReader br = new BufferedReader(isr);
		StringBuffer sb = new StringBuffer();
		while (br.ready()) {
			sb.append(br.readLine());
		}
		br.close();
		isr.close();
		bis.close();
		fis.close();

		return sb.toString();
	}

	
	public static File setStr2File(String str, File file,String charset,boolean append) throws IOException {
		if (file == null) {
			return null;
		}
		if (!file.exists() && !file.isFile()) {
			return null;
		}
		if(StringUtils.isBlank(charset)){
			charset="UTF-8";
		}
		OutputStream os = new FileOutputStream(file, append);
		BufferedOutputStream bos = new BufferedOutputStream(os);
		OutputStreamWriter osw = new OutputStreamWriter(bos, Charset.forName(charset));
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(str);
		bw.flush();
		osw.flush();
		bos.flush();
		os.flush();
		bw.close();
		osw.close();
		bos.close();
		os.close();
		return file;
	}

	/**
	 * 将字符串写入文件中
	 * 
	 * @param str
	 *            需要写入的字符串
	 * @param fileFullName
	 *            需要写入的文件的全路径
	 * @param overWrite
	 *            是否重写这个文件
	 * @return
	 * @throws IOException
	 */
	public static File setStr2File(String str, String fileFullName, boolean isCreate, boolean overWrite,boolean append)
			throws IOException {
		File file = FileUtil.getFileIsHisAndCreate(fileFullName, isCreate, overWrite);
		return setStr2File(str, file,"UTF-8",append);
	}


	public static boolean SetByte2File(byte[] bytes, File file) {
		if (file == null) {
			return false;
		}
		if (!file.exists() && !file.isFile()) {
			return false;
		}
		OutputStream os = null;
		BufferedOutputStream bos = null;
		try {
			os = new FileOutputStream(file);
			bos = new BufferedOutputStream(os);
			bos.write(bytes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				bos.flush();
				os.flush();
				bos.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;

	}

	/**
	 * 将一个文件写入到另一个文件中
	 * 
	 * @param sourceFileFullName
	 * @param descFileFullName
	 * @param overWrite
	 * @return
	 * @throws IOException
	 */
	public static File file2File(String sourceFileFullName, String descFileFullName, boolean isCreate,boolean overWrite
			) throws IOException {
		InputStream is = new FileInputStream(sourceFileFullName);
		inputStreamToFile(is, descFileFullName, isCreate, overWrite, true);
		return new File(descFileFullName);
	}

	/**
	 * 将一个文件写入到一个临时文件中并返回临时文件
	 * 
	 * @param sourceFileFullName
	 *            源文件完整名
	 * @param prefix
	 *            临时文件前缀
	 * @param suffix
	 *            临时文件后缀
	 * @param directory
	 * @return
	 * @throws IOException
	 */
	public static File file2TempFile(String sourceFileFullName, String prefix, String suffix, File directory)
			throws IOException {
		File tempFile = File.createTempFile(prefix, suffix, directory);
		return file2File(sourceFileFullName, tempFile.getPath(), true, false);
	}
	
	public static OutputStreamWriter getOutputStreamWriter(File file,String charsetName) throws FileNotFoundException, UnsupportedEncodingException{
		FileOutputStream fileOutputStream=new FileOutputStream(file);
		OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutputStream, charsetName);
		return outputStreamWriter;
	}
	
	public static OutputStreamWriter getOutputStreamWriter(OutputStream out,String charsetName) throws FileNotFoundException, UnsupportedEncodingException{
		OutputStreamWriter outputStreamWriter=new OutputStreamWriter(out, charsetName);
		return outputStreamWriter;
	}
	
	public static BufferedWriter getBufferedWriter(File file,String charsetName) throws FileNotFoundException, UnsupportedEncodingException{
		OutputStreamWriter outputStreamWriter=getOutputStreamWriter(file,charsetName);
		BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
		return bufferedWriter;
	}
}
