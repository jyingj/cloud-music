package org.music.common.utils;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONObject;

/**
 * 文件操作相关工具类 
 * <p><b>classname:</b> FileUtil
 * <p><b>date:</b>  Jan 24, 2011 9:58:49 AM
 * <p><b>lastUpdate:</b>  Jan 24, 2011 9:58:49 AM
 * <p><b>version:</b>  1.0
 */
public final class FileUtil {
    
    private static final int RETRY_SLEEP_MILLIS = 10;
    private static File defaultTempDir;

    private FileUtil() {
    }

    /**
     * 文件路径分割符转换 window compatitable
     */
    public static String normalizePath(String path) {
        if (path != null && System.getProperty("os.name").startsWith("Windows") && path.indexOf("/") >= 0) {
            return path.replace('/', '\\');
        }
        return path;
    }
    
    /**
     * 获得临时文件目录
     * @param flag
     * @return
     */
    private static synchronized File getDefaultTempDir(String flag) {
        if (defaultTempDir != null
            && defaultTempDir.exists()) {
            return defaultTempDir;
        }
        
        String s = null;
        try {
            s = System.getProperty(FileUtil.class.getName() + ".TempDirectory");
        } catch (SecurityException e) {
            //Ignorable, we'll use the default
        }
        if (s == null) {
            int x = (int)(Math.random() * 1000000);
            s = System.getProperty("java.io.tmpdir");
            File checkExists = new File(s);
            if (!checkExists.exists()) {
                throw new RuntimeException("The directory " 
                                       + checkExists.getAbsolutePath() 
                                       + " does not exist, please set java.io.tempdir"
                                       + " to an existing directory");
            }
            File f = new File(s, flag+"-tmp-" + x);
            while (!f.mkdir()) {
                x = (int)(Math.random() * 1000000);
                f = new File(s, flag+"-tmp-" + x);
            }
            defaultTempDir = f;
            Thread hook = new Thread() {
                @Override
                public void run() {
                    removeDir(defaultTempDir);
                }
            };
            Runtime.getRuntime().addShutdownHook(hook);            
        } else {
            //assume someone outside of us will manage the directory
            File f = new File(s);
            f.mkdirs();
            defaultTempDir = f;
        }
        return defaultTempDir;
    }

    /**
     * 创建目录
     * @param dir
     */
    public static void mkDir(File dir) {
        if (dir == null) {
            throw new RuntimeException("dir attribute is required");
        }

        if (dir.isFile()) {
            throw new RuntimeException("Unable to create directory as a file "
                                    + "already exists with that name: " + dir.getAbsolutePath());
        }

        if (!dir.exists()) {
            boolean result = doMkDirs(dir);
            if (!result) {
                String msg = "Directory " + dir.getAbsolutePath()
                             + " creation was not successful for an unknown reason";
                throw new RuntimeException(msg);
            }
        }
    }

    /**
     * 创建指定目录，包括父目录
     */
    private static boolean doMkDirs(File f) {
        if (!f.mkdirs()) {
            try {
                Thread.sleep(RETRY_SLEEP_MILLIS);
                return f.mkdirs();
            } catch (InterruptedException ex) {
                return f.mkdirs();
            }
        }
        return true;
    }

    /**
     * 删除目录
     * @param d
     */
    public static void removeDir(File d) {
        String[] list = d.list();
        if (list == null) {
            list = new String[0];
        }
        for (int i = 0; i < list.length; i++) {
            String s = list[i];
            File f = new File(d, s);
            if (f.isDirectory()) {
                removeDir(f);
            } else {
                delete(f);
            }
        }
        delete(d);
    }

    /**
     * 删除文件
     * @param f
     */
    public static void delete(File f) {
        if (!f.delete()) {
            if (isWindows()) {
                System.gc();
            }
            try {
                Thread.sleep(RETRY_SLEEP_MILLIS);
            } catch (InterruptedException ex) {
                // Ignore Exception
            }
            if (!f.delete()) {
                f.deleteOnExit();
            }
        }
    }

    /**
     * 操作系统类型
     * @return
     */
    private static boolean isWindows() {
        String osName = System.getProperty("os.name").toLowerCase(Locale.US);
        return osName.indexOf("windows") > -1;
    }

