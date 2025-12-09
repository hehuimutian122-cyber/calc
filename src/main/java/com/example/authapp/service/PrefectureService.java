package com.example.authapp.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class PrefectureService {

	    static Integer getAverageRent(String prefecture) {
	        return AVERAGE_RENT.get(prefecture);
	    }
	    
	    private static final Map<String, Integer> AVERAGE_RENT = new HashMap<String, Integer>() {{
	        put("北海道", 50000); // 北海道
	        put("東京都", 80000); // 東京都
	        put("大阪府", 60000); // 大阪府
	        // TODO: 他の都道府県は後で追加する
	        put("岐阜県", 60000); // 大阪府
	    }};

	}


