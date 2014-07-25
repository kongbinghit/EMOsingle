package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import v2.WeiboSentenceIDUnlabel;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class CotrainingForWeibo {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			FileWriter writer = new FileWriter(new File("result2.txt"));
			LibSVM m_classifier = new LibSVM();
			LibSVM m_classifier_sentence = new LibSVM();
			String[] options = weka.core.Utils
					.splitOptions("-S 0 -K 0 -D 3 -G 0.0078125 -R 0.0 -N 0.5 -M 40.0 -C 0.1 -E 0.001 -P 0.1 -B");
			m_classifier.setOptions(options);
			m_classifier_sentence.setOptions(options);

			File inputFile = new File("trainfeature.arff"); // 训练微博语料文件
			ArffLoader atf = new ArffLoader();
			atf.setFile(inputFile);
			Instances instancesTrain = atf.getDataSet();
			instancesTrain.setClassIndex(0);
			instancesTrain.deleteStringAttributes();

			inputFile = new File("testfeature.arff"); // 测试微博语料文件
			atf.setFile(inputFile);
			Instances instancesTest = atf.getDataSet();
			instancesTest.setClassIndex(0);
			instancesTest.deleteStringAttributes();

			inputFile = new File("unlabelfeature.arff"); // 未标注微博语料文件
			atf.setFile(inputFile);
			Instances instancesUnlabel = atf.getDataSet();
			instancesUnlabel.setClassIndex(0);
			instancesUnlabel.deleteStringAttributes();

			inputFile = new File("trainfeatureSentence.arff"); // 训练句子语料文件
			atf.setFile(inputFile);
			Instances instancesTrainSentence = atf.getDataSet();
			instancesTrainSentence.setClassIndex(0);
			instancesTrainSentence.deleteStringAttributes();

			inputFile = new File("unlabelfeatureSentence.arff"); // 未标注句子语料文件
			atf.setFile(inputFile);
			Instances instancesUnlabelSentence = atf.getDataSet();
			instancesUnlabelSentence.setClassIndex(0);
			instancesUnlabelSentence.deleteStringAttributes();

			inputFile = new File("trainfeature_yn.arff"); // 训练句子有无情绪语料文件——yn
			atf.setFile(inputFile);
			Instances instancesTrain_yn = atf.getDataSet();
			instancesTrain_yn.setClassIndex(0);
			instancesTrain_yn.deleteStringAttributes();

			inputFile = new File("unlabelfeature_yn.arff"); // 未标注句子有无情绪语料文件-yn
			atf.setFile(inputFile);
			Instances instancesUnlabel_yn = atf.getDataSet();
			instancesUnlabel_yn.setClassIndex(0);
			instancesUnlabel_yn.deleteStringAttributes();

			System.out.println(instancesUnlabel_yn.numInstances()
					+ "=================" + instancesUnlabel.numInstances());

			Map<Integer, List<Integer>> map_old = WeiboSentenceIDUnlabel
					.getID();
			Map<Integer, List<Integer>> map_new = new HashMap<Integer, List<Integer>>();
			// 有无情绪分类
			System.out.println("step:1-begin=======对unlabel微博有无情绪分类=======");
			Instances Instanceunlabel_new = new Instances(instancesUnlabel);
			Instanceunlabel_new.delete();
			Instances Instanceunlabelsentence_new = new Instances(
					instancesUnlabelSentence);
			Instanceunlabelsentence_new.delete();
			m_classifier.buildClassifier(instancesTrain_yn);
			m_classifier_sentence.buildClassifier(instancesTrainSentence);
			int k = 0;
			int num = 0;
			int weiboid = 0;
			while (k < instancesUnlabel_yn.numInstances()) {
				boolean h = false;
				if (m_classifier.classifyInstance(instancesUnlabel_yn
						.instance(k)) == instancesUnlabel_yn.instance(k)
						.classValue()) {

					List<Integer> list = map_old.get(k);
//					for (int i = 0; i < list.size(); i++) {
//						System.out.println(list.get(i)+" "+m_classifier_sentence
//								.classifyInstance(instancesUnlabelSentence
//										.instance(list.get(i))));
//						if (m_classifier_sentence
//								.classifyInstance(instancesUnlabelSentence
//										.instance(list.get(i))) != 7.0){
//							System.out.println("----------------------yes");
//							h = true;
//							break;
//						}
//					}
//					if (h) {
						Instanceunlabel_new.add(instancesUnlabel.instance(k));
						List<Integer> list_new = new ArrayList<Integer>();
						for (int i = 0; i < list.size(); i++) {
							list_new.add(num++);
							Instanceunlabelsentence_new
									.add(instancesUnlabelSentence.instance(list
											.get(i)));
						}
						map_new.put(weiboid++, list_new);
//					}

				}
				k++;
			}
			System.out.println(map_new.size());
			System.out.println(Instanceunlabel_new.numInstances());
			System.out.println("step:1-end=======对unlabel微博有无情绪分类=======");
			
			System.out.println("step:2-begin=======训练微博=======");
			m_classifier.setProbabilityEstimates(true);
			m_classifier.buildClassifier(instancesTrain); // 训练
			System.out.println("step:2-end=======训练微博=======");
//			System.out.println("step:3-begin=======训练句子=======");
//			m_classifier_sentence.buildClassifier(instancesTrainSentence);
//			System.out.println("step:3-end=======训练句子=======");
			
			
			
			Evaluation eval3 = new Evaluation(instancesTrain); // 构造评价器
			eval3.evaluateModel(m_classifier, instancesTest);// 用测试数据集来评价m_classifier
			System.out.println(eval3.toSummaryString("=== Summary ===", false)); // 输出信息
			System.out.println(eval3.toClassDetailsString()); // 输出信息
			System.out.println(eval3.toMatrixString("=== Confusion Matrix ==="));// Confusion
			writer.write(eval3.toSummaryString("=== Summary ===", false));
			writer.write(eval3.toClassDetailsString());
			writer.write(eval3.toMatrixString("=== Confusion Matrix ==="));
			
			int classnum[] = new int[7];
			for (k = 0; k < instancesTrain.numInstances(); k++) {
				classnum[(int) instancesTrain.instance(k).classValue()]++;
			}

			int maxnum = classnum[1];
			int wo = 0;
			while (classnum[4] < maxnum) {
				writer.write("------------------------------" + wo
						+ "-----------------------------\r\n");
				for (int i = 0; i < 7; i++) {
					System.out.println(classnum[i]);
				}
				Evaluation eval = new Evaluation(instancesTrain); // 构造评价器
				eval.evaluateModel(m_classifier, Instanceunlabel_new);// 用测试数据集来评价m_classifier
				Map[] map = new Map[7];
				Map[] map2 = new Map[7];
				for (int i = 0; i < 7; i++) {
					map[i] = new HashMap<Integer, Float>();
					map2[i] = new HashMap<Float, Integer>();
				}
				// Map<Integer, Float> map = new HashMap<Integer, Float>();
				// Map<Float, Integer> map2 = new HashMap<Float, Integer>();
				int length = eval.predictions().toArray().length;
				System.out.println(length);

				for (int i = 0; i < length; i++) {
					// System.out.println(eval.predictions().toArray()[i].toString().split(" ")[2]);
					for (int t = 0; t < 7; t++) {
						if (eval.predictions().toArray()[i].toString().split(
								" ")[2].toString().equals(t + ".0")
								&& Instanceunlabel_new.instance(i).classValue() == 1.0) {

							map[t].put(i, Float.parseFloat(eval.predictions()
									.toArray()[i].toString().split(" ")[t + 4]));
							map2[t].put(
									Float.parseFloat(eval.predictions()
											.toArray()[i].toString().split(" ")[t + 4]),
									i);
						}
					}

				}
				for (int t = 0; t < 7; t++) {
					if (classnum[t] < maxnum && t != 5) {
						map[t] = sortByValue(map[t], true);
						Set<Entry<Integer, Float>> set = map[t].entrySet();
						Iterator<Entry<Integer, Float>> it = set.iterator();
						for (int i = 0; i < 10; i++) {

							if (it.hasNext()) {
								Map.Entry<Integer, Float> entry = it
										.next();

								Integer key = entry.getKey();
								Float value = entry.getValue();
								System.out.println(key + " " + value);
								Instanceunlabel_new.instance(
										(int) map2[t].get(value))
										.setClassValue(t);
								instancesTrain.add(Instanceunlabel_new
										.instance((int) map2[t].get(value)));
								classnum[t]++;
								writer.write(Instanceunlabel_new
										.instance((int) map2[t].get(value))
										+ "\r\n");
								System.out.println(Instanceunlabel_new
										.instance((int) map2[t].get(value)));
							}
						}
					}
					writer.write(map[t].size() + "\r\n");
					System.out.println(map[t].size());
				}

				m_classifier.buildClassifier(instancesTrain); // 训练
				Evaluation eval2 = new Evaluation(instancesTrain); // 构造评价器
				eval2.evaluateModel(m_classifier, instancesTest);// 用测试数据集来评价m_classifier
				System.out.println(eval2.toSummaryString("=== Summary ===",
						false)); // 输出信息
				System.out.println(eval2.toClassDetailsString()); // 输出信息
				System.out.println(eval2
						.toMatrixString("=== Confusion Matrix ==="));// Confusion
				writer.write(eval2.toSummaryString("=== Summary ===", false));
				writer.write(eval2.toClassDetailsString());
				writer.write(eval2.toMatrixString("=== Confusion Matrix ==="));
			}
			// int k=0;
			// while(k<instancesTrain.numInstances()){
			//
			// if(m_classifier.classifyInstance(instancesTrain.instance(k))!=instancesTrain.instance(k).classValue()){
			// System.out.println(instancesTrain.instance(k));
			// }
			// k++;
			// }

			//
			// System.out.println("------classification precision:"
			// + (right / sum));

			// instancesTrain=instancesTraintemp; // Matrix
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

	public static Map sortByValue(Map<Integer, Float> map, final boolean reverse) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				if (reverse) {
					return -((Comparable) ((Map.Entry) o1).getValue())
							.compareTo(((Map.Entry) o2).getValue());
				}
				return ((Comparable) ((Map.Entry) o1).getValue())
						.compareTo(((Map.Entry) o2).getValue());
			}
		});

		Map result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
