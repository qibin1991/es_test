package com.tzj;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName CompanyInfo
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/2114:38
 * @Version 1.0
 **/

@Data
@Builder
public class CompanyInfo {
    String companyName;
    String companyId;
    String nature;
    String industryId;
    String linkedName;
    String linkedNum;
    String registryTime;
    String capital;
    String reAdress;
    String comAdress;
    String postCode;
    String userName;
    String email;
    String userPhone;
    String classify;
    String comFlag;

    public CompanyInfo() {
    }

    public CompanyInfo(String companyName, String companyId, String nature, String industryId, String linkedName, String linkedNum, String registryTime, String capital, String reAdress, String comAdress, String postCode, String userName, String email, String userPhone, String classify, String comFlag) {
        this.companyName = companyName;
        this.companyId = companyId;
        this.nature = nature;
        this.industryId = industryId;
        this.linkedName = linkedName;
        this.linkedNum = linkedNum;
        this.registryTime = registryTime;
        this.capital = capital;
        this.reAdress = reAdress;
        this.comAdress = comAdress;
        this.postCode = postCode;
        this.userName = userName;
        this.email = email;
        this.userPhone = userPhone;
        this.classify = classify;
        this.comFlag = comFlag;
    }
}
