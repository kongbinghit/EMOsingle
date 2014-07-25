import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;


public class calculateOT_strictmy {//�ϸ��
	
	static BufferedWriter bw1;
	static BufferedWriter bw2;
	
	public static void main(String[] args) throws Exception{
		bw1=new BufferedWriter(new FileWriter("可选任务Closeks.csv"));
		bw2=new BufferedWriter(new FileWriter("可选任务Openks.csv"));
		bw1.write("team,AveragePrecision\r\n");
		bw2.write("team,AveragePrecision\r\n");
		calculate("C:/Users/lin/Desktop/out_neighbor99bi.prediction");
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
		BufferedReader brOur=new BufferedReader(new FileReader("C:/Users/lin/Desktop/compare.txt"));
		String com=brCom.readLine();
		String our=brOur.readLine();
		double precision=0.0;
		int countSentence=0;
		while(com!=null&&our!=null){
			String[] coms=com.split(" ");
//			if(coms.length<6){
//				System.out.println("2222222222222222");
//					com=brCom.readLine();
//					our=brOur.readLine();
//					continue;
//			}
			String[] ours=our.split(",");
			String[] or89=new String[2];
			or89[0]="none";
			or89[1]="none";
			if(coms.length==2){
				or89[0]=coms[0];
				or89[1]=coms[1];
			}
			else if(coms.length==1){
				or89[0]=coms[0];
//				or89[1]=coms[8];
			}
			if(!ours[0].contains("none")&&!ours[1].contains("none")){
		//		System.out.println(countSentence+" ---------------");
				if(ours[0].equals(or89[0])){
					if(ours[1].equals(or89[1])){
						precision=precision+0.75;
					}
					else if(!ours[1].equals(or89[1])){
						precision=precision+0.5;
					}
				}
				else if(!ours[0].equals(or89[0])){
					if(ours[1].equals(or89[1])){
						precision=precision+0.25;
					}
					else if(ours[1].equals(or89[0])){
				//	else if(!ours[1].equals(or89[1])){
						precision=precision+0.25;
					}
				}
				countSentence++;
			}
			else if(!ours[0].contains("none")&&ours[1].contains("none")){
		//	else if(ours[1].contains("none")){
		//		System.out.println(countSentence+" ++++++++++");
				if(ours[0].equals(or89[0])){
					precision=precision+1;
				}
				countSentence++;
			}
			else if(ours[0].contains("none")&&!ours[1].contains("none")){
				System.out.println("*******888");
				System.in.read();
			}
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
	}
}
