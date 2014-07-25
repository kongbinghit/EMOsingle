package fileutil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FileOperateUtils {
	
	/**
	 * @description：得到目录下的所有文件的路径
	 * @param dir
	 *            :要分析的文件夹的路径
	 * @return：文件夹中所有文件的绝对路径集合
	 * @throws Exception
	 * @author tanshuguo
	 */
	// 
	static ArrayList allFilesPath = new ArrayList();

	public static ArrayList getAllFilesPath(File dir) {

		if (!dir.isDirectory()) {
			String filePath = dir.getAbsolutePath();
			System.out.println(filePath);
			allFilesPath.add(filePath);
		} else {
			File[] fs = dir.listFiles();
			for (int i = 0; i < fs.length; i++) {

				if (fs[i].isDirectory()) {
					try {
						getAllFilesPath(fs[i]);
					} catch (Exception e) {
					}
				} else {
					String filePath = fs[i].getAbsolutePath();
					System.out.println(filePath);
					allFilesPath.add(filePath);
				}
			}
		}
		System.out.println("Utils.getAllFilesPath-文件个数---->" + allFilesPath.size());
		return allFilesPath;
	}

	/**
	 * @description：得到文件内容
	 * @param filePath
	 *            :要读取的文件路径
	 * @return 返回文件内容
	 * @author tanshuguo
	 */
	public static String getFileContent(String filePath) {
		StringBuffer sb = new StringBuffer();
		InputStreamReader isr = null;
		BufferedReader bufferedReader = null;
		// String fileContent="";
		try {
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				isr = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				bufferedReader = new BufferedReader(isr);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					//System.out.println(lineTxt);
					sb.append(lineTxt);
				}

				isr.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		} finally {
			try {
				if (isr != null) {
					isr.close();
					isr = null;
				}
				if (bufferedReader != null) {
					bufferedReader.close();
					bufferedReader = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//System.out.println("--->" + sb.toString());
		// System.out.println("---->"+);
		return sb.toString();
	}

	/**
	 * @decription:把data写入targetFilePath中
	 * @param data
	 *            ：要写入的内容，采用编码为：utf-8
	 * @param targetFilePath
	 *            ：要写入到的文件路径
	 * @author tanshuguo
	 */
	public static void writeFile(String data, String targetFilePath) {
		OutputStreamWriter osw = null;
		BufferedWriter output = null;
		FileOutputStream fos=null;
		String encoding = "utf-8";
//		String encoding = "gbk";
//		String encoding = "gb2312";

		try {
			File file = new File(targetFilePath);
			if (file.exists()) {
				System.out.println("Utils.writeFile--文件存在，追加内容");
                fos=new FileOutputStream(file, true);
				osw = new OutputStreamWriter(fos,
						encoding);// 考虑到编码格式
				output = new BufferedWriter(osw);
				output.write(data + "\r\n");
			} else {
				System.out.println("Utils.writeFile--文件不存在--已创建");
				File parentOfFile = file.getParentFile();
				if (!parentOfFile.exists()) {
					parentOfFile.mkdirs();
					System.out.println("Utils--writeFile--存储文件父路径-->" + parentOfFile.getPath());

				}
				// file.mkdirs();
				file.createNewFile();// 不存在则创建
				fos=new FileOutputStream(file, true);
				osw = new OutputStreamWriter(fos,
						encoding);// 考虑到编码格式
				output = new BufferedWriter(osw);
				output.write(data + "\r\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
		
				if (output != null) {
					output.close();
					output = null;
				}
				if (osw != null) {
					osw.close();
					osw = null;
				}
				if (fos != null) {
					fos.close();
					fos = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 * @decription:把data写入targetFilePath中
	 * @param data
	 *            ：要写入的内容，采用编码为：utf-8
	 * @param targetFilePath
	 *            ：要写入到的文件路径
	 * @param encoding:写文件时要采用的编码格式
	 */
	public static void writeFile(String data, String targetFilePath,String encoding) {
		OutputStreamWriter osw = null;
		BufferedWriter output = null;
		FileOutputStream fos=null;
		//String encoding = "utf-8";
//		String encoding = "gbk";
//		String encoding = "gb2312";

		try {
			File file = new File(targetFilePath);
			if (file.exists()) {
				System.out.println("Utils.writeFile--文件存在，追加内容");
                fos=new FileOutputStream(file, true);
				osw = new OutputStreamWriter(fos,
						encoding);// 考虑到编码格式
				output = new BufferedWriter(osw);
				output.write(data + "\r\n");
			} else {
				System.out.println("Utils.writeFile--文件不存在--已创建");
				File parentOfFile = file.getParentFile();
				if (!parentOfFile.exists()) {
					parentOfFile.mkdirs();
					System.out.println("Utils--writeFile--存储文件父路径-->" + parentOfFile.getPath());

				}
				// file.mkdirs();
				file.createNewFile();// 不存在则创建
				fos=new FileOutputStream(file, true);
				osw = new OutputStreamWriter(fos,
						encoding);// 考虑到编码格式
				output = new BufferedWriter(osw);
				output.write(data + "\r\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
		
				if (output != null) {
					output.close();
					output = null;
				}
				if (osw != null) {
					osw.close();
					osw = null;
				}
				if (fos != null) {
					fos.close();
					fos = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) throws Exception {
		// 递归显示C盘下所有文件夹及其中文件
		File root = new File("E:");
		// getAllFilesPath(root);
		String filePath = "D:\\temp\\5341\\《女职工劳动保护特别规定》特刊\\2012\\6\\18951.txt";
		// getFileContent(filePath);
		writeFile("nihao",
				"D:\\temp\\5341_out_process\\产经广场\\2012\\7\\23298.txt");
	}

}