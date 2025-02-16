package com.zywl.test1229.utils

import android.content.Context
import android.util.Log
import com.zywl.test1229.bean.Constant
import com.zywl.test1229.bean.LineInfo
import com.zywl.test1229.bean.PointInfo
import com.zywl.test1229.bean.StakeInfo
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import java.io.File
import java.io.FileInputStream
import java.time.LocalDate


object ExcelUtils {
    fun parseExcel(path: String): MutableList<StakeInfo> {
        val list: MutableList<StakeInfo> = mutableListOf()
        var file = File(path)
        if (!file.exists())
            return list
        var fileByte = FileInputStream(file)
        val workbook = HSSFWorkbook(fileByte)
        val sheet = workbook.getSheetAt(0);
        for (rowIndex in 1 until sheet.physicalNumberOfRows) {
            val row = sheet.getRow(rowIndex)
            var index3 = 0.0
            var index4 = 0.0
            var index5 = 0.0
            try {
                index3 = getCellData(row.getCell(9)).toDouble();
            } catch (e: NumberFormatException) {
                index3 = 0.0
            }
            try {
                index4 = getCellData(row.getCell(10)).toDouble();
            } catch (e: NumberFormatException) {
                index4 = 0.0
            }
            try {
                index5 = getCellData(row.getCell(10)).toDouble();
            } catch (e: NumberFormatException) {
                index5 = 0.0
            }
            // 跳过空行
            if (row == null) continue
            var stakeInfo = StakeInfo(
                0,
                getCellData(row.getCell(1)),
                getCellData(row.getCell(0)),
                getCellData(row.getCell(2)),
                index3,
                index4,
                index5,
                getCellData(row.getCell(6)),
                getCellData(row.getCell(7)),
                getCellData(row.getCell(8)),
                getCellData(row.getCell(9)),
                getCellData(row.getCell(10)),
                getCellData(row.getCell(11)),
                getCellData(row.getCell(12)),
            )
            list.add(stakeInfo)
        }
        return list;
    }

    fun getCellData(cell: Cell): String {
        val cellValue = when (cell.cellType) {
            CellType.STRING -> cell.stringCellValue
            CellType.NUMERIC -> cell.numericCellValue.toString()
            CellType.BOOLEAN -> cell.booleanCellValue.toString()
            else -> "Unknown"
        }
        return cellValue
    }

    fun produceExcel(context: Context?, array: List<StakeInfo>, isbackUp: Boolean = false) {
        val primeExlPFixedSizeWidth = JsonUils.parseJSONListDouble(
            JsonUils.loadJSONFromAssets("pile_data.json", context),
            "primeExlPFixedSizeWidth"
        )
        val primeExlFixedHeight = JsonUils.parseJSONListDouble(
            JsonUils.loadJSONFromAssets("pile_data.json", context),
            "primeExlFixedHeight"
        )
        //                softexportPrimeDataP(m_excelFiledNameP,m_pointListGroup);

        var listMain = mutableListOf<List<String>>()
        for (index in array) {
            var list = mutableListOf<String>()
            list.add(index.pipeLine)
            list.add(index.tempNo)
            list.add(index.stakeType)
            list.add("${index.latitude}")
            list.add("${index.longitude}")
            list.add("${index.z}")
            list.add(index.pipeDepth)
            list.add(index.mileage)
            list.add(index.buryTech)
            list.add(index.isInPoint)
            list.add(index.terrain)
            list.add(index.imageName)
            list.add(index.collectDate)
            listMain.add(list)
        }
        //获得json文件数据
        val parseJSONprimeExlP: List<String> =
            JsonUils.parseJSONList(
                JsonUils.loadJSONFromAssets("pile_data.json", context),
                "primeExlP"
            )
        var directory = ""
        var date = LocalDate.now()
        if (!isbackUp) {
            var pointTitleInfo = Tools.getDataClassFieldNames(PointInfo())
            var lineTitleInfo = Tools.getDataClassFieldNames(LineInfo())
            var pointInfos = Tools.getDataClassFieldNamesAndValueForMap(collectPoints(array))
            var points =
                Tools.getDataClassFieldNamesAndValuesForList(pointTitleInfo, pointInfos)
            directory = Constant.SDCARD + "/" + Constant.projectName + "/tables"
            FileUtils.mkDir(directory)
            var fileNum = FileUtils.getFileCountInDirectory(directory, ".xls") / 2 + 1
            ExcelUtilsOfPoi.initExcelPrime(
                "$directory/桩表_${date}_${fileNum}.xls",
                parseJSONprimeExlP, "桩表", primeExlPFixedSizeWidth, primeExlFixedHeight
            )
            ExcelUtilsOfPoi.initExcel(
                "$directory/桩表PC_${date}_${fileNum}.xls",
                pointTitleInfo,
                lineTitleInfo,
                "P_ALL",
                "L_All"
            )

            ExcelUtilsOfPoi.writeObjListToExcelPrime(
                listMain, "$directory/桩表_${date}_${fileNum}.xls",
                primeExlFixedHeight, primeExlPFixedSizeWidth
            )
            ExcelUtilsOfPoi.writeObjListToExcel(
                0,
                points,
                "$directory/桩表PC_${date}_${fileNum}.xls"
            )
        } else {
            directory = Constant.SDCARD + "/" + Constant.projectName + "/backup"
            // FileUtils.mkDir(directory)
            File(directory).mkdirs()
            ExcelUtilsOfPoi.initExcelPrime(
                "$directory/main.xls",
                parseJSONprimeExlP, "桩表", primeExlPFixedSizeWidth, primeExlFixedHeight
            )
            ExcelUtilsOfPoi.writeObjListToExcelPrime(
                listMain, "$directory/main.xls",
                primeExlFixedHeight, primeExlPFixedSizeWidth
            )
        }
        Log.d("MainActivity", "produceExcel")
    }

    fun collectPoints(data: List<StakeInfo>): List<PointInfo> {
        var list: MutableList<PointInfo> = mutableListOf()
        var point = PointInfo()
        var index = 1;
        data.forEach { item ->
            point.buryTech = item.buryTech
            point.collectDate = item.collectDate
            point.exp_Date = item.collectDate
            point.exp_Num = "RQa$index"
            point.id = "RQa$index"
            point.isInPoint = item.isInPoint
            point.latitude = item.latitude.toString()
            point.longitude = item.longitude.toString()
            point.mileage = item.mileage
            point.pipeDepth = item.pipeDepth
            point.pipeLine = item.pipeLine
            point.serialNum = index.toString()
            point.situation = "T-正常"
            point.stakeType = item.stakeType
            point.state = "新测"
            point.subsid = item.stakeType
            point.symbol = "探测点"
            point.symbolExpression = "RQ-探测点"
            point.symbolID = "472"
            point.symbolSizeX = "4.0"
            point.symbolSizeY = "4.0"
            point.tempNo = item.tempNo
            point.terrain = item.terrain
            point.code = "RQ"
            point.pipeLine = "燃气-RQ"
            point.rangeExpression = "3.5"
            point.shortCode = "RQ"
            point.sysId = index.toString()
            point.type = "Type_None"
            list.add(point)
        }
        return list
    }

    fun collectLines(data: List<StakeInfo>): List<LineInfo> {
        var list: MutableList<LineInfo> = mutableListOf()
        return list
    }
}