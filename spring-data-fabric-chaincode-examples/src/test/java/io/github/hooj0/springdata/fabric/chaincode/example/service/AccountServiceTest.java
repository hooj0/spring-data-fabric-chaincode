package io.github.hooj0.springdata.fabric.chaincode.example.service;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.hooj0.springdata.fabric.chaincode.example.config.AccountConfiguration;
import io.github.hooj0.springdata.fabric.chaincode.example.domain.Account;
import io.github.hooj0.springdata.fabric.chaincode.example.repository.AccountRepository;
import io.github.hooj0.springdata.fabric.chaincode.example.repository.TransferRepository;
import io.github.hooj0.springdata.fabric.chaincode.repository.config.EnableChaincodeRepositories;

/**
 * account service test unit
 * @author hoojo
 * @createDate 2018年8月7日 下午5:13:17
 * @file AccountServiceTest.java
 * @package io.github.hooj0.springdata.fabric.chaincode.example.service
 * @project spring-data-fabric-chaincode-examples
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AccountServiceTest {

	@Configuration
	@EnableChaincodeRepositories(basePackageClasses = AccountRepository.class, 
			considerNestedRepositories = true,
			includeFilters = @Filter(pattern = ".*Repository", type = FilterType.REGEX))
	public static class Config extends AccountConfiguration {
		
	}
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	@Qualifier("transferRepository")
	private TransferRepository repo;
	
	@Test
	public void testService() {
		Assert.assertNotNull(accountService);
	}
	
	@Test
	public void testInstall() throws Exception {
		accountService.install();
	}
	
	@Test
	public void testInvoke() throws Exception {
		Account account = new Account();
		account.setaAmount(1000);
		account.setbAmount(800);
		account.setDate(new Date());
		account.setTimestamp(System.currentTimeMillis());
		account.setRequestId("xxx" + System.currentTimeMillis());
		
		System.out.println(repo.moveFor(account));
	}
}
