package com.neemre.btcdcli4j.client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.client.HttpClient;

import com.neemre.btcdcli4j.Commands;
import com.neemre.btcdcli4j.common.DataFormats;
import com.neemre.btcdcli4j.common.Defaults;
import com.neemre.btcdcli4j.domain.Account;
import com.neemre.btcdcli4j.domain.Address;
import com.neemre.btcdcli4j.domain.AddressOverview;
import com.neemre.btcdcli4j.domain.AddressInfo;
import com.neemre.btcdcli4j.domain.Block;
import com.neemre.btcdcli4j.domain.MultiSigAddress;
import com.neemre.btcdcli4j.domain.RawTransaction;
import com.neemre.btcdcli4j.domain.RedeemScript;
import com.neemre.btcdcli4j.domain.SignatureResult;
import com.neemre.btcdcli4j.domain.SinceBlock;
import com.neemre.btcdcli4j.domain.Transaction;
import com.neemre.btcdcli4j.domain.Info;
import com.neemre.btcdcli4j.domain.MemPoolInfo;
import com.neemre.btcdcli4j.domain.MiningInfo;
import com.neemre.btcdcli4j.domain.OutputOverview;
import com.neemre.btcdcli4j.domain.PeerNode;
import com.neemre.btcdcli4j.domain.Payment;
import com.neemre.btcdcli4j.domain.Output;
import com.neemre.btcdcli4j.domain.WalletInfo;
import com.neemre.btcdcli4j.jsonrpc.client.JsonRpcClient;
import com.neemre.btcdcli4j.jsonrpc.client.JsonRpcClientImpl;
import com.neemre.btcdcli4j.util.CollectionUtils;
import com.neemre.btcdcli4j.util.NumberUtils;

public class BtcdClientImpl implements BtcdClient {

	private JsonRpcClient rpcClient;
	
	
	public BtcdClientImpl(HttpClient httpProvider, Properties nodeConfig) {
		rpcClient = new JsonRpcClientImpl(httpProvider, nodeConfig);
	}
	
	@Override
	public String addMultiSigAddress(Integer minSignatures, List<String> addresses) {
		List<Object> params = CollectionUtils.asList(minSignatures, addresses);
		String multiSigAddressJson = rpcClient.execute(Commands.ADD_MULTI_SIG_ADDRESS.getName(), 
				params);
		String multiSigAddress = rpcClient.getParser().parseString(multiSigAddressJson);
		return multiSigAddress;		
	}

	@Override
	public String addMultiSigAddress(Integer minSignatures, List<String> addresses, 
			String account) {
		List<Object> params = CollectionUtils.asList(minSignatures, addresses, account);
		String multiSigAddressJson = rpcClient.execute(Commands.ADD_MULTI_SIG_ADDRESS.getName(),
				params);
		String multiSigAddress = rpcClient.getParser().parseString(multiSigAddressJson);
		return multiSigAddress;
	}
	
	@Override
	public void backupWallet(String filePath) {
		rpcClient.execute(Commands.BACKUP_WALLET.getName(), filePath);
	}
	
	@Override
	public MultiSigAddress createMultiSig(Integer minSignatures, List<String> addresses) {
		List<Object> params = CollectionUtils.asList(minSignatures, addresses);
		String multiSigAddressJson = rpcClient.execute(Commands.CREATE_MULTI_SIG.getName(), params);
		MultiSigAddress multiSigAddress = rpcClient.getMapper().mapToEntity(multiSigAddressJson, 
				MultiSigAddress.class);
		return multiSigAddress;
	}

	@Override
	public String createRawTransaction(List<OutputOverview> outputs, Map<String, BigDecimal> toAddresses) {
		List<Object> params = CollectionUtils.asList(outputs, toAddresses);
		String hexTransactionJson = rpcClient.execute(Commands.CREATE_RAW_TRANSACTION.getName(), 
				params);
		String hexTransaction = rpcClient.getParser().parseString(hexTransactionJson);
		return hexTransaction;
	}

	@Override
	public RawTransaction decodeRawTransaction(String hexTransaction) {
		String rawTransactionJson = rpcClient.execute(Commands.DECODE_RAW_TRANSACTION.getName(), 
				hexTransaction);
		RawTransaction rawTransaction = rpcClient.getMapper().mapToEntity(rawTransactionJson, 
				RawTransaction.class);
		return rawTransaction;
	}
	
	@Override
	public RedeemScript decodeScript(String hexRedeemScript) {
		String redeemScriptJson = rpcClient.execute(Commands.DECODE_SCRIPT.getName(), 
				hexRedeemScript);
		RedeemScript redeemScript = rpcClient.getMapper().mapToEntity(redeemScriptJson, 
				RedeemScript.class);
		return redeemScript;
	}
	
