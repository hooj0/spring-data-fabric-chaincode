package io.github.hooj0.springdata.fabric.chaincode;

/**
 * chaincode unsupported operation custom exception
 * @author hoojo
 * @createDate 2018年7月17日 下午3:52:07
 * @file UnsupportedOperationException.java
 * @package io.github.hooj0.springdata.fabric.chaincode
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeUnsupportedOperationException extends ChaincodeRootException {

	private static final long serialVersionUID = 8183721727089671557L;

	public ChaincodeUnsupportedOperationException() {
		super();
	}

	public ChaincodeUnsupportedOperationException(Throwable cause, String message, Object... args) {
		super(String.format(message, args), cause);
	}

	public ChaincodeUnsupportedOperationException(String message, Object... args) {
		super(String.format(message, args));
	}

	public ChaincodeUnsupportedOperationException(Throwable cause) {
		super(cause);
	}
}
