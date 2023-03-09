package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransactionDao;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    /*
    ########################################   Attributes   ##########################################
     */

    private TransactionDao dao;

    /*
   ########################################   Constructor   ##########################################
    */

    public TransactionController(TransactionDao dao) {
        this.dao = dao;
    }

    /*
   ########################################  API Endpoints  ##########################################
    */

    //Todo Create API Endpoints for TransactionController

    
}
