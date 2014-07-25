
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Class文件加解密操作类
 * @author Mars
 *
 */
public class FileUtil {
	private static final Logger logger = Logger.getLogger(FileUtil.class);

	/**
	 * 加密 Properties文件名
	 */
	public static final String algorithmFile = System.getProperty("user.dir") + File.separatorChar +"app_config" + File.separatorChar + "algorithm.properties";
	
	/**
	 * 所有被加密的class文件状态集合
	 */
	public static Map<String,Boolean> encryptStateMap = null;
	
	/**
	 * 缓存配置文件
	 */
	private static Properties resources = null;
	
	static {
		resources = new Properties();
		InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(algorithmFile));
			resources.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			logger.error("Properties file throws FileNotFoundException. " + e.getMessage());
		} catch (IOException e) {
			logger.error("Properties file throws IOException. " + e.getMessage());
		} finally {
			in = null;
		}
	}

	/**
	 * 读取源文件内容
	 * 
	 * @param filename
	 *            String 文件路径
	 * @throws IOException
	 * @return byte[] 文件内容
	 */
	public static byte[] readFile(String filename) throws IOException {

		File file = new File(filename);
		if (filename == null || filename.equals("")) {
			throw new NullPointerException("无效的文件路径");
		}
		long len = file.length();
		byte[] bytes = new byte[(int) len];

		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
		int r = bufferedInputStream.read(bytes);
		if (r != len)
			throw new IOException("读取文件不正确");
		bufferedInputStream.close();

		return bytes;

	}
	
	/**
	 * 读取源文件内容
	 * 
	 * @param filename
	 *            String 文件路径
	 * @throws IOException
	 * @return byte[] 文件内容
	 */
	public static String readFileValue(String filename) throws IOException {		
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		File file = new File(filename);
		if (filename == null || filename.equals("")) {
			throw new NullPointerException("无效的文件路径");
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
		}
		reader.close();
		return stringBuilder.toString();
	}
	
	/**
	 * 将加密的数据写入文件
	 * 
	 * @param data
	 *            byte[]
	 * @throws IOException
	 */
	public static void writeFile(byte[] data, String filename) throws IOException {
		File file = new File(filename);
		file.getParentFile().mkdirs();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(
		        file));		
		bufferedOutputStream.write(data);
		bufferedOutputStream.close();		
 	}
	
	/**
	 * 将加密的数据写入文件
	 * 
	 * @param data
	 *            String[]
	 * @throws IOException
	 */
	public static void writeFile(String data, String filename) throws IOException {
		File file = new File(filename);
		file.getParentFile().mkdirs();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(
		        file));		
		bufferedOutputStream.write(data.getBytes());
		bufferedOutputStream.close();		
 	}
	
	public native void writeFile(String data);
	public native String readFile();

	/**
	 * 从jar文件里读取class
	 * 
	 * @param filename
	 *            String
	 * @throws IOException
	 * @return byte[]
	 */
	public static byte[] readFileJar(String jarName, String filename) throws IOException {
		JarFile jarFile = new JarFile(jarName);
		JarEntry entry = jarFile.getJarEntry(filename);

		BufferedInputStream bufferedInputStream = new BufferedInputStream(
		        jarFile.getInputStream(entry));
		int len = bufferedInputStream.available();
		byte[] bytes = new byte[len];
		int r = bufferedInputStream.read(bytes);
		if (len != r) {
			bytes = null;
			throw new IOException("读取文件不正确");
		}
		bufferedInputStream.close();
		return bytes;
	}

	/**
	 * 获得密码生成法则
	 * 
	 * @return String
	 */
	public static String getAlgorithm() {
		return resources.getProperty("algorithm");
	}

	/**
	 * 获得值
	 * 
	 * @param skey
	 *            String
	 * @return String
	 */
	public static String getValue(String skey) {
		return resources.getProperty(skey);
	}

	/**
	 * @param path
	 *            文件路径
	 * @param suffix
	 *            后缀名, 为空则表示所有文件
	 * @param isdepth
	 *            是否遍历子目录
	 * @return list
	 */
	public static List<String> getListFiles(String path, String suffix, boolean isdepth) {
		List<String> lstFileNames = new ArrayList<String>();
		File file = new File(path);
		return listFile(lstFileNames, file, suffix, isdepth);
	}

	private static List<String> listFile(List<String> lstFileNames, File f, String suffix,
	        boolean isdepth) {
		// 若是目录, 采用递归的方法遍历子目录
		if (f.isDirectory()) {
			File[] t = f.listFiles();

			for (int i = 0; i < t.length; i++) {
				if (isdepth || t[i].isFile()) {
					listFile(lstFileNames, t[i], suffix, isdepth);
				}
			}
		} else {
			String filePath = f.getAbsolutePath();
			if (!suffix.equals("")) {
				int begIndex = filePath.lastIndexOf("."); // 最后一个.(即后缀名前面的.)的索引
				String tempsuffix = "";

				if (begIndex != -1) {
					tempsuffix = filePath.substring(begIndex + 1, filePath.length());
					if (tempsuffix.equals(suffix)) {
						lstFileNames.add(filePath);
					}
				}
			} else {
				lstFileNames.add(filePath);
			}
		}
		return lstFileNames;
	}

	/**
	 * BASE64解密
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) throws Exception {   
	    return (new BASE64Decoder()).decodeBuffer(key);   
	}   	
	
	/**  
	 * BASE64加密  
	 *   
	 * @param key  
	 * @return  
	 * @throws Exception  
	 */  
	public static String encryptBASE64(byte[] key) throws Exception {   
	    return (new BASE64Encoder()).encodeBuffer(key);   
	}
	
	  /**  
     * 转换密钥<br>  
     *   
     * @param key  
     * @return  
     * @throws Exception  
     */  
    public static Key toKey(String keyfile) throws Exception {     
    	// 从密钥文件key Filename中得到密钥数据
    	String rawKeyData = FileUtil.readFileValue(keyfile);    	
    	
     	//还原密钥
     	Key secretKeySpec = new SecretKeySpec(decryptBASE64(rawKeyData), FileUtil.getAlgorithm());        
        return secretKeySpec;   
    }   
	
    /**将二进制转换成16进制 
     * @param buf 
     * @return 
     */  
    public static String parseByte2HexStr(byte buf[]) {  
            StringBuffer sb = new StringBuffer();  
            for (int i = 0; i < buf.length; i++) {  
                    String hex = Integer.toHexString(buf[i] & 0xFF);  
                    if (hex.length() == 1) {  
                            hex = '0' + hex;  
                    }  
                    sb.append(hex.toUpperCase());  
            }  
            return sb.toString();  
    }  
    
    /**将16进制转换为二进制 
     * @param hexStr 
     * @return 
     */  
    public static byte[] parseHexStr2Byte(String hexStr) {  
            if (hexStr.length() < 1)  
                    return null;  
            byte[] result = new byte[hexStr.length()/2];  
            for (int i = 0;i< hexStr.length()/2; i++) {  
                    int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
                    int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
                    result[i] = (byte) (high * 16 + low);  
            }  
            return result;  
    }  
    
    /**
	 * 验证class文件是否可以加密
	 * 
	 * @param fileName
	 * @return
	 */
    public static boolean validateEncrypt(String fileName) {
		if (fileName.indexOf("encryptprocess") != -1 || fileName.indexOf("asm") != -1 || fileName.indexOf("springframework\\util") != -1 || fileName.indexOf("core") != -1) {
			return false;
		}
		return true;
	}
	
	/**
	 * 根据class文件的详细地址获取加密Map的key值
	 * @param urlPath
	 * @return
	 */
	public static String getEncryptMapKey(String urlPath){
		
		Set<Entry<String, Boolean>> mapEntry = encryptStateMap.entrySet();
		String encryptMapKey = "NoKey";
		
		if(urlPath.indexOf("jar:file")!=-1){
			int start = urlPath.indexOf("jar!");
			urlPath = FileUtil.getValue("sourcePath")+urlPath.substring(start+4);
		}
		
		for(Entry<String, Boolean> key:mapEntry){
			if(urlPath.indexOf(key.getKey())!=-1){
				encryptMapKey = key.getKey();
				break;
			}
		}
		return encryptMapKey;
	}	
	
	public static void main(String args[]) {		
		
	}
}