    /**
     * 创建临时文件
     * @param prefix 文件前缀
     * @param suffix 文件后缀
     * @return
     * @throws IOException
     */
    public static File createTempFile(String prefix, String suffix) throws IOException {
        return createTempFile(prefix, suffix, null, false);
    }
    
    /**
     * 创建临时文件
     * @param prefix        文件前缀
     * @param suffix        文件后缀
     * @param parentDir     父目录
     * @param deleteOnExit  程序退出时是否删除删除
     * @return
     * @throws IOException
     */
    public static File createTempFile(String prefix, String suffix, File parentDir,
                               boolean deleteOnExit) throws IOException {
        File result = null;
        File parent = (parentDir == null)
            ? getDefaultTempDir("temp")
            : parentDir;
            
        if (suffix == null) {
            suffix = ".tmp";
        }
        if (prefix == null) {
            prefix = "temp";
        } else if (prefix.length() < 3) {
            prefix = prefix + "temp";
        }
        result = File.createTempFile(prefix, suffix, parent);

        //if parentDir is null, we're in our default dir
        //which will get completely wiped on exit from our exit
        //hook.  No need to set deleteOnExit() which leaks memory.
        if (deleteOnExit && parentDir != null) {
            result.deleteOnExit();
        }
        return result;
    }
    
    /**
     * 读取文本文件
     * @param location
     * @return
     */
    public static String getStringFromFile(File location) {
        InputStream is = null;
        String result = null;

        try {
            is = new FileInputStream(location);
            result = normalizeCRLF(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    //do nothing
                }
            }
        }

