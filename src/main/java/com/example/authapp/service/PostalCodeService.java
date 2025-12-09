package com.example.authapp.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.authapp.dto.ZipCloudResponse;
import com.example.authapp.exception.ExternalApiException;
import com.example.authapp.exception.InvalidParameterException;
import com.example.authapp.exception.PostalCodeNotFoundException;

@Service
public class PostalCodeService {
    
    private static final Logger logger = LoggerFactory.getLogger(PostalCodeService.class);
    
    // zipcloud APIのベースURL
    private static final String ZIPCLOUD_API_URL = "https://zipcloud.ibsnet.co.jp/api/search";
    
    // RestTemplate（Spring Boot 2.xで使用）
    private final RestTemplate restTemplate;
    
    // 郵便番号の上2桁から都道府県名へのマッピング（フォールバック用）
    private static final Map<String, String> prefectureMap = new HashMap<>();
    static {
        prefectureMap.put("10", "東京都");
        prefectureMap.put("27", "大阪府");
        prefectureMap.put("26", "京都府");
        prefectureMap.put("14", "神奈川県");
        prefectureMap.put("11", "埼玉県");
        prefectureMap.put("12", "千葉県");
        // 他の主要な郵便番号範囲も追加可能
        prefectureMap.put("50", "岐阜県");
    }
    
    public PostalCodeService() {
        this.restTemplate = new RestTemplate();
    }
    
    /**
     * 郵便番号から都道府県名を取得（API優先、失敗時はフォールバック）
     * 
     * @param postalCode 郵便番号（7桁、ハイフンありでも可）
     * @return 都道府県名（取得できない場合はnull）
     */
    public String getPrefectureName(String postalCode) {
        // まずAPIから取得を試みる
        try {
            String prefectureName = getPrefectureNameFromApi(postalCode);
            if (prefectureName != null) {
                return prefectureName;
            }
        } catch (ExternalApiException e) {
            // APIが失敗した場合は、既存の上2桁から判定する方法を使用（フォールバック）
            logger.warn("API取得失敗、フォールバック処理を実行: {}", postalCode, e);
        }
        
        // フォールバック処理
        return getPrefectureNameFromPrefix(postalCode);
    }
    
    /**
     * 郵便番号から都道府県名を取得（API使用）
     * 
     * @param postalCode 郵便番号（7桁、ハイフンありでも可）
     * @return 都道府県名（取得できない場合はnull）
     */
    public String getPrefectureNameFromApi(String postalCode) {
        try {
            // ハイフンを除去
            String code = postalCode.replace("-", "");
            
            // 7桁でない場合はnullを返す
            if (code.length() != 7) {
                throw new InvalidParameterException("postalCode: " + postalCode);
            }
            
            // APIを呼び出し
            ResponseEntity<ZipCloudResponse> response = restTemplate.getForEntity(
                ZIPCLOUD_API_URL + "?zipcode=" + code, 
                ZipCloudResponse.class
            );
            
            //レスポンスの妥当性チェック
            ZipCloudResponse zipCloudResponse = response.getBody(); 
            
            // レスポンスのチェック
            if (zipCloudResponse == null || zipCloudResponse.getStatus() == null) {
            	throw new InvalidParameterException("APIレスポンス: " + zipCloudResponse);
            }
            
            // statusが200でない場合はエラー
            if (zipCloudResponse.getStatus() != 200) {
            	throw new InvalidParameterException("status: " + zipCloudResponse.getStatus());
            }
            
            // 結果が空の場合はnullを返す
            if (zipCloudResponse.getResults() == null || 
                zipCloudResponse.getResults().isEmpty()) {
            	throw new PostalCodeNotFoundException("postalCode: " + postalCode);
            }
            
            // 最初の結果から都道府県名を取得
            ZipCloudResponse.AddressResult result = zipCloudResponse.getResults().get(0);
            String prefectureName = result.getAddress1();
            
            logger.info("郵便番号 {} から都道府県名を取得: {}", postalCode, prefectureName);
            return prefectureName;
            
        } catch (RestClientException e) {
            logger.error("API呼び出しエラー: {}", e.getMessage(), e);
            throw new ExternalApiException("郵便番号API呼び出しに失敗しました: " + postalCode, e);
        } catch (Exception e) {
            logger.error("予期しないエラー: {}", e.getMessage(), e);
            throw new ExternalApiException("郵便番号取得処理で予期しないエラーが発生しました: " + postalCode, e);
        }
    }
    
    /**
     * 郵便番号から都道府県コードを取得（API使用）
     * 
     * @param postalCode 郵便番号
     * @return 都道府県コード（取得できない場合はnull）
     */
//    public String getPrefectureCodeFromApi(String postalCode) {
//        try {
//            String code = postalCode.replace("-", "");
//            if (code.length() != 7) {
//                return null;
//            }
//            
//            ResponseEntity<ZipCloudResponse> response = restClient.get()
//                .uri(ZIPCLOUD_API_URL + "?zipcode=" + code)
//                .retrieve()
//                .toEntity(ZipCloudResponse.class);
//            
//            ZipCloudResponse zipCloudResponse = response.getBody();
//            
//            if (zipCloudResponse == null || 
//                zipCloudResponse.getStatus() == null ||
//                zipCloudResponse.getStatus() != 200 ||
//                zipCloudResponse.getResults() == null ||
//                zipCloudResponse.getResults().isEmpty()) {
//                return null;
//            }
//            
//            // 都道府県コードを取得（2桁の文字列、必要に応じて0埋め）
//            String prefcode = zipCloudResponse.getResults().get(0).getPrefcode();
//            if (prefcode != null && prefcode.length() == 1) {
//                // 1桁の場合は0埋め
//                prefcode = "0" + prefcode;
//            }
//            return prefcode;
//            
//        } catch (Exception e) {
//            logger.error("都道府県コード取得エラー: {}", e.getMessage(), e);
//            return null;
//        }
//    }
    
    /**
     * 郵便番号の上2桁から都道府県名を取得（フォールバック用）
     * 
     * @param postalCode 郵便番号
     * @return 都道府県名（取得できない場合はnull）
     */
    private String getPrefectureNameFromPrefix(String postalCode) {
        // ハイフンを除去
        String code = postalCode.replace("-", "");
        
        // 郵便番号が7桁未満の場合はnullを返す
        if (code.length() < 7) {
            return null;
        }
        
        // 上2桁を取得
        String prefix = code.substring(0, 2);
        // 都道府県名を取得
        return prefectureMap.get(prefix);
    }
}

