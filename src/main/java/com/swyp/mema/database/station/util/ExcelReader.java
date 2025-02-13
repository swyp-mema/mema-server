package com.swyp.mema.database.station.util;

import com.swyp.mema.database.station.model._Station;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;

@Component
public class ExcelReader {

    public ArrayList<_Station> readLocationData() {

        ArrayList<_Station> stations = new ArrayList<>();
        String file = "/locationData.xlsx";

        XSSFSheet sheet = null;

        try {
            InputStream is = getClass().getResourceAsStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            sheet = workbook.getSheetAt(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int rowNum = sheet.getLastRowNum();

        for (int i = 1; i <= rowNum; i++) {
            XSSFRow row = sheet.getRow(i);

            if (row != null) {

                String stationName = row.getCell(1).getStringCellValue();
                String lineName = row.getCell(3).getStringCellValue();
                String lat, lot;
                if(row.getCell(4) == null) lat = "0.0";
                else lat = String.valueOf(row.getCell(4).getNumericCellValue());
                if(row.getCell(5) == null) lot = "0.0";
                else lot = String.valueOf(row.getCell(5).getNumericCellValue());
                String addr = row.getCell(7).getStringCellValue();

                stations.add(_Station.builder()
                        .stationName(stationName)
                        .lineName(lineName)
                        .lat(lat)
                        .lot(lot)
                        .address(addr)
                        .build());
            }
        }

        return stations;
    }

    public ArrayList<ArrayList<String>> readFile(String file) {

        ArrayList<ArrayList<String>> datas = new ArrayList<>();
        XSSFSheet sheet = null;

        try {
            InputStream is = getClass().getResourceAsStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            sheet = workbook.getSheetAt(0);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println(file + " not found");
            return null;
        }

        int rowNum = sheet.getLastRowNum();

        for (int i = 1; i <= rowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            int colNum = row.getLastCellNum();
            datas.add(new ArrayList());

            for (int j = 0; j < colNum; j++) {

                XSSFCell cell = row.getCell(j);
                if (cell == null) {
                    datas.get(i - 1).add("");
                    continue;
                }
                CellType type = cell.getCellType();
                switch (type) {
                    case STRING:
                        datas.get(i - 1).add(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        datas.get(i - 1).add(String.valueOf(cell.getNumericCellValue()));
                        break;
                    case BLANK:
                        datas.get(i - 1).add("");
                        break;
                    default:
                        System.out.println("Type error");
                        break;
                }
            }
        }

        return datas;
    }

    public ArrayList<ArrayList<String>> readFile(String file, String sheetName) {

        ArrayList<ArrayList<String>> datas = new ArrayList<>();
        XSSFSheet sheet = null;

        try {
            InputStream is = getClass().getResourceAsStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            sheet = workbook.getSheet(sheetName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(file + " not found");
            return null;
        }

        int rowNum = sheet.getLastRowNum();

        for (int i = 1; i <= rowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            int colNum = row.getLastCellNum();
            datas.add(new ArrayList());

            for (int j = 0; j < colNum; j++) {

                XSSFCell cell = row.getCell(j);
                if (cell == null) {
                    datas.get(i - 1).add("");
                    continue;
                }
                CellType type = cell.getCellType();
                switch (type) {
                    case STRING:
                        datas.get(i - 1).add(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        datas.get(i - 1).add(String.valueOf(cell.getNumericCellValue()));
                        break;
                    case BLANK:
                        datas.get(i - 1).add("");
                        break;
                    default:
                        System.out.println("Type error");
                        break;
                }
            }
        }

        return datas;
    }
}
