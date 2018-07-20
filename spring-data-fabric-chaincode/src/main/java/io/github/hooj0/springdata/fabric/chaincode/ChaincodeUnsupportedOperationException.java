package io.github.hooj0.springdata.fabric.chaincode;

/**
 * <b>function:</b> Unsupported Operation Exception
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

	public ChaincodeUnsupportedOperationException(String string) {
		super(string);
	}
	
	public ChaincodeUnsupportedOperationException(String string, Exception e) {
		super(string, e);
	}
}
