package io.github.hooj0.springdata.fabric.chaincode.repository.v13;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.CollectionConfigPackage;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Maps;

import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Chaincode;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Channel;
import io.github.hooj0.springdata.fabric.chaincode.config.AbstractChaincodeConfiguration;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.MappingChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.support.ChaincodeTemplate;
import io.github.hooj0.springdata.fabric.chaincode.domain.AbstractEntity;
import io.github.hooj0.springdata.fabric.chaincode.repository.ChaincodeRepository;
import io.github.hooj0.springdata.fabric.chaincode.repository.DeployChaincodeRepository;
import io.github.hooj0.springdata.fabric.chaincode.repository.config.EnableChaincodeRepositories;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.creator.ProposalBuilder;

/**
 * private data (deploy & transaction) based operation test units
 * @author hoojo
 * @createDate 2018年12月20日 下午3:26:17
 * @file PrivateDataRepositoryTest.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.v13
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PrivateDataRepositoryTest {

	private static final String CHAIN_CODE_LOCATION = "gocc/samplePrivateData";
	
	@Configuration
	@EnableChaincodeRepositories(basePackageClasses = MyDeployChaincodeRepository.class, 
			considerNestedRepositories = true,
			includeFilters = @Filter(pattern = ".*Repository", type = FilterType.REGEX))
	public static class Config extends AbstractChaincodeConfiguration {
		
		@Autowired
		private MappingChaincodeConverter mappingConverter;
		
		@Bean
		public ChaincodeTemplate chaincodeTemplate() throws ClassNotFoundException {
			return new ChaincodeTemplate(mappingConverter);
		}
		
		@Override
		protected Set<Class<?>> getInitialEntitySet() {
			return Collections.singleton(AbstractEntity.class);
		}
	}
	
	@Autowired
	private MyTransactionRepository txRepo;
	
	@Autowired
	private MyDeployChaincodeRepository deployRepo;
	
	@Test
	public void testInstall() throws Exception {
		ProposalBuilder.InstallProposal install = ProposalBuilder.install();
		
		deployRepo.install(install, Paths.get(deployRepo.getConfig().getCommonRootPath(), CHAIN_CODE_LOCATION).toFile());
	}
	
	@Test
	public void testInstantiate() throws Exception {
		
		try {
			ProposalBuilder.InstantiateProposal proposal = ProposalBuilder.instantiate();
			proposal.collectionConfiguration(Paths.get(deployRepo.getConfig().getCommonRootPath(), "collection-configs/PrivateData.yaml").toFile());
			proposal.endorsementPolicyFile(Paths.get(deployRepo.getConfig().getEndorsementPolicyFilePath()).toFile());
			
			ResultSet rs = deployRepo.instantiate(proposal, "init");
			System.out.println("-------->>>>>>>" + rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInvokeSet() {
		
		ProposalBuilder.InvokeProposal invokeProposal = ProposalBuilder.invoke();
		invokeProposal.clientUser("user1");
		
		Map<String, byte[]> transientMap = Maps.newHashMap();
		transientMap.put("A", "a".getBytes(UTF_8));   // test using bytes as args. End2end uses Strings.
        transientMap.put("AVal", "500".getBytes(UTF_8));
        transientMap.put("B", "b".getBytes(UTF_8));
        transientMap.put("BVal", String.valueOf(200 + 50).getBytes(UTF_8));
        invokeProposal.transientData(transientMap);
		
		ResultSet rs = txRepo.invoke(invokeProposal, "set");
		System.out.println(rs);
		
		try {
			TransactionEvent event = rs.getTransactionEvent();
			System.out.println(event.isValid());
			System.out.println(event.getBlockEvent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testQuery() {
		
		ProposalBuilder.QueryProposal proposal = ProposalBuilder.query();
		proposal.clientUser("user1");
		
		Map<String, byte[]> transientData = new HashMap<>();
		transientData.put("B", "b".getBytes(UTF_8)); // test using bytes as args. End2end uses Strings.
		proposal.transientData(transientData);
		
		ResultSet rs = txRepo.queryFor(proposal, "query");
		System.out.println(rs);
	}
	
	@Test
	public void testInvokeMove() {
		
		ProposalBuilder.InvokeProposal invokeProposal = ProposalBuilder.invoke();
		invokeProposal.clientUser("user1");
		
		Map<String, byte[]> transientMap = Maps.newHashMap();
		transientMap.put("A", "a".getBytes(UTF_8)); //test using bytes .. end2end uses Strings.
        transientMap.put("B", "b".getBytes(UTF_8));
		transientMap.put("moveAmount", "5".getBytes(UTF_8));
		invokeProposal.transientData(transientMap);
		
		ResultSet rs = txRepo.invoke(invokeProposal, "move");
		System.out.println(rs);
		
		try {
			TransactionEvent event = rs.getTransactionEvent();
			System.out.println(event.isValid());
			System.out.println(event.getBlockEvent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCollectionData() throws Exception {
		if (txRepo.getConfig().isFabricVersionAtOrAfter("1.3")) {
			
			org.hyperledger.fabric.sdk.Channel channel = txRepo.getChaincodeDeployOperations().getChannel();

			Set<String> expect = new HashSet<>(Arrays.asList("COLLECTION_FOR_A", "COLLECTION_FOR_B"));
			Set<String> got = new HashSet<>();

			CollectionConfigPackage packages = channel.queryCollectionsConfig("example_cc_private", channel.getPeers().iterator().next(), txRepo.getConfig().getOrganization("peerOrg1").getPeerAdmin());
			for (CollectionConfigPackage.CollectionConfig collectionConfig : packages.getCollectionConfigs()) {
				System.out.println(collectionConfig.getName());
				got.add(collectionConfig.getName());

			}
			assertEquals(expect, got);
		}
	}
	
	@Chaincode(name = "example_cc_private", type = Type.GO_LANG, version = "v13", path = "github.com/private_data_cc")
	@Channel(name = "mychannel", org = "peerOrg1")
	interface MyTransactionRepository extends ChaincodeRepository<Object> {
	}

	@Chaincode(name = "example_cc_private", type = Type.GO_LANG, version = "v13", path = "github.com/private_data_cc")
	@Channel(name = "mychannel", org = "peerOrg1")
	interface MyDeployChaincodeRepository extends DeployChaincodeRepository<Object> {
	}
}
