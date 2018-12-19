#!/bin/bash +x
#
# Copyright IBM Corp. All Rights Reserved.
#
# SPDX-License-Identifier: Apache-2.0
#

#set -e
#set -uo pipefail
trap "echo 'error: Script failed: see failed command above'" ERR

CHANNEL_NAME=$1
CHANNEL_ARTIFACTS_ROOT=$2

CRYPTO_CONFIG_OUTPUT_ROOT=$3
CRYPTO_CONFIG_FILE=$4

: ${CHANNEL_NAME:="mychannel"}
: ${CHANNEL_ARTIFACTS_ROOT:="channel-artifacts"}
: ${CRYPTO_CONFIG_OUTPUT_ROOT:="crypto-config"}
: ${CRYPTO_CONFIG_FILE:="crypto-config.yaml"}

echo "CHANNEL_NAME: $CHANNEL_NAME"
echo "CHANNEL_ARTIFACTS_ROOT: $CHANNEL_ARTIFACTS_ROOT"
echo "CRYPTO_CONFIG_OUTPUT_ROOT: $CRYPTO_CONFIG_OUTPUT_ROOT"
echo "CRYPTO_CONFIG_FILE: $CRYPTO_CONFIG_FILE"

#version="_v11"
version="_v13"
VERSION_DIR="v1.3"

FABRIC_ROOT="/opt/gopath/src/github.com/hyperledger/fabric"
export FABRIC_ROOT=$FABRIC_ROOT
export FABRIC_CFG_PATH=$PWD

echo "FABRIC_ROOT: ${FABRIC_ROOT}"
echo "FABRIC_CFG_PATH: ${FABRIC_CFG_PATH}"
echo

OS_ARCH=$(echo "$(uname -s|tr '[:upper:]' '[:lower:]'|sed 's/mingw64_nt.*/windows/')-$(uname -m | sed 's/x86_64/amd64/g')" | awk '{print tolower($0)}')

## Using docker-compose template replace private key file names with constants
function replacePrivateKey () {
	echo
	echo "##########################################################"
	echo "#####         replace certificates  key          #########"
	echo "##########################################################"
	
	ARCH=`uname -s | grep Darwin`
	echo "ARCH: $ARCH"
	if [ "$ARCH" == "Darwin" ]; then
		OPTS="-it"
	else
		OPTS="-i"
	fi
	echo "OPTS: $OPTS"

    echo
    echo "==> cp -r script/check-files.tpl.sh script/$VERSION_DIR/check-files.sh"
    mkdir -pv script/$VERSION_DIR/
	cp -rv script/check-files.tpl.sh script/$VERSION_DIR/check-files.sh

    CURRENT_DIR=$PWD
    cd ./$CRYPTO_CONFIG_OUTPUT_ROOT/peerOrganizations/org1.example.com/ca/
    PRIV_KEY=$(ls *_sk)
    cd $CURRENT_DIR
	
    sed $OPTS "s/CA1_PRIVATE_KEY/${PRIV_KEY}/g" script/$VERSION_DIR/check-files.sh 

    cd ./$CRYPTO_CONFIG_OUTPUT_ROOT/peerOrganizations/org2.example.com/ca/
    PRIV_KEY=$(ls *_sk)
    cd $CURRENT_DIR

    sed $OPTS "s/CA2_PRIVATE_KEY/${PRIV_KEY}/g" script/$VERSION_DIR/check-files.sh 
    echo
}

## Generates Org certs using cryptogen tool
function generateCerts (){
	CRYPTOGEN=$FABRIC_ROOT/release/$OS_ARCH/bin/cryptogen

	if [ -f "$CRYPTOGEN" ]; then
        echo "Using cryptogen -> $CRYPTOGEN"
	else
	    echo "Building cryptogen"
	    echo "===> make -C $FABRIC_ROOT release"
	    make -C $FABRIC_ROOT release
	fi

	echo
	echo "##########################################################"
	echo "##### Generate certificates using cryptogen tool #########"
	echo "##########################################################"

	echo "==> cryptogen generate --config=./$CRYPTO_CONFIG_FILE --output=./$CRYPTO_CONFIG_OUTPUT_ROOT"
	$CRYPTOGEN generate --config=./$CRYPTO_CONFIG_FILE --output=./$CRYPTO_CONFIG_OUTPUT_ROOT
	echo
}

