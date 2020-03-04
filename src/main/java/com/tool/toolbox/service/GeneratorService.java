package com.tool.toolbox.service;

import com.tool.toolbox.dto.GeneratorDto;
import com.tool.toolbox.generator.GeneratorConfig;
import com.tool.toolbox.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class GeneratorService {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClass;
    @Value("${spring.datasource.url}")
    private String connectionURL;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    public void born(HttpServletRequest request, HttpServletResponse response,String tableNames){
        GeneratorDto paramDto = new GeneratorDto();
        List<GeneratorDto.TableEntity> list = new ArrayList<>();
        String markStr = ",";
        if(tableNames.contains(markStr)){
            Arrays.stream(tableNames.split(","))
                    .forEach(val -> {
                        GeneratorDto.TableEntity tableEntity = new GeneratorDto.TableEntity();
                        tableEntity.setTableName(val);
                        tableEntity.setDomainObjectName(val.substring(0, 1).toUpperCase() + val.substring(1));
                        list.add(tableEntity);
                    });
        }else {
            GeneratorDto.TableEntity tableEntity = new GeneratorDto.TableEntity();
            tableEntity.setTableName(tableNames);
            tableEntity.setDomainObjectName(tableNames.substring(0, 1).toUpperCase() + tableNames.substring(1));
            list.add(tableEntity);
        }
        paramDto.setDriverClass(driverClass);
        paramDto.setConnectionURL(connectionURL);
        paramDto.setUsername(username);
        paramDto.setPassword(password);
        paramDto.setTableList(list);
        String url = new GeneratorConfig().generatorConfig(paramDto);
        List<File> files = FileUtils.readFile(url);
        FileUtils.downLoadFiles(files,request,response);
    }

}
