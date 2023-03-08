package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Wallet;

import java.util.List;

public interface WalletDao {

    List<Wallet> listWallets(); //for use by admins only

    Wallet getWallet(int walletId); //uses wallet ID

    Wallet getWalletByUser(int userId);  //uses user ID

    Wallet updateWallet(Wallet updatedWallet, int walletId);  //uses wallet ID

    Wallet createWallet(Wallet newWallet, int userId);  //uses user ID because wallet tied to user

}