	@Override
	public String dumpPrivKey(String address) {
		String privateKeyJson = rpcClient.execute(Commands.DUMP_PRIV_KEY.getName(), address);
		String privateKey = rpcClient.getParser().parseString(privateKeyJson);
		return privateKey;
	}
	
	@Override
	public void dumpWallet(String filePath) {
		rpcClient.execute(Commands.DUMP_WALLET.getName(), filePath);
	}
	
	@Override
	public String encryptWallet(String passphrase) {
		String noticeMsgJson = rpcClient.execute(Commands.ENCRYPT_WALLET.getName(), passphrase);
		String noticeMsg = rpcClient.getParser().parseString(noticeMsgJson);
		return noticeMsg;
	}
	
	@Override
	public String getAccount(String address) {
		String accountJson = rpcClient.execute(Commands.GET_ACCOUNT.getName(), address);
		String account = rpcClient.getParser().parseString(accountJson);
		return account;
	}
	
	@Override
	public String getAccountAddress(String account) {
		String addressJson = rpcClient.execute(Commands.GET_ACCOUNT_ADDRESS.getName(), account);
		String address = rpcClient.getParser().parseString(addressJson);
		return address;
	}
	
	@Override
	public List<String> getAddressesByAccount(String account) {
		String addressesJson = rpcClient.execute(Commands.GET_ADDRESSES_BY_ACCOUNT.getName(), 
				account);
		List<String> addresses = rpcClient.getMapper().mapToList(addressesJson, String.class);
		return addresses;
	}

	@Override
	public BigDecimal getBalance() {
		String balanceJson = rpcClient.execute(Commands.GET_BALANCE.getName());
		BigDecimal balance = rpcClient.getParser().parseBigDecimal(balanceJson);
		return balance;
	}
	
	@Override
	public BigDecimal getBalance(String account) {
		String balanceJson = rpcClient.execute(Commands.GET_BALANCE.getName(), account);
		BigDecimal balance = rpcClient.getParser().parseBigDecimal(balanceJson);
		return balance;
	}
	
	@Override
	public BigDecimal getBalance(String account, Integer confirmations) {
		List<Object> params = CollectionUtils.asList(account, confirmations);
		String balanceJson = rpcClient.execute(Commands.GET_BALANCE.getName(), params);
		BigDecimal balance = rpcClient.getParser().parseBigDecimal(balanceJson);
		return balance;
	}

	@Override
	public BigDecimal getBalance(String account, Integer confirmations, Boolean withWatchOnly) {
		List<Object> params = CollectionUtils.asList(account, confirmations, withWatchOnly);
		String balanceJson = rpcClient.execute(Commands.GET_BALANCE.getName(), params);
		BigDecimal balance = rpcClient.getParser().parseBigDecimal(balanceJson);
		return balance;
	}
	
	@Override
	public String getBestBlockHash() {
		String headerHashJson = rpcClient.execute(Commands.GET_BEST_BLOCK_HASH.getName());
		String headerHash = rpcClient.getParser().parseString(headerHashJson);
		return headerHash;
	}
	
	@Override
	public Block getBlock(String headerHash) {
		String blockJson = rpcClient.execute(Commands.GET_BLOCK.getName(), headerHash);
		Block block = rpcClient.getMapper().mapToEntity(blockJson, Block.class);
		return block;
	}

	@Override
	public Object getBlock(String headerHash, Boolean isDecoded) {
		List<Object> params = CollectionUtils.asList(headerHash, isDecoded);
		String blockJson = rpcClient.execute(Commands.GET_BLOCK.getName(), params);
		if(isDecoded) {
			Block block = rpcClient.getMapper().mapToEntity(blockJson, Block.class);
			return block;
		} else {
			String block = rpcClient.getParser().parseString(blockJson);
			return block;
		}
	}
	
	@Override
	public Integer getBlockCount() {
		String blockHeightJson = rpcClient.execute(Commands.GET_BLOCK_COUNT.getName());
		Integer blockHeight = rpcClient.getParser().parseInteger(blockHeightJson);
		return blockHeight;
	}

	@Override
	public String getBlockHash(Integer blockHeight) {
		String headerHashJson = rpcClient.execute(Commands.GET_BLOCK_HASH.getName(), blockHeight);
		String headerHash = rpcClient.getParser().parseString(headerHashJson);
		return headerHash;
	}
	
	@Override
	public BigDecimal getDifficulty() {
		String difficultyJson = rpcClient.execute(Commands.GET_DIFFICULTY.getName());
		BigDecimal difficulty = rpcClient.getParser().parseBigDecimal(difficultyJson);
		return difficulty;
	}
	
	@Override
	public Boolean getGenerate() {
		String isGenerateJson = rpcClient.execute(Commands.GET_GENERATE.getName());
		Boolean isGenerate = rpcClient.getParser().parseBoolean(isGenerateJson);
		return isGenerate;
	}
	
