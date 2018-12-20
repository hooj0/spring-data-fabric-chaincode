package io.github.hooj0.springdata.fabric.chaincode.repository.v13;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;

import org.hyperledger.fabric.sdk.ChaincodeID;
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

import io.github.hooj0.fabric.sdk.commons.core.ChaincodeDeployOperations;
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
 * Chaincode CRUD (deploy & transaction) based operation test units
 * @author hoojo
 * @createDate 2018年12月20日 下午3:06:53
 * @file CRUDRepositoryTests.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.v13
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JavaCRUDRepositoryTests {

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

	@Autowired
	private UpgradeRepository upgradeRepo;

	@Test
	public void testInstall() {
		ProposalBuilder.InstallProposal install = ProposalBuilder.install();
		install.chaincodeMetaINF(Paths.get(deployRepo.getConfig().getCommonRootPath(), "meta-infs/end2endit").toFile());
		
		deployRepo.install(install, Paths.get(deployRepo.getConfig().getCommonRootPath(), "javacc/sample1").toFile());
	}
	
	@Test
	public void testInstantiate() {
		try {
			ProposalBuilder.InstantiateProposal proposal = ProposalBuilder.instantiate();
			proposal.endorsementPolicyFile(Paths.get(deployRepo.getConfig().getEndorsementPolicyFilePath()).toFile());
			
			ResultSet rs = deployRepo.instantiate(proposal, "init", "a", 500, "b", 300);
			System.out.println(rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInvoke() {
		ProposalBuilder.InvokeProposal invokeProposal = ProposalBuilder.invoke();
		invokeProposal.clientUser("user1");
		
		System.out.println(txRepo.invoke(invokeProposal, "move", "b", "a", 50));
	}
	
	@Test
	public void testQuery() {
		try {
			ProposalBuilder.QueryProposal proposal = ProposalBuilder.query();
			proposal.clientUser("user1");
			
			System.out.println(txRepo.query(proposal, "query", "a"));
			System.out.println(txRepo.query(proposal, "query", "b"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUpgrade() {
		try {
			ChaincodeID oldId = deployRepo.getCriteria().getChaincodeID();
			ChaincodeDeployOperations deployOperations = deployRepo.getChaincodeDeployOperations();
			if (!deployOperations.checkChaincode(oldId, deployRepo.getOrganization())) {
				//throw new AssertionError(oldId + " Not installed and instantiated");
			}
			
			ProposalBuilder.InstallProposal installProposal = ProposalBuilder.install().upgradeVersion("v13.2");
			
			ChaincodeID newId = ChaincodeID.newBuilder().setName(oldId.getName())
														.setPath(oldId.getPath())
														.setVersion(installProposal.getUpgradeVersion())
														.build();
			
			if (deployOperations.checkChaincode(newId, deployRepo.getOrganization())) {
				//throw new AssertionError(newId + " Already installed or instantiated");
			}
			
			if (deployOperations.checkInstallChaincode(newId)) {
				System.err.println("Installed：" + newId);
			} else {
				deployRepo.install(installProposal, Paths.get(deployRepo.getConfig().getCommonRootPath(), "javacc/sample1").toFile());
			}
			
			System.out.println("install ---->>>> " + deployOperations.checkInstallChaincode(newId));
			
			if (deployOperations.checkInstallChaincode(newId) && !deployOperations.checkInstantiatedChaincode(newId)) {
				ProposalBuilder.UpgradeProposal proposal = ProposalBuilder.upgrade();
				proposal.endorsementPolicyFile(Paths.get(upgradeRepo.getConfig().getEndorsementPolicyFilePath()).toFile());
				
				ResultSet rs = upgradeRepo.upgrade(proposal, "init", "a", 900, "b", 800);
				System.out.println(rs);
			} else {
				System.err.println("Instantiated：" + newId);
			}

			System.out.println("instantiated ---->>>> " + deployOperations.checkInstantiatedChaincode(newId));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Chaincode(name = "example_cc_java", type = Type.JAVA, version = "v13")
	@Channel(name = "mychannel", org = "peerOrg1")
	interface MyTransactionRepository extends ChaincodeRepository<Object> {
	}

	@Chaincode(name = "example_cc_java", type = Type.JAVA, version = "v13")
	@Channel(name = "mychannel", org = "peerOrg1")
	interface MyDeployChaincodeRepository extends DeployChaincodeRepository<Object> {
	}
	
	@Chaincode(name = "example_cc_java", version = "v13.2")
	@Channel(name = "mychannel", org = "peerOrg1")
	interface UpgradeRepository extends DeployChaincodeRepository<Object> {
	}
}
