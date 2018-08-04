#!/bin/bash

set -euo pipefail
trap "echo 'error: Script failed: see failed command above'" ERR

ORG_HYPERLEDGER_FABRIC_SDKTEST_INTEGRATIONTESTS_CA_TLS=

V11_IDENTITIES_ALLOWREMOVE=--cfg.identities.allowremove
V11_AFFILIATIONS_ALLOWREMOVE=--cfg.affiliations.allowremove

function check_ca0() {
	ls -l /etc/hyperledger/fabric-ca-server-config/ca.org1.example.com-cert.pem
	ls -l /etc/hyperledger/fabric-ca-server-config/d222604fe1793e4a43136e0dfc93f22b8e1324b7058b6dc16644238bbf9e9b01_sk
	ls -l /etc/hyperledger/fabric-ca-server-config/ca.org1.example.com-cert.pem
	ls -l /etc/hyperledger/fabric-ca-server-config/d222604fe1793e4a43136e0dfc93f22b8e1324b7058b6dc16644238bbf9e9b01_sk
	
	fabric-ca-server start -n ca0 ${V11_IDENTITIES_ALLOWREMOVE} ${V11_AFFILIATIONS_ALLOWREMOVE} --registry.maxenrollments -1 --ca.certfile /etc/hyperledger/fabric-ca-server-config/ca.org1.example.com-cert.pem --ca.keyfile /etc/hyperledger/fabric-ca-server-config/d222604fe1793e4a43136e0dfc93f22b8e1324b7058b6dc16644238bbf9e9b01_sk -b admin:adminpw ${ORG_HYPERLEDGER_FABRIC_SDKTEST_INTEGRATIONTESTS_CA_TLS} --tls.certfile /etc/hyperledger/fabric-ca-server-config/ca.org1.example.com-cert.pem --tls.keyfile /etc/hyperledger/fabric-ca-server-config/d222604fe1793e4a43136e0dfc93f22b8e1324b7058b6dc16644238bbf9e9b01_sk -d
}


function check_ca1() {
	ls -l /etc/hyperledger/fabric-ca-server-config/ca.org2.example.com-cert.pem
	ls -l /etc/hyperledger/fabric-ca-server-config/f7b3a09189f5538ad512402d1ee72c96774145fa83b6776a812aefe7ca2107f9_sk
	
	ls -l /etc/hyperledger/fabric-ca-server-config/ca.org2.example.com-cert.pem
	ls -l /etc/hyperledger/fabric-ca-server-config/f7b3a09189f5538ad512402d1ee72c96774145fa83b6776a812aefe7ca2107f9_sk
	
	fabric-ca-server start --registry.maxenrollments -1 --ca.certfile /etc/hyperledger/fabric-ca-server-config/ca.org2.example.com-cert.pem --ca.keyfile /etc/hyperledger/fabric-ca-server-config/f7b3a09189f5538ad512402d1ee72c96774145fa83b6776a812aefe7ca2107f9_sk -b admin:adminpw ${ORG_HYPERLEDGER_FABRIC_SDKTEST_INTEGRATIONTESTS_CA_TLS} --tls.certfile /etc/hyperledger/fabric-ca-server-config/ca.org2.example.com-cert.pem --tls.keyfile /etc/hyperledger/fabric-ca-server-config/f7b3a09189f5538ad512402d1ee72c96774145fa83b6776a812aefe7ca2107f9_sk -d
}

function check_orderer() {
	ls -l /etc/hyperledger/configtx/genesis.block
	ls -l /etc/hyperledger/msp/orderer/msp
	ls -l /etc/hyperledger/msp/orderer/tls/server.key
	ls -l /etc/hyperledger/msp/orderer/tls/server.crt
	ls -l /etc/hyperledger/msp/orderer/tls/ca.crt
	
	ls -l /etc/hyperledger/msp/peerOrg1/msp/tlscacerts/tlsca.org1.example.com-cert.pem /etc/hyperledger/msp/peerOrg2/msp/tlscacerts/tlsca.org2.example.com-cert.pem
	
	orderer
}

function check_peer0_org1() {
	ls -l /etc/hyperledger/msp/peer/msp/tlscacerts/tlsca.org1.example.com-cert.pem
	
	ls -l /etc/hyperledger/msp/peer/msp
	ls -l /etc/hyperledger/msp/peer/tls/server.crt
	ls -l /etc/hyperledger/msp/peer/tls/server.key
	ls -l /etc/hyperledger/msp/peer/tls/ca.crt
	
	peer node start
	
	exit 0
}

function check_peer1_org1() {
	ls -l /etc/hyperledger/msp/peer/msp/tlscacerts/tlsca.org1.example.com-cert.pem
	
	ls -l /etc/hyperledger/msp/peer/msp
	ls -l /etc/hyperledger/msp/peer/tls/server.crt
	ls -l /etc/hyperledger/msp/peer/tls/server.key
	ls -l /etc/hyperledger/msp/peer/tls/ca.crt
	
	peer node start
	
	exit 0
}


function check_peer0_org2() {
	ls -l /etc/hyperledger/msp/peer/msp/tlscacerts/tlsca.org2.example.com-cert.pem
	
	ls -l /etc/hyperledger/msp/peer/msp
	ls -l /etc/hyperledger/msp/peer/tls/server.crt
	ls -l /etc/hyperledger/msp/peer/tls/server.key
	ls -l /etc/hyperledger/msp/peer/tls/ca.crt
	
	peer node start
	
	exit 0
}

function check_peer1_org2() {
	ls -l /etc/hyperledger/msp/peer/msp/tlscacerts/tlsca.org2.example.com-cert.pem
	
	ls -l /etc/hyperledger/msp/peer/msp
	ls -l /etc/hyperledger/msp/peer/tls/server.crt
	ls -l /etc/hyperledger/msp/peer/tls/server.key
	ls -l /etc/hyperledger/msp/peer/tls/ca.crt
	
	peer node start
	
	exit 0
}

op=$1

function main() {
	echo "first param: $op"
	
	case $op in
	    ca0)  
	    	echo '-------check_ca0---------'
	    	check_ca0
	    ;;
	    ca1)  
	    	echo '-------check_ca1---------'
	    	check_ca1
	    ;;
	    orderer)  
	    	echo '-------check_orderer---------'
	    	check_orderer
	    ;;
	    4)  
	    	echo '-------check_peer0_org1---------'
	    	check_peer0_org1
	    ;;
	    5)  
	    	echo '-------check_peer1_org1---------'
	    	check_peer1_org1
	    ;;
	    6)  
	    	echo '-------check_peer0_org2---------'
	    	check_peer0_org2
	    ;;
	    7)  
	    	echo '-------check_peer1_org2---------'
	    	check_peer1_org2
	    ;;
	esac
}

main