	@Override
	public Long getHashesPerSec() {
		String hashesPerSecJson = rpcClient.execute(Commands.GET_HASHES_PER_SEC.getName());
		Long hashesPerSec = rpcClient.getParser().parseLong(hashesPerSecJson);
		return hashesPerSec;
	}
	
	@Override
	public Info getInfo() {
		String infoJson = rpcClient.execute(Commands.GET_INFO.getName());
		Info info = rpcClient.getMapper().mapToEntity(infoJson, Info.class);
		return info;
	}
	
	@Override
	public MemPoolInfo getMemPoolInfo() {
		String memPoolInfoJson = rpcClient.execute(Commands.GET_MEM_POOL_INFO.getName());
		MemPoolInfo memPoolInfo = rpcClient.getMapper().mapToEntity(memPoolInfoJson, 
				MemPoolInfo.class);
		return memPoolInfo;
	}

	@Override
	public MiningInfo getMiningInfo() {
		String miningInfoJson = rpcClient.execute(Commands.GET_MINING_INFO.getName());
		MiningInfo miningInfo = rpcClient.getMapper().mapToEntity(miningInfoJson, MiningInfo.class);
		return miningInfo;
	}
	
	@Override
	public String getNewAddress() {
		String addressJson = rpcClient.execute(Commands.GET_NEW_ADDRESS.getName());
		String address = rpcClient.getParser().parseString(addressJson);
		return address;
	}

	@Override
	public String getNewAddress(String account) {
		String addressJson = rpcClient.execute(Commands.GET_NEW_ADDRESS.getName(), account);
		String address = rpcClient.getParser().parseString(addressJson);
		return address;
	}
	
	@Override
	public List<PeerNode> getPeerInfo() {
		String peerInfoJson = rpcClient.execute(Commands.GET_PEER_INFO.getName());
		List<PeerNode> peerInfo = rpcClient.getMapper().mapToList(peerInfoJson, PeerNode.class);
		return peerInfo;
	}
	
	@Override
	public String getRawChangeAddress() {
		String addressJson = rpcClient.execute(Commands.GET_RAW_CHANGE_ADDRESS.getName());
		String address = rpcClient.getParser().parseString(addressJson);
		return address;
	}
	
	@Override
	public String getRawTransaction(String txId) {
		String hexTransactionJson = rpcClient.execute(Commands.GET_RAW_TRANSACTION.getName(), txId);
		String hexTransaction = rpcClient.getParser().parseString(hexTransactionJson);
		return hexTransaction;
	}

	@Override
	public Object getRawTransaction(String txId, Integer verbosity) {
		List<Object> params = CollectionUtils.asList(txId, verbosity);
		String transactionJson = rpcClient.execute(Commands.GET_RAW_TRANSACTION.getName(), params);
		if(verbosity == DataFormats.HEX.getCode()) {
			String hexTransaction = rpcClient.getParser().parseString(transactionJson);
			return hexTransaction;
		} else {
			RawTransaction rawTransaction = rpcClient.getMapper().mapToEntity(transactionJson, 
					RawTransaction.class);
			return rawTransaction;
		}
	}
	
	@Override
	public BigDecimal getReceivedByAccount(String account) {
		String totalReceivedJson = rpcClient.execute(Commands.GET_RECEIVED_BY_ACCOUNT.getName(),
				account);
		BigDecimal totalReceived = rpcClient.getParser().parseBigDecimal(totalReceivedJson);
		return totalReceived;
	}

	@Override
	public BigDecimal getReceivedByAccount(String account, Integer confirmations) {
		List<Object> params = CollectionUtils.asList(account, confirmations);
		String totalReceivedJson = rpcClient.execute(Commands.GET_RECEIVED_BY_ACCOUNT.getName(), 
				params);
		BigDecimal totalReceived = rpcClient.getParser().parseBigDecimal(totalReceivedJson);
		return totalReceived;
	}
	
	@Override
	public BigDecimal getReceivedByAddress(String address) {
		String totalReceivedJson = rpcClient.execute(Commands.GET_RECEIVED_BY_ADDRESS.getName(),
				address);
		BigDecimal totalReceived = rpcClient.getParser().parseBigDecimal(totalReceivedJson);
		return totalReceived;
	}

	@Override
	public BigDecimal getReceivedByAddress(String address, Integer confirmations) {
		List<Object> params = CollectionUtils.asList(address, confirmations);
		String totalReceivedJson = rpcClient.execute(Commands.GET_RECEIVED_BY_ADDRESS.getName(),
				params);
		BigDecimal totalReceived = rpcClient.getParser().parseBigDecimal(totalReceivedJson);
		return totalReceived;
	}
	
	@Override
	public Transaction getTransaction(String txId) {
		String transactionJson = rpcClient.execute(Commands.GET_TRANSACTION.getName(), txId);
		Transaction transaction = rpcClient.getMapper().mapToEntity(transactionJson,
				Transaction.class);
		return transaction;
	}

