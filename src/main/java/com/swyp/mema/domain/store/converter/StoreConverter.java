package com.swyp.mema.domain.store.converter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.swyp.mema.domain.store.dto.naverAPI.BasicStoreRes;
import com.swyp.mema.domain.store.dto.naverAPI.BasicStoreRes.Item;
import com.swyp.mema.domain.store.dto.naverAPI.StoreRes;
import com.swyp.mema.domain.store.dto.naverAPI.TotalStoreRes;

@Component
public class StoreConverter {

	// basicStoreRes -> StoreRes 변환
	public StoreRes toStoreRes(Item newData) {

		return StoreRes.builder()
			.name(newData.getTitle())
			.link(newData.getLink())
			.category(newData.getCategory())
			.address(newData.getAddress())
			.mapx(newData.getMapx())
			.mapy(newData.getMapy())
			.build();

	}

	// basicStoreRes -> List<StoreRes> 변환
	public List<StoreRes> toStoreResList(BasicStoreRes basicStoreRes) {
		return basicStoreRes.getItems().stream()
			.map(item -> StoreRes.builder()
				.name(item.getTitle())
				.link(item.getLink())
				.category(item.getCategory())
				.address(item.getRoadAddress())
				.mapx(item.getMapx())
				.mapy(item.getMapy())
				.build())
			.toList();
	}

	// List<storeRes> -> TotalStoreRes 변환
	public TotalStoreRes totalStoreRes(List<StoreRes> list) {
		return new TotalStoreRes(list);
	}
}
