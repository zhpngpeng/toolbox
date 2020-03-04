package com.tool.toolbox.dto;

import lombok.Data;

import java.util.List;

@Data
public class GeneratorDto {

    /**数据表list*/
    private List<TableEntity> tableList;
    /**数据库驱动*/
    private String driverClass;
    /**数据库url*/
    private String driverURL;
    /**数据库url*/
    private String connectionURL;
    /**数据库用户名*/
    private String username;
    /**数据库密码*/
    private String password;
    /**生成类型：0-默认lombok,1-传统*/
    private int type;

    @Data
    public static class TableEntity{
        private String tableName;
        private String domainObjectName;
    }

}