	@Override
	public Transaction getTransaction(String txId, Boolean withWatchOnly) {
		List<Object> params = CollectionUtils.asList(txId, withWatchOnly);
		String transactionJson = rpcClient.execute(Commands.GET_TRANSACTION.getName(), params);
		Transaction transaction = rpcClient.getMapper().mapToEntity(transactionJson,
				Transaction.class);
		return transaction;
	}
	
	@Override
	public BigDecimal getUnconfirmedBalance() {
		String unconfirmedBalanceJson = rpcClient.execute(Commands.GET_UNCONFIRMED_BALANCE.getName());
		BigDecimal unconfirmedBalance = rpcClient.getParser().parseBigDecimal(unconfirmedBalanceJson);
		return unconfirmedBalance;
	}	

	@Override
	public WalletInfo getWalletInfo() {
		String walletInfoJson = rpcClient.execute(Commands.GET_WALLET_INFO.getName());
		WalletInfo walletInfo = rpcClient.getMapper().mapToEntity(walletInfoJson, WalletInfo.class);
		return walletInfo;
	}
	
	@Override
	public void importAddress(String address) {
		rpcClient.execute(Commands.IMPORT_ADDRESS.getName(), address);
	}

	@Override
	public void importAddress(String address, String account) {
		List<Object> params = CollectionUtils.asList(address, account);
		rpcClient.execute(Commands.IMPORT_ADDRESS.getName(), params);
	}

	@Override
	public void importAddress(String address, String account, Boolean withRescan) {
		List<Object> params = CollectionUtils.asList(address, account, withRescan);
		rpcClient.execute(Commands.IMPORT_ADDRESS.getName(), params);
	}
	
	@Override
	public void importPrivKey(String privateKey) {
		rpcClient.execute(Commands.IMPORT_PRIV_KEY.getName(), privateKey);
	}

	@Override
	public void importPrivKey(String privateKey, String account) {
		List<Object> params = CollectionUtils.asList(privateKey, account);
		rpcClient.execute(Commands.IMPORT_PRIV_KEY.getName(), params);
	}

	@Override
	public void importPrivKey(String privateKey, String account, Boolean withRescan) {
		List<Object> params = CollectionUtils.asList(privateKey, account, withRescan);
		rpcClient.execute(Commands.IMPORT_PRIV_KEY.getName(), params);
	}
	
	@Override
	public void importWallet(String filePath) {
		rpcClient.execute(Commands.IMPORT_WALLET.getName(), filePath);
	}
	
	@Override
	public void keyPoolRefill() {
		rpcClient.execute(Commands.KEY_POOL_REFILL.getName());
	}

	@Override
	public void keyPoolRefill(Integer keypoolSize) {
		rpcClient.execute(Commands.KEY_POOL_REFILL.getName(), keypoolSize);
	}
	
	@Override
	public Map<String, BigDecimal> listAccounts() {
		String accountsJson = rpcClient.execute(Commands.LIST_ACCOUNTS.getName());
		Map<String, BigDecimal> accounts = rpcClient.getMapper().mapToMap(accountsJson, 
				String.class, BigDecimal.class);
		accounts = NumberUtils.setValueScale(accounts, Defaults.DECIMAL_SCALE);
		return accounts;
	}
	
	@Override
	public Map<String, BigDecimal> listAccounts(Integer confirmations) {
		String accountsJson = rpcClient.execute(Commands.LIST_ACCOUNTS.getName(), confirmations);
		Map<String, BigDecimal> accounts = rpcClient.getMapper().mapToMap(accountsJson, 
				String.class, BigDecimal.class);
		accounts = NumberUtils.setValueScale(accounts, Defaults.DECIMAL_SCALE);
		return accounts;
	}

	@Override
	public Map<String, BigDecimal> listAccounts(Integer confirmations, Boolean withWatchOnly) {
		List<Object> params = CollectionUtils.asList(confirmations, withWatchOnly);
		String accountsJson = rpcClient.execute(Commands.LIST_ACCOUNTS.getName(), params);
		Map<String, BigDecimal> accounts = rpcClient.getMapper().mapToMap(accountsJson, 
				String.class, BigDecimal.class);
		accounts = NumberUtils.setValueScale(accounts, Defaults.DECIMAL_SCALE);
		return accounts;
	}
	
	@Override
	public List<List<AddressOverview>> listAddressGroupings() {
		String groupingsJson = rpcClient.execute(Commands.LIST_ADDRESS_GROUPINGS.getName());
		List<List<AddressOverview>> groupings = rpcClient.getMapper().mapToNestedLists(1, 
				groupingsJson, AddressOverview.class);
		return groupings;
	}
	
	@Override
	public List<OutputOverview> listLockUnspent() {
		String lockedOutputsJson = rpcClient.execute(Commands.LIST_LOCK_UNSPENT.getName());
		List<OutputOverview> lockedOutputs = rpcClient.getMapper().mapToList(lockedOutputsJson, 
				OutputOverview.class);
		return lockedOutputs;
	}
	
