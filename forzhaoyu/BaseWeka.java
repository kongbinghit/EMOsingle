package forzhaoyu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class BaseWeka {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			
			List<String[]> list= new ArrayList<>();
			//FileReader filereader = new FileReader(new File("date/weibofenci.txt"));
			//BufferedReader reader = new BufferedReader(filereader);
			FileWriter writer=new FileWriter(new File("result.txt"));
			LibSVM m_classifier = new LibSVM();
			//Classifier m_classifier2 = new NaiveBayes();
			//Classifier m_classifier3 = new LibSVM();
	
			String[] options = weka.core.Utils
					.splitOptions("-S 0 -K 0 -D 3 -G 0.0078125 -R 0.0 -N 0.5 -M 40.0 -C 0.1 -E 0.001 -P 0.1 -B");
			m_classifier.setOptions(options);
			// for(int i=0;i<m_classifier.getOptions().length;i++)
			// System.out.println(m_classifier.getOptions()[i]);
			// m_classifier.setKNN(3);
			// System.out.println(m_classifier.getKNN());
			//File inputFile = new File("newtrain.arff"); // 训练语料文件
			//ArffLoader atf = new ArffLoader();
			//atf.setFile(inputFile);
			//Instances instancesTrain = atf.getDataSet(); // 读入训练文件
			//instancesTrain.deleteStringAttributes();
			
			File inputFile = new File("tt.arff"); // 测试语料文件
			ArffLoader atf = new ArffLoader();
			atf.setFile(inputFile);
			Instances instancesTest = atf.getDataSet(); // 读入测试文件 
			instancesTest.setClassIndex(0); //设置分类属性所在行号（第一行为 0 号），
			instancesTest.deleteStringAttributes(); // instancesTest.numAttributes()
			
		
			// 可以取得属性总数
			instancesTest.setClassIndex(0);
//			m_classifier.buildClassifier(instancesTrain); // 训练
//			
//			ObjectOutputStream svm = new ObjectOutputStream(new FileOutputStream("weiboSVM.model"));
//			svm.writeObject(m_classifier);
//			svm.flush();
//			svm.close();
//			System.out.println("done");
			
			//Classifier cls = (Classifier) weka.core.SerializationHelper.read("weiboSVM.model");
			
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("weiboSVM.model"));
				Classifier cls = (Classifier) ois.readObject();
				ois.close();
			
			Evaluation eval = new Evaluation(instancesTest); // 构造评价器

			eval.evaluateModel(cls, instancesTest);// 用测试数据集来评价m_classifier
			System.out.println(eval.toSummaryString("=== Summary ===",
					false)); // 输出信息
			System.out.println(eval.toClassDetailsString()); // 输出信息
			System.out.println(eval
					.toMatrixString("=== Confusion Matrix ==="));// Confusion
			int k=0;
			String string = "";
			String emotion[] = {"happiness","like","anger","sadness","fear","disgust","surprise","none"};
			while(k<instancesTest.numInstances()){
				//string = reader.readLine();
//				System.out.println(string);
//				System.out.println(instancesTrain.instance(k).toString()//向量
//						+"\n"+Arrays.toString(m_classifier.distributionForInstance(instancesTest.instance(k)))//这个可能是置信度
//						+"\n"+m_classifier.classifyInstance(instancesTest.instance(k)));//这个是分类结果,0从0开始的，0代表happiness~6代表surprise等。。。
				//writer.write(string+"\r\n");
				writer.write(cls.classifyInstance(instancesTest.instance(k))+Arrays.toString(cls.distributionForInstance(instancesTest.instance(k)))//这个可能是置信度
						+"\r\n");
				String id = "1";
				
				list.add(new String[]{id,emotion[(int)cls.classifyInstance(instancesTest.instance(k))],
				          Double.toString(cls.distributionForInstance(instancesTest.instance(k))[(int)cls.classifyInstance(instancesTest.instance(k))])});
				
				k++;
				
			}
			//System.out.println(list.get(0)[2]);
			
//		
//				System.out.println("------classification precision:"
//						+ (right / sum));
			
