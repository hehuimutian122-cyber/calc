package com.example.authapp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.authapp.dto.LivingCostCalculation;
import com.example.authapp.entity.CalculationHistory;
import com.example.authapp.entity.User;
import com.example.authapp.repository.CalculationHistoryKensakuRepository;

@Service
public class LivingCostService {
	
	private static final Logger logger = LoggerFactory.getLogger(LivingCostService.class);
	
	private static final int MAX_RENT_RATE_PERCENT = 25; //家賃上限率(%)
    private static final int MONTHS_PER_YEAR = 12; // 1年の月数
    private static final int UTILITIES_RATE_PERCENT = 12; // 光熱費率（%）
    private static final int FOOD_RATE_PERCENT = 12; // 食費率（%）
    private static final int OTHERS_RATE_PERCENT = 5; // その他率（%）
    private static final int MONTHLY_COMMUNICATION_FEE = 4000; // 通信費（月額）
	
	 @Autowired
	    private PostalCodeService postalCodeService;
	 
	 @Autowired
	    private CalculationHistoryKensakuRepository calculationHistoryRepository;
	 
	 public LivingCostCalculation calculate(String postalCode, int annualIncome) {
	        return calculate(postalCode, annualIncome, null);
	    }
	 
	 @Transactional
	 public LivingCostCalculation calculate(String postalCode, int annualIncome, User user) {
	        
	        // 都道府県を取得
	        String prefecture = postalCodeService.getPrefectureName(postalCode);
	      
	        // 平均家賃を取得
	        int averageRent = PrefectureService.getAverageRent(prefecture);
	        
	        // 年収の25%を上限とする
	        int maxRent = annualIncome / MONTHS_PER_YEAR * MAX_RENT_RATE_PERCENT /100; //収入を一か月あたりに計算、その25％
	        int monthlyRent = Math.min(averageRent, maxRent);
	      
	        // 光熱費（家賃の12%）
	        int monthlyUtilities = (int)(monthlyRent * UTILITIES_RATE_PERCENT / 100);
	      
	        // 食費（年収の12%を12で割る）
	        int monthlyFood = annualIncome / MONTHS_PER_YEAR * FOOD_RATE_PERCENT / 100;
	      
	        // 通信費（固定）
	        int monthlyCommunication = MONTHLY_COMMUNICATION_FEE;
	      
	        // その他（年収の5%を12で割る）
	        int monthlyOthers = annualIncome / MONTHS_PER_YEAR * OTHERS_RATE_PERCENT / 100;
	      
	        LivingCostCalculation calculation = new LivingCostCalculation(
	            postalCode, prefecture, annualIncome,
	            monthlyRent, monthlyUtilities, monthlyFood, monthlyCommunication, monthlyOthers
	        );
	        
	        // 個人情報を含む計算結果はログに出力しない（プライバシー保護）
	        logger.info("生活費計算完了");
	        
	        // ログインしているユーザーの場合、履歴を保存
	        if (user != null) {
	            // ユーザーIDもログに出力しない（プライバシー保護）
	            logger.debug("計算履歴を保存します");
	            CalculationHistory history = new CalculationHistory(
	                user,
	                postalCode,
	                prefecture,
	                annualIncome,
	                calculation.getMonthlyRent(),
	                calculation.getMonthlyUtilities(),
	                calculation.getMonthlyFood(),
	                calculation.getMonthlyCommunication(),
	                calculation.getMonthlyOthers(),
	                calculation.getMonthlyTotal(),
	                calculation.getAnnualTotal()
	            );
	            calculationHistoryRepository.save(history);
	            logger.debug("計算履歴の保存が完了しました");
	        }
	        
	        return calculation;
	    }
	 
	/**
     * 計算履歴をページネーション付きで検索する
     * 
     * @param user ユーザー（nullの場合は全ユーザー）
     * @param postalCode 郵便番号（nullの場合は条件に含めない）
     * @param minAnnualIncome 年収の最小値（nullの場合は条件に含めない）
     * @param maxAnnualIncome 年収の最大値（nullの場合は条件に含めない）
     * @param pageable ページネーション情報
     * @return ページネーション情報を含む検索結果
     */
    @Transactional(readOnly = true)
    public Page<CalculationHistory> searchHistory(User user, String postalCode, 
                                                   Integer minAnnualIncome, 
                                                   Integer maxAnnualIncome,
                                                   Pageable pageable) {
        logger.debug("計算履歴検索開始 - page: {}, size: {}", 
                     pageable.getPageNumber(), pageable.getPageSize());
        
        // 検索結果を取得
        List<CalculationHistory> histories = calculationHistoryRepository.searchByConditions(
            user, postalCode, minAnnualIncome, maxAnnualIncome,
            pageable.getPageNumber(), pageable.getPageSize()
        );
        
        // 総件数を取得
        long total = calculationHistoryRepository.countByConditions(
            user, postalCode, minAnnualIncome, maxAnnualIncome
        );
        
        logger.info("計算履歴検索完了 - 取得件数: {}件 / 総件数: {}件", 
                   histories.size(), total);
        
        // Pageオブジェクトを作成して返す
        return new PageImpl<>(histories, pageable, total);
    }
	 
}
