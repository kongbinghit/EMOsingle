package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.Id3;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class TestWeka {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			FileWriter writer=new FileWriter(new File("result.txt"));
			Classifier m_classifier = new LibSVM();
			Classifier m_classifier2 = new NaiveBayes();
			Classifier m_classifier3 = new Id3();
			Classifier m_classifier4 = new IBk(3);
			// String []options={"-S","0","-K"
			// ,"0","-D","3","-G","0.0","-R","0.0","-N","0.5","-M","40.0","-C","1.0","-E","0.001","-P","0.1","-seed","1"};
			// options[0]="-S";
			// options[1]="1";
			String[] options = weka.core.Utils
					.splitOptions("-S 0 -K 0 -D 3 -G 0.0078125 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1 -B -H -q");
			m_classifier.setOptions(options);
			// for(int i=0;i<m_classifier.getOptions().length;i++)
			// System.out.println(m_classifier.getOptions()[i]);
			// m_classifier.setKNN(3);
			// System.out.println(m_classifier.getKNN());
			File inputFile = new File("trainData4000-weight.feature.arff"); // 训练语料文件
			ArffLoader atf = new ArffLoader();
			atf.setFile(inputFile);
			Instances instancesTrain = atf.getDataSet(); // 读入训练文件
			instancesTrain.deleteStringAttributes();
			
			inputFile = new File("testData10000-weight.feature.arff"); // 测试语料文件
			atf.setFile(inputFile);
			Instances instancesTest = atf.getDataSet(); // 读入测试文件
			instancesTest.setClassIndex(0); // 设置分类属性所在行号（第一行为 0 号），
			instancesTest.deleteStringAttributes(); // instancesTest.numAttributes()
			
			inputFile = new File("sentenceunlabel.feature.arff"); // 测试语料文件
			atf.setFile(inputFile);
			Instances instancesUnlabel = atf.getDataSet(); // 读入测试文件
			instancesUnlabel.setClassIndex(0);
			instancesUnlabel.deleteStringAttributes();
			// 可以取得属性总数
			double sum = instancesUnlabel.numInstances(), // Unlabel语料实例数
			right = 0.0f;
			instancesTrain.setClassIndex(0);
			System.out.println(instancesTrain.numDistinctValues(0));
			
//			int likenum = 0,happinessnum = 0,angernum = 0,fearnum = 0,sadnessnum = 0, disgustnum = 0, surprisenum = 0,nonenum = 0;
//			for(int k=0;k<instancesTrain.numInstances();k++){
//				if(instancesTrain.instance(k).stringValue(0).toString().equals("like")){
//					likenum++;
//				}
//				else if(instancesTrain.instance(k).stringValue(0).toString().equals("happiness")){
//					happinessnum++;
//				}
//				else if(instancesTrain.instance(k).stringValue(0).toString().equals("disgust")){
//					disgustnum++;
//				}
//				else if(instancesTrain.instance(k).stringValue(0).toString().equals("anger")){
//					angernum++;
//				}
//				else if(instancesTrain.instance(k).stringValue(0).toString().equals("sadness")){
//					sadnessnum++;
//				}
//				else if(instancesTrain.instance(k).stringValue(0).toString().equals("fear")){
//					fearnum++;
//				}
//				else if(instancesTrain.instance(k).stringValue(0).toString().equals("surprise")){
//					surprisenum++;
//				}
//				else{
//					nonenum++;
//				}
//			}
//			int n[]=new int[8];
//			Instances instancesTraintemp= new Instances(instancesTrain);
//			instancesTraintemp.delete();
//			int k=0;
//			while(n[0]<fearnum||n[1]<fearnum||n[2]<fearnum||n[3]<fearnum||n[4]<fearnum||n[5]<fearnum||n[5]<fearnum||n[6]<fearnum||n[7]<fearnum){
//				
//				if(n[0]<fearnum&&instancesTrain.instance(k).stringValue(0).toString().equals("like")){
//					n[0]++;
//					instancesTraintemp.add(instancesTrain.instance(k));
//				}
//				else if(n[1]<fearnum&&instancesTrain.instance(k).stringValue(0).toString().equals("happiness")){
//					instancesTraintemp.add(instancesTrain.instance(k));
//					n[1]++;
//				}
//				else if(n[2]<fearnum&&instancesTrain.instance(k).stringValue(0).toString().equals("disgust")){
//					instancesTraintemp.add(instancesTrain.instance(k));
//					n[2]++;
//				}
//				else if(n[3]<fearnum&&instancesTrain.instance(k).stringValue(0).toString().equals("anger")){
//					instancesTraintemp.add(instancesTrain.instance(k));
//					n[3]++;
//				}
//				else if(n[4]<fearnum&&instancesTrain.instance(k).stringValue(0).toString().equals("sadness")){
//					instancesTraintemp.add(instancesTrain.instance(k));
//					n[4]++;
//				}
//				else if(n[5]<fearnum&&instancesTrain.instance(k).stringValue(0).toString().equals("fear")){
//					instancesTraintemp.add(instancesTrain.instance(k));
//					n[5]++;
//				}
//				else if(n[6]<fearnum&&instancesTrain.instance(k).stringValue(0).toString().equals("surprise")){
//					instancesTraintemp.add(instancesTrain.instance(k));
//					n[6]++;
//				}
//				else if(n[7]<fearnum&&instancesTrain.instance(k).stringValue(0).toString().equals("none")){
//					instancesTraintemp.add(instancesTrain.instance(k));
//					n[7]++;
//				}
//				
//				k++;
//			}
//			System.out.println(instancesTraintemp.numInstances());
//			System.out.println(likenum);
			int i = 0;
			int likenum = 0,happinessnum = 0,angernum = 0,fearnum = 0,sadnessnum = 0, disgustnum = 0, surprisenum = 0,nonenum = 0;

			while (fearnum<=500) {
				likenum = 0;happinessnum = 0;angernum = 0;fearnum = 0;sadnessnum = 0; disgustnum = 0;surprisenum = 0;nonenum = 0;
				for(int k=0;k<instancesTrain.numInstances();k++){
					if(instancesTrain.instance(k).stringValue(0).toString().equals("like")){
						likenum++;
					}
					else if(instancesTrain.instance(k).stringValue(0).toString().equals("happiness")){
						happinessnum++;
					}
					else if(instancesTrain.instance(k).stringValue(0).toString().equals("disgust")){
						disgustnum++;
					}
					else if(instancesTrain.instance(k).stringValue(0).toString().equals("anger")){
						angernum++;
					}
					else if(instancesTrain.instance(k).stringValue(0).toString().equals("sadness")){
						sadnessnum++;
					}
					else if(instancesTrain.instance(k).stringValue(0).toString().equals("fear")){
						fearnum++;
					}
					else if(instancesTrain.instance(k).stringValue(0).toString().equals("surprise")){
						surprisenum++;
					}
					else{
						nonenum++;
					}
				}
				int n[]=new int[8];
				Instances instancesTraintemp= new Instances(instancesTrain);
				instancesTraintemp.delete();
				int k=0;
				while(k<instancesTrain.numInstances()){
					
					if(n[0]<fearnum&&instancesTrain.instance(k).stringValue(0).toString().equals("like")){
						n[0]++;
						instancesTraintemp.add(instancesTrain.instance(k));
					}
					else if(n[1]<fearnum&&instancesTrain.instance(k).stringValue(0).toString().equals("happiness")){
						instancesTraintemp.add(instancesTrain.instance(k));
						n[1]++;
					}
					else if(n[2]<fearnum&&instancesTrain.instance(k).stringValue(0).toString().equals("disgust")){
						instancesTraintemp.add(instancesTrain.instance(k));
						n[2]++;
					}
					else if(n[3]<fearnum&&instancesTrain.instance(k).stringValue(0).toString().equals("anger")){
						instancesTraintemp.add(instancesTrain.instance(k));
						n[3]++;
					}
					else if(n[4]<fearnum&&instancesTrain.instance(k).stringValue(0).toString().equals("sadness")){
						instancesTraintemp.add(instancesTrain.instance(k));
						n[4]++;
					}
					else if(n[5]<fearnum&&instancesTrain.instance(k).stringValue(0).toString().equals("fear")){
						instancesTraintemp.add(instancesTrain.instance(k));
						n[5]++;
					}
					else if(n[6]<fearnum&&instancesTrain.instance(k).stringValue(0).toString().equals("surprise")){
						instancesTraintemp.add(instancesTrain.instance(k));
						n[6]++;
					}
					else if(n[7]<fearnum&&instancesTrain.instance(k).stringValue(0).toString().equals("none")){
						instancesTraintemp.add(instancesTrain.instance(k));
						n[7]++;
					}
					k++;
					
				}
				System.out.println(instancesTraintemp.numInstances());
				System.out.println(fearnum);
				right = 0.0f;
//				Instances instancesTraintemp= new Instances(instancesTrain);
//				instancesTraintemp.delete();
				
				sum = instancesUnlabel.numInstances(); // 测试语料实例数
				
				System.out.println(instancesTraintemp.numInstances());
				m_classifier.buildClassifier(instancesTraintemp); // 训练
				System.out.println("------------------------------train1-over-------------------------------");
				m_classifier2.buildClassifier(instancesTraintemp);
				System.out.println("------------------------------train2-over-------------------------------");
				m_classifier3.buildClassifier(instancesTraintemp);
				System.out.println("------------------------------train3-over-------------------------------");
//				m_classifier4.buildClassifier(instancesTraintemp);
//				System.out.println("------------------------------train4-over-------------------------------");
				
				int t=0;
				while (i<sum&&t<5) // 测试分类结果
				{
					i++;
//					System.out.println(m_classifier.classifyInstance(instancesUnlabel.instance(i)));
					if (m_classifier.classifyInstance(instancesUnlabel.instance(i))==4.0&&m_classifier2.classifyInstance(instancesUnlabel.instance(i))==4.0&&m_classifier3.classifyInstance(instancesUnlabel.instance(i))==4.0) // 如果预测值和答案值相等（测试语料中的分类列提供的须为正确答案，结果才有意义）
					{
						System.out.println(instancesUnlabel.instance(i));
						instancesUnlabel.instance(i).setClassValue(4.0);
						instancesTrain.add(instancesUnlabel.instance(i));
						System.out.println(instancesUnlabel.instance(i).toString());
						t++; // 正确值加 1
						fearnum++;

					} else {
//						// System.out.println(instancesTest.instance(i).toString());
//						instancesTrain.delete(i);
					}
				}
				
//				System.out.println("------classification precision:"
//						+ (right / sum));
			
//				instancesTrain=instancesTraintemp;													// Matrix
			}
			m_classifier.buildClassifier(instancesTrain); // 训练
			Evaluation eval = new Evaluation(instancesTrain); // 构造评价器

			eval.evaluateModel(m_classifier, instancesTest);// 用测试数据集来评价m_classifier
			writer.write(fearnum+":\r\n"+eval.toSummaryString("=== Summary ===",
					false)+"\r\n"+eval.toClassDetailsString()+"\r\n"+eval
					.toMatrixString("=== Confusion Matrix ===")+"\r\n");
			System.out.println(eval.toSummaryString("=== Summary ===",
					false)); // 输出信息
			System.out.println(eval.toClassDetailsString()); // 输出信息
			System.out.println(eval
					.toMatrixString("=== Confusion Matrix ==="));// Confusion
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
