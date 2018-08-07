package io.github.hooj0.springdata.fabric.chaincode.example.repository;

import org.hyperledger.fabric.sdk.TransactionRequest.Type;

import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Chaincode;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Channel;
import io.github.hooj0.springdata.fabric.chaincode.example.domain.Account;
import io.github.hooj0.springdata.fabric.chaincode.repository.DeployChaincodeRepository;

/**
 * based chaincode `install & instantiate & invoke & query & upgrade` account repository interface use example
 * @author hoojo
 * @createDate 2018年8月7日 上午10:48:15
 * @file AccountRepository.java
 * @package io.github.hooj0.springdata.fabric.chaincode.example.repository
 * @project spring-data-fabric-chaincode-examples
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Channel(name = "mychannel", org = "peerOrg1")
@Chaincode(name = "example_cc_go", type = Type.GO_LANG, version = "11.1", path = "github.com/example_cc")
public interface AccountRepository extends DeployChaincodeRepository<Account> {

	@Chaincode(name = "example_cc_go", version = "11.2")
	@Channel(name = "mychannel", org = "peerOrg1")
	public interface UpgradeRepository extends DeployChaincodeRepository<Account> {
	}
}

