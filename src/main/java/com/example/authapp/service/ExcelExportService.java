package com.example.authapp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.authapp.dto.LivingCostCalculation;

@Service
public class ExcelExportService {
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelExportService.class);
	
	/**
	 * 計算結果をエクセルファイルとして出力する
	 * 
	 * @param calculation 計算結果
	 * @return エクセルファイルのバイト配列
	 */
	public byte[] export(LivingCostCalculation calculation) {
		logger.debug("エクセルファイルの作成を開始");
		
		//エクセルファイルの作成
		try (XSSFWorkbook workBook = new XSSFWorkbook();
			 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			
			//シートの作成
			XSSFSheet sheet = workBook.createSheet();
			workBook.setSheetName(0,  "月計算結果");
			sheet = workBook.getSheet("月計算結果");
			
			// 行の作成
			XSSFRow headderRow = sheet.createRow(0);
			XSSFRow dateRow = sheet.createRow(1);
			
			//ヘッダー行の作成
			headderRow.createCell(0).setCellValue("家賃");
			headderRow.createCell(1).setCellValue("光熱費");
			headderRow.createCell(2).setCellValue("食費");
			headderRow.createCell(3).setCellValue("通信費");
			headderRow.createCell(4).setCellValue("その他");
			headderRow.createCell(5).setCellValue("合計");
			
			//値の入力
			int monthlyRent = calculation.getMonthlyRent();
			int monthlyUtilities = calculation.getMonthlyUtilities();
			int monthlyFood = calculation.getMonthlyFood();
			int monthlyCommunication = calculation.getMonthlyCommunication();
			int monthlyOthers = calculation.getMonthlyOthers();
			
			dateRow.createCell(0).setCellValue(monthlyRent);
			dateRow.createCell(1).setCellValue(monthlyUtilities);
			dateRow.createCell(2).setCellValue(monthlyFood);
			dateRow.createCell(3).setCellValue(monthlyCommunication);
			dateRow.createCell(4).setCellValue(monthlyOthers);
			dateRow.createCell(5).setCellValue(monthlyRent + monthlyUtilities + monthlyFood + monthlyCommunication + monthlyOthers);
			
			// エクセルファイルをバイト配列に出力
			workBook.write(outputStream);
			logger.info("エクセルファイルの作成が完了しました。");
			
			return outputStream.toByteArray();
			
		} catch(IOException e) {
			logger.error("エクセルファイルの作成中に予期しないエラーが発生しました。", e);
			throw new RuntimeException("エクセルファイルの作成に失敗しました。", e);
		}
	}
	
	/**
	 * エクセルファイル名を生成する
	 * 
	 * @return ファイル名（例: livingCost_20231201.xlsx）
	 */
	public String generateFileName() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return "livingCost_" + dateFormat.format(date) + ".xlsx";
	}

}