## Generate orderer genesis block , channel configuration transaction and anchor peer update transactions
function generateChannelArtifacts() {

	CONFIGTXGEN=$FABRIC_ROOT/release/$OS_ARCH/bin/configtxgen
	if [ -f "$CONFIGTXGEN" ]; then
        echo "Using configtxgen -> $CONFIGTXGEN"
	else
	    echo "Building configtxgen"
	    echo "===> make -C $FABRIC_ROOT release"
	    make -C $FABRIC_ROOT release
	fi

    echo
	echo "##########################################################"
	echo "#########  Generating Orderer Genesis block ##############"
	echo "##########################################################"
	# Note: For some unknown reason (at least for now) the block file can't be
	# named orderer.genesis.block or the orderer will fail to launch!
	echo "==> cryptogen -profile TwoOrgsOrdererGenesis${version} -outputBlock ./$CHANNEL_ARTIFACTS_ROOT/genesis.block"
	$CONFIGTXGEN -profile TwoOrgsOrdererGenesis${version} -outputBlock ./$CHANNEL_ARTIFACTS_ROOT/genesis.block
	

	echo
	echo "#################################################################"
	echo "### Generating channel configuration transaction 'channel.tx' ###"
	echo "#################################################################"
	echo "==> cryptogen -profile TwoOrgsChannel${version} -outputCreateChannelTx ./$CHANNEL_ARTIFACTS_ROOT/$CHANNEL_NAME.tx -channelID $CHANNEL_NAME"
	$CONFIGTXGEN -profile TwoOrgsChannel${version} -outputCreateChannelTx ./$CHANNEL_ARTIFACTS_ROOT/$CHANNEL_NAME.tx -channelID $CHANNEL_NAME

	echo
	echo "#################################################################"
	echo "### Generating channel configuration transaction 'channel.tx' ###"
	echo "#################################################################"
	echo "==> cryptogen -profile TwoOrgsChannel${version} -outputCreateChannelTx ./$CHANNEL_ARTIFACTS_ROOT/${CHANNEL_NAME}2.tx -channelID ${CHANNEL_NAME}2"
	$CONFIGTXGEN -profile TwoOrgsChannel${version} -outputCreateChannelTx ./$CHANNEL_ARTIFACTS_ROOT/${CHANNEL_NAME}2.tx -channelID ${CHANNEL_NAME}2
}

function skip() {
	echo
	echo "#################################################################"
	echo "#######    Generating anchor peer update for Org1MSP   ##########"
	echo "#################################################################"
	echo "==> cryptogen -profile TwoOrgsChannel -outputAnchorPeersUpdate ./$CHANNEL_ARTIFACTS_ROOT/Org1MSPanchors.tx -channelID $CHANNEL_NAME -asOrg Org1MSP"
	$CONFIGTXGEN -profile TwoOrgsChannel -outputAnchorPeersUpdate ./$CHANNEL_ARTIFACTS_ROOT/Org1MSPanchors.tx -channelID $CHANNEL_NAME -asOrg Org1MSP

	echo
	echo "#################################################################"
	echo "#######    Generating anchor peer update for Org2MSP   ##########"
	echo "#################################################################"
	echo "==> cryptogen -profile TwoOrgsChannel -outputAnchorPeersUpdate ./$CHANNEL_ARTIFACTS_ROOT/Org2MSPanchors.tx -channelID $CHANNEL_NAME -asOrg Org2MSP"
	$CONFIGTXGEN -profile TwoOrgsChannel -outputAnchorPeersUpdate ./$CHANNEL_ARTIFACTS_ROOT/Org2MSPanchors.tx -channelID $CHANNEL_NAME -asOrg Org2MSP
	echo
}

function cleanChannelArtifacts() {

    echo
	echo "#################################################################"
	echo "#######            clean channel artifacts             ##########"
	echo "#################################################################"

	
	echo "==> rm -rf ./$VERSION_DIR/"
    [ -n $VERSION_DIR ] && [ -d "./$VERSION_DIR" ] && rm -rf ./$VERSION_DIR/*

	echo "==> rm -rf ./channel-artifacts/* ./crypto-config/* ./script/$VERSION_DIR"
    rm -rfv ./channel-artifacts/* ./crypto-config/* ./script/$VERSION_DIR
    
    echo
}

function createChannelArtifactsDir() {

    echo
	echo "#################################################################"
	echo "#######            create channel artifacts            ##########"
	echo "#################################################################"

    echo "==> mkdir ./channel-artifacts"
	[ ! -d "./channel-artifacts" ] && mkdir ./channel-artifacts 

    echo "==> mkdir ./crypto-config"
	[ ! -d "./crypto-config" ] && mkdir ./crypto-config 
    
    echo
}

function mergeArtifactsCryptoDir() {
	echo
	echo "#################################################################"
	echo "#######            merge channel artifacts             ##########"
	echo "#################################################################"

	echo "==> mv ./channel-artifacts ./$VERSION_DIR/"
    mv -v ./channel-artifacts ./$VERSION_DIR/

	echo "==> mv ./crypto-config ./$VERSION_DIR/"
    mv -v ./crypto-config ./$VERSION_DIR/

    echo
}

echo  "shell run example: $ ./generate.sh tx_channel channel_out crypto_out crypto_config_extend.yaml"

cleanChannelArtifacts
createChannelArtifactsDir
generateCerts
replacePrivateKey
generateChannelArtifacts
#skip
mergeArtifactsCryptoDir

