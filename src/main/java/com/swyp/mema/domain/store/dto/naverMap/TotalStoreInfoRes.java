package com.swyp.mema.domain.store.dto.naverMap;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TotalStoreInfoRes {

    private List<StoreInfoRes> storeList;

}
