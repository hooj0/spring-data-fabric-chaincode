package io.github.hooj0.springdata.fabric.chaincode.repository.support;

import java.io.File;
import java.io.InputStream;

import org.hyperledger.fabric.sdk.TransactionRequest.Type;

import io.github.hooj0.springdata.fabric.chaincode.annotations.Chaincode;
import io.github.hooj0.springdata.fabric.chaincode.annotations.Install;

/**
 * <b>function:</b> AnnotationedRepositoryTests
 * @author hoojo
 * @createDate 2018年7月16日 下午6:02:28
 * @file AnnotationedRepository.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.support
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class AnnotationedRepositoryTests {

	@Chaincode(channel = "mychannel", name = "mycc", type = Type.GO_LANG, version = "1.1", path = "github.com/example_cc")
	interface Repo {
		
		@Install
		public void installChaincode(String ccPath);
		
		@Install
		public void installChaincode(File ccFile);

		@Install
		public void installChaincode(InputStream ccStream);
	}
}
