package com.example.authapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZipCloudResponse {
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("results")
    private List<AddressResult> results;
    
    @JsonProperty("status")
    private Integer status;
    
    // ゲッター・セッター
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public List<AddressResult> getResults() {
        return results;
    }
    
    public void setResults(List<AddressResult> results) {
        this.results = results;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    // 内部クラス：住所情報
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AddressResult {
        @JsonProperty("address1")  // 都道府県
        private String address1;
        
        @JsonProperty("address2")  // 市区町村
        private String address2;
        
        @JsonProperty("address3")  // 町域
        private String address3;
        
        @JsonProperty("kana1")     // 都道府県（カナ）
        private String kana1;
        
        @JsonProperty("kana2")     // 市区町村（カナ）
        private String kana2;
        
        @JsonProperty("kana3")     // 町域（カナ）
        private String kana3;
        
        @JsonProperty("prefcode")  // 都道府県コード
        private String prefcode;
        
        @JsonProperty("zipcode")   // 郵便番号
        private String zipcode;
        
        // ゲッター・セッター
        public String getAddress1() {
            return address1;
        }
        
        public void setAddress1(String address1) {
            this.address1 = address1;
        }
        
        public String getAddress2() {
            return address2;
        }
        
        public void setAddress2(String address2) {
            this.address2 = address2;
        }
        
        public String getAddress3() {
            return address3;
        }
        
        public void setAddress3(String address3) {
            this.address3 = address3;
        }
        
        public String getKana1() {
            return kana1;
        }
        
        public void setKana1(String kana1) {
            this.kana1 = kana1;
        }
        
        public String getKana2() {
            return kana2;
        }
        
        public void setKana2(String kana2) {
            this.kana2 = kana2;
        }
        
        public String getKana3() {
            return kana3;
        }
        
        public void setKana3(String kana3) {
            this.kana3 = kana3;
        }
        
        public String getPrefcode() {
            return prefcode;
        }
        
        public void setPrefcode(String prefcode) {
            this.prefcode = prefcode;
        }
        
        public String getZipcode() {
            return zipcode;
        }
        
        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }
    }
}

