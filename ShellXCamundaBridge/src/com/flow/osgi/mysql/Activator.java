package com.flow.osgi.mysql;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.jdbc.DataSourceFactory;

import com.mysql.jdbc.Driver;

public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        MysqlDataSourceFactory dsf = new MysqlDataSourceFactory();
        Dictionary<String, String> props = new Hashtable<String, String>();
        props.put(DataSourceFactory.OSGI_JDBC_DRIVER_CLASS, Driver.class.getName());
        props.put(DataSourceFactory.OSGI_JDBC_DRIVER_NAME, "mysql");
        context.registerService(DataSourceFactory.class.getName(), dsf, props);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        // empty
    }
}