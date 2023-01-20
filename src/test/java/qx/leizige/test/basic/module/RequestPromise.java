package qx.leizige.test.basic.module;

public class RequestPromise {

	private UserRequest userRequest;

	private RequestResult requestResult;

	public RequestPromise(UserRequest userRequest) {
		this.userRequest = userRequest;
	}

	public UserRequest getUserRequest() {
		return userRequest;
	}

	public void setUserRequest(UserRequest userRequest) {
		this.userRequest = userRequest;
	}

	public RequestResult getRequestResult() {
		return requestResult;
	}

	public void setRequestResult(RequestResult requestResult) {
		this.requestResult = requestResult;
	}

	@Override
	public String toString() {
		return "RequestPromise{" +
				"userRequest=" + userRequest +
				", requestResult=" + requestResult +
				'}';
	}
}
