package io.github.hooj0.springdata.fabric.chaincode.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.springdata.fabric.chaincode.example.domain.Account;
import io.github.hooj0.springdata.fabric.chaincode.example.repository.ProposalTransferRepository;

/**
 * example @Proposal annotation Transfer Service
 * @author hoojo
 * @createDate 2018年8月7日 下午3:12:22
 * @file ProposalTransferService.java
 * @package io.github.hooj0.springdata.fabric.chaincode.example.service
 * @project spring-data-fabric-chaincode-examples
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Service
public class ProposalTransferService {

	@Autowired
	private ProposalTransferRepository transferRepo;

	public void install() throws Exception {
		if (transferRepo.getChaincodeDeployOperations().checkInstallChaincode(transferRepo.getCriteria().getChaincodeID())) {
			return;
		}
		
		transferRepo.install();
	}
	
	public ResultSet instantiate(Account account) throws Exception {
		if (transferRepo.getChaincodeDeployOperations().checkInstantiatedChaincode(transferRepo.getCriteria().getChaincodeID())) {
			return null;
		}
		
		return transferRepo.instantiate(account.getaAmount(), account.getbAmount());
	}
	
	public String move(int amount) {
		return transferRepo.invokeProposal(amount);
	}
	
	public String query() {
		return transferRepo.queryProposal();
	}
}
