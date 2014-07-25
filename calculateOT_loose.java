import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;


public class calculateOT_loose {//���ɵ�
	
	static BufferedWriter bw1;
	static BufferedWriter bw2;
	
	public static void main(String[] args) throws Exception{
		bw1=new BufferedWriter(new FileWriter("可选任务Closeks.csv"));
		bw2=new BufferedWriter(new FileWriter("可选任务Openks.csv"));
		bw1.write("team,AveragePrecision\r\n");
		bw2.write("team,AveragePrecision\r\n");
		calculate("C:/Users/lin/Desktop/北信科大微博情感挖掘系统_中文微博情绪识别_2_CLOSE_2.txt");
//		File file=new File("E:/eclipse/workspace/pingce/src/评测文件");
//		File[] files=file.listFiles();
//		for(File f:files){
//			calculate(f.toString());
//		}
		bw1.close();
		bw2.close();
	}
	
	public static void calculate(String path) throws Exception{
		System.out.println(path);
		BufferedReader brCom=new BufferedReader(new FileReader(path));
		BufferedReader brOur=new BufferedReader(new FileReader("C:/Users/lin/Desktop/OurResult10000Sentence.txt"));
		String com=brCom.readLine();
		String our=brOur.readLine();
		double precision=0.0;
		int countSentence=0;
		int a1=0,a2=0,a3=0,a4=0,a5=0,a6=0,a7=0;
		while(com!=null&&our!=null){
			String[] coms=com.split(" ");
			if(coms.length<6){
				System.out.println("2222222222222222");
					com=brCom.readLine();
					our=brOur.readLine();
					continue;
			}
			String[] ours=our.split(" ");
			String[] or89=new String[2];
			if(coms.length==8){
				or89[0]=coms[6];
				or89[1]=coms[7];
			}
			else{
				or89[0]=coms[7];
				or89[1]=coms[8];
			}
			if(!ours[2].contains("none")&&!ours[3].contains("none")){
				if((ours[2].equals(or89[0])&&ours[3].equals(or89[1]))||(ours[2].equals(or89[1])&&ours[3].equals(or89[0]))){
					a1++;
					precision=precision+1;
				}
				else if((or89[0].equals(ours[2])||or89[0].equals(ours[3]))&&(!or89[1].equals(ours[2])&&!or89[1].equals(ours[3]))){
					a2++;
					precision=precision+0.5;
				}
				else if((or89[1].equals(ours[2])||or89[1].equals(ours[3]))&&(!or89[0].equals(ours[2])&&!or89[0].equals(ours[3]))){
					a3++;
					precision=precision+0.5;
				}
				else
					a4++;
				countSentence++;
			}
			else if(!ours[2].contains("none")&&ours[3].contains("none")){
				if(or89[0].equals(ours[2])){
					a5++;
					precision=precision+1;
				}
				else
					a6++;
				countSentence++;
			}
			else if(ours[2].contains("none")&&!ours[3].contains("none")){
				System.out.println("*******888");
				System.in.read();
			}
			else if(ours[2].contains("none")&&ours[3].contains("none"))
				a7++;
			com=brCom.readLine();
			our=brOur.readLine();
		}
		System.out.println(precision+"         "+countSentence);
		precision=precision/countSentence;
		System.out.println(precision);
		if(path.contains("CLOSE")){
			DecimalFormat df = new DecimalFormat("0.0000");
			bw1.write(path.substring(path.lastIndexOf("\\")+1,path.lastIndexOf("."))+",");
			bw1.write(df.format(precision)+"\r\n");
		}
		else{
			DecimalFormat df = new DecimalFormat("0.0000");
			bw2.write(path.substring(path.lastIndexOf("\\")+1,path.lastIndexOf("."))+",");
			bw2.write(df.format(precision)+"\r\n");
		}
		System.out.println(a1+"   "+a2+"   "+a3+"   ***"+a4+"   "+a5+"   *"+a6+"   *"+a7+"   "+(a1+a2+a3+a4+a5+a6+a7));
	}
}