	@Override
	public List<Account> listReceivedByAccount() {
		String accountsJson = rpcClient.execute(Commands.LIST_RECEIVED_BY_ACCOUNT.getName());
		List<Account> accounts = rpcClient.getMapper().mapToList(accountsJson, Account.class);
		return accounts;
	}

	@Override
	public List<Account> listReceivedByAccount(Integer confirmations) {
		String accountsJson = rpcClient.execute(Commands.LIST_RECEIVED_BY_ACCOUNT.getName(), 
				confirmations);
		List<Account> accounts = rpcClient.getMapper().mapToList(accountsJson, Account.class);
		return accounts;
	}

	@Override
	public List<Account> listReceivedByAccount(Integer confirmations, Boolean withUnused) {
		List<Object> params = CollectionUtils.asList(confirmations, withUnused);
		String accountsJson = rpcClient.execute(Commands.LIST_RECEIVED_BY_ACCOUNT.getName(), 
				params);
		List<Account> accounts = rpcClient.getMapper().mapToList(accountsJson, Account.class);
		return accounts;
	}

	@Override
	public List<Account> listReceivedByAccount(Integer confirmations, Boolean withUnused, 
			Boolean withWatchOnly) {
		List<Object> params = CollectionUtils.asList(confirmations, withUnused, withWatchOnly);
		String accountsJson = rpcClient.execute(Commands.LIST_RECEIVED_BY_ACCOUNT.getName(), 
				params);
		List<Account> accounts = rpcClient.getMapper().mapToList(accountsJson, Account.class);
		return accounts;
	}
	

	@Override
	public List<Address> listReceivedByAddress() {
		String addressesJson = rpcClient.execute(Commands.LIST_RECEIVED_BY_ADDRESS.getName());
		List<Address> addresses = rpcClient.getMapper().mapToList(addressesJson, Address.class);
		return addresses;
	}

	@Override
	public List<Address> listReceivedByAddress(Integer confirmations) {
		String addressesJson = rpcClient.execute(Commands.LIST_RECEIVED_BY_ADDRESS.getName(),
				confirmations);
		List<Address> addresses = rpcClient.getMapper().mapToList(addressesJson, Address.class);
		return addresses;
	}

	@Override
	public List<Address> listReceivedByAddress(Integer confirmations, Boolean withUnused) {
		List<Object> params = CollectionUtils.asList(confirmations, withUnused);
		String addressesJson = rpcClient.execute(Commands.LIST_RECEIVED_BY_ADDRESS.getName(),
				params);
		List<Address> addresses = rpcClient.getMapper().mapToList(addressesJson, Address.class);
		return addresses;
	}

	@Override
	public List<Address> listReceivedByAddress(Integer confirmations, Boolean withUnused, 
			Boolean withWatchOnly) {
		List<Object> params = CollectionUtils.asList(confirmations, withUnused, withWatchOnly);
		String addressesJson = rpcClient.execute(Commands.LIST_RECEIVED_BY_ADDRESS.getName(),
				params);
		List<Address> addresses = rpcClient.getMapper().mapToList(addressesJson,  Address.class);
		return addresses;
	}
	
	@Override
	public SinceBlock listSinceBlock() {
		String sinceBlockJson = rpcClient.execute(Commands.LIST_SINCE_BLOCK.getName());
		SinceBlock sinceBlock = rpcClient.getMapper().mapToEntity(sinceBlockJson, SinceBlock.class);
		return sinceBlock;
	}

	@Override
	public SinceBlock listSinceBlock(String headerHash) {
		String sinceBlockJson = rpcClient.execute(Commands.LIST_SINCE_BLOCK.getName(), headerHash);
		SinceBlock sinceBlock = rpcClient.getMapper().mapToEntity(sinceBlockJson, SinceBlock.class);
		return sinceBlock;
	}

	@Override
	public SinceBlock listSinceBlock(String headerHash, Integer confirmations) {
		List<Object> params = CollectionUtils.asList(headerHash, confirmations);
		String sinceBlockJson = rpcClient.execute(Commands.LIST_SINCE_BLOCK.getName(), params);
		SinceBlock sinceBlock = rpcClient.getMapper().mapToEntity(sinceBlockJson, SinceBlock.class);
		return sinceBlock;
	}

	@Override
	public SinceBlock listSinceBlock(String headerHash, Integer confirmations, 
			Boolean withWatchOnly) {
		List<Object> params = CollectionUtils.asList(headerHash, confirmations, withWatchOnly);
		String sinceBlockJson = rpcClient.execute(Commands.LIST_SINCE_BLOCK.getName(), params);
		SinceBlock sinceBlock = rpcClient.getMapper().mapToEntity(sinceBlockJson, SinceBlock.class);
		return sinceBlock;
	}
	
