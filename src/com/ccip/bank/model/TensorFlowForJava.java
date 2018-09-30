package com.ccip.bank.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.nd4j.nativeblas.Nd4jCpu.float_absolute_difference_loss;
import org.tensorflow.Operation;
import org.tensorflow.Output;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;

import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import MinMaxScaler.MinMax;

/**
 * @date 2018年8月7日 下午5:17:31 ；20180820测试完成信用评级CNN模型的调用
 * @author Mason
 * 实现Java下TensorFlow神经网络模型调用
 */
public class TensorFlowForJava {

public static void main(String[] args) throws FileNotFoundException, IOException, MWException {
	

	
	System.out.println("TensorFlow version: " + TensorFlow.version());
	
	
	/**
	 * 信用模型调用
	 */
	//TensorFlowInferenceInterface tfi = new TensorFlowInferenceInterface("D://java-project/enterpriseInfo/TFModel/hydt/mnist.pb","dataType");
	
//	final Operation operation = tfi.graphOperation("cnn");
	// 加载预测数据
//	String srcCSV = System.getProperty("user.dir")+"/datasets/xypj/train.csv";         
//    CsvReader reader = new CsvReader(srcCSV, ',', Charset.forName("UTF-8"));
//    String[] header = {};   
//    System.out.println("您预测的信用评级为：\n");
//    while(reader.readRecord()) {
//    	float[] floatValue = new float[106];
//        if (reader.getCurrentRecord()==0){
//            header = reader.getValues();            
//        }
//        else{
//        	String t = reader.getRawRecord();
//        	String[] raw_data = t.split(",");
//        	//从第四列开始去预测数据
//        	for (int x = 3; x < 109; x++) {
//        		float d = Float.parseFloat(raw_data[x]); 
//        		floatValue[x-3] = d;
//        	}
//        	float keep = 1.0f;
//        	tfi.feed("inputs", floatValue,1, 106);
//        	Tensor  keep_prob = Tensor.create(keep);
//        	tfi.addFeed("prob",keep_prob);
//        	tfi.run(new String[] { "cnn" }, false);//输出张量
//        	long [] outPuts = new long [1];//结果分类
//        	tfi.fetch("cnn", outPuts);//接收结果 outPuts保存的即为预测结果对应的概率，最大的一个通常为本次预测结果}
//        	System.out.println(outPuts[0]);
//        }
//    }			
		
	/**
	 * Risk Model 调用
	 */
//        try (Graph graph = new Graph()) {
//            //导入图
//            byte[] graphBytes = IOUtils.toByteArray(new FileInputStream(System.getProperty("user.dir")+"/TFModel/hydt/basicPred_0.pb"));
//            graph.importGraphDef(graphBytes);
//            //float[] a = new float[]{-2.52894469e-02f , -4.47493123e-02f , -1.11403992e-01f , -1.00096164e-01f,
//           // -1.05890850e-01f , -3.96117458e-02f};
//            float[] a = new float[]{-2.21283262e-02f,  -4.22827772e-02f,  -1.22634684e-01f,  -1.01158189e-01f,
//            		   -1.16706481e-01f,  -3.61754572e-02f};
//            //根据图建立Session        		
//         try(Session sess = new Session(graph)){
//            float keep = 1.0f;        
//            Tensor x = Tensor.create(a);
//            Tensor  keep_prob = Tensor.create(keep);
//            Tensor<?> y = sess.runner().feed("input_x", x).feed("keep_prob", keep_prob).fetch("output/predict").run().get(0);  
//            float[][] t = new float[1][1];
//            y.copyTo(t);
//            float[] result = t[0];          
//            System.out.println("企业流动性为："+result[0]);
//                //相当于TensorFlow Python中的sess.run(z, feed_dict = {'in_put': 10.0})
////            for (Tensor s : y) {
////            	float[][] t = new float[1][1];
////            	s.copyTo(t);
////            	for (float i : t[0])
//                System.out.println(System.getProperty("user.dir"));
////            	}         
//            }
//        }
  






	/**
	 * 行业动态模型 0930
	*/
	
	// 接收数据的最大、最小值
	String dataSetPrex = "D://java-project/Enterprise_Credit_Analysis/datasets/";
	String input = dataSetPrex + "hydt/JingQiIndex/JingQi_real_Index.txt"; // 房地产市场
	MinMax minMaxs = new MinMax();
	Object[] Results = null;
	Results = minMaxs.MinMaxScaler(2,input, 1); // 房地产市场
	MWNumericArray output1 = null;
	MWNumericArray output2 = null;
	output1 = (MWNumericArray) Results[0]; // 将结果object转换成MWNumericArray
	output2 = (MWNumericArray) Results[1];
	float min = output1.getFloat(1);
	float max = output2.getFloat(1);

	
	
	
	
	
	
	
	
	SavedModelBundle SB = SavedModelBundle.load(System.getProperty("user.dir")+"/TFModel/hydt/model2", "mytag");
    Session tfSession = SB.session();
    Operation operationPredict = SB.graph().operation("rnn/preds");   //要执行的op
    Output output = new Output(operationPredict, 0);
   
    // 初始化队列元素，即训练数据最后一列
    Deque<Float> queue = new ArrayDeque<Float>();
    Deque<Float> predQue = new ArrayDeque<Float>();
    queue.add(0.5376470927352686f);
    queue.add(0.4244357630777214f);
    queue.add(0.3645078126405753f);
    queue.add(0.06482944780183306f);
    queue.add(0.0494559806531145f);
    
    float predValue = 0.0f;
    // 根据预测年数进行循环   
    for(int i =1;i<=7; i++) {
    	int k = 0;
    	float[][] a = new float[5][5];
    	for(Iterator<Float> itr= queue.iterator();itr.hasNext();){
    	a[0][k] = itr.next();
    	k++;
    	}
    Tensor input_x = Tensor.create(a); 
    List<Tensor<?>> out = tfSession.runner().feed("inputs", input_x).fetch(output).run();        
    for (Tensor s : out) {   
    	// 字符串数组，使用for(:)获得数据
    	float[][] t = new float[25][1];  
    	s.copyTo(t);     
    	for (float pred : t[4]) {
    		// 必须经过转化后才可得到真实预测值
			predValue = pred*(max-min)+min;
    		queue.remove();//队首元素出队
    	    queue.add(pred);
    	}
    }
    predQue.add(predValue);
  }
System.out.println("最终预测值为："+predQue);
    

    





















}


