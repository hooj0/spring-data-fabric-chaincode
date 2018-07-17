package io.github.hooj0.springdata.fabric.chaincode;

/**
 * ChaincodeMapping Exception
 * @author hoojo
 * @createDate 2018年7月17日 下午3:41:32
 * @file ChaincodeMappingException.java
 * @package io.github.hooj0.springdata.fabric.chaincode
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeMappingException extends ChaincodeRootException {

	private static final long serialVersionUID = 3468241227887083482L;

	public ChaincodeMappingException(String string, Exception e) {
		super(string, e);
	}

	public ChaincodeMappingException(String string) {
		super(string);
	}
}
