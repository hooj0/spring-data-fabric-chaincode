package io.github.hooj0.springdata.fabric.chaincode.repository.v13;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.hooj0.fabric.sdk.commons.core.ChaincodeDeployOperations;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Chaincode;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Channel;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Install;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Instantiate;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Invoke;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Query;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Upgrade;
import io.github.hooj0.springdata.fabric.chaincode.config.AbstractChaincodeConfiguration;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.MappingChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.support.ChaincodeTemplate;
import io.github.hooj0.springdata.fabric.chaincode.repository.ChaincodeRepository;
import io.github.hooj0.springdata.fabric.chaincode.repository.config.EnableChaincodeRepositories;

/**
 * annotationed repository test units
 * @author hoojo
 * @createDate 2018年12月20日 下午4:13:37
 * @file AnnotationRepositoryTests.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.v13
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AnnotationRepositoryTests {

	@Configuration
	@EnableChaincodeRepositories(basePackageClasses = TransactionRepo.class,
		considerNestedRepositories = true, 
		includeFilters = @Filter(pattern = ".*Repo", type = FilterType.REGEX))
	public static class Config extends AbstractChaincodeConfiguration {
		
		@Inject
		private MappingChaincodeConverter mappingConverter;
		
		@Bean
		public ChaincodeTemplate chaincodeTemplate() throws ClassNotFoundException {
			return new ChaincodeTemplate(mappingConverter);
		}
	}
	
	@Autowired 
	private TransactionRepo txRepo;
	
	@Autowired 
	private DeployRepo deployRepo;
	
	@Autowired
	private UpgradeDeployRepo upgradeRepo;
	
	@Test
	public void testDeployChaincode() throws Exception {
		
		ChaincodeDeployOperations deployOperations = deployRepo.getChaincodeDeployOperations();
		
		// install chaincode
		if (!deployOperations.checkInstallChaincode(deployRepo.getCriteria().getChaincodeID())) {
			deployRepo.install();		
		}
		
		// instantiate chaincode
		if (!deployOperations.checkInstantiatedChaincode(deployRepo.getCriteria().getChaincodeID())) {
			deployRepo.instantiate(300, 500);	// 依赖 Configuration 中配置的 getEndorsementPolicyFilePath 路径
		}
	}
	
	@Test
	public void testInvoke() throws InterruptedException, ExecutionException {
		txRepo.move(5);
	}
	
	@Test
	public void testQuery() {
		System.out.println(txRepo.query());
	}
	
	@Test
	public void testDeployNewChaincode() throws Exception {
		
		ChaincodeDeployOperations deployOperations = upgradeRepo.getChaincodeDeployOperations();
		
		ChaincodeID id = upgradeRepo.getCriteria().getChaincodeID();
		
		// install new chaincode
		if (!deployOperations.checkInstallChaincode(id)) {
			deployRepo.installNewVersion();
		}
		
		// upgrade chaincode
		if (!deployOperations.checkInstantiatedChaincode(upgradeRepo.getCriteria().getChaincodeID())) {
			upgradeRepo.upgrade();
		}
	}
	
	@Channel(name = "mychannel", org = "peerOrg1")
	@Chaincode(name = "example_cc_java", type = Type.JAVA, version = "v13")
	@Repository
	interface DeployRepo extends ChaincodeRepository<Object> {
		
		@Install(clientUser = "admin", chaincodeLocation = "javacc/sample1")
		void install();
		
		@Instantiate(clientUser = "admin", endorsementPolicyFile = "chaincode-endorsement-policy.yaml", func = "init", args = { "a", "?0", "b", "?1" })
		TransactionEvent instantiate(int aAmount, int bAmount);
		
		@Install(clientUser = "admin", chaincodeLocation = "javacc/sample1", version = "v13.2")
		void installNewVersion();
	}
	
	@Channel(name = "mychannel", org = "peerOrg1")
	@Chaincode(name = "example_cc_java", type = Type.JAVA, version = "v13")
	@Repository
	interface TransactionRepo extends ChaincodeRepository<Object> {
		
		@Query(clientUser = "user1", args = "b")
		String query();
		
		@Invoke(clientUser = "user1", args = { "a", "b", "?0" })
		void move(int amount);
	}
	
	@Chaincode(channel = "mychannel", org = "peerOrg1", name = "example_cc_java", type = Type.JAVA, version = "v13.2")
	@Repository
	interface UpgradeDeployRepo extends ChaincodeRepository<Object> {
		
		@Upgrade(clientUser = "admin", endorsementPolicyFile = "chaincode-endorsement-policy.yaml", func = "init")
		TransactionEvent upgrade();
	}
}