    /**
     * 0均值\标准差归一化 公式：X(norm) = (X - μ) / σ
     * X(norm) = (X - 均值) / 标准差
     *
     * @param points 原始数据
     * @return 归一化后的数据
     */
    public static float[] normalize4ZScore(float[] points) {
        if (points == null || points.length < 1) {
            return points;
        }
        float[] p = new float[points.length];
        float[] matrixJ;
        float avg;
        float std;
        for (int j = 0; j < points.length; j++) {
          
            
            avg = average(points);
            System.out.println(avg);
            std = standardDeviation(points);
            for (int i = 0; i < points.length; i++) {
                p[i]= std == 0 ? points[i] : (points[i]- avg) / std;
            }
        }
        return p;
    }
 
    /**
     * 方差s^2=[(x1-x)^2 +...(xn-x)^2]/n
     *
     * @param x x
     * @return 方差
     */
    public static float variance(float[] x) {
        int m = x.length;
        float sum = 0;
        for (int i = 0; i < m; i++) {//求和
            sum += x[i];
        }
        float dAve = sum / m;//求平均值
        float dVar = 0;
        for (int i = 0; i < m; i++) {//求方差
            dVar += (x[i] - dAve) * (x[i] - dAve);
        }
        return dVar / m;
    }
 
    /**
     * 标准差σ=sqrt(s^2)
     *
     * @param x x
     * @return 标准差
     */
    public static float standardDeviation(float[] x) {
        return (float)Math.sqrt(variance(x));
    }
 
    /**
     * 平均值
     *
     * @param x x
     * @return 平均值
     */
    public static float average(float[] x) {
        int m = x.length;
        float sum = 0;
        for (int i = 0; i < m; i++) {
            sum += x[i];
        }
        float dAve = sum / m;
        return dAve;
    }



    public static float[] getMatrixCol(float[][] points, int column) {
        float[] matrixJ = new float[points.length];
        for (int i = 0; i < points.length; i++) {
            matrixJ[i] = points[i][column];
        }
        return matrixJ;
    }










  }    