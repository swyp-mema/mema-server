package com.swyp.mema.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TotalStoreList {

    private List<StoreInfo> storeList;

}
