package io.github.hooj0.springdata.fabric.chaincode.repository.support;

import java.io.File;
import java.io.IOError;
import java.io.InputStream;
import java.util.Date;

import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.annotation.Id;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.hooj0.springdata.fabric.chaincode.annotations.Entity;
import io.github.hooj0.springdata.fabric.chaincode.annotations.Field;
import io.github.hooj0.springdata.fabric.chaincode.annotations.Transient;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Chaincode;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Channel;
import io.github.hooj0.springdata.fabric.chaincode.config.AbstractChaincodeConfiguration;
import io.github.hooj0.springdata.fabric.chaincode.entity.BaseEntity;
import io.github.hooj0.springdata.fabric.chaincode.repository.ChaincodeRepository;
import io.github.hooj0.springdata.fabric.chaincode.repository.DeployChaincodeRepository;
import io.github.hooj0.springdata.fabric.chaincode.repository.config.EnableChaincodeRepositories;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Chaincode CRUD 基本的操作
 * @author hoojo
 * @createDate 2018年7月20日 上午11:34:28
 * @file BasicCRUDRepositoryTests.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.support
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class BasicCRUDRepositoryTests {

	@Configuration
	@EnableChaincodeRepositories(basePackageClasses = MyRepository2.class, 
			considerNestedRepositories = true,
			includeFilters = @Filter(pattern = ".*MyRepository2", type = FilterType.REGEX))
	public static class Config extends AbstractChaincodeConfiguration {
		/*@Override
		protected Set<Class<?>> getInitialEntitySet() {
			return Collections.singleton(User.class);
		}*/
	}
	
	@Autowired
	private MyRepository2 repo;

	@Test
	public void testInvoke() {
		Assert.assertEquals(repo.invoke("move", "a", "b", "c"), "success");
	}
	
	@Test
	public void testQuery() {
		Assert.assertEquals(repo.query("find", "a", "b", "c"), "success");
	}
	
	@Test
	public void testInstall() {
		repo.install(new File("."));
		InputStream is = null;
		repo.install(is);
	}
	
	@Test
	public void testInstantiate() {
		repo.instantiate("".getBytes(), "move", "a", "b");
		InputStream is = null;
		repo.instantiate(is, "move", "a", "b");
		repo.instantiate(new File("a.yaml"), "move", "a", "b");
		repo.instantiate(new File("a.yml"), "move", "a", "b");
		repo.instantiate(new File("a.json"), "move", "a", "b");
	}
	
	@Test
	public void testUpgrade() {
		repo.upgrade("".getBytes(), "v1.3", "move", "a", "b");
		InputStream is = null;
		repo.upgrade(is, "move", "v1.3", "a", "b");
		repo.upgrade(new File("a.yaml"), "v1.3", "move", "a", "b");
		repo.upgrade(new File("a.yml"), "v1.3", "move", "a", "b");
		repo.upgrade(new File("a.json"), "v1.3", "move", "a", "b");
	}
	
	@Chaincode(orgs = { "org1", "org2" }, channel = "mychannel_1", name = "mycc", type = Type.GO_LANG, version = "1.1", path = "github.com/example_cc")
	interface MyRepository extends ChaincodeRepository<User> {
	}

	@Chaincode(name = "mycc", type = Type.GO_LANG, version = "1.1", path = "github.com/example_cc")
	@Channel(name = "channel-2", orgs = {"a", "b"})
	interface MyRepository2 extends DeployChaincodeRepository<User> {
	}
	
	@Entity
	@AllArgsConstructor
	@RequiredArgsConstructor
	static class User extends BaseEntity {
		@Id private final int number;
		
		@Field(transientAlias = "personName") String name;
		
		@Transient(alias = "oldVersion") Long version;
		
		@Transient Date date;
	}
}
