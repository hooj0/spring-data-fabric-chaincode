package io.github.hooj0.springdata.fabric.chaincode.core.query;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;

import lombok.Getter;
import lombok.ToString;

/**
 * Chaincode repository common criteria
 * @author hoojo
 * @createDate 2018年7月19日 下午6:17:16
 * @file Criteria.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.query
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Getter
@ToString
public final class Criteria {

	/** 通道名称 */
	private String channel;
	/** 链码合约名称 */
	private String name;
	/** 链码合约路径 */
	private String path;
	/** 链码合约版本 */
	private String version;
	/** 链码类型 */
	private Type type;
	/** 认证组织 */
	private String org;
	
	private Criteria() {
	}

	private Criteria(Criteria criteria) {
		this.channel = criteria.channel;
		this.name = criteria.name;
		this.path = criteria.path;
		this.version = criteria.version;
		this.type = criteria.type;
		this.org = criteria.org;
	}
	
	public ChaincodeID getChaincodeID() {
		return ChaincodeID.newBuilder().setName(name).setPath(path).setVersion(version).build();
	}
	
	public static final class CriteriaBuilder {
		
		private Criteria criteria;
		
		public CriteriaBuilder() {
			this.criteria = new Criteria();
		}
		
		public CriteriaBuilder(Criteria criteria) {
			this.criteria = new Criteria(criteria);
		}
		
		public static CriteriaBuilder newBuilder() {
			return new CriteriaBuilder();
		}
		
		public static CriteriaBuilder newBuilder(Criteria criteria) {
			return new CriteriaBuilder(criteria);
		}
		
		public static CriteriaBuilder from(Criteria criteria) {
			return new CriteriaBuilder(criteria);
		}
		
		public CriteriaBuilder channel(String channel) {
			criteria.channel = channel;
			return this;
		}
		
		public CriteriaBuilder name(String name) {
			criteria.name = name;
			return this;
		}
		
		public CriteriaBuilder path(String path) {
			criteria.path = path;
			return this;
		}
		
		public CriteriaBuilder version(String version) {
			criteria.version = version;
			return this;
		}
		
		public CriteriaBuilder type(Type type) {
			criteria.type = type;
			return this;
		}

		public CriteriaBuilder org(String org) {
			criteria.org = org;
			return this;
		}
		
		public Criteria build() {
			return criteria;
		}
	}
}
