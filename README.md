# Spring data `fabric chaincode` Framework
Quickly develop Chancode client applications based on SpringData and Hyperledger Fabric Chaincode SDK.

# 运行环境
+ `fabric-sdk-java` v1.1+
+ `spring data` 2.1.0+
+ `jdk` 8+

**extra jar**
+ `fabric-sdk-commons`  v1.1+
+ `spring-data-fabric-chaincode`  v1.1+

# 添加依赖
在 `pom.xml` 中添加如下配置
```xml
<dependency>
	<groupId>spring.data.fabric.chaincode</groupId>
	<artifactId>spring-data-fabric-chaincode</artifactId>
	<version>1.0.2</version>
</dependency>
```		

# 基本配置
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

# 创建`Configuration`
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

# 构建实体对象
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

# 编写`Repository`完成`CRUD`操作
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

# 编写`Service`完成业务部分
在`Service`中注入`Repository`后就可以使用常规的`API`接口完成`CRUD`操作。通过使用 `Repository` 中的`API` 完成`Chaincode`的业务接入部分，大部分的业务都在` Chaincode` 中完成，在这个 `Service` 中可以完成简单的参数校验或数据转换的业务操作。

+ `install` 完成 `Chaincode` 的安装
+ `instantiate` 完成 `Chaincode` 的实例化
+ `upgrade` 完成 `Chaincode` 升级 
+ `invoke` 完成 `Chaincode` 调用操作（修改数据）
+ `query` 完成 `Chaincode` 数据查询

```java
@Service
public class AccountService {

	private final static Logger log = LoggerFactory.getLogger(AccountService.class);
	
	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private UpgradeRepository upgradeRepo;

	public void install() throws Exception {
		if (accountRepo.getChaincodeDeployOperations().checkInstallChaincode(accountRepo.getCriteria().getChaincodeID())) {
			return;
		}
		
		InstallProposal proposal = ProposalBuilder.install();
		proposal.clientUser("admin");
		
		accountRepo.install(proposal, Paths.get(accountRepo.getConfig().getCommonRootPath(), "chaincode/go/sample_11").toFile());
	}
	
	public ResultSet instantiate(Account account) throws Exception {
		if (accountRepo.getChaincodeDeployOperations().checkInstantiatedChaincode(accountRepo.getCriteria().getChaincodeID())) {
			return null;
		}
		if (accountRepo.getChaincodeDeployOperations().checkInstantiatedChaincode(upgradeRepo.getCriteria().getChaincodeID())) {
			return null;
		}
		
		InstantiateProposal proposal = ProposalBuilder.instantiate();
		proposal.clientUser("admin");
		proposal.endorsementPolicyFile(Paths.get(accountRepo.getConfig().getCommonRootPath(), "chaincode-endorsement-policy.yaml").toFile());
		
		return accountRepo.instantiate(proposal, "init", "a", account.getaAmount(), "b", account.getbAmount());
	}
	
	public ResultSet upgrade() throws Exception {
		ChaincodeDeployOperations operations = accountRepo.getChaincodeDeployOperations();
		
		ChaincodeID cc11_1 = accountRepo.getCriteria().getChaincodeID();
		
		if (!operations.checkChaincode(cc11_1, accountRepo.getOrganization())) {
			log.error(cc11_1 + " chaincode not install or instantiate!");
			return null;
		} else {
			log.info(cc11_1 + " chaincode already install & instantiate!");
		}
		
		InstallProposal install = ProposalBuilder.install().upgradeVersion("11.2");
		install.clientUser("admin");
		
		ChaincodeID cc11_2 = ChaincodeID.newBuilder().setName(cc11_1.getName())
				.setPath(cc11_1.getPath())
				.setVersion(install.getUpgradeVersion())
				.build();
		
		if (!operations.checkInstallChaincode(cc11_2)) {
			accountRepo.install(install, accountRepo.getConfig().getChaincodeRootPath());
		} else {
			log.info(cc11_2 + " chaincode already install!");
		}
		
		UpgradeProposal proposal = ProposalBuilder.upgrade();
		proposal.clientUser("admin");
		proposal.endorsementPolicyFile(Paths.get(accountRepo.getConfig().getCommonRootPath(), "chaincode-endorsement-policy.yaml").toFile());
		
		if (operations.checkInstantiatedChaincode(cc11_2)) {
			log.info(cc11_2 + " chaincode already instantiate!");
			return null;
		} 
		
		ResultSet rs =  upgradeRepo.upgrade(proposal, "func");
		
		if (!operations.checkChaincode(cc11_2, accountRepo.getOrganization())) {
			log.error(cc11_2 + " chaincode not install or instantiate!");
			return null;
		}
		
		return rs;
	}
	
	public ResultSet invoke(String from, String to, int amount) {
		InvokeProposal proposal = ProposalBuilder.invoke();
		proposal.clientUser("user1");
		
		return accountRepo.invoke(proposal, "move", from, to, amount);
	}
	
	public ResultSet query(String account) {
		QueryProposal proposal = ProposalBuilder.query();
		proposal.clientUser("user1");
		
		return accountRepo.queryFor(proposal, "query", account);
	}
}
```

# 调用与测试
编写调用`Service`的`Application`类，完成基本调用。通过 `AccountConfiguration` 创建  `AnnotationConfigApplicationContext` 上下文环境，就可以在IOC容器中获取 `Service` 和 `Repository` 完成后续的` Chaincode` 操作 

