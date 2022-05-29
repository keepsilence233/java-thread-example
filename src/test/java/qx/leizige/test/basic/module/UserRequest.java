package qx.leizige.test.basic.module;

/**
 * 用户请求
 */
public class UserRequest {

	private Long orderId;

	private Long userId;

	private Integer count;	//要扣减的数量

	public UserRequest(Long orderId, Long userId, Integer count) {
		this.orderId = orderId;
		this.userId = userId;
		this.count = count;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "UserRequest{" +
				"orderId=" + orderId +
				", userId=" + userId +
				", count=" + count +
				'}';
	}
}
