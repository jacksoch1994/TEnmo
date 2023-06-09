package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface WalletDao {

    /**
     * Obtains a List of all Wallets.
     *
     * @return a List containing all Wallets.
     */
    List<Wallet> listWallets(); //for use by admins only

    /**
     * Returns the Wallet with the specified id. Returns null if no Wallet by that id exists.
     *
     * @param walletId the id of the Wallet.
     * @return the Wallet with the specified id.
     */
    Wallet getWallet(int walletId); //uses wallet ID

    /**
     * Returns the Wallet associated with the specified User. Returns null if no wallet with the specified User id
     * exists.
     *
     * @param userId the id of the User.
     * @return the Wallet associated with the provided userId.
     */
    Wallet getWalletByUser(int userId);  //uses user ID

    /**
     * Updates the values of an existing wallet.
     *
     * @param updatedWallet a Wallet object containing updated values.
     * @param walletId the id of the Wallet to update.
     * @return the Updated Wallet
     */
    Wallet updateWallet(Wallet updatedWallet, int walletId);  //uses wallet ID

    /**
     * Transfers balance between two wallets.
     *
     * @param sendingWalletId the id of the Wallet to withdraw balance from.
     * @param receivingWalletId the id of the Wallet to deposit balance into.
     * @return a boolean indicating a successful operation
     */
    boolean transferBalance(int sendingWalletId, int receivingWalletId, BigDecimal transferAmount);


    /**
     * Creates a new Wallet.
     *
     * @param newWallet a Wallet object representing the Wallet to add.
     * @param userId the id of the User associated with this wallet.
     * @return the newly created Wallet.
     */
    Wallet createWallet(Wallet newWallet, int userId);  //uses user ID because wallet tied to user

}