	@Override
	public List<Payment> listTransactions() {
		String paymentsJson = rpcClient.execute(Commands.LIST_TRANSACTIONS.getName());
		List<Payment> payments = rpcClient.getMapper().mapToList(paymentsJson, Payment.class);
		return payments;
	}

	@Override
	public List<Payment> listTransactions(String account) {
		String paymentsJson = rpcClient.execute(Commands.LIST_TRANSACTIONS.getName(), account);
		List<Payment> payments = rpcClient.getMapper().mapToList(paymentsJson, Payment.class);
		return payments;
	}

	@Override
	public List<Payment> listTransactions(String account, Integer count) {
		List<Object> params = CollectionUtils.asList(account, count);
		String paymentsJson = rpcClient.execute(Commands.LIST_TRANSACTIONS.getName(), params);
		List<Payment> payments = rpcClient.getMapper().mapToList(paymentsJson, Payment.class);
		return payments;
	}

	@Override
	public List<Payment> listTransactions(String account, Integer count, Integer offset) {
		List<Object> params = CollectionUtils.asList(account, count, offset);
		String paymentsJson = rpcClient.execute(Commands.LIST_TRANSACTIONS.getName(), params);
		List<Payment> payments = rpcClient.getMapper().mapToList(paymentsJson, Payment.class);
		return payments;
	}

	@Override
	public List<Payment> listTransactions(String account, Integer count, Integer offset, 
			Boolean withWatchOnly) {
		List<Object> params = CollectionUtils.asList(account, count, offset, withWatchOnly);
		String paymentsJson = rpcClient.execute(Commands.LIST_TRANSACTIONS.getName(), params);
		List<Payment> payments = rpcClient.getMapper().mapToList(paymentsJson, Payment.class);
		return payments;
	}
	
	@Override
	public List<Output> listUnspent() {
		String unspentOutputsJson = rpcClient.execute(Commands.LIST_UNSPENT.getName());
		List<Output> unspentOutputs = rpcClient.getMapper().mapToList(unspentOutputsJson,
				Output.class);
		return unspentOutputs;
	}

	@Override
	public List<Output> listUnspent(Integer minConfirmations) {
		String unspentOutputsJson = rpcClient.execute(Commands.LIST_UNSPENT.getName(), 
				minConfirmations);
		List<Output> unspentOutputs = rpcClient.getMapper().mapToList(unspentOutputsJson,
				Output.class);
		return unspentOutputs;
	}

	@Override
	public List<Output> listUnspent(Integer minConfirmations, Integer maxConfirmations) {
		List<Object> params = CollectionUtils.asList(minConfirmations, maxConfirmations);
		String unspentOutputsJson = rpcClient.execute(Commands.LIST_UNSPENT.getName(), params);
		List<Output> unspentOutputs = rpcClient.getMapper().mapToList(unspentOutputsJson,
				Output.class);
		return unspentOutputs;
	}

	@Override
	public List<Output> listUnspent(Integer minConfirmations, Integer maxConfirmations, 
			List<String> addresses) {
		List<Object> params = CollectionUtils.asList(minConfirmations, maxConfirmations, addresses);
		String unspentOutputsJson = rpcClient.execute(Commands.LIST_UNSPENT.getName(), params);
		List<Output> unspentOutputs = rpcClient.getMapper().mapToList(unspentOutputsJson,
				Output.class);
		return unspentOutputs;
	}
	
	@Override
	public Boolean lockUnspent(Boolean isUnlocked) {
		String isSuccessJson = rpcClient.execute(Commands.LOCK_UNSPENT.getName(), isUnlocked);
		Boolean isSuccess = rpcClient.getParser().parseBoolean(isSuccessJson);
		return isSuccess;
	}

	@Override
	public Boolean lockUnspent(Boolean isUnlocked, List<OutputOverview> outputs) {
		List<Object> params = CollectionUtils.asList(isUnlocked, outputs);
		String isSuccessJson = rpcClient.execute(Commands.LOCK_UNSPENT.getName(), params);
		Boolean isSuccess = rpcClient.getParser().parseBoolean(isSuccessJson);
		return isSuccess;
	}
	
	@Override
	public Boolean move(String fromAccount, String toAccount, BigDecimal amount) {
		List<Object> params = CollectionUtils.asList(fromAccount, toAccount, amount);
		String isSuccessJson = rpcClient.execute(Commands.MOVE.getName(), params);
		Boolean isSuccess = rpcClient.getParser().parseBoolean(isSuccessJson);
		return isSuccess;
	}

	@Override
	public Boolean move(String fromAccount, String toAccount, BigDecimal amount, Integer dummy,
			String comment) {
		List<Object> params = CollectionUtils.asList(fromAccount, toAccount, amount, dummy, comment);
		String isSuccessJson = rpcClient.execute(Commands.MOVE.getName(), params);
		Boolean isSuccess = rpcClient.getParser().parseBoolean(isSuccessJson);
		return isSuccess;
	}
	
