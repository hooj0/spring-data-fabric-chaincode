package io.github.hooj0.springdata.fabric.chaincode;

/**
 * <b>function:</b> Chaincode Root Exception
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

	private static final long serialVersionUID = 4481306050552270511L;

	public ChaincodeRootException(String string, Exception e) {
		super(string, e);
	}

	public ChaincodeRootException(String string) {
		super(string);
	}
}
