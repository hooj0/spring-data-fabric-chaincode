package io.github.hooj0.springdata.fabric.chaincode.example.service;

import java.nio.file.Paths;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.hooj0.fabric.sdk.commons.core.ChaincodeDeployOperations;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.springdata.fabric.chaincode.example.domain.Account;
import io.github.hooj0.springdata.fabric.chaincode.example.repository.AccountRepository;
import io.github.hooj0.springdata.fabric.chaincode.example.repository.AccountRepository.UpgradeRepository;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.creator.ProposalBuilder;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.creator.ProposalBuilder.InstallProposal;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.creator.ProposalBuilder.InstantiateProposal;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.creator.ProposalBuilder.InvokeProposal;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.creator.ProposalBuilder.QueryProposal;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.creator.ProposalBuilder.UpgradeProposal;

/**
 * based chaincode `install & instantiate & invoke & query & upgrade` account service interface use example
 * @author hoojo
 * @createDate 2018年8月7日 上午11:42:08
 * @file AccountService.java
 * @package io.github.hooj0.springdata.fabric.chaincode.example.service
 * @project spring-data-fabric-chaincode-examples
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Service
public class AccountService {

	private final static Logger log = LoggerFactory.getLogger(AccountService.class);
	
	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private UpgradeRepository upgradeRepo;

	public void install() throws Exception {
		if (accountRepo.getChaincodeDeployOperations().checkInstallChaincode(accountRepo.getCriteria().getChaincodeID())) {
			return;
		}
		
		InstallProposal proposal = ProposalBuilder.install();
		proposal.clientUser("admin");
		
		accountRepo.install(proposal, Paths.get(accountRepo.getConfig().getCommonRootPath(), "chaincode/go/sample_11").toFile());
	}
	
	public ResultSet instantiate(Account account) throws Exception {
		if (accountRepo.getChaincodeDeployOperations().checkInstantiatedChaincode(accountRepo.getCriteria().getChaincodeID())) {
			return null;
		}
		if (accountRepo.getChaincodeDeployOperations().checkInstantiatedChaincode(upgradeRepo.getCriteria().getChaincodeID())) {
			return null;
		}
		
		InstantiateProposal proposal = ProposalBuilder.instantiate();
		proposal.clientUser("admin");
		proposal.endorsementPolicyFile(Paths.get(accountRepo.getConfig().getCommonRootPath(), "chaincode-endorsement-policy.yaml").toFile());
		
		return accountRepo.instantiate(proposal, "init", "a", account.getaAmount(), "b", account.getbAmount());
	}
	
	public ResultSet upgrade() throws Exception {
		ChaincodeDeployOperations operations = accountRepo.getChaincodeDeployOperations();
		
		ChaincodeID cc11_1 = accountRepo.getCriteria().getChaincodeID();
		
		if (!operations.checkChaincode(cc11_1, accountRepo.getOrganization())) {
			log.error(cc11_1 + " chaincode not install or instantiate!");
			return null;
		} else {
			log.info(cc11_1 + " chaincode already install & instantiate!");
		}
		
		InstallProposal install = ProposalBuilder.install().upgradeVersion("11.2");
		install.clientUser("admin");
		
		ChaincodeID cc11_2 = ChaincodeID.newBuilder().setName(cc11_1.getName())
				.setPath(cc11_1.getPath())
				.setVersion(install.getUpgradeVersion())
				.build();
		
		if (!operations.checkInstallChaincode(cc11_2)) {
			accountRepo.install(install, accountRepo.getConfig().getChaincodeRootPath());
		} else {
			log.info(cc11_2 + " chaincode already install!");
		}
		
		UpgradeProposal proposal = ProposalBuilder.upgrade();
		proposal.clientUser("admin");
		proposal.endorsementPolicyFile(Paths.get(accountRepo.getConfig().getCommonRootPath(), "chaincode-endorsement-policy.yaml").toFile());
		
		if (operations.checkInstantiatedChaincode(cc11_2)) {
			log.info(cc11_2 + " chaincode already instantiate!");
			return null;
		} 
		
		ResultSet rs =  upgradeRepo.upgrade(proposal, "func");
		
		if (!operations.checkChaincode(cc11_2, accountRepo.getOrganization())) {
			log.error(cc11_2 + " chaincode not install or instantiate!");
			return null;
		}
		
		return rs;
	}
	
	public ResultSet invoke(String from, String to, int amount) {
		InvokeProposal proposal = ProposalBuilder.invoke();
		proposal.clientUser("user1");
		
		return accountRepo.invoke(proposal, "move", from, to, amount);
	}
	
	public ResultSet query(String account) {
		QueryProposal proposal = ProposalBuilder.query();
		proposal.clientUser("user1");
		
		return accountRepo.queryFor(proposal, "query", account);
	}
}
