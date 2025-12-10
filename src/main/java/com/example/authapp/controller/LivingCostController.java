package com.example.authapp.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.authapp.dto.LivingCostCalculation;
import com.example.authapp.dto.LivingCostRequest;
import com.example.authapp.entity.CalculationHistory;
import com.example.authapp.entity.User;
import com.example.authapp.repository.CalculationHistoryKensakuRepository;
import com.example.authapp.service.ExcelExportService;
import com.example.authapp.service.LivingCostService;
import com.example.authapp.service.UserService;

@Controller
@RequestMapping("/living-cost") //RequestMappingはコントローラークラス全体に共通のURLを設定する
public class LivingCostController {
	
	private static final Logger logger = LoggerFactory.getLogger(LivingCostController.class);
	
	@Autowired
    private LivingCostService livingCostService;
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private CalculationHistoryKensakuRepository calculationHistoryRepository;
	
	@Autowired
    private ExcelExportService excelExportService;
	
	// 入力画面を表示
    @GetMapping("/calculate")
    public String showCalculateForm(Model model) {
        logger.debug("生活費計算入力画面を表示");
        model.addAttribute("request", new LivingCostRequest());
        return "living-cost/calculate";
    }
    
 // 計算を実行して結果を表示
    @PostMapping("/calculate")
    public String calculate(@Valid @ModelAttribute LivingCostRequest request,
                           BindingResult result, Model model,
                           Authentication authentication) {
        logger.info("生活費計算リクエスト受信");
        
        // @Validにより、以下のチェックが実行：
        // 1. postalCodeが空でないか？
        // 2. postalCodeが7桁の数字か？
        // 3. annualIncomeがnullでないか？
        // 4. annualIncomeが1以上1億以下か？
        if (result.hasErrors()) {
        	// エラーがあれば入力画面に戻る
            logger.warn("バリデーションエラー - エラー数: {}", result.getErrorCount());
            return "living-cost/calculate";
        }
      
        // 現在ログインしているユーザーを取得
        User currentUser = null;
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
        logger.debug("認証状態: {}", isAuthenticated ? "認証済み" : "未認証");
        
        if (isAuthenticated) {
            String username = authentication.getName();
            // ユーザーが見つからない場合はUserNotFoundExceptionがスローされ、
            // グローバル例外ハンドラーで処理される
            currentUser = userService.findByUsername(username);
            logger.debug("ユーザー取得成功");
        }
      
        //計算処理
        logger.debug("計算処理を開始");
        LivingCostCalculation calculation = 
            livingCostService.calculate(request.getPostalCode(), request.getAnnualIncome(), currentUser);
        logger.info("計算処理完了 - 結果画面を表示");
        
        //modelに追加
        model.addAttribute("calculation", calculation); //HTMLではキー値を使って、参照する（箱のラベルのイメージ）
        return "living-cost/result";
    }
    
    // 履歴一覧を表示（ページネーション対応）
    @GetMapping("/history")
    public String showHistory(
            Model model, 
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,  // ページ番号（デフォルト: 0）
            @RequestParam(defaultValue = "10") int size) { // 1ページあたりの件数（デフォルト: 10）
        
        logger.info("計算履歴一覧表示リクエスト受信 - page: {}, size: {}", page, size);
        
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("未認証ユーザーが履歴一覧にアクセス - ログイン画面にリダイレクト");
            return "redirect:/login";
        }
        
        String username = authentication.getName();
        logger.debug("履歴取得開始 - username: {}", username);
        
        User user = userService.findByUsername(username);
        
        // Pageableオブジェクトを作成
        Pageable pageable = PageRequest.of(page, size);
        
        // 検索結果を取得
        List<CalculationHistory> histories = 
            calculationHistoryRepository.searchByConditions(user, null, null, null, page, size);
        
        // 総件数を取得
        long total = calculationHistoryRepository.countByConditions(user, null, null, null);
        
        // Pageオブジェクトを作成
        Page<CalculationHistory> historyPage = new PageImpl<>(histories, pageable, total);
        
        logger.info("計算履歴一覧表示 - 取得件数: {}件 / 総件数: {}件 / 総ページ数: {}", 
                   histories.size(), total, historyPage.getTotalPages());
        
        // Modelに追加
        model.addAttribute("historyPage", historyPage);  // Pageオブジェクト
        model.addAttribute("histories", histories);        // リスト（既存のコードとの互換性のため）
        
        return "living-cost/history";
    }
    
    //エクセル出力
    @GetMapping("/export")
    public ResponseEntity<Resource> exportXlsx(Authentication authentication) {
    	logger.info("エクセル出力リクエスト受信");
    	
    	// 認証チェック
    	if (authentication == null || !authentication.isAuthenticated()) {
    		logger.warn("未認証ユーザーがエクセル出力にアクセス - ログイン画面にリダイレクト");
    		return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
    	}
    	
    	String username = authentication.getName();
    	logger.debug("エクセル出力開始 - username: {}", username);
    	
    	User user = userService.findByUsername(username);
    	
    	// 最新の計算履歴を取得（1件のみ）
    	List<CalculationHistory> histories = 
    		calculationHistoryRepository.searchByConditions(user, null, null, null, 0, 1);
    	
    	if (histories.isEmpty()) {
    		logger.warn("計算履歴が見つかりません - username: {}", username);
    		return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).build();
    	}
    	
    	CalculationHistory latestHistory = histories.get(0);
    	
    	// CalculationHistoryをLivingCostCalculationに変換
    	LivingCostCalculation calculation = new LivingCostCalculation(
    		latestHistory.getPostalCode(),
    		latestHistory.getPrefectureName(),
    		latestHistory.getAnnualIncome(),
    		latestHistory.getMonthlyRent() != null ? latestHistory.getMonthlyRent() : 0,
    		latestHistory.getMonthlyUtilities() != null ? latestHistory.getMonthlyUtilities() : 0,
    		latestHistory.getMonthlyFood() != null ? latestHistory.getMonthlyFood() : 0,
    		latestHistory.getMonthlyCommunication() != null ? latestHistory.getMonthlyCommunication() : 0,
    		latestHistory.getMonthlyOthers() != null ? latestHistory.getMonthlyOthers() : 0
    	);
    	
    	// エクセルファイルを生成
    	byte[] excelBytes = excelExportService.export(calculation);
    	String fileName = excelExportService.generateFileName();
    	
    	// レスポンスヘッダーを設定
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    	headers.setContentDispositionFormData("attachment", fileName);
    	headers.setContentLength(excelBytes.length);
    	
    	logger.info("エクセルファイルの出力が完了しました - filename: {}", fileName);
    	
    	return ResponseEntity.ok()
    		.headers(headers)
    		.body(new ByteArrayResource(excelBytes));
    }
    

}
