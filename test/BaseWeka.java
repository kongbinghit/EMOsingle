package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
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
			FileWriter writer=new FileWriter(new File("result.txt"));
			LibSVM m_classifier = new LibSVM();
			Classifier m_classifier2 = new NaiveBayes();
			Classifier m_classifier3 = new LibSVM();
	
			String[] options = weka.core.Utils
					.splitOptions("-S 0 -K 0 -D 3 -G 0.0078125 -R 0.0 -N 0.5 -M 40.0 -C 0.1 -E 0.001 -P 0.1 -B");
			m_classifier.setOptions(options);
			// for(int i=0;i<m_classifier.getOptions().length;i++)
			// System.out.println(m_classifier.getOptions()[i]);
			// m_classifier.setKNN(3);
			// System.out.println(m_classifier.getKNN());
			File inputFile = new File("trainfeature.arff"); // 训练语料文件
			ArffLoader atf = new ArffLoader();
			atf.setFile(inputFile);
			Instances instancesTrain = atf.getDataSet(); // 读入训练文件
			instancesTrain.deleteStringAttributes();
			
			inputFile = new File("tt.arff"); // 测试语料文件
			atf.setFile(inputFile);
			Instances instancesTest = atf.getDataSet(); // 读入测试文件 
			instancesTest.setClassIndex(0); //设置分类属性所在行号（第一行为 0 号），
			instancesTest.deleteStringAttributes(); // instancesTest.numAttributes()
			
		
			// 可以取得属性总数
			instancesTrain.setClassIndex(0);
			m_classifier.buildClassifier(instancesTrain); // 训练
			Evaluation eval = new Evaluation(instancesTrain); // 构造评价器

			eval.evaluateModel(m_classifier, instancesTest);// 用测试数据集来评价m_classifier
			System.out.println(eval.toSummaryString("=== Summary ===",
					false)); // 输出信息
			System.out.println(eval.toClassDetailsString()); // 输出信息
			System.out.println(eval
					.toMatrixString("=== Confusion Matrix ==="));// Confusion
			int k=0;
			while(k<instancesTest.numInstances()){
				
				System.out.println(instancesTrain.instance(k).toString()//向量
						+"\n"+Arrays.toString(m_classifier.distributionForInstance(instancesTest.instance(k)))//这个可能是置信度
						+"\n"+m_classifier.classifyInstance(instancesTest.instance(k)));//这个是分类结果,0从0开始的，0代表happiness~6代表surprise等。。。
				k++;
				
			}
			
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

}
