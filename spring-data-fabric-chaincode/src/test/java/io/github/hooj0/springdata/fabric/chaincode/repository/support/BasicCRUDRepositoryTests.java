package io.github.hooj0.springdata.fabric.chaincode.repository.support;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.domain.AbstractEntity;
import io.github.hooj0.springdata.fabric.chaincode.repository.ChaincodeRepository;
import io.github.hooj0.springdata.fabric.chaincode.repository.DeployChaincodeRepository;
import io.github.hooj0.springdata.fabric.chaincode.repository.config.EnableChaincodeRepositories;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.creator.ProposalBuilder;

/**
 * Chaincode CRUD based operation test units
 * @author hoojo
 * @createDate 2018年7月20日 上午11:34:28
 * @file BasicCRUDRepositoryTests.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.support
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class BasicCRUDRepositoryTests {

	@Configuration
	@EnableChaincodeRepositories(basePackageClasses = MyRepository2.class, 
			considerNestedRepositories = true,
			includeFilters = @Filter(pattern = ".*MyRepository2", type = FilterType.REGEX))
	public static class Config extends AbstractChaincodeConfiguration {
		@Override
		protected Set<Class<?>> getInitialEntitySet() {
			return Collections.singleton(AbstractEntity.class);
		}
	}
	
	@Autowired
	private ChaincodeOperations operations;
	
	@Autowired
	private MyRepository2 repo;

	private UpgradeRepository upgradeRepository;

	@Test
	public void testInvoke() {
		
		ProposalBuilder.InvokeProposal invokeProposal = ProposalBuilder.invoke();
		invokeProposal.clientUser("user1");
		
		System.out.println(repo.getOrganization());
		System.out.println(repo.getOrganization().getUser("user1"));
		System.out.println(operations.getOrganization(repo.getCriteria()).getUser("user1"));
		
		System.out.println(repo.invoke(invokeProposal, "move", "a", "b", 5));
	}
	
	@Test
	public void testQuery() {
		try {
			ProposalBuilder.QueryProposal proposal = ProposalBuilder.query();
			proposal.clientUser("user1");
			
			System.out.println(repo.query(proposal, "query", "a"));
			System.out.println(repo.query(proposal, "query", "b"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInstall() {
		
		repo.install(ProposalBuilder.install(), repo.getConfig().getChaincodeRootPath());
	}
	
	@Test
	public void testInstantiate() {
		
		try {
			ProposalBuilder.InstantiateProposal proposal = ProposalBuilder.instantiate();
			//proposal.endorsementPolicyFile(Paths.get(operations.getConfig().getEndorsementPolicyFilePath()).toFile());
			
			ResultSet rs = repo.instantiate(proposal, "init", "a", 500, "b", 300);
			System.out.println(rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUpgrade() {
		try {
			ChaincodeDeployOperations deployOperations = operations.getChaincodeDeployOperations(repo.getCriteria());
			if (!deployOperations.checkChaincode(repo.getCriteria().getChaincodeID(), repo.getOrganization())) {
				throw new AssertionError("chaincode 1 没有安装和实例化");
			}
			
			ProposalBuilder.InstallProposal installProposal = ProposalBuilder.install().upgradeVersion("11_2");
			
			ChaincodeID oldId = repo.getCriteria().getChaincodeID();
			ChaincodeID newId = ChaincodeID.newBuilder().setName(oldId.getName()).setPath(oldId.getPath()).setVersion(installProposal.getUpgradeVersion()).build();
			
			if (!deployOperations.checkChaincode(newId, repo.getOrganization())) {
				throw new AssertionError("chaincode 11 已经安装或实例化");
			}
			
			repo.install(installProposal, Paths.get(repo.getConfig().getCommonRootPath(), "gocc/sample_11").toFile());
			
			System.out.println(deployOperations.checkInstallChaincode(repo.getCriteria().getChaincodeID()));
			
			ProposalBuilder.UpgradeProposal proposal = ProposalBuilder.upgrade();
			proposal.endorsementPolicyFile(Paths.get(operations.getConfig(repo.getCriteria()).getEndorsementPolicyFilePath()).toFile());
			
			ResultSet rs = upgradeRepository.upgrade(proposal, "init", "a", 900, "b", 800);
			System.out.println(rs);

			System.out.println(deployOperations.checkInstantiatedChaincode(newId));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	interface MyRepository extends ChaincodeRepository<Object> {
	}

	@Chaincode(name = "example_cc_go", type = Type.GO_LANG, version = "11", path = "github.com/example_cc")
	@Channel(name = "mychannel", org = "peerOrg1")
	interface MyRepository2 extends DeployChaincodeRepository<Object> {
	}
	
	@Chaincode(name = "example_cc_go", version = "11_2")
	interface UpgradeRepository extends DeployChaincodeRepository<Object> {
	}
}
