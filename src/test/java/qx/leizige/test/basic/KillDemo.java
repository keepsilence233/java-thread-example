package qx.leizige.test.basic;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import qx.leizige.test.basic.module.RequestPromise;
import qx.leizige.test.basic.module.RequestResult;
import qx.leizige.test.basic.module.UserRequest;

/**
 * @author leizige
 * 2022/05/29
 * wait notify 应用。扣减库存
 */
public class KillDemo {


	/**
	 * 启动10个线程
	 * 库存有六个
	 * 生成一个合并队列
	 * 每个用户都能拿到自己的响应和请求
	 */
	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newCachedThreadPool();
		KillDemo killDemo = new KillDemo();
		killDemo.mergeJob();
		Thread.sleep(2000);
		List<Future<RequestResult>> futureList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			final Long orderId = i + 100L;
			final Long userId = (long) i;
			Future<RequestResult> future = executor.submit(() -> killDemo.operate(new UserRequest(orderId, userId, 1)));
			futureList.add(future);
		}
		futureList.forEach(future -> {
			try {
				RequestResult requestResult = future.get(300, TimeUnit.MILLISECONDS);
				System.out.println(Thread.currentThread().getName() + "客户端请求响应" + requestResult);
			}
			catch (InterruptedException | ExecutionException | TimeoutException e) {
				e.printStackTrace();
			}
		});
	}

	private Integer stock = 6;    //模拟只有6个库存

	private BlockingQueue<RequestPromise> queue = new LinkedBlockingQueue<>(10);

	/**
	 * 用户下单库存扣减
	 * @param userRequest
	 * @return
	 */
	public RequestResult operate(UserRequest userRequest) throws InterruptedException {
		RequestPromise requestPromise = new RequestPromise(userRequest);
		//往队列添加新的任务
		if (!queue.offer(requestPromise, 100, TimeUnit.MILLISECONDS)) {
			return new RequestResult(false, "系统繁忙");
		}
		synchronized (requestPromise) {
			try {
				requestPromise.wait(200);
				if (requestPromise.getRequestResult() == null) {
					requestPromise.setRequestResult(new RequestResult(false, "等待超时"));
				}
			}
			catch (InterruptedException e) {
				return new RequestResult(false, "等待超时");
			}
		}
		return requestPromise.getRequestResult();
	}


	/**
	 * 合并用户下单任务
	 */
	public void mergeJob() {
		new Thread(() -> {
			List<RequestPromise> requestPromiseList = new ArrayList<>();
			while (true) {
				if (queue.isEmpty()) {
					try {
						Thread.sleep(10);
						continue;
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				while (queue.peek() != null) {
					requestPromiseList.add(queue.poll());
				}

				System.out.println(Thread.currentThread().getName() + ":合并扣减库存:" + requestPromiseList);

				int sum = requestPromiseList.stream().mapToInt(e -> e.getUserRequest().getCount()).sum();
				if (sum <= stock) {    //库存足够
					stock -= sum;
					//notify all user
					requestPromiseList.forEach(requestPromise -> {
						requestPromise.setRequestResult(new RequestResult(true, "success"));
						synchronized (requestPromise) {
							requestPromise.notify();
						}
					});
					continue;
				}
				else {    //库存不足,退化成一个循环。每个用户下单的数量从高到底排序,依次判断数量是否足够 足够则扣减
					for (RequestPromise requestPromise : requestPromiseList) {
						Integer count = requestPromise.getUserRequest().getCount();
						if (count <= stock) {
							stock -= count;
							requestPromise.setRequestResult(new RequestResult(true, "success"));
						}
						else {
							requestPromise.setRequestResult(new RequestResult(false, "库存不足"));
						}
						synchronized (requestPromise) {
							requestPromise.notify();
						}
					}
				}

				requestPromiseList.clear();
			}
		}, "mergeThread").start();
	}
}