	@Override
	public void ping() {
		rpcClient.execute(Commands.PING.getName());
	}
	
	@Override
	public String sendFrom(String fromAccount, String toAddress, BigDecimal amount) {
		List<Object> params = CollectionUtils.asList(fromAccount, toAddress, amount);
		String transactionIdJson = rpcClient.execute(Commands.SEND_FROM.getName(), params);
		String transactionId = rpcClient.getParser().parseString(transactionIdJson);
		return transactionId;
	}

	@Override
	public String sendFrom(String fromAccount, String toAddress, BigDecimal amount, 
			Integer confirmations) {
		List<Object> params = CollectionUtils.asList(fromAccount, toAddress, amount, confirmations);
		String transactionIdJson = rpcClient.execute(Commands.SEND_FROM.getName(), params);
		String transactionId = rpcClient.getParser().parseString(transactionIdJson);
		return transactionId;
	}

	@Override
	public String sendFrom(String fromAccount, String toAddress, BigDecimal amount, 
			Integer confirmations, String comment) {
		List<Object> params = CollectionUtils.asList(fromAccount, toAddress, amount, confirmations, 
				comment);
		String transactionIdJson = rpcClient.execute(Commands.SEND_FROM.getName(), params);
		String transactionId = rpcClient.getParser().parseString(transactionIdJson);
		return transactionId;
	}

	@Override
	public String sendFrom(String fromAccount, String toAddress, BigDecimal amount, 
			Integer confirmations, String comment, String commentTo) {
		List<Object> params = CollectionUtils.asList(fromAccount, toAddress, amount, confirmations,
				comment, commentTo);
		String transactionIdJson = rpcClient.execute(Commands.SEND_FROM.getName(), params);
		String transactionId = rpcClient.getParser().parseString(transactionIdJson);
		return transactionId;
	}
	
	@Override
	public String sendMany(String fromAccount, Map<String, BigDecimal> toAddresses) {
		List<Object> params = CollectionUtils.asList(fromAccount, toAddresses);
		String transactionIdJson = rpcClient.execute(Commands.SEND_MANY.getName(), params);
		String transactionId = rpcClient.getParser().parseString(transactionIdJson);
		return transactionId;
	}

	@Override
	public String sendMany(String fromAccount, Map<String, BigDecimal> toAddresses,	
			Integer confirmations) {
		List<Object> params = CollectionUtils.asList(fromAccount, toAddresses, confirmations);
		String transactionIdJson = rpcClient.execute(Commands.SEND_MANY.getName(), params);
		String transactionId = rpcClient.getParser().parseString(transactionIdJson);
		return transactionId;
	}

	@Override
	public String sendMany(String fromAccount, Map<String, BigDecimal> toAddresses,
			Integer confirmations, String comment) {
		List<Object> params = CollectionUtils.asList(fromAccount, toAddresses, confirmations,
				comment);
		String transactionIdJson = rpcClient.execute(Commands.SEND_MANY.getName(), params);
		String transactionId = rpcClient.getParser().parseString(transactionIdJson);
		return transactionId;
	}
	
	@Override
	public String sendRawTransaction(String hexTransaction) {
		String transactionIdJson = rpcClient.execute(Commands.SEND_RAW_TRANSACTION.getName(), 
				hexTransaction);
		String transactionId = rpcClient.getParser().parseString(transactionIdJson);
		return transactionId;
	}

	@Override
	public String sendRawTransaction(String hexTransaction, Boolean withHighFees) {
		List<Object> params = CollectionUtils.asList(hexTransaction, withHighFees);
		String transactionIdJson = rpcClient.execute(Commands.SEND_RAW_TRANSACTION.getName(), 
				params);
		String transactionId = rpcClient.getParser().parseString(transactionIdJson);
		return transactionId;
	}
	
	@Override
	public String sendToAddress(String toAddress, BigDecimal amount) {
		List<Object> params = CollectionUtils.asList(toAddress, amount);
		String transactionIdJson = rpcClient.execute(Commands.SEND_TO_ADDRESS.getName(), params);
		String transactionId = rpcClient.getParser().parseString(transactionIdJson);
		return transactionId;
	}

	@Override
	public String sendToAddress(String toAddress, BigDecimal amount, String comment) {
		List<Object> params = CollectionUtils.asList(toAddress, amount, comment);
		String transactionIdJson = rpcClient.execute(Commands.SEND_TO_ADDRESS.getName(), params);
		String transactionId = rpcClient.getParser().parseString(transactionIdJson);
		return transactionId;
	}

	@Override
	public String sendToAddress(String toAddress, BigDecimal amount, String comment, 
			String commentTo) {
		List<Object> params = CollectionUtils.asList(toAddress, amount, comment, commentTo);
		String transactionIdJson = rpcClient.execute(Commands.SEND_TO_ADDRESS.getName(), params);
		String transactionId = rpcClient.getParser().parseString(transactionIdJson);
		return transactionId;
	}
	
