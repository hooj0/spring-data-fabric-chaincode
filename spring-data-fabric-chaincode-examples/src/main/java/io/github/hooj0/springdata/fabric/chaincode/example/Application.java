package io.github.hooj0.springdata.fabric.chaincode.example;

import java.io.IOException;
import java.util.Date;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.springdata.fabric.chaincode.example.config.AccountConfiguration;
import io.github.hooj0.springdata.fabric.chaincode.example.domain.Account;
import io.github.hooj0.springdata.fabric.chaincode.example.service.AccountService;
import io.github.hooj0.springdata.fabric.chaincode.example.service.ProposalTransferService;
import io.github.hooj0.springdata.fabric.chaincode.example.service.TransferService;

/**
 * chaincode account transfer application program entry
 * @author hoojo
 * @createDate 2018年8月7日 上午11:43:12
 * @file Application.java
 * @package io.github.hooj0.springdata.fabric.chaincode.example
 * @project spring-data-fabric-chaincode-examples
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class Application {

	public static void main(String[] args) {
		start();
	}

	public static void start() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AccountConfiguration.class);

		try {
			
			//runAccountExample(context);
			
			//runTransferExample(context);
			
			runProposalExample(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		context.close();
	}
	
	public static void runAccountExample(AnnotationConfigApplicationContext context) throws Exception {
		AccountService accountService = context.getBean(AccountService.class);
		
		// install chaincode
		accountService.install();
		
		Account account = new Account();
		account.setaAmount(1000);
		account.setbAmount(800);
		account.setDate(new Date());
		account.setTimestamp(System.currentTimeMillis());
		account.setTxId("xxx" + System.currentTimeMillis());
		
		// instantiate chaincode
		ResultSet rs = accountService.instantiate(account);
		System.out.println(rs);
		
		// invoke chaincode 
		rs = accountService.invoke("a", "b", 50);
		System.out.println(rs);
		
		// query chaincode
		rs = accountService.query("a");
		System.out.println(rs);
		
		rs = accountService.query("b");
		System.out.println(rs);
		
		// upgrade chaincode
		rs = accountService.upgrade();
		System.out.println(rs);
		
		System.err.println("done!");
	}
	
	public static void runTransferExample(AnnotationConfigApplicationContext context) throws Exception {
		TransferService service = context.getBean(TransferService.class);
		
		// install chaincode
		service.install();
		
		Account account = new Account();
		account.setaAmount(1000);
		account.setbAmount(800);
		account.setDate(new Date());
		account.setTimestamp(System.currentTimeMillis());
		account.setTxId("xxx" + System.currentTimeMillis());
		
		// instantiate chaincode
		TransactionEvent event = service.instantiate(account);
		if (event != null) {
			System.out.println(event.isValid());
			System.out.println(event.getValidationCode());
			System.out.println(event.getBlockEvent());
		}
		
		// move transfer chaincode
		ResultSet rs = service.move("b", "a", 110);
		System.out.println(rs);
		
		// query chaincode
		int amount = service.query("a");
		System.out.println(amount);

		amount = service.query("b");
		System.out.println(amount);
		
		// upgrade chaincode
		event = service.upgrade();
		if (event != null) {
			System.out.println(event.isValid());
			System.out.println(event.getValidationCode());
			System.out.println(event.getBlockEvent());
		}
		
		System.err.println("done!");
	}
	
	public static void runProposalExample(AnnotationConfigApplicationContext context) throws Exception {
		ProposalTransferService service = context.getBean(ProposalTransferService.class);
		
		// install chaincode
		service.install();
		
		Account account = new Account();
		account.setaAmount(1000);
		account.setbAmount(800);
		account.setDate(new Date());
		account.setTimestamp(System.currentTimeMillis());
		account.setTxId("xxx" + System.currentTimeMillis());
		
		// instantiate chaincode
		ResultSet rs = service.instantiate(account);
		System.out.println(rs);
		
		// move transfer chaincode
		String result = service.move(100);
		System.out.println(result);
		
		// query chaincode
		result = service.query();
		System.out.println(result);
		
		System.err.println("done!");
	}
}
