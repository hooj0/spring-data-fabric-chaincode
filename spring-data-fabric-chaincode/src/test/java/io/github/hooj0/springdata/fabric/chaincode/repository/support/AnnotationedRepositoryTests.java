package io.github.hooj0.springdata.fabric.chaincode.repository.support;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.annotation.Id;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.hooj0.fabric.sdk.commons.core.ChaincodeDeployOperations;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.springdata.fabric.chaincode.annotations.Entity;
import io.github.hooj0.springdata.fabric.chaincode.annotations.Field;
import io.github.hooj0.springdata.fabric.chaincode.annotations.Transient;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Chaincode;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Channel;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Install;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Instantiate;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Invoke;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Proposal;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Query;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Serialization;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Upgrade;
import io.github.hooj0.springdata.fabric.chaincode.config.AbstractChaincodeConfiguration;
import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.ChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.SimpleChaincodeMappingContext;
import io.github.hooj0.springdata.fabric.chaincode.core.support.ChaincodeTemplate;
import io.github.hooj0.springdata.fabric.chaincode.enums.ProposalType;
import io.github.hooj0.springdata.fabric.chaincode.enums.SerializationMode;
import io.github.hooj0.springdata.fabric.chaincode.repository.ChaincodeRepository;
import io.github.hooj0.springdata.fabric.chaincode.repository.config.EnableChaincodeRepositories;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.creator.ProposalBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * annotationed repository test units
 * @author hoojo
 * @createDate 2018年7月16日 下午6:02:28
 * @file AnnotationedRepository.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.support
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AnnotationedRepositoryTests {

	@Configuration
	@EnableChaincodeRepositories(basePackageClasses = TransactionRepo.class,
		considerNestedRepositories = true, 
		includeFilters = @Filter(pattern = ".*Repo", type = FilterType.REGEX))
	public static class Config extends AbstractChaincodeConfiguration {
		@Override
		protected Set<Class<?>> getInitialEntitySet() {
			return Collections.singleton(Person.class);
		}

		@Bean
		CaptureEventListener eventListener() {
			return new CaptureEventListener();
		}
	}
	
	@Autowired 
	@Qualifier("myRepo")
	private MyRepo myRepo;
	
	@Autowired 
	@Qualifier("txRepo")
	private TransactionRepo txRepo;
	
	@Autowired 
	private DeployRepo deployRepo;
	
	@Autowired
	private UpgradeDeployRepo upgradeRepo;
	
	@Autowired
	private ProposalRepo proposalRepo;

	@Autowired
	private NewProposalRepo proposalRepoNew;
	
	@Autowired
	@Qualifier("nodeRepo")
	private NodeRepo nodeRepo;
	
	@Autowired ChaincodeTemplate template;
	@Autowired ChaincodeOperations operations;
	@Autowired SimpleChaincodeMappingContext context;
	@Autowired ChaincodeConverter converter;
	@Autowired CustomConversions conversion;
	
	@Test
	public void testInject() {
		Assert.assertNotNull("null template", template);
		Assert.assertNotNull("null operations", operations);
		Assert.assertNotNull("null context", context);
		Assert.assertNotNull("null converter", converter);
		Assert.assertNotNull("null conversion", conversion);
		
		Assert.assertNotNull("null myRepo", myRepo);
		Assert.assertNotNull("null txRepo", txRepo);
		Assert.assertNotNull("null nodeRepo", nodeRepo);
	}
	
	@Test
	public void testBasicInvoke() {
		ProposalBuilder.InvokeProposal invokeProposal = ProposalBuilder.invoke();
		invokeProposal.clientUser("user1");
		
		System.out.println(myRepo.getOrganization());
		System.out.println(myRepo.getOrganization().getUser("user1"));
		System.out.println(operations.getOrganization(myRepo.getCriteria()).getUser("user1"));
		
		System.out.println(myRepo.invoke(invokeProposal, "move", "a", "b", 5));
	}
	
	@Test
	public void testBasicQuery() {
		try {
			ProposalBuilder.QueryProposal proposal = ProposalBuilder.query();
			proposal.clientUser("user1");
			
			System.out.println(myRepo.query(proposal, "query", "a"));
			System.out.println(myRepo.query(proposal, "query", "b"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInvoke() throws InterruptedException, ExecutionException {
		//goRepo.move(10);
		//goRepo.move("b", "a", 10);
		//goRepo.moveMethod(5);
		
		//System.out.println(goRepo.moveString("b", "a", 20));
		//System.out.println(goRepo.moveResult(10));
		//System.out.println(goRepo.moveFuture(10).get());
		System.out.println(txRepo.moveEvent(10));
	}
	
	@Test
	public void testQuery() {
		//System.out.println(goRepo.query("a"));
		//System.out.println(goRepo.query());
		
		//System.out.println(goRepo.queryCustom());
		//System.out.println(goRepo.queryExprssion("a"));
		//System.out.println(goRepo.queryExprssion2("x", "y", "b"));
		
		//System.out.println(goRepo.queryResult());
		//System.out.println(goRepo.queryInt());
		
		//goRepo.queryNull();
		//System.out.println(goRepo.queryFor());
		
		Person p = new Person(1);
		p.setName("b");
		p.setDate(new Date());
		p.setVersion(1223L);
		System.out.println(txRepo.queryForExpression(p));
		
		//System.out.println(goRepo.queryForJSON(p));
		//System.out.println(goRepo.queryForJSONOPerson(p));
	}
	
	@Test
	public void testProposalDeploy() throws Exception {
		
		ChaincodeDeployOperations deployOperations = proposalRepo.getChaincodeDeployOperations();
		
		// install chaincode
		if (!deployOperations.checkInstallChaincode(proposalRepo.getCriteria().getChaincodeID())) {
			proposalRepo.installMyCC();		// 依赖 Configuration 中配置的 getChaincodeRootPath 路径	
		}
		
		// instantiate chaincode
		if (!deployOperations.checkInstantiatedChaincode(proposalRepo.getCriteria().getChaincodeID())) {
			proposalRepo.instantiateMyCC(1000, 2000);	// 依赖 Configuration 中配置的 getEndorsementPolicyFilePath 路径
		}
		
		ChaincodeID old = proposalRepo.getCriteria().getChaincodeID();
		ChaincodeID newId = ChaincodeID.newBuilder().setName(old.getName())
				.setPath("gocc/sample_11")
				.setVersion("v11.1")
				.build();
		
		
		// upgrade chaincode
		if (!deployOperations.checkChaincode(old, proposalRepo.getOrganization())) {
			throw new AssertionError(old + " Not installed and instantiated");
		}
		if (deployOperations.checkChaincode(newId, proposalRepo.getOrganization())) {
			throw new AssertionError(newId + " Already installed or instantiated");
		}
		
		if (!deployOperations.checkInstallChaincode(newId)) {
			proposalRepo.installNewVersionMyCC();
		} else {
			System.err.println("Successful Installed：" + newId);
		}
		
		if (deployOperations.checkInstallChaincode(newId) && !deployOperations.checkInstantiatedChaincode(newId)) {
			ResultSet rs = proposalRepoNew.upgradeMyCC();		// 依赖 Configuration 中配置的 getEndorsementPolicyFilePath 路径
			System.out.println(rs);
		} else {
			System.err.println("Successful Instantiated：" + newId);
		}
		
		proposalRepoNew.query();
	}
	
	@Test
	public void testProposalTransaction() throws Exception {
		
		// query
		System.out.println(proposalRepo.queryProposal());
		
		// invoke
		System.out.println(proposalRepo.invokeProposal(10));
		
		// invoke
		System.out.println(proposalRepo.move(20));
		
		// query
		System.out.println(proposalRepo.query());
	}
	
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
	public void testDeployNewChaincode() throws Exception {
		
		ChaincodeDeployOperations deployOperations = upgradeRepo.getChaincodeDeployOperations();
		
		ChaincodeID id = upgradeRepo.getCriteria().getChaincodeID();
		id = ChaincodeID.newBuilder().setName(id.getName()).setPath(id.getPath()).setVersion("v11.2").build();
		
		// install new chaincode
		if (!deployOperations.checkInstallChaincode(id)) {
			deployRepo.installNewVersion();
		}
		
		// upgrade chaincode
		if (!deployOperations.checkInstantiatedChaincode(upgradeRepo.getCriteria().getChaincodeID())) {
			upgradeRepo.upgrade();
		}
	}
	
	@Entity
	@Data
	@AllArgsConstructor
	@RequiredArgsConstructor
	static class Person {
		@Id private final int number;
		@Field(transientAlias = "personName") String name;
		@Transient(alias = "oldVersion") Long version;
		@Transient Date date;
	}
	
	@Channel(name = "mychannel", org = "peerOrg1")
	@Chaincode(name = "example_cc_go", type = Type.GO_LANG, version = "10", path = "github.com/example_cc")
	@Repository("myRepo")
	interface MyRepo extends ChaincodeRepository<Person> {
	}
	
	@Channel(name = "mychannel", org = "peerOrg1")
	@Chaincode(name = "example_cc_go", type = Type.GO_LANG, version = "11", path = "github.com/example_cc")
	@Repository("txRepo")
	interface TransactionRepo extends MyRepo {
		
		@Query(clientUser = "user1")
		String query(String account);
		
		@Query(clientUser = "user1", args = "b")
		String query();
		
		@Query(clientUser = "user1", func = "query", args = "b")
		String queryCustom();
		
		@Query(clientUser = "user1", func = "query", args = ":account")
		String queryExprssion(@Param("account") String account);
		
		@Query(clientUser = "user1", func = "query", args = "?2")
		String queryExprssion2(String param, String param2, String account);
		
		@Query(clientUser = "user1", func = "query", args = "b")
		ResultSet queryResult();
		
		@Query(clientUser = "user1", func = "query", args = "b")
		int queryInt();
		
		@Query(clientUser = "user1", func = "query", args = "b")
		void queryNull();
		
		@Query(clientUser = "user1", func = "query", args = "b")
		Person queryFor();
		
		@Query(clientUser = "user1", func = "query", args = ":#{#person.name}")
		Person queryForExpression(@Param("person") Person p);
		
		@Query(clientUser = "user1", func = "query")
		@Serialization
		Person queryForJSON(Person p);
		
		@Query(clientUser = "user1", func = "query", args = ":#{#person.name}")
		@Serialization(SerializationMode.DESERIALIZE)
		Person queryForJSONOPerson(@Param("person") Person p);
		
		
		
		@Invoke(clientUser = "user1", args = { "a", "b", "?0" })
		void move(int amount);
		
		@Invoke(clientUser = "user1")
		void move(String from, String to, int amount);
		
		@Invoke(clientUser = "user1", func = "move", args = { "a", "b", "?0" })
		void moveMethod(int amount);
		
		@Invoke(clientUser = "user1", func = "move")
		String moveString(String from, String to, int amount);
		
		@Invoke(clientUser = "user1", func = "move", args = { "a", "b", "?0" })
		ResultSet moveResult(int amount);

		@Invoke(clientUser = "user1", func = "move", args = { "a", "b", "?0" })
		CompletableFuture<TransactionEvent> moveFuture(int amount);
		
		@Invoke(clientUser = "user1", func = "move", args = { "a", "b", "?0" })
		TransactionEvent moveEvent(int amount);
	}
	
	@Channel(name = "mychannel", org = "peerOrg1")
	@Chaincode(name = "example_cc_go", type = Type.GO_LANG, version = "11", path = "github.com/example_cc")
	interface DeployRepo extends ChaincodeRepository<Person> {
		
		@Install(clientUser = "admin", chaincodeLocation = "gocc/sample_11")
		void install();
		
		@Instantiate(clientUser = "admin", endorsementPolicyFile = "chaincode-endorsement-policy.yaml", func = "init", args = { "a", "?0", "b", "?1" })
		TransactionEvent instantiate(int aAmount, int bAmount);
		
		@Install(clientUser = "admin", chaincodeLocation = "gocc/sample_11", version = "v11.2")
		void installNewVersion();
	}
	
	@Chaincode(channel = "mychannel", org = "peerOrg1", name = "example_cc_go", type = Type.GO_LANG, version = "v11.2", path = "github.com/example_cc")
	interface UpgradeDeployRepo extends ChaincodeRepository<Person> {
		
		@Upgrade(clientUser = "admin", endorsementPolicyFile = "chaincode-endorsement-policy.yaml", func = "init")
		TransactionEvent upgrade();
	}
	
	@Channel(name = "mychannel", org = "peerOrg1")
	@Chaincode(name = "example_cc_go", type = Type.GO_LANG, version = "11", path = "github.com/example_cc")
	interface ProposalRepo extends ChaincodeRepository<Person> {
		
		@Proposal(type = ProposalType.INSTALL, clientUser = "admin", waitTime = 2000L)
		void installMyCC();
		
		@Proposal(type = ProposalType.INSTANTIATE, clientUser = "admin", waitTime = 5000L, func = "init", args = { "a", "?0", "b", "?1" })
		ResultSet instantiateMyCC(int aAmount, int bAmount);
		
		@Proposal(type = ProposalType.INSTALL, clientUser = "admin", waitTime = 2000L)
		@Install(chaincodeLocation = "gocc/sample_11", version = "v11.1")	// setter new version & chaincode location
		void installNewVersionMyCC();
		
		
		@Proposal(type = ProposalType.QUERY, clientUser = "user1", func = "query", args = "b")
		String queryProposal();
		
		@Proposal(type = ProposalType.QUERY, clientUser = "user1", args = "b")
		String query();
		
		@Proposal(type = ProposalType.INVOKE, clientUser = "user1", func = "move", args = { "b", "a", "?0" })
		String invokeProposal(int amount);

		@Proposal(type = ProposalType.INVOKE, clientUser = "user1", args = { "a", "b", "?0" })
		String move(int amount);
	}
	
	// used new chaincode version & path
	@Chaincode(channel = "mychannel", org = "peerOrg1", name = "example_cc_go", type = Type.GO_LANG, version = "v11.1", path = "github.com/example_cc")
	interface NewProposalRepo extends ChaincodeRepository<Person> {
		
		@Proposal(type = ProposalType.UPGRADE, clientUser = "admin", waitTime = 50000L, func = "init")
		ResultSet upgradeMyCC();
		
		@Proposal(type = ProposalType.QUERY, clientUser = "user1", args = "b")
		String query();
	}
	
	//@Chaincode(channel = "mychannel_3", name = "mycc", type = Type.NODE, version = "1.1")
	@Repository("nodeRepo")
	interface NodeRepo extends TransactionRepo {
		
	}
	
	static class CaptureEventListener implements ApplicationListener<ApplicationEvent> {
		@Override
		public void onApplicationEvent(ApplicationEvent event) {
			Object source = event.getSource();
			
			log.debug("event source: {}, timestamp: {}", source, event.getTimestamp());
		}
	}
}
