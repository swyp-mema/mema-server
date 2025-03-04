package com.swyp.mema.domain.store.dto.naverAPI;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TotalStoreRes {

	private List<StoreRes> stores = new ArrayList<>();

}
