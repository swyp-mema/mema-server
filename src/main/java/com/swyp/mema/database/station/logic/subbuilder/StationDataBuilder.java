package com.swyp.mema.database.station.logic.subbuilder;

import com.swyp.mema.database.openapi.location.logic.StationMasterService;
import com.swyp.mema.database.station.repository._StationRepository;
import com.swyp.mema.database.station.util.Addr2CodeConverter;
import com.swyp.mema.database.station.util.ExcelReader;
import com.swyp.mema.database.station.util.StationConverter;
import com.swyp.mema.database.station.util.StringCleaner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StationDataBuilder {

    private final _StationRepository stationRepository;
    private final StationMasterService subwayLocationService;
    private final ExcelReader excelData;
    private final Addr2CodeConverter addr2CodeConverter;
    private final StringCleaner stringCleaner;
    private final StationConverter stationConverter;



}
