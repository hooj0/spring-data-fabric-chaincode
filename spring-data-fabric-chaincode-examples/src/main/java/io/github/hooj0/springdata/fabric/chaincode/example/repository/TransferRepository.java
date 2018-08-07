package io.github.hooj0.springdata.fabric.chaincode.example.repository;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Chaincode;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Channel;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Install;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Instantiate;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Invoke;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Query;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Upgrade;
import io.github.hooj0.springdata.fabric.chaincode.example.domain.Account;
import io.github.hooj0.springdata.fabric.chaincode.repository.ChaincodeRepository;

/**
 * simple annotaion `@Install & @Instantiate & @Invoke & @Query & @Upgrade` account transfer repository used example
 * @author hoojo
 * @createDate 2018年8月7日 上午11:27:58
 * @file TransferRepository.java
 * @package io.github.hooj0.springdata.fabric.chaincode.example.repository
 * @project spring-data-fabric-chaincode-examples
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Chaincode(channel = "mychannel", org = "peerOrg1", name = "example_cc_go", type = Type.GO_LANG, version = "11.1", path = "github.com/example_cc")
@Repository("transferRepository")
public interface TransferRepository extends ChaincodeRepository<Account> {

	@Install(clientUser = "admin", chaincodeLocation = "chaincode/go/sample_11")
	public void install();
	
	@Instantiate(clientUser = "admin", endorsementPolicyFile = "chaincode-endorsement-policy.yaml", func = "init", args = { "a", "?0", "b", "?1" })
	TransactionEvent instantiate(int aAmount, int bAmount);
	
	@Install(clientUser = "admin", chaincodeLocation = "chaincode/go/sample_11", version = "v11.2")
	void installNewVersion();
	
	@Query(clientUser = "user1")
	int query(String account);
	
	@Invoke(clientUser = "user1")
	ResultSet move(String from, String to, int amount);
	
	@Invoke(clientUser = "user1", args = { "a", "b", ":#{#account.aAmount}"})
	ResultSet move(@Param("account") Account account);
	
	@Channel(name = "mychannel", org = "peerOrg1")
	@Chaincode(name = "example_cc_go", version = "v11.2")
	@Repository("newTransferRepository")
	public interface NewTransferRepository extends TransferRepository {

		@Upgrade(clientUser = "admin", endorsementPolicyFile = "chaincode-endorsement-policy.yaml", func = "init")
		TransactionEvent upgrade();
	}
}
