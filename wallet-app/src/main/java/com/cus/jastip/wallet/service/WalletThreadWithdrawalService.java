package com.cus.jastip.wallet.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cus.jastip.wallet.domain.Wallet;
import com.cus.jastip.wallet.domain.WalletTransaction;
import com.cus.jastip.wallet.domain.WalletWithdrawal;
import com.cus.jastip.wallet.domain.WithdrawalTransferFailed;
import com.cus.jastip.wallet.domain.WithdrawalTransferHistory;
import com.cus.jastip.wallet.domain.WithdrawalTransferList;
import com.cus.jastip.wallet.domain.enumeration.WalletTransactionType;
import com.cus.jastip.wallet.domain.enumeration.WithdrawalStatus;
import com.cus.jastip.wallet.model.TransfersModel;
import com.cus.jastip.wallet.repository.WalletRepository;
import com.cus.jastip.wallet.repository.WalletWithdrawalRepository;
import com.cus.jastip.wallet.repository.WithdrawalTransferListRepository;
import com.cus.jastip.wallet.web.rest.BcaApi;
import com.cus.jastip.wallet.web.rest.WalletResource;
import com.cus.jastip.wallet.web.rest.WalletTransactionResource;
import com.cus.jastip.wallet.web.rest.WalletWithdrawalResource;
import com.cus.jastip.wallet.web.rest.WithdrawalTransferFailedResource;
import com.cus.jastip.wallet.web.rest.WithdrawalTransferHistoryResource;
import com.cus.jastip.wallet.web.rest.WithdrawalTransferListResource;

@Service
public class WalletThreadWithdrawalService {

	@Autowired
	private WalletRepository walletRepsotiry;

	@Autowired
	private WalletWithdrawalRepository walletWithdrawalRepository;

	@Autowired
	private WalletResource walletResource;

	@Autowired
	private BcaApi bcaApi;

	@Autowired
	private WithdrawalTransferListRepository withdrawalTransferListRepository;

	@Autowired
	private WithdrawalTransferListResource withdrawalTransferListResource;

	@Autowired
	private WithdrawalTransferHistoryResource withdrawalTransferHistoryResource;

	@Autowired
	private WithdrawalTransferFailedResource withdrawalTransferFailedResource;

	@Autowired
	private WalletTransactionResource walletTransactionResource;

	@Autowired
	private WalletWithdrawalResource walletWithdrawalResource;

	
	public void walletWithdraw() throws IOException, URISyntaxException {
		List<WithdrawalTransferList> transferList = withdrawalTransferListRepository.findAll();
		if (transferList.size() > 0) {
			for (WithdrawalTransferList withdrawalTransferList : transferList) {
				WalletWithdrawal walletWithdrawal = walletWithdrawalRepository
						.findOne(withdrawalTransferList.getWithdrawalId());
				Wallet wallet = walletRepsotiry.findByOwnerId(walletWithdrawal.getOwnerId());
				if (withdrawalTransferList.getNominal() < wallet.getBalance()) {
					TransfersModel transModel = bcaApi.getBankingCorpTransfer();
					if (transModel.getStatus().equalsIgnoreCase("Success")) {
						WithdrawalTransferHistory Withdrawhistory = new WithdrawalTransferHistory();
						Withdrawhistory.setNominal(withdrawalTransferList.getNominal());
						Withdrawhistory.setWithdrawalId(withdrawalTransferList.getWithdrawalId());
						Withdrawhistory.setDestBankName(withdrawalTransferList.getDestBankName());
						Withdrawhistory.setDestBankAccount(withdrawalTransferList.getDestBankAccount());
						WalletTransaction walletTransaction = new WalletTransaction();
						walletTransaction.setNominal(withdrawalTransferList.getNominal());
						walletTransaction.setWithdrawalId(withdrawalTransferList.getWithdrawalId());
						walletTransaction.setTransactionType(WalletTransactionType.WITHDRAWAL);
						walletTransaction.setTransactionDateTime(Instant.now());
						withdrawalTransferHistoryResource.createWithdrawalTransferHistory(Withdrawhistory);
						walletTransactionResource.createWalletTransaction(walletTransaction);
						wallet.setBalance(wallet.getBalance() - withdrawalTransferList.getNominal());
						walletResource.updateWallet(wallet);
						walletWithdrawal.setStatus(WithdrawalStatus.SUCCESS);
						walletWithdrawalResource.updateWalletWithdrawal(walletWithdrawal);
						withdrawalTransferListResource.deleteWithdrawalTransferList(withdrawalTransferList.getId());
					} else if (transModel.getStatus().equalsIgnoreCase("Failed")) {
						WithdrawalTransferFailed withdrawalFailed = new WithdrawalTransferFailed();
						withdrawalFailed.setNominal(withdrawalTransferList.getNominal());
						withdrawalFailed.setWithdrawalId(withdrawalTransferList.getWithdrawalId());
						withdrawalFailed.setDestBankName(withdrawalTransferList.getDestBankName());
						withdrawalFailed.setDestBankAccount(withdrawalTransferList.getDestBankAccount());
						withdrawalTransferFailedResource.createWithdrawalTransferFailed(withdrawalFailed);
						walletWithdrawal.setStatus(WithdrawalStatus.FAIL);
						walletWithdrawalResource.updateWalletWithdrawal(walletWithdrawal);
						withdrawalTransferListResource.deleteWithdrawalTransferList(withdrawalTransferList.getId());
					}
				}
			}
		}
	}
}
