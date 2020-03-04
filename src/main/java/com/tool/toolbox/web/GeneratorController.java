package com.tool.toolbox.web;

import com.tool.toolbox.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/generator")
public class GeneratorController {

    @Autowired
    private GeneratorService service;

    @GetMapping("/born")
    public void born(HttpServletRequest request, HttpServletResponse response,
                     @RequestParam("tableName") String tableName) {
        service.born(request,response,tableName);
    }

}
