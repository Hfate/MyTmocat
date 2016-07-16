package threadpool;

public class SimpleThread extends Thread{
private boolean runningFlag;//运行状态
private Taskable task;//要执行的操作
private int id;//线程编号
private Mynotify myNotify;
//标志runningFlag 用以激活线程
public boolean isRunning(){
	return runningFlag;
}
public synchronized void setRunning(boolean flag){
	runningFlag=flag;        //线程的状态
	if(flag)
		this.notifyAll();   //通知其他线程准备就绪
	}
	public Taskable getTask(){
		return task;
	}
	public 	void setTask(Taskable task){
		this.task=task;
	}
	//提示是哪个线程工作
	public SimpleThread(int threadNumber,Mynotify notify){
		runningFlag=false;
		this.id=threadNumber;
		System.out.println("Thread"+threadNumber+"started.");
		this.myNotify=notify;
	}
	public synchronized void run(){
		try {
			while(true){       //无限循环
				if(!runningFlag){//判断标志位是否为true
					
						this.wait();//所有的线程在没有任务是都在等待
					
				}else{
					System.out.println("线程"+id+"开始执行任务");
					Object returnvalue=this.task.doTask();
					if(myNotify!=null){
						myNotify.notifyResult(returnvalue);
					}
				
					sleep(5000);
					System.out.println("线程"+id+"重新开始准备");
					setRunning(false);
				}
			}
	}catch (InterruptedException e) {
				System.out.println("interrupt");
	}
}
}


