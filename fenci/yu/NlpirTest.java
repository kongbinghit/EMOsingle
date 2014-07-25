package fenci.yu;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class NlpirTest {

	// 定义接口CLibrary，继承自com.sun.jna.Library
	public interface CLibrary extends Library {
		// 定义并初始化接口的静态变量 这一个语句是来加载dll的，注意dll文件的路径可以是绝对路径也可以是相对路径，只需要填写dll的文件名，不能加后缀。
		CLibrary Instance = (CLibrary) Native.loadLibrary(
				"NLPIR", CLibrary.class);

		// 初始化函数声明
		public int NLPIR_Init(byte[] sDataPath, int encoding,
				byte[] sLicenceCode);
//执行分词函数声明
		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);
//提取关键词函数声明
		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);
//退出函数声明
		public void NLPIR_Exit();
	}
}

