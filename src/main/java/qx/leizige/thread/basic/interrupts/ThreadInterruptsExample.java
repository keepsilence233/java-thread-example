package qx.leizige.thread.basic.interrupts;

import java.util.concurrent.TimeUnit;

/**
 * @author leizige
 * 2022/06/03
 *
 * <p>
 *     中断是对线程的指示，它应该停止正在做的事情并做其他事情。
 *     中断是操作系统级别的机制，用于向线程发送有关特定事件的信号。
 *     在被中断的线程内，Thread.currentThread().isInterrupted() 可以用来查找被中断的状态。
 *     如果线程被阻塞 Object.wait(),Thread.join()或者Thread.sleep(),将抛出 InterruptedException。
 *
 * </p>
 */
public class ThreadInterruptsExample {

	public static void main(String[] args) throws InterruptedException {
		Task task1 = new Task();
		Thread thread1 = new Thread(task1);
		thread1.start();
		while (true){
			if(Math.random() > 0.5){
				thread1.interrupt();
				break;
			}
			TimeUnit.MICROSECONDS.sleep(1);
		}
	}


	private static class Task implements Runnable{

		@Override
		public void run() {
			int c = 0;

			while (true){

				System.out.println("task running .. " + ++c);
				if(Thread.currentThread().isInterrupted()){
					System.out.println("interrupted flag=true");
					terminate();
					return;
				}
				try {
					TimeUnit.MICROSECONDS.sleep(1);
				}
				catch (InterruptedException e) {
					System.out.println("interrupted exception ");
					terminate();
					return;
				}
			}
		}

		private void terminate () {
			System.out.println("Terminating task");
		}
	}
}
