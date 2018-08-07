package io.github.hooj0.springdata.fabric.chaincode.example.service;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import io.github.hooj0.fabric.sdk.commons.core.ChaincodeDeployOperations;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.springdata.fabric.chaincode.example.domain.Account;
import io.github.hooj0.springdata.fabric.chaincode.example.repository.TransferRepository;
import io.github.hooj0.springdata.fabric.chaincode.example.repository.TransferRepository.NewTransferRepository;

/**
 * simple annotaion `@Install & @Instantiate & @Invoke & @Query & @Upgrade` account transfer service used example
 * @author hoojo
 * @createDate 2018年8月7日 上午11:42:08
 * @file TransferService.java
 * @package io.github.hooj0.springdata.fabric.chaincode.example.service
 * @project spring-data-fabric-chaincode-examples
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Service
public class TransferService {

	private final static Logger log = LoggerFactory.getLogger(TransferService.class);
	
	@Autowired
	@Qualifier("transferRepository")
	private TransferRepository transferRepo;

	@Autowired
	@Qualifier("newTransferRepository")
	private NewTransferRepository newTransferRepo;
	
	public void install() throws Exception {
		if (transferRepo.getChaincodeDeployOperations().checkInstallChaincode(transferRepo.getCriteria().getChaincodeID())) {
			return;
		}
		
		transferRepo.install();
	}
	
	public TransactionEvent instantiate(Account account) throws Exception {
		if (transferRepo.getChaincodeDeployOperations().checkInstantiatedChaincode(transferRepo.getCriteria().getChaincodeID())) {
			return null;
		}
		if (transferRepo.getChaincodeDeployOperations().checkInstantiatedChaincode(newTransferRepo.getCriteria().getChaincodeID())) {
			return null;
		}
		
		return transferRepo.instantiate(account.getaAmount(), account.getbAmount());
	}
	
	public TransactionEvent upgrade() throws Exception {
		ChaincodeDeployOperations operations = transferRepo.getChaincodeDeployOperations();
		
		ChaincodeID cc11_1 = transferRepo.getCriteria().getChaincodeID();
		
		if (!operations.checkChaincode(cc11_1, transferRepo.getOrganization())) {
			//throw new RuntimeException(cc11_1 + " chaincode not install or instantiate!");
			log.error(cc11_1 + " chaincode not install or instantiate!");
			return null;
		} else {
			log.info(cc11_1 + " chaincode already install & instantiate!");
		}
		
		ChaincodeID cc11_2 = newTransferRepo.getCriteria().getChaincodeID();
		
		if (!operations.checkInstallChaincode(cc11_2)) {
			transferRepo.installNewVersion();
		} else {
			log.info(cc11_2 + " chaincode already install!");
		}
		
		if (operations.checkInstantiatedChaincode(cc11_2)) {
			log.info(cc11_2 + " chaincode already instantiate!");
			return null;
		} 
		
		TransactionEvent rs =  newTransferRepo.upgrade();
		if (!operations.checkChaincode(cc11_2, transferRepo.getOrganization())) {
			log.info(cc11_2 + " chaincode already instantiate!");
			return null;
		}
		
		return rs;
	}
	
	public ResultSet move(String from, String to, int amount) {
		return transferRepo.move(from, to, amount);
	}
	
	public int query(String account) {
		return transferRepo.query(account);
	}
}
