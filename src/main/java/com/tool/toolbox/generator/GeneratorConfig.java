package com.tool.toolbox.generator;

import com.tool.toolbox.dto.GeneratorDto;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class GeneratorConfig {

    /**模板，暂时不需要*/
    private boolean exampleTf = false;
    /**静态地址，非项目结构变更不做修改*/
    private static String sysUrl = System.getProperty("user.dir")
            .concat(File.separator).concat("src")
            .concat(File.separator).concat("main")
            .concat(File.separator).concat("resources");

    public String generatorConfig(GeneratorDto generatorDto) {
        String classpathEntry;
        if(generatorDto.getDriverClass().contains("mysql")){
            classpathEntry  = sysUrl + File.separator + "lib" + File.separator + "mysql-connector-java-8.0.13.jar";
        }else
            throw new IllegalStateException("Unexpected value: " + generatorDto.getDriverClass());
        List<String> warnings = new ArrayList<>();
        Context context = new Context(ModelType.CONDITIONAL);
        context.setTargetRuntime("MyBatis3");
        context.setId("mysql");

        CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
        commentGeneratorConfiguration.addProperty("suppressDate","true");
        commentGeneratorConfiguration.addProperty("suppressAllComments","true");

        /*数据库链接URL，用户名、密码 */
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setDriverClass(generatorDto.getDriverClass());
        jdbcConnectionConfiguration.setConnectionURL(generatorDto.getConnectionURL());
        jdbcConnectionConfiguration.setUserId(generatorDto.getUsername());
        jdbcConnectionConfiguration.setPassword(generatorDto.getPassword());

        JavaTypeResolverConfiguration javaTypeResolverConfiguration = new JavaTypeResolverConfiguration();
        javaTypeResolverConfiguration.addProperty("forceBigDecimals","false");

        /*生成模型的包名和位置*/
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
        javaModelGeneratorConfiguration.setTargetPackage("tamp");
        javaModelGeneratorConfiguration.setTargetProject(sysUrl);
        javaModelGeneratorConfiguration.addProperty("enableSubPackages","true");
        javaModelGeneratorConfiguration.addProperty("trimStrings","true");

        /*生成映射文件的包名和位置*/
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetPackage("tamp");
        sqlMapGeneratorConfiguration.setTargetProject(sysUrl);
        sqlMapGeneratorConfiguration.addProperty("enableSubPackages","true");

        /*生成DAO的包名和位置*/
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        javaClientGeneratorConfiguration.setTargetPackage("tamp");
        javaClientGeneratorConfiguration.setTargetProject(sysUrl);
        javaClientGeneratorConfiguration.addProperty("enableSubPackages","true");

        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        switch (generatorDto.getType()){
            case 0: pluginConfiguration.setConfigurationType("com.tool.toolbox.generator.LombokPlugin");break;
            case 1: pluginConfiguration.setConfigurationType("com.tool.toolbox.generator.CommentPlugin");break;
        }

        context.addPluginConfiguration(pluginConfiguration);

        context.addProperty("javaFileEncoding","UTF-8");
        context.addProperty("javaFormatter","org.mybatis.generator.api.dom.DefaultJavaFormatter");
        context.addProperty("xmlFormatter","org.mybatis.generator.api.dom.DefaultXmlFormatter");
        context.addProperty("beginningDelimiter","`");
        context.addProperty("endingDelimiter","`");

        context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);
        context.setJavaTypeResolverConfiguration(javaTypeResolverConfiguration);
        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);


        generatorDto.getTableList().forEach(tableEntity->{
            TableConfiguration tableConfiguration = new TableConfiguration(context);
            tableConfiguration.setTableName(tableEntity.getTableName());
            tableConfiguration.setDomainObjectName(tableEntity.getDomainObjectName());
            tableConfiguration.setCountByExampleStatementEnabled(exampleTf);
            tableConfiguration.setUpdateByExampleStatementEnabled(exampleTf);
            tableConfiguration.setSelectByExampleStatementEnabled(exampleTf);
            tableConfiguration.setSelectByExampleStatementEnabled(exampleTf);
            tableConfiguration.setDeleteByExampleStatementEnabled(exampleTf);
            context.addTableConfiguration(tableConfiguration);
        });

        Configuration config = new Configuration();
        config.addClasspathEntry(classpathEntry);
        config.addContext(context);

        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator;
        try {
            myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
            return sysUrl + File.separator + "tamp";
        } catch (InvalidConfigurationException | InterruptedException | IOException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

