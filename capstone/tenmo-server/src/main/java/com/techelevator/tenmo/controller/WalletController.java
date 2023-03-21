package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.dao.WalletDao;
import com.techelevator.tenmo.model.Authority;
import com.techelevator.tenmo.model.Wallet;
import com.techelevator.tenmo.model.WalletDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/wallets")
@PreAuthorize("isAuthenticated()")
public class WalletController {

    /*
    ########################################   Attributes   ##########################################
     */

    private WalletDao walletDao;
    private UserDao userDao;

    /*
   ########################################   Constructor   ##########################################
    */

    public WalletController(WalletDao walletDao, UserDao userDao) {
        this.walletDao = walletDao;
        this.userDao=userDao;
    }

    /*
   ########################################  API Endpoints  ##########################################
    */

    //ToDo: Restrict to admin only
    @GetMapping
    public List<WalletDto> list(@RequestParam(required = false, name = "user-id") Integer userId, Principal principal){

        List<Wallet> wallets = walletDao.listWallets();
        List<WalletDto> walletDtos = new ArrayList<>();
        int currentUserId = userDao.findIdByUsername(principal.getName());

        if ((userId != null && userId!=currentUserId && !isAdmin(principal)) || (userId == null && !isAdmin(principal))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized. Cannot access other users' resource.");
        }


        if(userId != null){
            if (userDao.getUserById(userId) == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown User.");

            Wallet wallet = walletDao.getWalletByUser(userId);
            walletDtos.add(mapWalletToDto(wallet));

        } else {
            for (Wallet wallet: wallets){
                walletDtos.add(mapWalletToDto(wallet));
            }
        }

        return walletDtos;
    }

    //ToDo: Restrict to admin only
    @GetMapping (path="/{id}")
    public WalletDto get(@PathVariable int id, Principal principal){

        Wallet wallet = walletDao.getWallet(id);
        int currentUserId = userDao.findIdByUsername(principal.getName());

        if(wallet.getUserId()!=currentUserId && !isAdmin(principal)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized. Cannot access other users' resource.");
        }

        return mapWalletToDto(wallet);
    }

    @GetMapping("/me")
    public WalletDto userWallet(Principal principal) {
        int currentUserId = userDao.findIdByUsername(principal.getName());
        Wallet userWallet = walletDao.getWalletByUser(currentUserId);
        return mapWalletToDto(userWallet);
    }

    /*
    ########################################  Helper Methods  ##########################################
     */

    //Helper Function to map a Wallet object to a WalletDto
    private WalletDto mapWalletToDto(Wallet wallet) {

        WalletDto walletDto = new WalletDto();
        walletDto.setBalance(wallet.getBalance());
        walletDto.setId(wallet.getId());
        walletDto.setUserId(wallet.getUserId());

        return walletDto;
    }

    private boolean isAdmin(Principal principal) {
        //Todo : use findByUsername in Dao
        int currentUserId=userDao.findIdByUsername(principal.getName());
        return userDao.getUserById(currentUserId).getAuthorities().contains(new Authority("ROLE_ADMIN"));
    }


}
