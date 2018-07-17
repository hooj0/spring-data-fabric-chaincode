package io.github.hooj0.springdata.fabric.chaincode.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <b>function:</b> Chaincode Operations Template
 * @author hoojo
 * @createDate 2018年7月17日 上午10:32:32
 * @file ChaincodeTemplate.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeTemplate implements /*ChaincodeOperations,*/ ApplicationContextAware, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {

	}
}
