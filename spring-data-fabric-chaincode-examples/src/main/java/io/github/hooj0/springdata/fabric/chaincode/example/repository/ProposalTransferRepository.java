package io.github.hooj0.springdata.fabric.chaincode.example.repository;

import org.hyperledger.fabric.sdk.TransactionRequest.Type;

import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Chaincode;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Install;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Proposal;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Transaction;
import io.github.hooj0.springdata.fabric.chaincode.enums.ProposalType;
import io.github.hooj0.springdata.fabric.chaincode.example.domain.Account;
import io.github.hooj0.springdata.fabric.chaincode.repository.ChaincodeRepository;

/**
 * transfer account repository @Proposal Annotation used example
 * @author hoojo
 * @createDate 2018年8月7日 下午3:07:05
 * @file ProposalTransferRepository.java
 * @package io.github.hooj0.springdata.fabric.chaincode.example.repository
 * @project spring-data-fabric-chaincode-examples
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Chaincode(channel = "mychannel", org = "peerOrg1", name = "example_cc_go", type = Type.GO_LANG, version = "v11.2", path = "github.com/example_cc")
public interface ProposalTransferRepository extends ChaincodeRepository<Account> {

	@Proposal(type = ProposalType.INSTALL, clientUser = "admin", waitTime = 2000L)
	void install();
	
	@Proposal(type = ProposalType.INSTANTIATE, clientUser = "admin", waitTime = 5000L, func = "init", args = { "a", "?0", "b", "?1" })
	ResultSet instantiate(int aAmount, int bAmount);
	
	@Proposal(type = ProposalType.INSTALL, clientUser = "admin", waitTime = 2000L)
	@Install(chaincodeLocation = "chaincode/go/sample_11", version = "v11.3")	// setter new version & chaincode location
	void installNewVersion();
	
	
	@Proposal(type = ProposalType.QUERY, clientUser = "user1", func = "query", args = "b")
	String queryProposal();
	
	@Proposal(type = ProposalType.QUERY, clientUser = "user1", args = "b")
	String query();
	
	@Proposal(type = ProposalType.INVOKE, clientUser = "user1", func = "move", args = { "a", "b", "?0" })
	String invokeProposal(int amount);

	@Proposal(type = ProposalType.INVOKE, clientUser = "user1", args = { "a", "b", "?0" })
	@Transaction(user = "user1", waitTime = 10000)
	String move(int amount);
}