//				instancesTrain=instancesTraintemp;													// Matrix
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static  List<String[]> emotionClassify(String ttpath){
		try {
			List<String[]> list= new ArrayList<>();
			//FileReader filereader = new FileReader(new File("date/weibofenci.txt"));
			//BufferedReader reader = new BufferedReader(filereader);
			//FileWriter writer=new FileWriter(new File(path));
			LibSVM m_classifier = new LibSVM();
			//Classifier m_classifier2 = new NaiveBayes();
			//Classifier m_classifier3 = new LibSVM();
	
			String[] options = weka.core.Utils
					.splitOptions("-S 0 -K 0 -D 3 -G 0.0078125 -R 0.0 -N 0.5 -M 40.0 -C 0.1 -E 0.001 -P 0.1 -B");
			m_classifier.setOptions(options);
			// for(int i=0;i<m_classifier.getOptions().length;i++)
			// System.out.println(m_classifier.getOptions()[i]);
			// m_classifier.setKNN(3);
			// System.out.println(m_classifier.getKNN());
			//File inputFile = new File("newtrain.arff"); // 训练语料文件
			//ArffLoader atf = new ArffLoader();
			//atf.setFile(inputFile);
			//Instances instancesTrain = atf.getDataSet(); // 读入训练文件
			//instancesTrain.deleteStringAttributes();
			
			//File inputFile = new File("tt.arff"); // 测试语料文件
			File inputFile = new File(ttpath); // 测试语料文件
			ArffLoader atf = new ArffLoader();
			atf.setFile(inputFile);
			Instances instancesTest = atf.getDataSet(); // 读入测试文件 
			instancesTest.setClassIndex(0); //设置分类属性所在行号（第一行为 0 号），
			instancesTest.deleteStringAttributes(); // instancesTest.numAttributes()
			
		
			// 可以取得属性总数
			instancesTest.setClassIndex(0);
//			m_classifier.buildClassifier(instancesTrain); // 训练
//			
//			ObjectOutputStream svm = new ObjectOutputStream(new FileOutputStream("weiboSVM.model"));
//			svm.writeObject(m_classifier);
//			svm.flush();
//			svm.close();
//			System.out.println("done");
			
			//Classifier cls = (Classifier) weka.core.SerializationHelper.read("weiboSVM.model");
			
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("weiboSVM.model"));
				Classifier cls = (Classifier) ois.readObject();
				ois.close();
			
			Evaluation eval = new Evaluation(instancesTest); // 构造评价器

			eval.evaluateModel(cls, instancesTest);// 用测试数据集来评价m_classifier
			System.out.println(eval.toSummaryString("=== Summary ===",
					false)); // 输出信息
			System.out.println(eval.toClassDetailsString()); // 输出信息
			System.out.println(eval
					.toMatrixString("=== Confusion Matrix ==="));// Confusion
			int k=0;
			String emotion[] = {"happiness","like","anger","sadness","fear","disgust","surprise","none"};
			while(k<instancesTest.numInstances()){
				//string = reader.readLine();
//				System.out.println(string);
//				System.out.println(instancesTrain.instance(k).toString()//向量
//						+"\n"+Arrays.toString(m_classifier.distributionForInstance(instancesTest.instance(k)))//这个可能是置信度
//						+"\n"+m_classifier.classifyInstance(instancesTest.instance(k)));//这个是分类结果,0从0开始的，0代表happiness~6代表surprise等。。。
				//writer.write(string+"\r\n");
				//writer.write(cls.classifyInstance(instancesTest.instance(k))+Arrays.toString(cls.distributionForInstance(instancesTest.instance(k)))//这个可能是置信度
						//+"\r\n");
				list.add(new String[]{emotion[(int)cls.classifyInstance(instancesTest.instance(k))],
				          Double.toString(cls.distributionForInstance(instancesTest.instance(k))[(int)cls.classifyInstance(instancesTest.instance(k))])});
				k++;
				
			}
			
//		
//				System.out.println("------classification precision:"
//						+ (right / sum));
			
//				instancesTrain=instancesTraintemp;													// Matrix
			//writer.flush();
			//writer.close();
			return list;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

}