```java
AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AccountConfiguration.class);

public static void runTransferExample(AnnotationConfigApplicationContext context) throws Exception {
	TransferService service = context.getBean(TransferService.class);
	
	// install chaincode
	service.install();
	
	Account account = new Account();
	account.setaAmount(1000);
	account.setbAmount(800);
	account.setDate(new Date());
	account.setTimestamp(System.currentTimeMillis());
	account.setRequestId("xxx" + System.currentTimeMillis());
	
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
```

# 使用 `Annotation` 注解优雅的编写 `Repository`
前面使用的是底层封装常规的` API` 接口完成 `Chaincode` 的调用操作，现在可以使用`Annotation` 完成 `Chaincode` 的调用操作。Annotation 都可以设置 `func` 和 `args` 设置调用智能合约的方法名称和参数签名。

+ `@Install` 安装  `Chaincode`，必要参数 `clientUser` 安装的用户，需要` peerAdmin` 角色权限用户、`chaincodeLocation` 合约的位置。
+ `@Instantiate` 实例化  `Chaincode`，必要参数 `endorsementPolicyFile` 背书文件，设置背书策略
+ `@Query` 查询  `Chaincode`，查询交易动作
+ `@Invoke` 调用修改  `Chaincode`，所有交易的动作
+ `@Upgrade` 升级  `Chaincode`，升级智能合约

+ `@Serialization` 可以完成入参或返回数据的JSON序列化或反序列，`Chaincode`接受 `json` 参数 或返回`json`参数适用。

```java
@Chaincode(channel = "mychannel", org = "peerOrg1", name = "example_cc_go", type = Type.GO_LANG, version = "11.1", path = "github.com/example_cc")
@Repository("transferRepository")
public interface TransferRepository extends ChaincodeRepository<Account> {

	@Install(clientUser = "admin", chaincodeLocation = "chaincode/go/sample_11")
	public void install();
	
	@Instantiate(clientUser = "admin", endorsementPolicyFile = "chaincode-endorsement-policy.yaml", func = "init", args = { "a", "?0", "b", "?1" })
	TransactionEvent instantiate(int aAmount, int bAmount);
	
	@Install(clientUser = "admin", chaincodeLocation = "chaincode/go/sample_11", version = "v11.2")
	void installNewVersion();
	
	@Query(clientUser = "user1")
	int query(String account);
	
	@Invoke(clientUser = "user1")
	ResultSet move(String from, String to, int amount);
	
	@Invoke(clientUser = "user1", args = { "a", "b", ":#{#account.aAmount}"})
	ResultSet move(@Param("account") Account account);
	
	@Invoke(clientUser = "user1", func = "move", args = { "a", "b", ":#{#account.aAmount}"})
	Account moveFor(@Param("account") Account account);
	
	@Channel(name = "mychannel", org = "peerOrg1")
	@Chaincode(name = "example_cc_go", version = "v11.2")
	@Repository("newTransferRepository")
	public interface NewTransferRepository extends TransferRepository {

		@Upgrade(clientUser = "admin", endorsementPolicyFile = "chaincode-endorsement-policy.yaml", func = "init")
		TransactionEvent upgrade();
	}
}
```


# 使用 `@Proposal` 编写 `Repository`
`@Proposal` 是所有交易类 annotation 的父类，通过使用 `@Proposal` 也能完成` Chaincode` 的基本业务操作。

+ `@Proposal` 注解需要设置具体类型 `type = ProposalType.*`，`ProposalType` 是一个枚举类型，`waitTime` 可以设置当前请求提议等待时间。
	+ `ProposalType.INSTALL` 安装  `Chaincode`
	+ `ProposalType.INSTANTIATE` 实例化  `Chaincode`
	+ `ProposalType.QUERY` 查询 `Chaincode`
	+ `ProposalType.INVOKE` 交易 `Chaincode`
	+ `ProposalType.UPGRADE` 升级  `Chaincode`
	
+ `@Transaction` 可以设置交易人和交易等待时间

```java
@Chaincode(channel = "mychannel", org = "peerOrg1", name = "example_cc_go", type = Type.GO_LANG, version = "v11.2", path = "github.com/example_cc")
public interface ProposalTransferRepository extends ChaincodeRepository<Account> {

	@Proposal(type = ProposalType.INSTALL, clientUser = "admin", waitTime = 2000L)
	void install();
	
	@Proposal(type = ProposalType.INSTANTIATE, clientUser = "admin", waitTime = 5000L, func = "init", args = { "a", "?0", "b", "?1" })
	ResultSet instantiate(int aAmount, int bAmount);
	
	@Proposal(type = ProposalType.INSTALL, clientUser = "admin", waitTime = 2000L)
	@Install(chaincodeLocation = "chaincode/go/sample_11", version = "v11.3")	// setter new version & chaincode location
	void installNewVersion();
	
	
	@Proposal(type = ProposalType.QUERY, clientUser = "user1", func = "query", args = "b")
	String queryProposal();
	
	@Proposal(type = ProposalType.QUERY, clientUser = "user1", args = "b")
	String query();
	
	@Proposal(type = ProposalType.INVOKE, clientUser = "user1", func = "move", args = { "a", "b", "?0" })
	String invokeProposal(int amount);

	@Proposal(type = ProposalType.INVOKE, clientUser = "user1", args = { "a", "b", "?0" })
	@Transaction(user = "user1", waitTime = 10000)
	String move(int amount);
}
```
 


