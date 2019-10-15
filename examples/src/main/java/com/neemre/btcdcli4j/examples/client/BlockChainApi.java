package com.neemre.btcdcli4j.examples.client;

import java.util.Properties;

import com.neemre.btcdcli4j.core.domain.BlockChainInfo;
import org.apache.http.impl.client.CloseableHttpClient;

import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.domain.enums.CheckLevels;
import com.neemre.btcdcli4j.examples.util.ResourceUtils;

/**A list of examples demonstrating the use of <i>bitcoind</i>'s block chain RPCs (via the JSON-RPC 
 * API).*/
public class BlockChainApi {

	public static void main(String[] args) throws Exception {
		CloseableHttpClient httpProvider = ResourceUtils.getHttpProvider();
		Properties nodeConfig = ResourceUtils.getNodeConfig();
		BtcdClient client = new VerboseBtcdClientImpl(httpProvider, nodeConfig);

		String blockID = client.getBestBlockHash();
		client.getBlock(blockID);
		client.getBlock(blockID, true);
		BlockChainInfo info = client.getBlockChainInfo();
		client.getBlockCount();
		client.getBlockHash(345168);
		client.getChainTips();
		client.getDifficulty();
		client.getMemPoolInfo();
		client.getRawMemPool();
		client.getRawMemPool(true);
		client.getTxOutSetInfo();
		client.verifyChain();
		client.verifyChain(CheckLevels.LEVEL_4.getIdentifier());
		client.verifyChain(CheckLevels.LEVEL_4.getIdentifier(), 1000);
	}
}