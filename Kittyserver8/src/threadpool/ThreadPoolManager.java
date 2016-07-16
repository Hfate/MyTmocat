package threadpool;

import java.util.Vector;

public class ThreadPoolManager {//线程池
	private int maxThread;
	public Vector vector;//vector 类可以实现可增长的对像数组，默认大小为10
	private Mynotify notify;
	
	
	public void setMaxThread(int threadCount){
		this.maxThread=threadCount;
	}
	
	
	public 	ThreadPoolManager(int threadCount,Mynotify notify){
		this.notify=notify;
		this.setMaxThread(threadCount);
		System.out.println("线程池开始");
		vector= new Vector();
		for(int i=0;i<=threadCount;i++){
			SimpleThread thread=new SimpleThread(i,this.notify);
			vector.addElement(thread);//将指定的组件添加到此向量的末尾，将其大小增加一；
			thread.start();
		}
	}
	
	
	public void process(Taskable task){
		int i;
		for(i=0;i<vector.size();i++){
			SimpleThread currentThread=(SimpleThread)vector.elementAt(i);//返回指定索引处的组件
			//从vector中取出一个空闲的线程
			if(!currentThread.isRunning()){
				System.out.println("Thread"+(i+1)+"开始执行");
				currentThread.setTask(task);
				currentThread.setRunning(true);
				return;
			}
		}	
			System.out.println("================ ==============================");
			System.out.println("=================线程池中没有空的线程==========");
			System.out.println("==============================================");
			//如果线程池中所有的线程都不是空闲的
			if(i==vector.size()){
				//从新分配10个线程
				//ThreadPoolManager manager=new ThreadPoolManager(10);
				//System.out.println("重新分配10个线程");
				int temp=maxThread;
				this.setMaxThread(maxThread+10);
				for(int j=temp+1;j<=maxThread;j++){
					SimpleThread thread=new SimpleThread(j,this.notify);
					vector.addElement(thread);//将指定的组件添加到此向量的末尾，将其大小增加一；
					thread.start();
				}
			}
			//创建之后需要重建任务
			this.process(task);
		}
	
}
