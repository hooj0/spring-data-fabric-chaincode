package io.github.hooj0.springdata.fabric.chaincode;

/**
 * chaincode serialization custom exception
 * @author hoojo
 * @createDate 2018年7月17日 下午3:41:32
 * @file ChaincodeOperationException.java
 * @package io.github.hooj0.springdata.fabric.chaincode
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeSerializationException extends ChaincodeRootException {

	private static final long serialVersionUID = 3468241227887083482L;

	public ChaincodeSerializationException() {
		super();
	}

	public ChaincodeSerializationException(Throwable cause) {
		super(cause);
	}
	
	public ChaincodeSerializationException(String message, Object... args) {
		super(String.format(message, args));
	}
	
	public ChaincodeSerializationException(Throwable cause, String message, Object... args) {
		super(String.format(message, args), cause);
	}
}
