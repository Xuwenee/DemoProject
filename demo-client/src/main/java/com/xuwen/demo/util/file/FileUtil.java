package com.xuwen.demo.util.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 封装文件工具类
 *
 * @author XuWen
 * @Date 2018/4/17 10:57
 */
public class FileUtil {
	
	/**
	 * 
	 * @author 许稳
	 * @date 2018/4/17 下午3:26:08
	 * @version 1.0
	 * @description 非递归获取目录下的所有文件（不包含目录中的目录）
	 *
	 * @param dirPath
	 * @return	不会为null
	 */
	public static List<File> getFileList(String dirPath){
		return getFileList(dirPath, null);
	}
	
	
	/**
	 * 
	 * @author 许稳
	 * @date 2018/4/17 下午3:37:01
	 * @version 1.0
	 * @description 非递归获取目录下的所有符合正则表达式的文件（不包含目录中的目录）
	 *
	 * @param dirPath	目录
	 * @param regex		当该正则表达式为""或者null的时候，则不会使用正则表达式匹配
	 * @return	不会为null
	 */
	public static List<File> getFileList(String dirPath,String regex){
		List<File> fileList=new ArrayList<File>();
		File fileDir=new File(dirPath);
		if(fileDir==null||!fileDir.exists()||fileDir.isFile()){
			return fileList;
		}
		File[] fileArray=fileDir.listFiles();
		if(fileArray==null||fileArray.length==0){
			return fileList;
		}
		for (File file: fileArray) {
			if(file.isFile()){
				String fileName=file.getName();
				if(StringUtils.isNotBlank(regex)){
					if(fileName.matches(regex)){
						fileList.add(file);
					}
				}else{
					fileList.add(file);
				}
			}
		}
		return fileList;
	}
	
	/**
	 * 
	 * @author 许稳
	 * @date 2018/4/17 下午3:39:14
	 * @version 1.0
	 * @description 非递归获取目录下的所有符合正则表达式的文件并按照文件名称进行排序
	 * （不包含目录中的目录）
	 *
	 * @param dirPath	目录
	 * @param regex		当该正则表达式为""或者null的时候，则不会使用正则表达式匹配
	 * @return 不会为null
	 */
	public static List<File> getSortFileList(String dirPath,String regex){
		List<File> fileList=getFileList(dirPath,regex);
		fileList.sort(new Comparator<File>() {

			public int compare(File o1, File o2) {
				if (o1.isDirectory() && o2.isFile())
		            return -1;
		        if (o1.isFile() && o2.isDirectory())
		            return 1;
		        return o1.getName().compareTo(o2.getName());
			}
		    
		});
		return fileList;
	}
	
	/**
	 * 
	 * @author 许稳
	 * @date 2018/4/17 下午3:39:00
	 * @version 1.0
	 * @description 非递归获取目录下的所有文件并按照文件名称进行排序（不包含目录中的目录）
	 *
	 * @param dirPath
	 * @return	不会为null
	 */
	public static List<File> getSortFileList(String dirPath){
		return getSortFileList(dirPath, null);
	}
	
	/**
	 * 创建文件
	 * <p>
	 * 用户传入需要创建文件的完整文件名(包括路径及文件名)：
	 * <p>
	 * 如果选择覆盖则会在文件存在的时候先删除文件
	 * <p>
	 * 如果文件已经存在则返回false；
	 * <p>
	 * 如果文件不存在先尝试创建目录，创建目录请参考mkDir(path)，如果目录创建失败返回false；
	 * <p>
	 * 如果目录创建成功则尝试创建文件，如果文件创建成功返回true，否则返回false
	 * 
	 * @param fileFullName
	 *            完整文件名(包括路径及文件名)
	 * @param overWrite
	 *            true为覆盖源文件，false为不覆盖源文件
	 * @return true or false
	 */
	public static boolean mkFile(String fileFullName, boolean overWrite) {
		File file = new File(fileFullName);
		return mkFile(file, overWrite);
	}
	
	/**
	 * 创建文件
	 * <p>
	 * 用户传入需要创建文件的完整文件名(包括路径及文件名)：
	 * <p>
	 * 如果选择覆盖则会在文件存在的时候先删除文件
	 * <p>
	 * 如果文件已经存在则返回false；
	 * <p>
	 * 如果文件不存在先尝试创建目录，创建目录请参考mkDir(path)，如果目录创建失败返回false；
	 * <p>
	 * 如果目录创建成功则尝试创建文件，如果文件创建成功返回true，否则返回false
	 * 
	 * @param fileFullName
	 *            完整文件名(包括路径及文件名)
	 * @param overWrite
	 *            true为覆盖源文件，false为不覆盖源文件
	 * @return true or false
	 */
	public static boolean mkFile(File file,boolean overWrite){
		if (!overWrite) {
			if (file.exists()) {
				return false;
			}
		} else {
			if (file.exists()) {
				file.delete();
			}
		}
		if (file.getParentFile().exists() && file.getParentFile().isDirectory()) {
			try {
				file.createNewFile();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			String path = file.getParent();
			if (mkDir(path)) {
				try {
					file.createNewFile();
					return true;
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 创建目录
	 * <p>
	 * 说明：用户传入一个完整的路径字符串：
	 * <p>
	 * 如果该完整路径存在返回true；
	 * <p>
	 * 如果该"完整路径"存在但是其实一个文件，返回false；
	 * <p>
	 * 如果该完整路径不存在则创建，如果创建成功返回true，创建失败返回false。
	 * <p>
	 * 注意：当递归创建目录失败，可能在服务器中可能存在部分路径。
	 * 
	 * @param path
	 *            完整路径字符串
	 * @return true or false
	 */
	public static boolean mkDir(String path) {
		File file = new File(path);
		if (file.exists()) {
			if (file.isFile()) {
				return false;
			}
			return true;
		} else {
			file.mkdirs();
			return true;
		}
	}

	/**
	 * 获得文件，没有则自动创建并获得新创建的文件
	 * <p>
	 * 具体创建过程请参考mkFile(String fileFullName)
	 * 
	 * @param fileFullName
	 *            文件完整名
	 * @param overWrite
	 *            是否覆盖，如果isCreate为false,该属性失效
	 * @param isCreate
	 *            是否创建
	 * @return file对象
	 */
	public static File getFileIsHisAndCreate(String fileFullName, boolean isCreate, boolean overWrite ) {
		File file = new File(fileFullName);
		if (isCreate && overWrite) {
			if (file.exists()) {
				file.delete();
			}
			return getFileIsHisAndCreate(fileFullName, isCreate,false);
		} else {
			if (file.exists()) {
				return file;
			}
			if (isCreate) {
				if (mkFile(fileFullName, false)) {
					return file;
				}
			}

		}
		return null;
	}
}
