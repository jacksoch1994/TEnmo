package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.WalletDao;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Wallet;
import com.techelevator.tenmo.model.WalletDto;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    /*
    ########################################   Attributes   ##########################################
     */

    private WalletDao dao;

    /*
   ########################################   Constructor   ##########################################
    */

    public WalletController(WalletDao dao) {
        this.dao = dao;
    }

    /*
   ########################################  API Endpoints  ##########################################
    */

    @GetMapping
    public List<WalletDto> list(@RequestParam(required = false, name = "user-id") Integer userId){
        List<Wallet> wallets = dao.listWallets();
        List<WalletDto> walletDtos = new ArrayList<>();
        if(userId != null){
            Wallet wallet = dao.getWalletByUser(userId);

            WalletDto walletDto = new WalletDto();
            walletDto.balance=wallet.getBalance();
            walletDto.id=wallet.getId();
            walletDto.userId=wallet.getUserId();

            walletDtos.add(walletDto);
        } else {
            for (Wallet wallet: wallets){
                WalletDto walletDto = new WalletDto();
                walletDto.balance=wallet.getBalance();
                walletDto.id=wallet.getId();
                walletDto.userId=wallet.getUserId();

                walletDtos.add(walletDto);
            }

        }
        return walletDtos;
    }

    @GetMapping (path="/{id}")
    public WalletDto get(@PathVariable int id){
        Wallet wallet = dao.getWallet(id);
        WalletDto walletDto = new WalletDto();
        walletDto.balance=wallet.getBalance();
        walletDto.id=wallet.getId();
        walletDto.userId=wallet.getUserId();

        return walletDto;
    }

}
