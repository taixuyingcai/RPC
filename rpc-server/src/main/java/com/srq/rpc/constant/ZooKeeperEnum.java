package com.srq.rpc.constant;

/**
 * <p/>
 * User: ryanshi@tcl.com
 * Date: 2017-01-09
 * Time: 15:46
 */
public enum ZooKeeperEnum {

    ZK_SESSION_TIME("timeout", "5000"),
    ZK_REGISTRY_PATH("registryPath", "/registry"),
    ZK_DATA_PATH("dataPath", ZK_REGISTRY_PATH.value + "/data");

    private String name;
    private String value;

    private ZooKeeperEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String value() {
        return value;
    }
}
