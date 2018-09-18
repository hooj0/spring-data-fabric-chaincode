# Spring data `fabric chaincode` Framework
Quickly develop Chancode client applications based on SpringData and Hyperledger Fabric Chaincode SDK.

# 1、运行环境
+ hyperledger chaincode v1.1+
+ spring data 2.1.0+
+ jdk 8+

# 2、添加依赖
在 `pom.xml` 中添加如下配置
```xml
<dependency>
	<groupId>spring.data.fabric.chaincode</groupId>
	<artifactId>spring-data-fabric-chaincode</artifactId>
	<version>1.0.2</version>
</dependency>
```		

# 3、基本配置
在` fabric-chaincode.properties` 中加入配置
```properties
#Sat Jul 28 20:10:31 CST 2018
# hyperledger fabric 生成的configtx版本 
fabric.network.configtx.version=v1.1

# 管理员 和 普通用户
hyperledger.fabric.sdk.commons.network.ca.admin.name=admin
hyperledger.fabric.sdk.commons.network.ca.admin.passwd=adminpw
hyperledger.fabric.sdk.commons.network.orgs.member.users=user1

# 是否启用TLS模式
hyperledger.fabric.sdk.commons.network.tls.enable=false

# 区块链网络配置相关材料 公共目录
hyperledger.fabric.sdk.commons.config.root.path=src/main/resources/fabric-integration
# e2e-2orgs 网络配置材料
hyperledger.fabric.sdk.commons.crypto.channel.config.root.path=/e2e-2orgs
# 通道和创世块配置目录
hyperledger.fabric.sdk.commons.channel.artifacts.root.path=/channel-artifacts
# chaincode 目录位置
hyperledger.fabric.sdk.commons.chaincode.source.code.root.path=/chaincode/go/sample_11
# 背书文件位置
hyperledger.fabric.sdk.commons.endorsement.policy.file.path=chaincode-endorsement-policy.yaml
# 网络配置位置
hyperledger.fabric.sdk.commons.network.config.root.path=network_configs
```

# 4、创建`Configuration`
创建 `configuration`, 注入配置对象 `ChaincodeTemplate` 使用的配置对象`PropertiesConfiguration`和缓存数据存储对象`FileSystemKeyValueStore`。
同时，设置扫描的 `basePackages = "io.github.hooj0.springdata.fabric.chaincode.example"` 对应到具体的`package`.
最后设置`EnableChaincodeRepositories` 开启 `spring-data-chaincode` 的 `repository`接口扫描方式。
```java
@Configuration
@ComponentScan(basePackages = "io.github.hooj0.springdata.fabric.chaincode.example")
@EnableChaincodeRepositories(basePackages = "io.github.hooj0.springdata.fabric.chaincode.example.repository", considerNestedRepositories = true)
public class AccountConfiguration extends AbstractChaincodeConfiguration {

	private final static Logger log = LoggerFactory.getLogger(AccountConfiguration.class);
	
	@Autowired
	private MappingChaincodeConverter mappingConverter;
	
	@Bean
	public ChaincodeTemplate chaincodeTemplate() throws ClassNotFoundException {
		log.debug("create chaincode configuration \"ChaincodeTemplate\" instance");
		
		FabricKeyValueStore store =  new FileSystemKeyValueStore(new File("src/main/resources/fabric-kv-store.properties"));
		return new ChaincodeTemplate(mappingConverter, DefaultFabricConfiguration.INSTANCE.getPropertiesConfiguration(), store);
	}
}
```

# 5、构建实体对象
实体对象可以用于参数传递、`transient-map Ledger` 数据传递或保存、JSON格式数据传递的**序列化和反序列化**。在实体对象中可以使用注解`@Entity`、`@Field`、`@Transient`，分别代表实体对象、实体属性、和`TransientMap`数据缓存
```java
@Entity
public class Account extends AbstractEntity {

	private int aAmount;
	private int bAmount;
	
	@Field
	private long timestamp;
	
	@Field(transientAlias = "transactionId")
	private String requestId;
	
	// proposal request transient map data 
	@Transient(alias = "dateTime")
	private Date date;
	
	// setter
	// getter
}
```	

# 6、


# 配置链接到区块链网络的HOST
在运行程序的时候，需要设置环境变量，添加环境变量配置
```sh
export HYPERLEDGER_FABRIC_SDK_COMMONS_NETWORK_HOST=localhost
# or remote ip address
export HYPERLEDGER_FABRIC_SDK_COMMONS_NETWORK_HOST=192.168.99.100
```


