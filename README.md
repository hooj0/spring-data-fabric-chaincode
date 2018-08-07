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
	<version>1.0.1</version>
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

