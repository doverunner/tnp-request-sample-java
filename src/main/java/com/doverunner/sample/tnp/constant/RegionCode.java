package com.doverunner.sample.tnp.constant;

public enum RegionCode {

    OREGON("RG004", "us-west-2"),
    SEOUL("RG011", "ap-northeast-2"),
    SINGAPORE("RG013", "ap-southeast-1");

    private final String regionCode;
    private final String awsCode;

    RegionCode(String regionCode, String awsCode) {
        this.regionCode = regionCode;
        this.awsCode = awsCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public String getAwsCode() {
        return awsCode;
    }
}
