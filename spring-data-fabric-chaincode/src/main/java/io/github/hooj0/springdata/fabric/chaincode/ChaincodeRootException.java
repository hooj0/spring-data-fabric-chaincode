package io.github.hooj0.springdata.fabric.chaincode;

/**
 * chaincode root exception defined
 * @author hoojo
 * @createDate 2018年7月17日 上午10:24:00
 * @file ChaincodeRootException.java
 * @package io.github.hooj0.springdata.fabric.chaincode
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeRootException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ChaincodeRootException() {
		super();
	}

	public ChaincodeRootException(Throwable cause, String message, Object... args) {
		super(String.format(message, args), cause);
	}

	public ChaincodeRootException(String message, Object... args) {
		super(String.format(message, args));
	}

	public ChaincodeRootException(Throwable cause) {
		super(cause);
	}
}
