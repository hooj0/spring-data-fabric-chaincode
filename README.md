# Spring data `fabric chaincode` Framework
Quickly develop Chancode client applications based on SpringData and Hyperledger Fabric Chaincode SDK.

# 1、运行环境
+ `fabric-sdk-java` v1.1+
+ `spring data` 2.1.0+
+ `jdk` 8+

**extra jar**
+ `fabric-sdk-commons`  v1.1+
+ `spring-data-fabric-chaincode`  v1.1+

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

由于在` fabric-chaincode.properties` 中没有配置自定义的区块链网络，系统将在底层使用默认的网络配置，下面是使用默认的区块链网络配置信息：
```properties
hyperledger.fabric.sdk.commons.network.org.peerOrg1.mspid=Org1MSP
hyperledger.fabric.sdk.commons.network.org.peerOrg1.caName=ca0
hyperledger.fabric.sdk.commons.network.org.peerOrg1.domname=org1.example.com
hyperledger.fabric.sdk.commons.network.org.peerOrg1.ca_location=http\://192.168.8.8\:7054
hyperledger.fabric.sdk.commons.network.org.peerOrg1.orderer_locations=orderer.example.com@grpc\://192.168.8.8\:7050
hyperledger.fabric.sdk.commons.network.org.peerOrg1.peer_locations=peer0.org1.example.com@grpc\://192.168.8.8\:7051, peer1.org1.example.com@grpc\://192.168.8.8\:7056
hyperledger.fabric.sdk.commons.network.org.peerOrg1.eventhub_locations=peer0.org1.example.com@grpc\://192.168.8.8\:7053, peer1.org1.example.com@grpc\://192.168.8.8\:7058


hyperledger.fabric.sdk.commons.network.org.peerOrg2.mspid=Org2MSP
hyperledger.fabric.sdk.commons.network.org.peerOrg2.domname=org2.example.com
hyperledger.fabric.sdk.commons.network.org.peerOrg2.ca_location=http\://192.168.8.8\:8054
hyperledger.fabric.sdk.commons.network.org.peerOrg2.orderer_locations=orderer.example.com@grpc\://192.168.8.8\:7050
hyperledger.fabric.sdk.commons.network.org.peerOrg2.peer_locations=peer0.org2.example.com@grpc\://192.168.8.8\:8051, peer1.org2.example.com@grpc\://192.168.8.8\:8056
hyperledger.fabric.sdk.commons.network.org.peerOrg2.eventhub_locations=peer0.org2.example.com@grpc\://192.168.8.8\:8053, peer1.org2.example.com@grpc\://192.168.8.8\:8058
```
上面的配置信息的IP地址取值于环境变量值 `HYPERLEDGER_FABRIC_SDK_COMMONS_NETWORK_HOST`, 通过**配置环境变量**来设置默认配置的`Host`。
## 配置链接到区块链网络的HOST
在运行程序的时候，需要设置环境变量，添加环境变量配置
```sh
export HYPERLEDGER_FABRIC_SDK_COMMONS_NETWORK_HOST=localhost
# or remote ip address
export HYPERLEDGER_FABRIC_SDK_COMMONS_NETWORK_HOST=192.168.99.100
```
当在环境变量中配置host后，默认的区块链网络配置的`host`将是自定义的`host`。如果是使用自定义区块链网络的配置，这个值将忽略。

# 4、创建`Configuration`
继承`AbstractChaincodeConfiguration`创建 `Configuration`对象， 注入配置对象 `ChaincodeTemplate` 传入必要的参数，可以使用的系统配置方式实现类 `PropertiesConfiguration` 和缓存数据存储对象实现类`FileSystemKeyValueStore`。 在 `Configuration`对象上设置扫描的路径 `basePackages = "io.github.hooj0.springdata.fabric.chaincode.example"` 对应到具体的`package`。设置`EnableChaincodeRepositories` 开启 `spring-data-chaincode` 的 `repository`接口扫描方式。

+ `@Configuration` 标记为配置对象
+ `@ComponentScan` 设置依赖注入的扫描环境
+ `@EnableChaincodeRepositories` 启用`Chaincode Repository`

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
实体对象可以用于在做`Chaincode CRUD`操作时的参数传递、`transient-map Ledger` 数据传递或保存、JSON格式数据传递的**序列化和反序列化**。
在实体对象中可以使用注解:
+ `@Entity` 实体对象，用于在`repository`中和`Chaincode`交互进行`ORM`映射，并且可以做`JSON`的转换处理。
+ `@Field` 实体属性，用于配置属性的映射别名等
+ `@Transient` `TransientMap`数据缓存，做数据回传或瞬时存储

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

# 6、编写核心的`CRUD Repository`
`Repository` 可以完成 `Chaincode` 的 `CRUD` 操作，可以简单指定操作的 `Chaincode`、`Channel`、`Org`等必要信息，就可以完成一个智能合约的基本常用业务操作。在`Service`中注入`Repository`后就可以使用常规的`API`接口完成`CRUD`操作。
+ `@Channel` 配置通道信息，合约所运行的通道和组织
+ `@Chaincode` 配置`Chaincode`信息，合约名称、类型、版本、路径等

```java
@Channel(name = "mychannel", org = "peerOrg1")
@Chaincode(name = "example_cc_go", type = Type.GO_LANG, version = "11.1", path = "github.com/example_cc")
public interface AccountRepository extends DeployChaincodeRepository<Account> {

	@Chaincode(name = "example_cc_go", version = "11.2")
	@Channel(name = "mychannel", org = "peerOrg1")
	public interface UpgradeRepository extends DeployChaincodeRepository<Account> {
	}
}
```





