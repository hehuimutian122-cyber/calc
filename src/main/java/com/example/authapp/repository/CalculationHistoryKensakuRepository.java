package com.example.authapp.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.authapp.entity.CalculationHistory;
import com.example.authapp.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class CalculationHistoryKensakuRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private static final Logger logger = LoggerFactory.getLogger(CalculationHistoryKensakuRepository.class);
    
    
    
    /**
     * 動的な条件で計算履歴を検索する
     * 
     * @param user ユーザー（nullの場合は全ユーザー）
     * @param postalCode 郵便番号（nullの場合は条件に含めない）
     * @param minAnnualIncome 年収の最小値（nullの場合は条件に含めない）
     * @param maxAnnualIncome 年収の最大値（nullの場合は条件に含めない）
     * @param page ページ番号（0始まり）
     * @param size 1ページあたりの件数
     * @return 検索結果のリスト
     */
    @Transactional(readOnly = true)
    public List<CalculationHistory> searchByConditions(User user, String postalCode, 
                                                       Integer minAnnualIncome, 
                                                       Integer maxAnnualIncome,
                                                       int page, int size) {
        // 個人情報を含む検索条件はログに出力しない（プライバシー保護）
        logger.debug("検索開始 - user: {}, postalCode: {}**, 条件数: {}, page: {}, size: {}", 
                     user != null ? "あり" : "なし", 
                     postalCode != null && postalCode.length() >= 3 ? postalCode.substring(0, 3) + "****" : "****",
                     (user != null ? 1 : 0) + (postalCode != null && !postalCode.isEmpty() ? 1 : 0) + 
                     (minAnnualIncome != null ? 1 : 0) + (maxAnnualIncome != null ? 1 : 0),
                     page, size);
                
        // StringBuilderでネイティブSQL文を組み立て
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        
        sql.append("A.id, ");        
        sql.append("A.user_id, ");
        sql.append("A.postal_code, ");
        sql.append("A.prefecture_name, ");
        sql.append("A.annual_income, ");
        sql.append("A.monthly_rent, ");  
        sql.append("A.monthly_utilities, ");  
        sql.append("A.monthly_food, ");  
        sql.append("A.monthly_communication, ");  
        sql.append("A.monthly_others, ");  
        sql.append("A.monthly_total, ");  
        sql.append("A.annual_total, ");  
        sql.append("A.created_at "); 
        
        sql.append("FROM calculation_histories A ");
        
        sql.append("WHERE 1 = 1 ");  //後続がANDでも問題ない
        
        // パラメータの位置を管理
        int paramIndex = 1;
        
        // ユーザーIDで絞り込み
        if (user != null) {
            sql.append(" AND A.user_id = ?");
            paramIndex++;
        }
        
        // 郵便番号で絞り込み
        if (postalCode != null && !postalCode.isEmpty()) {
            sql.append(" AND A.postal_code = ?");
            paramIndex++;
        }
        
        // 年収の最小値で絞り込み
        if (minAnnualIncome != null) {
            sql.append(" AND A.annual_income >= ?");
            paramIndex++;
        }
        
        // 年収の最大値で絞り込み
        if (maxAnnualIncome != null) {
            sql.append(" AND A.annual_income <= ?");
            paramIndex++;
        }
        
        // 作成日時の降順でソート
        sql.append(" ORDER BY A.created_at DESC");
        
        // ページネーション（OFFSETとLIMIT）
        int offset = page * size;
        sql.append(" LIMIT ? OFFSET ?");
        
        // ネイティブSQLクエリを作成
        Query query = entityManager.createNativeQuery(sql.toString(), CalculationHistory.class);
        
        // パラメータを設定（位置を明示的に指定）
        paramIndex = 1;
        if (user != null) {
            query.setParameter(paramIndex, user.getId());
            paramIndex++;
        }
        if (postalCode != null && !postalCode.isEmpty()) {
            query.setParameter(paramIndex, postalCode);
            paramIndex++;
        }
        if (minAnnualIncome != null) {
            query.setParameter(paramIndex, minAnnualIncome);
            paramIndex++;
        }
        if (maxAnnualIncome != null) {
            query.setParameter(paramIndex, maxAnnualIncome);
            paramIndex++;
        }
        
        // LIMITとOFFSETのパラメータを設定
        query.setParameter(paramIndex++, size);
        query.setParameter(paramIndex++, offset);
        
        // クエリを実行して結果を取得
        @SuppressWarnings("unchecked")
        List<CalculationHistory> results = query.getResultList();
        
        logger.debug("実行SQL: {}", sql.toString());
        logger.info("検索結果: {}件", results.size());
        return results;
    }
    
    /**
     * 検索条件に一致する計算履歴の総件数を取得する
     * 
     * @param user ユーザー（nullの場合は全ユーザー）
     * @param postalCode 郵便番号（nullの場合は条件に含めない）
     * @param minAnnualIncome 年収の最小値（nullの場合は条件に含めない）
     * @param maxAnnualIncome 年収の最大値（nullの場合は条件に含めない）
     * @return 総件数
     */
    @Transactional(readOnly = true)
    public long countByConditions(User user, String postalCode, 
                                  Integer minAnnualIncome, 
                                  Integer maxAnnualIncome) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) ");
        sql.append("FROM calculation_histories A ");
        sql.append("WHERE 1 = 1 ");
        
        int paramIndex = 1;
        
        if (user != null) {
            sql.append(" AND A.user_id = ?");
            paramIndex++;
        }
        if (postalCode != null && !postalCode.isEmpty()) {
            sql.append(" AND A.postal_code = ?");
            paramIndex++;
        }
        if (minAnnualIncome != null) {
            sql.append(" AND A.annual_income >= ?");
            paramIndex++;
        }
        if (maxAnnualIncome != null) {
            sql.append(" AND A.annual_income <= ?");
            paramIndex++;
        }
        
        Query query = entityManager.createNativeQuery(sql.toString());
        
        paramIndex = 1;
        if (user != null) {
            query.setParameter(paramIndex, user.getId());
            paramIndex++;
        }
        if (postalCode != null && !postalCode.isEmpty()) {
            query.setParameter(paramIndex, postalCode);
            paramIndex++;
        }
        if (minAnnualIncome != null) {
            query.setParameter(paramIndex, minAnnualIncome);
            paramIndex++;
        }
        if (maxAnnualIncome != null) {
            query.setParameter(paramIndex, maxAnnualIncome);
            paramIndex++;
        }
        
        Object result = query.getSingleResult();
        return ((Number) result).longValue();
    }
    
    /**
     * 計算履歴を保存する
     * 
     * @param history 保存する計算履歴
     * @return 保存された計算履歴
     */
    @Transactional
    public CalculationHistory save(CalculationHistory history) {
        if (history.getId() == null) {
            // 新規作成
            // 個人情報を含む詳細はログに出力しない（プライバシー保護）
            logger.debug("計算履歴を新規作成");
            entityManager.persist(history);
            logger.info("計算履歴を新規作成しました");
            return history;
        } else {
            // 更新
            // 個人情報を含む詳細はログに出力しない（プライバシー保護）
            logger.debug("計算履歴を更新");
            CalculationHistory updated = entityManager.merge(history);
            logger.info("計算履歴を更新しました");
            return updated;
        }
    }
}

