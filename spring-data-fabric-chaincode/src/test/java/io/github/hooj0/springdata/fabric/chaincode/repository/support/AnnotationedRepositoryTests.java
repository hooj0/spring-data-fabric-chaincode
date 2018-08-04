package io.github.hooj0.springdata.fabric.chaincode.repository.support;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.annotation.Id;
import org.springframework.data.convert.CustomConversions;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.hooj0.springdata.fabric.chaincode.annotations.Entity;
import io.github.hooj0.springdata.fabric.chaincode.annotations.Field;
import io.github.hooj0.springdata.fabric.chaincode.annotations.Transient;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Chaincode;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Channel;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Invoke;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Proposal;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Query;
import io.github.hooj0.springdata.fabric.chaincode.config.AbstractChaincodeConfiguration;
import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.ChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.SimpleChaincodeMappingContext;
import io.github.hooj0.springdata.fabric.chaincode.core.support.ChaincodeTemplate;
import io.github.hooj0.springdata.fabric.chaincode.repository.ChaincodeRepository;
import io.github.hooj0.springdata.fabric.chaincode.repository.config.EnableChaincodeRepositories;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * annotationed repository test units
 * @author hoojo
 * @createDate 2018年7月16日 下午6:02:28
 * @file AnnotationedRepository.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.support
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AnnotationedRepositoryTests {

	@Configuration
	@EnableChaincodeRepositories(basePackageClasses = GoRepo.class,
		considerNestedRepositories = true, 
		includeFilters = @Filter(pattern = ".*Repo", type = FilterType.REGEX))
	public static class Config extends AbstractChaincodeConfiguration {
		@Override
		protected Set<Class<?>> getInitialEntitySet() {
			return Collections.singleton(Person.class);
		}

		@Bean
		CaptureEventListener eventListener() {
			return new CaptureEventListener();
		}
	}
	
	@Autowired @Qualifier("goRepo")
	private GoRepo goRepo;
	
	@Autowired @Qualifier("goRepo")
	private MyRepo myRepo;
	
	@Autowired
	@Qualifier("nodeRepo")
	private NodeRepo nodeRepo;
	
	@Autowired ChaincodeTemplate template;
	@Autowired ChaincodeOperations operations;
	@Autowired SimpleChaincodeMappingContext context;
	@Autowired ChaincodeConverter converter;
	@Autowired CustomConversions conversion;
	
	@Test
	public void testInject() {
		Assert.assertNotNull("null template", template);
		Assert.assertNotNull("null operations", operations);
		Assert.assertNotNull("null context", context);
		Assert.assertNotNull("null converter", converter);
		Assert.assertNotNull("null conversion", conversion);
		
		Assert.assertNotNull("null goRepo", goRepo);
		Assert.assertNotNull("null myRepo", myRepo);
	}
	
	@Test
	public void testInvoke() {
		//System.out.println(myRepo.invoke("move", "2", "3"));
		//System.out.println(myRepo.query("2223333"));
		//goRepo.installChaincode("src/gochaincode");
		//nodeRepo.installChaincode("src/gochaincode22222");
	}
	
	@Test
	public void testInstallChaincode() {
		goRepo.installChaincode("src/gochaincode");
	}
	
	@Entity
	@Data
	@AllArgsConstructor
	@RequiredArgsConstructor
	static class Person {
		@Id private final int number;
		
		@Field(transientAlias = "personName") String name;
		
		@Transient(alias = "oldVersion") Long version;
		
		@Transient Date date;
	}
	
	@Channel(name = "channel_0.1", org = "org2")
	@Chaincode(name = "mycc", type = Type.GO_LANG, version = "1.1", path = "github.com/example_cc")
	interface MyRepo extends ChaincodeRepository<Person> {
	}
	
	@Chaincode(channel = "mychannel_2", name = "mycc", type = Type.GO_LANG, version = "1.1", path = "github.com/example_cc")
	@Repository("goRepo")
	interface GoRepo extends MyRepo {
		
		@Proposal("select * from a where a = ?0")
		@Channel(name = "mychannel_123", org = "org1")
		public void installChaincode(String ccPath);
		
		@Query("a,b,c,e")
		public void installChaincode2(File ccFile);

		@Invoke("xyz")
		public void installChaincode3(InputStream ccStream);
	}
	
	@Chaincode(channel = "mychannel_3", name = "mycc", type = Type.NODE, version = "1.1")
	@Repository("nodeRepo")
	interface NodeRepo extends GoRepo {
		
	}
	
	static class CaptureEventListener implements ApplicationListener<ApplicationEvent> {
		@Override
		public void onApplicationEvent(ApplicationEvent event) {
			Object source = event.getSource();
			
			log.debug("event source: {}, timestamp: {}", source, event.getTimestamp());
		}
	}
}