	@Override
	public void setAccount(String address, String account) {
		List<Object> params = CollectionUtils.asList(address, account);
		rpcClient.execute(Commands.SET_ACCOUNT.getName(), params);
	}
	
	@Override
	public void setGenerate(Boolean isGenerate) {
		rpcClient.execute(Commands.SET_GENERATE.getName(), isGenerate);		
	}
	
	@Override
	public void setGenerate(Boolean isGenerate, Integer processors) {
		List<Object> params = CollectionUtils.asList(isGenerate, processors);
		rpcClient.execute(Commands.SET_GENERATE.getName(), params);
	}
	
	@Override
	public Boolean setTxFee(BigDecimal txFee) {
		String resultJson = rpcClient.execute(Commands.SET_TX_FEE.getName(), txFee);
		Boolean result = rpcClient.getParser().parseBoolean(resultJson);
		return result;
	}
	
	@Override
	public String signMessage(String address, String message) {
		List<Object> params = CollectionUtils.asList(address, message);
		String signatureJson = rpcClient.execute(Commands.SIGN_MESSAGE.getName(), params);
		String signature = rpcClient.getParser().parseString(signatureJson);
		return signature;
	}
	
	@Override
	public SignatureResult signRawTransaction(String hexTransaction) {
		String signatureResultJson = rpcClient.execute(Commands.SIGN_RAW_TRANSACTION.getName(), 
				hexTransaction);
		SignatureResult signatureResult = rpcClient.getMapper().mapToEntity(signatureResultJson, 
				SignatureResult.class);
		return signatureResult;
	}

	@Override
	public SignatureResult signRawTransaction(String hexTransaction, List<Output> outputs) {
		List<Object> params = CollectionUtils.asList(hexTransaction, outputs);
		String signatureResultJson = rpcClient.execute(Commands.SIGN_RAW_TRANSACTION.getName(), 
				params);
		SignatureResult signatureResult = rpcClient.getMapper().mapToEntity(signatureResultJson, 
				SignatureResult.class);
		return signatureResult;
	}

	@Override
	public SignatureResult signRawTransaction(String hexTransaction, List<Output> outputs, 
			List<String> privateKeys) {
		List<Object> params = CollectionUtils.asList(hexTransaction, outputs, privateKeys);
		String signatureResultJson = rpcClient.execute(Commands.SIGN_RAW_TRANSACTION.getName(),
				params);
		SignatureResult signatureResult = rpcClient.getMapper().mapToEntity(signatureResultJson, 
				SignatureResult.class);
		return signatureResult;
	}

	@Override
	public SignatureResult signRawTransaction(String hexTransaction, List<Output> outputs, 
			List<String> privateKeys, String sigHashType) {
		List<Object> params = CollectionUtils.asList(hexTransaction, outputs, privateKeys, 
				sigHashType);
		String signatureResultJson = rpcClient.execute(Commands.SIGN_RAW_TRANSACTION.getName(), 
				params);
		SignatureResult signatureResult = rpcClient.getMapper().mapToEntity(signatureResultJson,
				SignatureResult.class);
		return signatureResult;
	}
	
	@Override
	public String stop() {
		String noticeMsgJson = rpcClient.execute(Commands.STOP.getName());
		String noticeMsg = rpcClient.getParser().parseString(noticeMsgJson);
		return noticeMsg;
	}
	
	@Override
	public AddressInfo validateAddress(String address) {
		String addressInfoJson = rpcClient.execute(Commands.VALIDATE_ADDRESS.getName(), address);
		AddressInfo addressInfo = rpcClient.getMapper().mapToEntity(addressInfoJson, 
				AddressInfo.class);
		return addressInfo;
	}
	
	@Override
	public Boolean verifyMessage(String address, String signature, String message) {
		List<Object> params = CollectionUtils.asList(address, signature, message);
		String isSigValidJson = rpcClient.execute(Commands.VERIFY_MESSAGE.getName(), params);
		Boolean isSigValid = rpcClient.getParser().parseBoolean(isSigValidJson);
		return isSigValid;
	}
	
	@Override
	public void walletLock() {
		rpcClient.execute(Commands.WALLET_LOCK.getName());
	}
	
	@Override
	public void walletPassphrase(String passphrase, Integer authTimeout) {
		List<Object> params = CollectionUtils.asList(passphrase, authTimeout);
		rpcClient.execute(Commands.WALLET_PASSPHRASE.getName(), params);
	}

	@Override
	public void walletPassphraseChange(String curPassphrase, String newPassphrase) {
		List<Object> params = CollectionUtils.asList(curPassphrase, newPassphrase);
		rpcClient.execute(Commands.WALLET_PASSPHRASE_CHANGE.getName(), params);
	}
}