        return result;
    }

    /**
     * 控制符规格化
     * @param instream
     * @return
     */
    public static String normalizeCRLF(InputStream instream) {
        BufferedReader in = new BufferedReader(new InputStreamReader(instream));
        StringBuffer result = new StringBuffer();
        String line = null;

        try {
            line = in.readLine();
            while (line != null) {
                String[] tok = line.split("\\s");

                for (int x = 0; x < tok.length; x++) {
                    String token = tok[x];
                    result.append("  " + token);
                }
                line = in.readLine();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String rtn = result.toString();

        rtn = ignoreTokens(rtn, "<!--", "-->");
        rtn = ignoreTokens(rtn, "/*", "*/");
        return rtn;
    }
    
    /**
     * 忽略字符
     * @param contents   元字符串
     * @param startToken  忽略字符串开始
     * @param endToken    忽略字符串结束
     * @return
     */
    private static String ignoreTokens(final String contents, 
                                       final String startToken, final String endToken) {
        String rtn = contents;
        int headerIndexStart = rtn.indexOf(startToken);
        int headerIndexEnd = rtn.indexOf(endToken);
        if (headerIndexStart != -1 && headerIndexEnd != -1 && headerIndexStart < headerIndexEnd) {
            rtn = rtn.substring(0, headerIndexStart - 1)
                + rtn.substring(headerIndexEnd + endToken.length() + 1);
        }
        return rtn;
    }

    /**
     * 获取指定目录的文件列表
     * @param dir
     * @param pattern
     * @return
     */
    public static List<File> getFiles(File dir, final String pattern) {
        return getFiles(dir, pattern, null);
    }
    public static List<File> getFilesRecurse(File dir, final String pattern) {
        return getFilesRecurse(dir, pattern, null);
    }

    /**
     * 获取指定目录下的文件
     * @param dir   目录
     * @param pattern  模式串
     * @param exclude  不包含exclude指定的文件
     * @return
     */
    public static List<File> getFiles(File dir, final String pattern, File exclude) {
        return getFilesRecurse(dir, Pattern.compile(pattern), exclude, false, new ArrayList<File>());
    }
    public static List<File> getFilesRecurse(File dir, final String pattern, File exclude) {
        return getFilesRecurse(dir, Pattern.compile(pattern), exclude, true, new ArrayList<File>());    
    }
    private static List<File> getFilesRecurse(File dir, 
                                              Pattern pattern,
                                              File exclude, boolean rec,
                                              List<File> fileList) {
        for (File file : dir.listFiles()) {
            if (file.equals(exclude)) {
                continue;
            }
            if (file.isDirectory() && rec) {
                getFilesRecurse(file, pattern, exclude, rec, fileList);
            } else {
                Matcher m = pattern.matcher(file.getName());
                if (m.matches()) {
                    fileList.add(file);                                
                }
            }
        }
        return fileList;
    }

    /**
     * 按行读取文本文件
     * @param file 对应文本文件 
     * @return List<String> 文本行
     * @throws Exception
     */
    public static List<String> readLines(File file) throws Exception {
        if (!file.exists()) {
            return new ArrayList<String>();
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> results = new ArrayList<String>();
        String line = reader.readLine();
        while (line != null) {
            results.add(line);
            line = reader.readLine();
        }
        return results;
    }

    /**
     * 前且前导空白
     */
    public static String stripLeadingSeparator(String name) {
        if (name == null) {
            return null;
        }
        while (name.startsWith("/") || name.startsWith(File.separator)) {
            name = name.substring(1);
        }
        return name;
    }

    /**
     * 剪切尾部的分割符
     */
    public static String stripTrailingSeparator(String name) {
        if (name == null) {
            return null;
        }
        while (name.endsWith("/") || name.endsWith(File.separator)) {
            name = name.substring(0, name.length() - 1);
        }
        return name;
    }

    /**
     * 剪切首部的分隔符
     */
    public static String stripPath(String name) {
        if (name == null) {
            return null;
        }
        int pos = name.lastIndexOf("/");
        if (pos == -1) {
            pos = name.lastIndexOf(File.separator);
        }
        if (pos != -1) {
            return name.substring(pos + 1);
        }
        return name;
    }

    /**
     * 处理相对路径中出现的 ..
     * @param path
     * @return
     */
    public static String compactPath(String path) {
        // only normalize path if it contains .. as we want to avoid: path/../sub/../sub2 as this can leads to trouble
        if (path.indexOf("..") == -1) {
            return path;
        }

        Stack<String> stack = new Stack<String>();
        String[] parts = path.split(File.separator);
        for (String part : parts) {
            if (part.equals("..") && !stack.isEmpty()) {
                // only pop if there is a previous path
                stack.pop();
            } else {
                stack.push(part);
            }
        }

        // build path based on stack
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> it = stack.iterator(); it.hasNext();) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(File.separator);
            }
        }

        return sb.toString();
    }
    
    /**
	 * 往文件写数据
	 * @param filePath 文件完整路径 如:c:\alex\k1113k.txt
	 * @param content 要添加的内容
	 * @param addFlag 是否追加内容 true追加新内容 false覆盖原来的内容
	 * @return
	 */
	public static boolean writeFile(String filePath,String content,boolean addFlag){
	File file = new File(filePath);
	OutputStream os;
	try {
	    File parent = file.getAbsoluteFile().getParentFile();
	    if (!parent.exists()) {
		parent.mkdirs();
	    }
	    os = new FileOutputStream(file, addFlag);
	    OutputStreamWriter osr = new OutputStreamWriter(os);
	    BufferedWriter bw = new BufferedWriter(osr);
	    bw.write(content);
	    bw.close();
	    osr.close();
	    os.close();
	    return true;
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
    }
	
    // 根据传入的字节数判断文件的大小
    public static String parseFileSize(String str) {
	String size = "";
	try {
	    double fileSize = Double.parseDouble(str);
	    DecimalFormat df = new DecimalFormat("#.##");
	    int j = 0;
	    double i = 0.0;
	    for (i = fileSize; i > 1024.0; i = i / 1024.0) {
		j++;
	    }
	    size = df.format(i);
	    switch (j) {
	    case 1: {
		size = size + "KB";
		break;
	    }
	    case 2: {
		size = size + "MB";
		break;
	    }
	    case 3: {
		size = size + "GB";
		break;
	    }
	    case 4: {
		size = size + "TB";
		break;
	    }
	    default:
		size = size + "byte";
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return size;
    }

    /**
     * 根据文件名生成新的带日期的文件名
     * @param fileName
     * @return
     */
    public static String generateFileName(String fileName,String pattern) {
		DateFormat format = new SimpleDateFormat(pattern);
		String formatDate = format.format(new Date());
		int random = new Random().nextInt(10000);
		int position = fileName.lastIndexOf(".");
		String extension = fileName.substring(position);
		return formatDate + random + extension;
    }
    
    /**
     * 上传图片到指定目录
     * @param file
     * @param filePath
     */
    public static String upload(MultipartFile file,String filePath){
    	if (!file.isEmpty()) {
			String url = "";
			try {
				String rootPath = FileUtil.class.getResource("/").toURI().getPath();
				if (rootPath.startsWith("/") && rootPath.contains(":")) {
					rootPath = rootPath.substring(1);
				}
				rootPath = rootPath.substring(0,rootPath.indexOf("/WEB-INF"));
				String fileName = ""+new Date().getTime()+(int)(Math.random()*10000);
				String fileSuffix = "";
				if(file.getOriginalFilename() != null && file.getOriginalFilename().indexOf(".") != -1){
					fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
				}
				url = filePath + "/" + fileName + fileSuffix;
				File img = new File(rootPath + filePath);
				if(!img.exists()){
					img.mkdirs();
				}
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(
						new File(rootPath + filePath + "/" + fileName + fileSuffix)));
				out.write(file.getBytes());
				out.flush();
				out.close();
				return out(true, url);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return out(false,"上传失败," + e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				return out(false,"上传失败," + e.getMessage());
			}
		} else {
			return out(false,"上传失败，因为文件是空的.");
		}
    }
    
    public static String out(boolean result,String desc){
		JSONObject json = new JSONObject();
		json.put("result", result);
		json.put("desc", desc);
		return json.toString();
	}
    
    /**
     * 上传图片到指定目录
     * @param file
     * @param realPath
     * @param filePath
     * @return
     */
    public static String upload2(MultipartFile file,String realPath,String filePath){
    	if (!file.isEmpty()) {
			String url = "";
			try {
				String fileName = ""+new Date().getTime()+(int)(Math.random()*10000);
				String fileSuffix = "";
				if(file.getOriginalFilename() != null && file.getOriginalFilename().indexOf(".") != -1){
					fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
				}
				url = filePath + "/" + fileName + fileSuffix;
				File img = new File(realPath + filePath);
				if(!img.exists()){
					img.mkdirs();
				}
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(
						new File(realPath + filePath + "/" + fileName + fileSuffix)));
				out.write(file.getBytes());
				out.flush();
				out.close();
				return out(true, url);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return out(false,"上传失败," + e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				return out(false,"上传失败," + e.getMessage());
			}
		} else {
			return out(false,"上传失败，因为文件是空的.");
		}
    }
    
    //base64
    public static String upload(byte[] bs,String filePath,String fileSuffix){
    	if (bs!=null&&bs.length>0) {
			String url = "";
			try {
				String rootPath = FileUtil.class.getResource("/").toURI().getPath();
				if (rootPath.startsWith("/") && rootPath.contains(":")) {
					rootPath = rootPath.substring(1);
				}
				rootPath = rootPath.substring(0,rootPath.indexOf("/WEB-INF"));
				String fileName = ""+new Date().getTime()+(int)(Math.random()*10000);
				
				url = filePath + "/" + fileName + fileSuffix;
				File img = new File(rootPath + filePath);
				if(!img.exists()){
					img.mkdirs();
				}
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(
						new File(rootPath + filePath + "/" + fileName + fileSuffix)));
				out.write(bs);
				out.flush();
				out.close();
				return JsonUtil.out("1", url);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return JsonUtil.out("0","上传失败," + e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				return JsonUtil.out("0","上传失败," + e.getMessage());
			}
		} else {
			return JsonUtil.out("0","上传失败，因为文件是空的.");
		}
    }
    /**
   	 * 
   	 * @param fileNameList
   	 *            文件名数组
   	 * @param zipFileName
   	 *            压缩后文件名
   	 */
   	public static File ZipFiles(ArrayList<String> filePathList, String zipFilePath) {
   		File srcFile[] = new File[filePathList.size()];
   		for (int i = 0; i < filePathList.size(); i++) {
   			srcFile[i] = new File(filePathList.get(i));
   		}
   		File zipFile = new File(zipFilePath);
   		if(!zipFile.exists()){
   			zipFile.getParentFile().mkdirs();
   		}
   		byte[] buf = new byte[1024];
   		try {
   			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
   			for (int i = 0; i < srcFile.length; i++) {
   				FileInputStream in = new FileInputStream(srcFile[i]);
   				out.putNextEntry(new ZipEntry(srcFile[i].getName()));
   				int len;
   				while ((len = in.read(buf)) > 0) {
   					out.write(buf, 0, len);
   				}
   				out.closeEntry();
   				in.close();
   			}
   			out.close();
   		} catch (IOException e) {
   			e.printStackTrace();
   			return null;
   		}
   		return zipFile;
   	}
}
