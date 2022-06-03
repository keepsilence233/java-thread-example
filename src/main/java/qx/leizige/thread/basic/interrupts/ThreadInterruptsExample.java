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
 * </p>
 * <p>
 *     interrupt() : 中断线程，线程A运行时，线程B可以调用线程A的interrupt来设置A的中断标志为true并返回
 *    					 设置标志仅仅是设置标志，线程A实际并没有被中断，它会继续往下执行。
 *     isInterrupted() : 检测当前线程是否被中断，如果是返回true，否则返回false
 *     interrupted() : 检测当前线程是否被中断，如果是返回true，否则返回false
 *     					与isInterrupted不同的是，该方法如果发现当前线程被中断，则会清除中断标志，并且该方法是static方法，可以通过Thread类直接调用。
 * </p>
 */
public class ThreadInterruptsExample {

	public static void main(String[] args) throws InterruptedException {
		Task task1 = new Task();
		Thread thread1 = new Thread(task1);
		thread1.start();
		while (true) {
			if (Math.random() > 0.5) {
				thread1.interrupt();
				break;
			}
			TimeUnit.MICROSECONDS.sleep(1);
		}
	}


	private static class Task implements Runnable {

		@Override
		public void run() {
			int c = 0;

			while (true) {

				System.out.println("task running .. " + ++c);
				if (Thread.currentThread().isInterrupted()) {
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

		private void terminate() {
			System.out.println("Terminating task");
		}
	}
}
