package io.github.hooj0.springdata.fabric.chaincode.repository.support.creator;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import org.hyperledger.fabric.sdk.ChaincodeEndorsementPolicy;
import org.hyperledger.fabric.sdk.Channel.TransactionOptions;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;

import lombok.Getter;

/**
 * chaincode repository interface `Proposal` parameter builder 
 * @author hoojo
 * @createDate 2018年7月31日 上午9:15:55
 * @file ProposalBuilder.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.support.creator
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ProposalBuilder {

	static InstallProposal install() {
		return new InstallProposal();
	}
	
	static InstantiateProposal instantiate() {
		return new InstantiateProposal();
	}
	
	static InvokeProposal invoke() {
		return new InvokeProposal();
	}
	
	static QueryProposal query() {
		return new QueryProposal();
	}
	
	static UpgradeProposal upgrade() {
		return new UpgradeProposal();
	}
	
	@Getter
	class Proposal {
		/** 向特定的Peer节点发送请求 */
		private boolean specificPeers;
		/** 向集合中的peer发送请求 */
		private Collection<Peer> send2Peers;
		/** 请求等待时间 */
		private long proposalWaitTime;
		/** 账本瞬时数据 */
		private Map<String, byte[]> transientData;
		/** 客户端User上下文 */
		private String clientUser;
		/** 发起请求的User */
		private String requestUser;
		
		private Proposal() {}
		
		public Proposal specificPeers(boolean specificPeers) {
			this.specificPeers = specificPeers;
			return this;
		}
		
		public Proposal send2Peers(Collection<Peer> send2Peers) {
			this.send2Peers = send2Peers;
			return this;
		}
		
		public Proposal proposalWaitTime(long proposalWaitTime) {
			this.proposalWaitTime = proposalWaitTime;
			return this;
		}
		
		public Proposal transientData(Map<String, byte[]> transientData) {
			this.transientData = transientData;
			return this;
		}
		
		public Proposal clientUser(String clientUser) {
			this.clientUser = clientUser;
			return this;
		}

		public Proposal requestUser(String requestUser) {
			this.requestUser = requestUser;
			return this;
		}
	}
	
	@Getter
	class TransactionProposal extends Proposal {
		/** 交易等待时间 */
		private long transactionWaitTime;
		/** 发起交易的用户 */
		private String transactionsUser;
		/** 向指定 Orderer发送交易进行排序共识 */
		private Collection<Orderer> orderers;
		/** 交易选项 */
		private TransactionOptions options;
		
		private TransactionProposal() {}
		
		public TransactionProposal transactionWaitTime(long transactionWaitTime) {
			this.transactionWaitTime = transactionWaitTime;
			return this;
		}

		public TransactionProposal transactionsUser(String transactionsUser) {
			this.transactionsUser = transactionsUser;
			return this;
		}
		
		public TransactionProposal orderers(Collection<Orderer> orderers) {
			this.orderers = orderers;
			return this;
		}
		
		public TransactionProposal transactionOptions(TransactionOptions options) {
			this.options = options;
			return this;
		}
	}
	
	@Getter
	final class InstallProposal extends Proposal {
		private String upgradeVersion;
		private File chaincodeMetaINF;
		
		private InstallProposal() {}
		
		public InstallProposal upgradeVersion(String upgradeVersion) {
			this.upgradeVersion = upgradeVersion;
			return this;
		}
		
		public InstallProposal chaincodeMetaINF(File chaincodeMetaINF) {
			this.chaincodeMetaINF = chaincodeMetaINF;
			return this;
		}
	}
	
	@Getter
	static class InstantiateProposal extends TransactionProposal {
		/** 背书策略文件：yaml/yml/json/其他 */
		private File endorsementPolicyFile;
		/** 背书策略文件流 */
		private InputStream endorsementPolicyInputStream;
		/** 背书策略 */
		private ChaincodeEndorsementPolicy endorsementPolicy;
		/** 链码集合配置 */
		private File collectionConfiguration;
		
		private InstantiateProposal() {}
		
		public InstantiateProposal endorsementPolicyFile(File endorsementPolicyFile) {
			this.endorsementPolicyFile = endorsementPolicyFile;
			return this;
		}

		public InstantiateProposal endorsementPolicyInputStream(InputStream endorsementPolicyInputStream) {
			this.endorsementPolicyInputStream = endorsementPolicyInputStream;
			return this;
		}
		
		public InstantiateProposal endorsementPolicy(ChaincodeEndorsementPolicy endorsementPolicy) {
			this.endorsementPolicy = endorsementPolicy;
			return this;
		}

		public InstantiateProposal collectionConfiguration(File collectionConfiguration) {
			this.collectionConfiguration = collectionConfiguration;
			return this;
		}
	}
	
	@Getter
	final class InvokeProposal extends TransactionProposal {
		private InvokeProposal() {}
	}
	
	@Getter
	final class QueryProposal extends Proposal {
		private QueryProposal() {}
	}
	
	@Getter
	final class UpgradeProposal extends InstantiateProposal {
		private UpgradeProposal() {}
	}
}
