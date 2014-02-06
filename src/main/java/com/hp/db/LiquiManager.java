package com.hp.db;

import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.ChangeGeneratorFactory;
import liquibase.diff.output.changelog.core.MissingColumnChangeGenerator;
import liquibase.exception.LiquibaseException;
import liquibase.ext.vertica.database.VerticaDatabase;
import liquibase.ext.vertica.snapshot.ColumnVerticaSnapshotGenerator;
import liquibase.integration.commandline.CommandLineUtils;
import liquibase.snapshot.jvm.UniqueConstraintSnapshotGenerator;
import liquibase.util.StringUtils;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: vesterma
 * Date: 10/11/13
 * Time: 21:47
 * To change this template use File | Settings | File Templates.
 */
public class LiquiManager {
    public static void main(String[] args){

        VerticaDatabase verticaDatabase = new VerticaDatabase();

        liquibase.database.DatabaseFactory.getInstance().clearRegistry();
        liquibase.database.DatabaseFactory.getInstance().register(verticaDatabase);
        liquibase.snapshot.SnapshotGeneratorFactory.getInstance().unregister(UniqueConstraintSnapshotGenerator.class);
        liquibase.snapshot.SnapshotGeneratorFactory.getInstance().register(new ColumnVerticaSnapshotGenerator());

//        DataTypeFactory.getInstance().register(BinaryType.class);
//        DataTypeFactory.getInstance().register(LongBinaryType.class);
//        DataTypeFactory.getInstance().register(LongVarcharType.class);
//        DataTypeFactory.getInstance().register(VarBinaryType.class);
//        DataTypeFactory.getInstance().unregister(BlobType.class.getName());
//        DataTypeFactory.getInstance().unregister(ClobType.class.getName());
//        liquibase.snapshot.SnapshotGeneratorFactory.getInstance().unregister(ColumnSnapshotGenerator.class);

        ChangeGeneratorFactory.getInstance().unregister(MissingColumnChangeGenerator.class);




        Properties myProp = new Properties();

//        myProp.put("user", "z_admin");
//        myProp.put("password", "5_admin");
        myProp.put("user", "maas_admin");
        myProp.put("password", "maas_admin_123");

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:vertica://localhost:5433/EMS",
                    myProp);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



        DatabaseConnection dc = new JdbcConnection(conn);
        verticaDatabase.setConnection(dc);

        Liquibase liquibase = null;
        try {
//            liquibase = new Liquibase("C:\\Users\\vesterma\\Documents\\Projects\\liquibase\\target\\classes\\db\\db.changelog.xml", new FileSystemResourceAccessor(),dc);
//            liquibase = new Liquibase("C:\\Users\\vesterma\\Documents\\Projects\\liquibase\\target\\classes\\db\\db_change2.xml", new FileSystemResourceAccessor(),dc);
//            liquibase = new Liquibase("C:\\Temp\\test.xml", new FileSystemResourceAccessor(),dc);
//            liquibase.rollback(2,"");
//            liquibase.update(2,"");
//            liquibase.changeLogSync("");
//            liquibase.generateDocumentation("c:\\temp");
            String defaultCatalogName = "public";

            String defaultSchemaName = "public";
            String changeLogFile = "c:\\temp\\test.xml";
            String changeSetAuthor = "jony";
            String diffTypes = null;
            String changeSetContext = null;;
            String dataOutputDirectory = "c:\\temp";

            boolean includeCatalog = false; //Boolean.parseBoolean(getCommandParam("includeCatalog", "false"));
            boolean includeSchema = false; //Boolean.parseBoolean(getCommandParam("includeSchema", "false"));
            boolean includeTablespace = false; //Boolean.parseBoolean(getCommandParam("includeTablespace", "false"));
            DiffOutputControl diffOutputControl = new DiffOutputControl(includeCatalog, includeSchema, includeTablespace);

            try {
                CommandLineUtils.doGenerateChangeLog(changeLogFile, verticaDatabase, defaultCatalogName, defaultSchemaName, StringUtils.trimToNull(diffTypes), StringUtils.trimToNull(changeSetAuthor), StringUtils.trimToNull(changeSetContext), StringUtils.trimToNull(dataOutputDirectory), diffOutputControl);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        } catch (LiquibaseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }




    }
}