package io.github.hooj0.springdata.fabric.chaincode.example.domain;

import java.util.Date;

import io.github.hooj0.springdata.fabric.chaincode.annotations.Entity;
import io.github.hooj0.springdata.fabric.chaincode.annotations.Field;
import io.github.hooj0.springdata.fabric.chaincode.annotations.Transient;
import io.github.hooj0.springdata.fabric.chaincode.domain.AbstractEntity;

/**
 * user account domain entity
 * @author hoojo
 * @createDate 2018年8月7日 上午10:34:07
 * @file Account.java
 * @package io.github.hooj0.springdata.fabric.chaincode.example.domain
 * @project spring-data-fabric-chaincode-examples
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Entity
public class Account extends AbstractEntity {

	private int aAmount;
	private int bAmount;
	
	@Field
	private long timestamp;
	
	@Field(transientAlias = "transactionId")
	private String txId;
	
	// proposal request transient map data 
	@Transient(alias = "dateTime")
	private Date date;

	public int getaAmount() {
		return aAmount;
	}

	public void setaAmount(int aAmount) {
		this.aAmount = aAmount;
	}

	public int getbAmount() {
		return bAmount;
	}

	public void setbAmount(int bAmount) {
		this.bAmount = bAmount;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getTxId() {
		return txId;
	}

	public void setTxId(String txId) {
		this.txId = txId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
