package com.tr.nebula.security.web.controller;

import com.tr.nebula.security.db.dao.RestDao;
import com.tr.nebula.security.db.domain.Rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Mustafa Erbin on 12.05.2017.
 */
@RestController
@RequestMapping(name = "restServices")
public class RestServiceController {

    @Autowired
    private RestDao restDao;

    @RequestMapping(method = RequestMethod.GET)
    public List<Rest> findAll() {
        return restDao.findAll();
    }
}
