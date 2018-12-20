package io.github.hooj0.springdata.fabric.chaincode.repository.v13;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.hooj0.fabric.sdk.commons.core.ChaincodeDeployOperations;
import io.github.hooj0.springdata.fabric.chaincode.annotations.Entity;
import io.github.hooj0.springdata.fabric.chaincode.annotations.Transient;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Chaincode;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Channel;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Install;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Instantiate;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Invoke;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Query;
import io.github.hooj0.springdata.fabric.chaincode.config.AbstractChaincodeConfiguration;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.MappingChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.support.ChaincodeTemplate;
import io.github.hooj0.springdata.fabric.chaincode.repository.ChaincodeRepository;
import io.github.hooj0.springdata.fabric.chaincode.repository.config.EnableChaincodeRepositories;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2018年12月20日 下午4:39:46
 * @file PrivateDataAnnotationRepoTests.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.v13
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PrivateDataAnnotationRepoTests {

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
		
		@Override
		protected Set<Class<?>> getInitialEntitySet() {
			return Collections.singleton(PrivateData.class);
		}
	}
	
	@Autowired 
	private TransactionRepo txRepo;
	
	@Autowired 
	private DeployRepo deployRepo;
	
	@Test
	public void testDeployChaincode() throws Exception {
		
		ChaincodeDeployOperations deployOperations = deployRepo.getChaincodeDeployOperations();
		
		// install chaincode
		if (!deployOperations.checkInstallChaincode(deployRepo.getCriteria().getChaincodeID())) {
			deployRepo.install();		
		}
		
		// instantiate chaincode
		if (!deployOperations.checkInstantiatedChaincode(deployRepo.getCriteria().getChaincodeID())) {
			deployRepo.instantiate();	
		}
	}
	
	@Test
	public void testSet() throws InterruptedException, ExecutionException {
		PrivateData data = new PrivateData(1);
		data.a = 10L;
		data.b = 20L;
		data.accountA = "a";
		data.accountB = "b";
		
		txRepo.set(data);
	}
	
	@Test
	public void testQuery() {
		PrivateData data = new PrivateData(1);
		data.accountB = "b";
		
		System.out.println(txRepo.query(data));
	}
	
	@Test
	public void testMove() throws InterruptedException, ExecutionException {
		PrivateData data = new PrivateData(1);
		data.moveAmount = 3;
		data.accountA = "a";
		data.accountB = "b";
		
		txRepo.move(data);
	}
	
	@Entity
	@Data
	@AllArgsConstructor
	@RequiredArgsConstructor
	static class PrivateData {
		@Id private final int number;
		@Transient(alias = "AVal") Long a;
		@Transient(alias = "BVal") Long b;
		@Transient(alias = "A") String accountA;
		@Transient(alias = "B") String accountB;
		@Transient Integer moveAmount;
	}
	
	@Channel(name = "mychannel", org = "peerOrg1")
	@Chaincode(name = "example_cc_private", type = Type.GO_LANG, version = "v13", path = "github.com/private_data_cc")
	@Repository
	interface DeployRepo extends ChaincodeRepository<PrivateData> {
		
		@Install(clientUser = "admin", chaincodeLocation = "gocc/samplePrivateData")
		void install();
		
		@Instantiate(clientUser = "admin", endorsementPolicyFile = "chaincode-endorsement-policy.yaml", collectionConfiguration = "collection-configs/PrivateData.yaml", func = "init")
		TransactionEvent instantiate();
	}
	
	@Channel(name = "mychannel", org = "peerOrg1")
	@Chaincode(name = "example_cc_private", type = Type.GO_LANG, version = "v13", path = "github.com/private_data_cc")
	@Repository
	interface TransactionRepo extends ChaincodeRepository<PrivateData> {
		
		@Invoke
		void set(PrivateData data);
		
		@Query
		String query(PrivateData data);
		
		@Invoke
		void move(PrivateData data);
	}
}
